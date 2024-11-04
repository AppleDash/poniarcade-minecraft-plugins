package com.poniarcade.core.utils;

public final class BooleanUtil {
    private static final String[] TRUTHY_VALUES = {"1", "true", "on", "yes", "enable", "enabled", "aye", "yep", "oui"};
    private static final String[] FALSY_VALUES = {"0", "false", "off", "no", "disable", "disabled", "nay", "nope", "non"};
    public static final String TRUTHY_VALUES_STR = StringUtil.combine(BooleanUtil.TRUTHY_VALUES, "|");
    public static final String FALSY_VALUES_STR = StringUtil.combine(BooleanUtil.FALSY_VALUES, "|");

	private BooleanUtil() {
	}

	public static boolean isTruthy(String value) {
        for (String s : BooleanUtil.TRUTHY_VALUES) {
            if (value.equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }

    /* This isn't just !isTruthy() because we don't want to just consider any non-true arg to be false. */
    public static boolean isFalsy(String value) {
        for (String s : BooleanUtil.FALSY_VALUES) {
            if (value.equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }

    /* Used when we don't care if it's a valid value, just whether it's true or not. */
    public static boolean parse(String s) {
        return BooleanUtil.isTruthy(s);
    }

    public static AlmostBoolean parseHard(String s) {
        if (BooleanUtil.isTruthy(s)) {
            return AlmostBoolean.TRUE;
        }

        if (BooleanUtil.isFalsy(s)) {
            return AlmostBoolean.FALSE;
        }

        return AlmostBoolean.NULL;
    }

    public static String truthyValues() {
        return StringUtil.combine(BooleanUtil.TRUTHY_VALUES, "|");
    }

    public static String falsyValues() {
        return StringUtil.combine(BooleanUtil.FALSY_VALUES, "|");
    }

    public enum AlmostBoolean {
        TRUE(true), FALSE(false), NULL(null);

        private final Boolean bool;

        AlmostBoolean(Boolean bool) {
            this.bool = bool;
        }

        public boolean getBool() {
            return this.bool;
        }
    }
}
