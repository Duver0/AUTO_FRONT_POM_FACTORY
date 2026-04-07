package utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ImplicitTimeoutUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private ImplicitTimeoutUtils() {
    }

    public static void executeWithDefaultTimeout(Consumer<Integer> setTimeout, Runnable action, Runnable resetTimeout) {
        executeWithTimeout(DEFAULT_TIMEOUT_SECONDS, setTimeout, action, resetTimeout);
    }

    public static <T> T supplyWithDefaultTimeout(Consumer<Integer> setTimeout, Supplier<T> action, Runnable resetTimeout) {
        return supplyWithTimeout(DEFAULT_TIMEOUT_SECONDS, setTimeout, action, resetTimeout);
    }

    public static void executeWithTimeout(int timeoutSeconds, Consumer<Integer> setTimeout, Runnable action, Runnable resetTimeout) {
        setTimeout.accept(timeoutSeconds);
        try {
            action.run();
        } finally {
            resetTimeout.run();
        }
    }

    public static <T> T supplyWithTimeout(int timeoutSeconds, Consumer<Integer> setTimeout, Supplier<T> action, Runnable resetTimeout) {
        setTimeout.accept(timeoutSeconds);
        try {
            return action.get();
        } finally {
            resetTimeout.run();
        }
    }
}