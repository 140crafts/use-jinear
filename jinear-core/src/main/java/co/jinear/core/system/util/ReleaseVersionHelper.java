package co.jinear.core.system.util;

import java.util.Objects;
import java.util.regex.Pattern;

public class ReleaseVersionHelper {

    public static final String RELEASE_VERSION_PATTERN = "^v\\d{1,9}(\\.\\d{1,9}){1,3}$";

    private static final Pattern RELEASE_VERSION = Pattern.compile(RELEASE_VERSION_PATTERN);

    private ReleaseVersionHelper() {
    }

    public static boolean isRelease(String version) {
        return Objects.nonNull(version) && RELEASE_VERSION.matcher(version).matches();
    }

    public static boolean isNewer(String candidate, String current) {
        if (!isRelease(candidate) || !isRelease(current)) {
            return false;
        }
        long[] candidateSegments = segments(candidate);
        long[] currentSegments = segments(current);
        int length = Math.max(candidateSegments.length, currentSegments.length);
        for (int i = 0; i < length; i++) {
            long candidateSegment = i < candidateSegments.length ? candidateSegments[i] : 0L;
            long currentSegment = i < currentSegments.length ? currentSegments[i] : 0L;
            if (candidateSegment != currentSegment) {
                return candidateSegment > currentSegment;
            }
        }
        return false;
    }

    private static long[] segments(String version) {
        String[] parts = version.substring(1).split("\\.");
        long[] segments = new long[parts.length];
        for (int i = 0; i < parts.length; i++) {
            segments[i] = Long.parseLong(parts[i]);
        }
        return segments;
    }
}
