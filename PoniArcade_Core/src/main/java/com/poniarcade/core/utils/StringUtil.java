package com.poniarcade.core.utils;

import com.google.common.base.Strings;

import java.util.List;

public final class StringUtil {
	private StringUtil() {
	}

	/**
     * Take a string array, starting at the specified index, and copy all elements including and after
     * the start index into a new array. This is the old popStringArr()
     *
     * @param array      Array of strings
     * @param startIndex Start index
     * @return New array
     */
    public static String[] arraySubset(String[] array, int startIndex) {
        try {
            String[] tmp = new String[array.length - startIndex];

            System.arraycopy(array, startIndex, tmp, 0, tmp.length);

            return tmp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Take a string array, starting at the specified index, and combine all elements including and after
     * the start index into a space separated string. This is the old popString()
     *
     * @param array      Array of strings
     * @param startIndex Start index
     * @return Space-separated string
     */
    public static String implodeString(String[] array, int startIndex) {
        return StringUtil.combine(StringUtil.arraySubset(array, startIndex), " ");
    }

    public static String combine(String[] array, String glue) {
        if (array.length == 0) {
            return "";
        }

        try {
            StringBuilder returnable = new StringBuilder();
            for (String line : array) {
                if (!line.isEmpty()) {
                    returnable.append(glue).append(line);
                }
            }
            return returnable.substring(glue.length());
        } catch (Exception e) {
            throw new RuntimeException("Failed to combine strings", e);
        }
    }

    public static boolean matchAll(String in, String... strings) {
        for (String s : strings) {
            if (!s.equalsIgnoreCase(in)) {
                return false;
            }
        }
        return true;
    }

    public static boolean matchAny(String in, String... strings) {
        for (String s : strings) {
            if (s.equalsIgnoreCase(in)) {
                return true;
            }
        }
        return false;
    }

    public static String commafy(Object... objects) {
        StringBuilder ret = new StringBuilder();
        for (Object o : objects) {
            String s = o.toString();
            if (s.equalsIgnoreCase(objects[objects.length - 1].toString())) {
                ret.append(s).append(".");
                break;
            }
            ret.append(s).append(", ");
        }
        return ret.toString();
    }

    public static String commafy(List<?> list) {
        return StringUtil.commafy(list.toArray(new Object[0]));
    }

    public static String[] toStringArr(Object[] objects) {
        String[] ret = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            ret[i] = objects[i].toString();
        }
        return ret;
    }

    public static String capitalizeWord(String word) {
        if (Strings.isNullOrEmpty(word)) {
            return word;
        }

        if (word.length() == 1) {
            return word.toUpperCase();
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}

