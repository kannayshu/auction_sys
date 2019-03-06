package com.auction_sys.exception;

/**
 * Created By liuda on 2018/7/12
 */
public class DatabaseUpdateErrorExecption extends RuntimeException{
    public DatabaseUpdateErrorExecption() {
    }

    public DatabaseUpdateErrorExecption(String message) {
        super(message);
    }

    public DatabaseUpdateErrorExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseUpdateErrorExecption(Throwable cause) {
        super(cause);
    }

    public DatabaseUpdateErrorExecption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
