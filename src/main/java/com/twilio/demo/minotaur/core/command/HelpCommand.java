package com.twilio.demo.minotaur.core.command;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeRegistry;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(final MazeRegistry mazeRegistry) {
        super(mazeRegistry);
    }

    @Override
    public Response action(final String userId) {
        final Maze maze = super.mazeRegistry.get(userId);
        if (maze.isInState(maze.getConfig().getFinalSpace())) {
            return new SimpleResponse("Congratulations, you found your way out! Type Start to start over.");
        } else if (maze.isKilledByMinotaur()) {
            return new SimpleResponse("You have been killed by Minotaur! Type Start to start over.");
        } else {
            return new SimpleResponse("Type N, S, E, W to move to the North, South, East or West. Type Show to see a map of the Labyrinth.");
        }
    }

}
