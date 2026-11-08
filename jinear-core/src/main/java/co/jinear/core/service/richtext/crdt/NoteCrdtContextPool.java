package co.jinear.core.service.richtext.crdt;

import co.jinear.core.exception.note.NoteCrdtException;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.ResourceLimits;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NoteCrdtContextPool {

    private static final String LANGUAGE = "js";
    private static final String PRELUDE_PATH = "note-crdt/prelude.js";
    private static final String BUNDLE_PATH = "note-crdt/note-crdt.bundle.js";
    private static final String API_MEMBER = "noteCrdt";
    private static final String RANDOM_BYTES_MEMBER = "secureRandomBytes";
    private static final int POOL_SIZE = 2;
    private static final long STATEMENT_LIMIT = 2_000_000_000L;
    private static final long PERMIT_TIMEOUT_SECONDS = 10L;

    private final Semaphore permits = new Semaphore(POOL_SIZE, true);
    private final Queue<Context> idleContexts = new ConcurrentLinkedQueue<>();
    private final SecureRandom secureRandom = new SecureRandom();

    private Engine engine;
    private Source prelude;
    private Source bundle;

    public String execute(String function, String... arguments) {
        acquirePermit();
        try {
            Context context = borrowContext();
            String result = executeOn(context, function, arguments);
            idleContexts.add(context);
            return result;
        } finally {
            permits.release();
        }
    }

    @PreDestroy
    public synchronized void close() {
        idleContexts.forEach(Context::close);
        idleContexts.clear();
        Optional.ofNullable(engine).ifPresent(Engine::close);
    }

    private void acquirePermit() {
        try {
            boolean acquired = permits.tryAcquire(PERMIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!acquired) {
                log.warn("Note CRDT permit wait has timed out.");
                throw new NoteCrdtException("note.error.document.process-failed");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new NoteCrdtException("note.error.document.process-failed");
        }
    }

    private Context borrowContext() {
        return Optional.ofNullable(idleContexts.poll()).orElseGet(this::createContext);
    }

    private String executeOn(Context context, String function, String... arguments) {
        try {
            Value api = context.getBindings(LANGUAGE).getMember(API_MEMBER);
            Value result = api.getMember(function).execute((Object[]) arguments);
            String serialized = result.asString();
            context.resetLimits();
            return serialized;
        } catch (RuntimeException exception) {
            context.close(true);
            log.error("Note CRDT call has failed. function: {}", function, exception);
            throw new NoteCrdtException("note.error.document.process-failed");
        }
    }

    private Context createContext() {
        loadEngine();
        ResourceLimits limits = ResourceLimits.newBuilder().statementLimit(STATEMENT_LIMIT, null).build();
        Context context = Context.newBuilder(LANGUAGE)
                .engine(engine)
                .allowAllAccess(false)
                .resourceLimits(limits)
                .build();
        try {
            initialize(context);
            return context;
        } catch (RuntimeException exception) {
            context.close(true);
            log.error("Note CRDT context load has failed.", exception);
            throw new NoteCrdtException("note.error.document.process-failed");
        }
    }

    private void initialize(Context context) {
        context.getBindings(LANGUAGE).putMember(RANDOM_BYTES_MEMBER, (ProxyExecutable) this::randomBytes);
        context.eval(prelude);
        context.eval(bundle);
        context.resetLimits();
    }

    private synchronized void loadEngine() {
        if (Objects.nonNull(engine)) {
            return;
        }
        log.info("Note CRDT engine load has started.");
        prelude = readSource(PRELUDE_PATH);
        bundle = readSource(BUNDLE_PATH);
        engine = Engine.newBuilder(LANGUAGE).option("engine.WarnInterpreterOnly", "false").build();
    }

    private Source readSource(String path) {
        try (InputStream stream = new ClassPathResource(path).getInputStream()) {
            String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return Source.newBuilder(LANGUAGE, content, path).cached(true).build();
        } catch (IOException exception) {
            throw new IllegalStateException("Note CRDT resource could not be read: " + path, exception);
        }
    }

    private ProxyArray randomBytes(Value... arguments) {
        byte[] bytes = new byte[arguments[0].asInt()];
        secureRandom.nextBytes(bytes);
        List<Object> values = new ArrayList<>(bytes.length);
        for (byte value : bytes) {
            values.add(value & 0xff);
        }
        return ProxyArray.fromList(values);
    }
}
