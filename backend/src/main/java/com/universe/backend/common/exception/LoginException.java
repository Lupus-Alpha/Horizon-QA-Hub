package com.universe.backend.common.exception;

public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }

    private LoginException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new LoginException(message);
    }

    public static LoginException getException(String message) {
        throw new LoginException(message);
    }

    public static void throwException(Throwable t) {
        throw new LoginException(t);
    }

}
