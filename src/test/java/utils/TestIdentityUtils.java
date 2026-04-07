package utils;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class TestIdentityUtils {

    private static final String TESTMAIL_DOMAIN = "testmail.local";

    private TestIdentityUtils() {
    }

    public static String buildUniqueEmail(String seed) {
        String normalizedSeed = sanitizeSeed(seed);
        long now = System.currentTimeMillis();
        int randomSuffix = ThreadLocalRandom.current().nextInt(1000, 9999);
        return String.format(Locale.ROOT, "%s_%d_%d@%s", normalizedSeed, now, randomSuffix, TESTMAIL_DOMAIN);
    }

    public static String buildUniqueName(String seed) {
        String normalizedSeed = sanitizeSeed(seed);
        long now = System.currentTimeMillis();
        return String.format(Locale.ROOT, "%s_%d", normalizedSeed, now);
    }

    public static boolean isTestmailAddress(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return email.trim().toLowerCase(Locale.ROOT).endsWith("@" + TESTMAIL_DOMAIN);
    }

    private static String sanitizeSeed(String seed) {
        if (seed == null) {
            return "usuario";
        }

        String normalized = seed.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(Locale.ROOT);
        return normalized.isBlank() ? "usuario" : normalized;
    }
}