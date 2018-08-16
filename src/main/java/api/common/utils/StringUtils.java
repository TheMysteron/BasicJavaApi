package api.common.utils;

/**
 * StringUtils - wrapper class providing utility methods to get or manipulate strings
 */
public class StringUtils {

    /**
     * Trims a field if it is not null
     *
     * @param s - a string
     * @return the trimmed string, or itself if null
     */
    public static String trim(String s) {
        if (s != null) {
            s = s.trim();
        }
        return s;
    }

    /**
     * Checks if the first string is null, and returns itself if not, or the second if it is.
     *
     * @param s     - the string to check if null
     * @param other - the string to return if the first is null
     * @return the first string if not null, the second if the it is.
     */
    public static String ifNull(String s, String other) {
        if (s == null || s.equals("")) {
            return other;
        }
        return s;
    }
}
