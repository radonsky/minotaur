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

    public String getDirections() {
        final List<Direction> list = this.stateMachine.getPermittedTriggers();
        final StringBuilder bldr = new StringBuilder("You can go to the ");
        bldr.append(list.get(0));
        for (int i = 1; i < list.size() - 1; i++) {
            bldr.append(", ");
            bldr.append(list.get(i));
        }
        if (list.size() > 1) {
            bldr.append(" and ");
            bldr.append(list.get(list.size() - 1));
        }
        bldr.append(".");
        return bldr.toString();
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
