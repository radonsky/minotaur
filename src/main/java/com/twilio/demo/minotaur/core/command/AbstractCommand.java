package com.twilio.demo.minotaur.core.command;

import com.twilio.demo.minotaur.core.MazeRegistry;

public abstract class AbstractCommand implements Command {

    protected final MazeRegistry mazeRegistry;

    protected AbstractCommand(final MazeRegistry mazeRegistry) {
        this.mazeRegistry = mazeRegistry;
    }
}
