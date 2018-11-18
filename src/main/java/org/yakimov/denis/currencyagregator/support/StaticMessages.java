package org.yakimov.denis.currencyagregator.support;

public class StaticMessages {
    public static final String HISTORY_UNKNOWN_MESSAGE = "Unknown action for two currency values";
    public static final String ENABLED ="enabled";
    public static final String DISABLED ="disabled";
    public static final String CHECK_USERS_SQL = "SELECT count(*) from users";
    public static final String CHECK_CURRENCIES_SQL = "SELECT count(*) from currencyvalue";
    public static final String MESSAGE_ILLEGAL_CURRENCY_NAME = "There are no currency with such name: %s";
    public static final String MESSAGE_ILLEGAL_CURRENCY_CREATION = "Can't create currency for: %s";
    public static final String EMPTY_VALUE = "-";
    public static final String NO_FLAGS = "No allow or delete flag present - what do you wanted to do with data?";
}
