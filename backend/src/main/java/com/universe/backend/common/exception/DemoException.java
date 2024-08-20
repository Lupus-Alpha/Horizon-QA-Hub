package com.universe.backend.common.exception;


public class DemoException extends RuntimeException{
    public DemoException(String message) {
        super(message);
    }

    private DemoException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new DemoException(message);
    }

    public static DemoException getException(String message) {
        throw new DemoException(message);
    }

    public static void throwException(Throwable t) {
        throw new DemoException(t);
    }

}
