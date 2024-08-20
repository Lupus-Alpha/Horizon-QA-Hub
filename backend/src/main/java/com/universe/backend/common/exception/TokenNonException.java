package com.universe.backend.common.exception;

public class TokenNonException extends RuntimeException{
    public TokenNonException(String message){super(message);}

    private TokenNonException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new TokenNonException(message);
    }

    public static TokenNonException getException(String message) {
        throw new TokenNonException(message);
    }

    public static void throwException(Throwable t) {
        throw new TokenNonException(t);
    }
}
