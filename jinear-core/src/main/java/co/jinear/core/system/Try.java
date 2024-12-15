package co.jinear.core.system;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Try<T> {

    private final boolean success;

    public Try(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isFailure() {
        return this.success == false;
    }

    public abstract Throwable getThrownMessage();

    public abstract void throwIfFailed();

    public abstract T get();

    public abstract <U> Try<U> map(Function<? super T, ? extends U> fn);

    public abstract <U> Try<U> flatMap(Function<? super T, Try<U>> fn);

    static <T> Try<T> failure(Throwable t) {
        return new Failure<>(t);
    }

    static <V> Success<V> success(V value) {
        return new Success<>(value);
    }

    static Success success() {
        return new Success<>();
    }

    public static <T> Try<T> of(Supplier<T> fn) {
        Objects.requireNonNull(fn);
        try {
            return Try.success(fn.get());
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    public static Try of(Runnable fn) {
        Objects.requireNonNull(fn);
        try {
            fn.run();
            return Try.success();
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    static class Failure<T> extends Try<T> {

        private final RuntimeException exception;

        public Failure(Throwable t) {
            super(false);
            this.exception = new RuntimeException(t);
        }

        @Override
        public T get() {
            throw this.exception;
        }

        @Override
        public <U> Try<U> map(Function<? super T, ? extends U> fn) {
            Objects.requireNonNull(fn);
            return Try.failure(this.exception);
        }

        @Override
        public <U> Try<U> flatMap(Function<? super T, Try<U>> fn) {
            Objects.requireNonNull(fn);
            return Try.failure(this.exception);
        }

        @Override
        public Throwable getThrownMessage() {
            return this.exception;
        }

        @Override
        public void throwIfFailed() {
            if (Objects.nonNull(exception)) {
                throw exception;
            }
        }
    }

    static class Success<T> extends Try<T> {

        private final T value;

        public Success() {
            super(true);
            this.value = null;
        }

        public Success(T value) {
            super(true);
            this.value = value;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public <U> Try<U> map(Function<? super T, ? extends U> fn) {
            Objects.requireNonNull(fn);
            try {
                return Try.success(fn.apply(this.value));
            } catch (Throwable t) {
                return Try.failure(t);
            }
        }

        @Override
        public <U> Try<U> flatMap(Function<? super T, Try<U>> fn) {
            Objects.requireNonNull(fn);
            try {
                return fn.apply(this.value);
            } catch (Throwable t) {
                return Try.failure(t);
            }
        }

        @Override
        public Throwable getThrownMessage() {
            throw new IllegalStateException("Success never has an exception");
        }

        @Override
        public void throwIfFailed() {
        }
    }
}