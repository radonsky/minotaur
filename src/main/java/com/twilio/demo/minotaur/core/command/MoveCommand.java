package com.twilio.demo.minotaur.core.command;

import java.net.URI;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeConfig.Direction;
import com.twilio.demo.minotaur.core.MazeRegistry;

public class MoveCommand extends AbstractCommand {

    private static final URI KILLED_URI = URI.create("https://upload.wikimedia.org/wikipedia/commons/d/db/Blake_Dante_Hell_XII.jpg");
    private static final URI FINAL_URI = URI.create("https://upload.wikimedia.org/wikipedia/commons/9/90/Theseus_Minotaur_Mosaic.jpg");

    private final Direction direction;


    public MoveCommand(final MazeRegistry mazeRegistry, final Direction direction) {
        super(mazeRegistry);
        this.direction = direction;
    }

    @Override
    public Response action(final String userId) {
        final Maze maze = super.mazeRegistry.get(userId);
        if (maze.isKilledByMinotaur()) {
            return new SimpleResponse("You have been killed by Minotaur! Type Start to start over.");
        }
        final boolean valid = maze.move(this.direction);
        if (maze.isKilledByMinotaur()) {
            return new MultimediaResponse("You have been killed by Minotaur! Type Start to start over.", KILLED_URI);
        }
        maze.minotaurMove();
        if (!valid) {
            return new SimpleResponse("I'm sorry, you can't go there! " + maze.getDirections());
        } else {
            if (maze.isMinotaurVisible()) {
                return new SimpleResponse("Run! Minotaur is right next to you! " + maze.getDirections());
            }
            if (maze.isInState(maze.getConfig().getFinalSpace())) {
                return new MultimediaResponse("Congratulations, you found your way out! Type Start to start over.", FINAL_URI);
            } else {
                return new SimpleResponse(maze.getDirections());
            }
        }
    }

}
