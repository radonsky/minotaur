package com.twilio.demo.minotaur.core.command;

public class InvalidCommand implements Command {

    private final String errorMessage;

    public InvalidCommand(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Response action(final String userId) {
        return new SimpleResponse(this.errorMessage);
    }

}
