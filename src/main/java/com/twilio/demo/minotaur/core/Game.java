package com.twilio.demo.minotaur.core;

import com.twilio.demo.minotaur.core.MazeConfig.Direction;

public class Game {
    private final UserRegistry userRegistry;
    private final MazeRegistry mazeRegistry;

    public Game(final UserRegistry userRegistry, final MazeRegistry mazeRegistry) {
        this.userRegistry = userRegistry;
        this.mazeRegistry = mazeRegistry;
    }

    public String command(final String userId, final String body) {
        if (this.userRegistry.getUser(userId) == null) {
            try {
                this.userRegistry.addUser(userId, body);
                final Maze maze = this.mazeRegistry.start(userId);
                return "Hello " + body + "! Welcome to the Labyrinth. Type N, S, E, W to move around this maze and find your way out. " + maze.getDirections();
            } catch (final IllegalArgumentException e) {
                return "I'm sorry but user " + body + " has been already registered. Please choose a different name.";
            }
        }
        return createMessageFrom(userId, body);
    }

    private String createMessageFrom(final String phone, final String command) {
        Maze maze = this.mazeRegistry.get(phone);
        final StringBuilder bldr = new StringBuilder();
        boolean valid = true;
        switch (command.trim().toUpperCase()) {
        case "START":
            maze = this.mazeRegistry.start(phone);
            break;
        case "W":
            valid = maze.move(Direction.WEST);
            break;
        case "S":
            valid = maze.move(Direction.SOUTH);
            break;
        case "N":
            valid = maze.move(Direction.NORTH);
            break;
        case "E":
            valid = maze.move(Direction.EAST);
            break;
        default:
            bldr.append("I'm sorry, I don't know where you want to go. Type N, S, E, W to move around this maze and find your way out.");
        }
        if (!valid) {
            bldr.append("I'm sorry, you can't go there! ");
            bldr.append(maze.getDirections());
        } else {
            bldr.append(maze.getDescription());
            bldr.append(' ');
            bldr.append(maze.getDirections());
        }
        return bldr.toString();
    }


}
