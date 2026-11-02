package co.jinear.core.note;

import co.jinear.core.exception.note.NoteCrdtException;
import co.jinear.core.model.dto.note.NoteContentDto;
import co.jinear.core.model.dto.note.NoteCrdtResultDto;
import co.jinear.core.service.richtext.crdt.NoteCrdtContextPool;
import co.jinear.core.service.richtext.crdt.NoteCrdtEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoteCrdtEngineTest {

    private static final String EMPTY_DOC = "AAA=";

    private static NoteCrdtContextPool pool;
    private static NoteCrdtEngine engine;

    @BeforeAll
    static void startEngine() {
        pool = new NoteCrdtContextPool();
        engine = new NoteCrdtEngine(pool, new ObjectMapper());
    }

    @AfterAll
    static void stopEngine() {
        pool.close();
    }

    @Test
    void createsAndReadsTitleAndBody() {
        List<String> states = apply(List.of(EMPTY_DOC), "<p>Hello <strong>world</strong></p>", "Plan");

        NoteContentDto content = engine.read(states);

        assertThat(content.getTitle()).isEqualTo("Plan");
        assertThat(content.getHtml()).isEqualTo("<p>Hello <strong>world</strong></p>");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<h2>Heading</h2><p>text</p>",
            "<p><strong>b</strong> <em>i</em> <u>u</u> <s>s</s> <code>c</code></p>",
            "<p><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://jinear.co\">link</a></p>",
            "<ul><li><p>one</p></li><li><p>two</p></li></ul>",
            "<ol><li><p>one</p></li></ol>",
            "<ul data-type=\"taskList\"><li data-checked=\"true\" data-type=\"taskItem\"><label><input type=\"checkbox\" checked=\"checked\"><span></span></label><div><p>done</p></div></li></ul>",
            "<pre><code class=\"language-java\">int a = 1;</code></pre>",
            "<blockquote><p>quote</p></blockquote>",
            "<hr>",
            "<img src=\"https://jinear.co/a.png\">",
            "<table><tbody><tr><th colspan=\"1\" rowspan=\"1\"><p>h</p></th></tr><tr><td colspan=\"1\" rowspan=\"1\"><p>c</p></td></tr></tbody></table>",
            "<p>a &amp; b &lt; c</p>"
    })
    void renderedBodyParsesBackWithoutAnyChange(String html) {
        List<String> states = apply(List.of(EMPTY_DOC), html, null);
        String rendered = engine.read(states).getHtml();

        NoteCrdtResultDto roundTrip = engine.edit(states, rendered, null);

        assertThat(roundTrip.isChanged()).isFalse();
    }

    @Test
    void removesScriptsAtParse() {
        List<String> states = apply(List.of(EMPTY_DOC), "<p onclick=\"x()\">safe</p><script>x()</script>", null);

        assertThat(engine.read(states).getHtml()).isEqualTo("<p>safe</p>");
    }

    @Test
    void editKeepsAConcurrentChangeItDidNotSee() {
        List<String> base = apply(List.of(EMPTY_DOC), "<p>first</p><p>second</p>", "T");
        NoteCrdtResultDto theirs = engine.edit(base, "<p>first THEIRS</p><p>second</p>", null);
        NoteCrdtResultDto ours = engine.edit(base, "<p>first</p><p>second OURS</p>", null);

        List<String> merged = plus(plus(base, theirs), ours);

        assertThat(engine.read(merged).getHtml()).isEqualTo("<p>first THEIRS</p><p>second OURS</p>");
    }

    @Test
    void reportsNoChangeOnANoteThatHasDeletions() {
        List<String> base = apply(List.of(EMPTY_DOC), "<p>one</p><p>two</p>", null);
        List<String> deleted = apply(base, "<p>one</p>", null);

        NoteCrdtResultDto same = engine.edit(deleted, "<p>one</p>", null);

        assertThat(same.isChanged()).isFalse();
    }

    @Test
    void editsALargeNoteInsideTheStatementLimit() {
        String paragraphs = IntStream.range(0, 2000).mapToObj(index -> "<p>paragraph " + index + "</p>").collect(Collectors.joining());
        List<String> states = apply(List.of(EMPTY_DOC), paragraphs, null);

        NoteCrdtResultDto edited = engine.edit(states, paragraphs + "<p>end</p>", null);

        assertThat(edited.isChanged()).isTrue();
    }

    @Test
    void runsOnManyThreads() {
        List<CompletableFuture<String>> calls = IntStream.range(0, 8)
                .mapToObj(index -> CompletableFuture.supplyAsync(() -> engine.read(apply(List.of(EMPTY_DOC), "<p>" + index + "</p>", null)).getHtml()))
                .toList();

        List<String> results = calls.stream().map(CompletableFuture::join).toList();

        assertThat(results).containsExactlyInAnyOrderElementsOf(
                IntStream.range(0, 8).mapToObj(index -> "<p>" + index + "</p>").toList());
    }

    @Test
    void failsWhenNoPermitBecomesFreeAndKeepsThePermitCount() {
        NoteCrdtContextPool busyPool = new NoteCrdtContextPool();
        Semaphore permits = (Semaphore) ReflectionTestUtils.getField(busyPool, "permits");
        permits.drainPermits();

        assertThatThrownBy(() -> busyPool.execute("read", "[]")).isInstanceOf(NoteCrdtException.class);

        assertThat(permits.availablePermits()).isZero();
    }

    @Test
    void failsWhenInterruptedWhileWaitingAndKeepsTheInterruptFlag() {
        NoteCrdtContextPool busyPool = new NoteCrdtContextPool();
        Semaphore permits = (Semaphore) ReflectionTestUtils.getField(busyPool, "permits");
        permits.drainPermits();
        Thread.currentThread().interrupt();

        assertThatThrownBy(() -> busyPool.execute("read", "[]")).isInstanceOf(NoteCrdtException.class);

        assertThat(Thread.interrupted()).isTrue();
        assertThat(permits.availablePermits()).isZero();
    }

    @Test
    void closesTheContextOnANonPolyglotFailureAndStaysUsable() {
        NoteCrdtContextPool failingPool = new NoteCrdtContextPool();
        Queue<?> idleContexts = (Queue<?>) ReflectionTestUtils.getField(failingPool, "idleContexts");
        try {
            assertThatThrownBy(() -> failingPool.execute("missingFunction", "[]")).isInstanceOf(NoteCrdtException.class);
            assertThat(idleContexts).isEmpty();

            String result = failingPool.execute("read", "[\"" + EMPTY_DOC + "\"]");

            assertThat(result).isNotBlank();
            assertThat(idleContexts).hasSize(1);
        } finally {
            failingPool.close();
        }
    }

    @Test
    void reportsABundleLoadFailureAndStaysUsable() {
        NoteCrdtContextPool failingPool = new NoteCrdtContextPool();
        Queue<Context> idleContexts = (Queue<Context>) ReflectionTestUtils.getField(failingPool, "idleContexts");
        String states = "[\"" + EMPTY_DOC + "\"]";
        try {
            failingPool.execute("read", states);
            idleContexts.remove().close();
            Object bundle = ReflectionTestUtils.getField(failingPool, "bundle");
            ReflectionTestUtils.setField(failingPool, "bundle", Source.create("js", "throw new Error('broken')"));

            assertThatThrownBy(() -> failingPool.execute("read", states)).isInstanceOf(NoteCrdtException.class);

            ReflectionTestUtils.setField(failingPool, "bundle", bundle);
            assertThat(failingPool.execute("read", states)).isNotBlank();
        } finally {
            failingPool.close();
        }
    }

    @Test
    void rejectsCorruptStateAndStaysUsable() {
        assertThatThrownBy(() -> engine.read(List.of("//////////8="))).isInstanceOf(NoteCrdtException.class);

        assertThat(engine.read(List.of(EMPTY_DOC)).getTitle()).isEmpty();
    }

    private List<String> apply(List<String> states, String html, String title) {
        return plus(states, engine.edit(states, html, title));
    }

    private List<String> plus(List<String> states, NoteCrdtResultDto result) {
        List<String> next = new ArrayList<>(states);
        next.add(result.getUpdate());
        return next;
    }
}
