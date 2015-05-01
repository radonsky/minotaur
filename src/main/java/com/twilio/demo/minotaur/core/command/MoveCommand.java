package com.twilio.demo.minotaur.core.command;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeConfig.Direction;
import com.twilio.demo.minotaur.core.MazeRegistry;

public class MoveCommand extends AbstractCommand {

    private final Direction direction;

    public MoveCommand(final MazeRegistry mazeRegistry, final Direction direction) {
        super(mazeRegistry);
        this.direction = direction;
    }

    @Override
    public Response action(final String userId) {
        final Maze maze = super.mazeRegistry.get(userId);
        final boolean valid = maze.move(this.direction);
        if (maze.isKilledByMinotaur()) {
            return new SimpleResponse("You have been killed by Minotaur! Type START to start over.");
        }
        maze.minotaurMove();
        if (!valid) {
            return new SimpleResponse("I'm sorry, you can't go there! " + maze.getDirections());
        } else {
            return new SimpleResponse(maze.getDescription() + " " + maze.getDirections());
        }
    }

}
