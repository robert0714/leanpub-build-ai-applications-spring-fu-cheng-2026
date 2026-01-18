package dev.danvega.javaone.service;

/**
 * Utility class for String operations.
 */
public final class StringUtils {

    private StringUtils() {
        // Utility class
    }

    /**
     * Removes the specified prefix from the beginning of the string.
     * If the string does not start with the prefix, returns the original string.
     * 
     * @param str the string to process
     * @param prefix the prefix to remove
     * @return the string with the prefix removed, or the original string if it doesn't start with the prefix
     */
    public static String removeStart(String str, String prefix) {
        if (str == null || prefix == null) {
            return str;
        }
        if (str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }

    /**
     * Removes the specified suffix from the end of the string.
     * If the string does not end with the suffix, returns the original string.
     * 
     * @param str the string to process
     * @param suffix the suffix to remove
     * @return the string with the suffix removed, or the original string if it doesn't end with the suffix
     */
    public static String removeEnd(String str, String suffix) {
        if (str == null || suffix == null) {
            return str;
        }
        if (str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length());
        }
        return str;
    }

    /**
     * Checks if a string is null or empty.
     * 
     * @param str the string to check
     * @return true if the string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a string is not null and not empty.
     * 
     * @param str the string to check
     * @return true if the string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
