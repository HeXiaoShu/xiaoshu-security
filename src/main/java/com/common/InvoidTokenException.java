package com.common;

/**
 * token失效异常
 * @author xiaoshu
 */
public class InvoidTokenException extends RuntimeException{
    private static final long serialVersionUID = 509751730847093927L;

    public InvoidTokenException() {}

    public InvoidTokenException(String message) {
        super(message);
    }

    public InvoidTokenException(Throwable throwable) {
        super(throwable);
    }

    public InvoidTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InvoidTokenException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
