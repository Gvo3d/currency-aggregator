package org.yakimov.denis.currencyagregator.support;

public class WrongIncomingDataException extends Exception {
    public WrongIncomingDataException() {
    }

    public WrongIncomingDataException(String message) {
        super(message);
    }

    public WrongIncomingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
