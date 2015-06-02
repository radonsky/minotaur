package com.twilio.demo.minotaur.core.command;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.twilio.demo.minotaur.resources.MediaResource;

public class ShowCommand implements Command {

    @Override
    public Response action(final String userId) {
        final URI uri = UriBuilder.fromResource(MediaResource.class).queryParam("From", userId).build();
        return new MultimediaResponse("Here is an image of the maze.", uri);
    }

}
