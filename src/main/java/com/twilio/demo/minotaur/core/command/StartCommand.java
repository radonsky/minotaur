package com.twilio.demo.minotaur.core.command;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeRegistry;

public class StartCommand extends AbstractCommand {

    public StartCommand(final MazeRegistry mazeRegistry) {
        super(mazeRegistry);
    }

    @Override
    public Response action(final String userId) {
        final Maze maze = super.mazeRegistry.start(userId);
        return new SimpleResponse("Welcome to the Labyrinth. Type N, S, E, W to move around this maze and find your way out. " + maze.getDirections());
    }

}
