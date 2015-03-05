package com.twilio.demo.minotaur.core;

import static com.twilio.demo.minotaur.core.MazeConfig.Space.*;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.*;

import com.github.oxo42.stateless4j.StateMachineConfig;


public class MazeConfig {

    public enum Space {
                /*+--------------+--------------+-------------+*/
                /*|*/ SPACE11, /* */ SPACE12, /* */ SPACE13 /*|*/,
                /*+--------------+              +-------------+*/
          EXIT, /* */ SPACE21, /*|*/ SPACE22, /* */ SPACE23 /*|*/,
                /*+              +--------------+             +*/
                /*|*/ SPACE31, /* */ SPACE32, /* */ SPACE33 /*|*/,
                /*+-------------------------------------------+*/
    }

    public enum Direction {
        WEST, SOUTH, EAST, NORTH
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
        return config;
    }

}
