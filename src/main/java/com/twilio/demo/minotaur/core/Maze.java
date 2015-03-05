package com.twilio.demo.minotaur.core;

import java.util.List;

import com.github.oxo42.stateless4j.StateMachine;
import com.twilio.demo.minotaur.core.MazeConfig.Direction;
import com.twilio.demo.minotaur.core.MazeConfig.Space;

public class Maze {

    private final StateMachine<Space, Direction> stateMachine;

    public Maze(final MazeConfig mazeConfig) {
        this.stateMachine = new StateMachine<>(Space.SPACE11, mazeConfig.getConfig());
    }

    public List<Direction> getPermittedDirections() {
        return this.stateMachine.getPermittedTriggers();
    }

    public boolean move(final Direction dir) {
        if (this.stateMachine.canFire(dir)) {
            this.stateMachine.fire(dir);
            return true;
        } else {
            return false;
        }
    }

    public boolean isInExitState() {
        return this.stateMachine.isInState(Space.EXIT);
    }

}
