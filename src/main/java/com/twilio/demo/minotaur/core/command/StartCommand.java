package com.twilio.demo.minotaur.core.command;

import java.net.URI;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeRegistry;

public class StartCommand extends AbstractCommand {

    private static final URI WELCOME_URI = URI.create("http://upload.wikimedia.org/wikipedia/commons/e/e5/Minotaurus.gif");

    public StartCommand(final MazeRegistry mazeRegistry) {
        super(mazeRegistry);
    }

    @Override
    public Response action(final String userId) {
        final Maze maze = super.mazeRegistry.start(userId);
        return new MultimediaResponse("Welcome to the Labyrinth. "
                + "Type N, S, E, W to move around this maze and find your way out. "
                + "Type SHOW to see the maze. " + maze.getDirections(),
                WELCOME_URI
                );
    }

}
