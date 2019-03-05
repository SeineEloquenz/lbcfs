package de.seine_eloquenz.lbcfs.utils;

import java.util.StringJoiner;

/**
 * Class containing utils for Strings
 */
public final class StringUtils {

    /**
     * Concatenates the string array to a message using blanks as delimiters
     * @param args args to concat
     * @return concatenated string
     */
    public static String createMessageFromArgs(final String[] args) {
        return createMessageFromArgs(args, 0, args.length - 1);
    }

    /**
     * Concatenates the string array to a message using blanks as delimiters
     * @param args args to concat
     * @param beginIndex first index to include in concatenation
     * @return concatenated string
     */
    public static String createMessageFromArgs(final String[] args, final int beginIndex) {
        return createMessageFromArgs(args, beginIndex, args.length - 1);
    }

    /**
     * Concatenates the string array to a message using blanks as delimiters
     * @param args args to concat
     * @param beginIndex first index to include in concatenation
     * @param endIndex last index to include in concatenation
     * @return concatenated string
     */
    public static String createMessageFromArgs(final String[] args, final int beginIndex, final int endIndex) {
        if (beginIndex < 0) {
            throw new IllegalArgumentException("Begin index may not be smaller than 0");
        } else if (beginIndex >= args.length) {
            throw new IllegalArgumentException("Begin index may not be larger than the last element of the array");
        } else if (beginIndex > endIndex) {
            throw new IllegalArgumentException("Begin index must be smaller than the end index");
        } else if (endIndex >= args.length) {
            throw new IllegalArgumentException("End index must not be larger than the last element of the array");
        }
        final StringJoiner joiner = new StringJoiner(" ");
        for (int i = beginIndex; i <= endIndex; i++) {
            joiner.add(args[i]);
        }
        return joiner.toString();
    }

    private StringUtils() {

    }
}
