package com.universe.backend.common.exception;


public class FileException extends RuntimeException{
    public FileException(String message) {
        super(message);
    }

    private FileException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new FileException(message);
    }

    public static FileException getException(String message) {
        throw new FileException(message);
    }

    public static void throwException(Throwable t) {
        throw new FileException(t);
    }

}
