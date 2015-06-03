package com.twilio.demo.minotaur.core.command;

import java.net.URI;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.core.UserRegistry;

public class StartCommand extends AbstractCommand {

    private static final URI WELCOME_URI = URI.create("http://upload.wikimedia.org/wikipedia/commons/e/e5/Minotaurus.gif");

    private final UserRegistry userRegistry;

    public StartCommand(final MazeRegistry mazeRegistry, final UserRegistry userRegistry) {
        super(mazeRegistry);
        this.userRegistry = userRegistry;
    }

    @Override
    public Response action(final String userId) {
        final Maze maze = super.mazeRegistry.start(userId);
        final String user = this.userRegistry.getUser(userId);
        return new MultimediaResponse("Welcome to the Labyrinth, " + user + ". "
                + "Type N, S, E, W to move around and find your way out. "
                + "Type SHOW to see the maze, ? for help. Beware of the Minotaur! " + maze.getDirections(),
                WELCOME_URI
                );
    }

}
