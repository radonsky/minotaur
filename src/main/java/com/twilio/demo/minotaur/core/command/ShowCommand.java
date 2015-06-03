package com.twilio.demo.minotaur.core.command;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.resources.MediaResource;

public class ShowCommand extends AbstractCommand {

    public ShowCommand(final MazeRegistry mazeRegistry) {
        super(mazeRegistry);
    }

    @Override
    public Response action(final String userId) {
        final Maze maze = super.mazeRegistry.get(userId);
        final URI uri = UriBuilder.fromResource(MediaResource.class).queryParam("From", userId).build();
        return new MultimediaResponse(maze.getDirections(), uri);
    }

}
