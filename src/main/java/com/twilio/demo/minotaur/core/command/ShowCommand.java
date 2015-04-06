package com.twilio.demo.minotaur.core.command;

public class ShowCommand implements Command {

    @Override
    public Response action(final String userId) {
        return new SimpleResponse("Here is an image of the maze.");
    }

}
