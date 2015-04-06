package com.twilio.demo.minotaur.core.command;

public class SimpleResponse implements Response {

    private final String message;

    public SimpleResponse(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
