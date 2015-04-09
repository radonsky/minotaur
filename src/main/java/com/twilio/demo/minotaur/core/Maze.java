package com.twilio.demo.minotaur.core;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.google.common.collect.Sets;
import com.twilio.demo.minotaur.core.MazeConfig.Direction;
import com.twilio.demo.minotaur.core.MazeConfig.Space;

public class Maze {

    private final MazeConfig mazeConfig;
    private final StateMachine<Space, Direction> stateMachine;
    private final Set<Space> visitedStates;

    public Maze(final MazeConfig mazeConfig) {
        this.mazeConfig = mazeConfig;
        final Space initialState = mazeConfig.getInitialSpace();
        this.stateMachine = new StateMachine<>(initialState, mazeConfig.configure(this::onEntry));
        this.visitedStates = Sets.newLinkedHashSet(Collections.singleton(initialState));
    }

    public void onEntry(final Transition<Space, Direction> transition) {
        this.visitedStates.add(transition.getDestination());
    }

    public String getDirections() {
        final List<Direction> list = this.stateMachine.getPermittedTriggers();
        final StringBuilder bldr = new StringBuilder();
        if (list.size() > 0) {
            bldr.append("You can go to the ");
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
        }
        return bldr.toString();
    }

    public String getDescription() {
        if (this.stateMachine.getState() == this.mazeConfig.getFinalSpace()) {
            return "Congratulations, you found your way out! Type Start to start over.";
        } else {
            return "";
        }
    }

    public boolean move(final Direction dir) {
        if (this.stateMachine.canFire(dir)) {
            this.stateMachine.fire(dir);
            return true;
        } else {
            return false;
        }
    }

    public boolean isInState(final Space space) {
        return this.stateMachine.isInState(space);
    }

    public MazeConfig getConfig() {
        return this.mazeConfig;
    }

    public boolean isVisitedState(final Space space) {
        return this.visitedStates.contains(space);
    }

}
