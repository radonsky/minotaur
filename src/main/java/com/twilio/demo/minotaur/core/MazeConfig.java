package com.twilio.demo.minotaur.core;

import static com.twilio.demo.minotaur.core.MazeConfig.Direction.EAST;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.NORTH;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.SOUTH;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.WEST;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.EXIT;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE11;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE12;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE13;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE21;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE22;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE23;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE31;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE32;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE33;

import com.github.oxo42.stateless4j.StateMachineConfig;


public class MazeConfig {

    public enum Space {
        /*+--------------+--------------+-------------+*/
        /*|*/ SPACE11, /* */ SPACE12, /* */ SPACE13 /*|*/,
        /*+--------------+              +-------------+*/
        /* */ SPACE21, /*|*/ SPACE22, /* */ SPACE23 /*|*/,
        /*+              +--------------+             +*/
        /*|*/ SPACE31, /* */ SPACE32, /* */ SPACE33 /*|*/,
        /*+-------------------------------------------+*/
        EXIT
    }

    public enum Direction {
        WEST, SOUTH, EAST, NORTH;

        @Override
        public String toString() {
            switch (this) {
            case WEST: return "West";
            case SOUTH: return "South";
            case EAST: return "East";
            case NORTH: return "North";
            default:
                return this.name();
            }
        }
    }

    private final StateMachineConfig<Space, Direction> config = new StateMachineConfig<>();

    public MazeConfig() {
        this.config.configure(SPACE11)
        .permit(EAST, SPACE12);
        this.config.configure(SPACE12)
        .permit(WEST, SPACE11)
        .permit(SOUTH, SPACE22)
        .permit(EAST, SPACE13);
        this.config.configure(SPACE13)
        .permit(WEST, SPACE12);
        this.config.configure(SPACE21)
        .permit(WEST, EXIT)
        .permit(SOUTH, SPACE31);
        this.config.configure(SPACE22)
        .permit(NORTH, SPACE12)
        .permit(EAST, SPACE23);
        this.config.configure(SPACE23)
        .permit(SOUTH, SPACE33)
        .permit(WEST, SPACE22);
        this.config.configure(SPACE31)
        .permit(NORTH, SPACE21)
        .permit(EAST, SPACE32);
        this.config.configure(SPACE32)
        .permit(EAST, SPACE33)
        .permit(WEST, SPACE31);
        this.config.configure(SPACE33)
        .permit(NORTH, SPACE23)
        .permit(WEST, SPACE32);
    }

    public StateMachineConfig<Space, Direction> getConfig() {
        return this.config;
    }

}
