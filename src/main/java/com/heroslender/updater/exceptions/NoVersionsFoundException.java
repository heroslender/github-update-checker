package com.heroslender.updater.exceptions;

public class NoVersionsFoundException extends RuntimeException {
    public NoVersionsFoundException() {
        super("No versions found in the repository");
    }
}
