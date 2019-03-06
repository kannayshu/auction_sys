package com.auction_sys.exception;

/**
 * Created By liuda on 2018/7/12
 */
public class RefundUnkownException extends RuntimeException{
    public RefundUnkownException() {
    }

    public RefundUnkownException(String message) {
        super(message);
    }

    public RefundUnkownException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefundUnkownException(Throwable cause) {
        super(cause);
    }

    public RefundUnkownException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
