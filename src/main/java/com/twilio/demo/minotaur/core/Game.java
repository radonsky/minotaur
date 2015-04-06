package com.twilio.demo.minotaur.core;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.twilio.demo.minotaur.core.MazeConfig.Direction;
import com.twilio.demo.minotaur.core.command.Command;
import com.twilio.demo.minotaur.core.command.InvalidCommand;
import com.twilio.demo.minotaur.core.command.MoveCommand;
import com.twilio.demo.minotaur.core.command.ShowCommand;
import com.twilio.demo.minotaur.core.command.StartCommand;

public class Game {
    private final UserRegistry userRegistry;
    private final MazeRegistry mazeRegistry;
    private final LinkedHashMap<String, Command> commands = new LinkedHashMap<String, Command>();

    public Game(final UserRegistry userRegistry, final MazeRegistry mazeRegistry) {
        this.userRegistry = userRegistry;
        this.mazeRegistry = mazeRegistry;
        this.commands.put("START", new StartCommand(this.mazeRegistry));
        this.commands.put("W", new MoveCommand(this.mazeRegistry, Direction.WEST));
        this.commands.put("S", new MoveCommand(this.mazeRegistry, Direction.SOUTH));
        this.commands.put("N", new MoveCommand(this.mazeRegistry, Direction.NORTH));
        this.commands.put("E", new MoveCommand(this.mazeRegistry, Direction.EAST));
        this.commands.put("SHOW", new ShowCommand());
    }

    public Command parseCommand(final String userId, final String body) {
        if (this.userRegistry.getUser(userId) == null) {
            try {
                this.userRegistry.addUser(userId, body);
                return this.commands.get("START");
            } catch (final IllegalArgumentException e) {
                return new InvalidCommand("I'm sorry but user " + body + " has been already registered. Please choose a different name.");
            }
        } else {
            for (final Entry<String, Command> e : this.commands.entrySet()) {
                if (e.getKey().equals(body.trim().toUpperCase())) {
                    return e.getValue();
                }
            }
            return new InvalidCommand("I'm sorry, I don't know this command. The valid commands are: " + this.commands.keySet());
        }
    }


}
