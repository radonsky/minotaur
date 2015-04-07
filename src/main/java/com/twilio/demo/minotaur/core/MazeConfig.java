package com.twilio.demo.minotaur.core;

import static com.twilio.demo.minotaur.core.MazeConfig.Direction.EAST;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.NORTH;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.SOUTH;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.WEST;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE11;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE12;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE13;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE21;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE22;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE23;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE31;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE32;
import static com.twilio.demo.minotaur.core.MazeConfig.Space.SPACE33;

import java.util.EnumSet;
import java.util.Set;

import com.github.oxo42.stateless4j.StateConfiguration;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;


public class MazeConfig {

    public enum Space {
        SPACE11(""),
        SPACE12(""),
        SPACE13(""),
        SPACE21(""),
        SPACE22(""),
        SPACE23(""),
        SPACE31(""),
        SPACE32(""),
        SPACE33(""),
        EXIT("Congratulations, you found your way out! Type Start to start over.");

        private final String description;

        private Space(final String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
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

    //        /*+--------------+--------------+-------------+*/
    //        /*|*/ SPACE11, /* */ SPACE12, /* */ SPACE13 /*|*/,
    //        /*+--------------+              +-------------+*/
    //        /* */ SPACE21, /*|*/ SPACE22, /* */ SPACE23 /*|*/,
    //        /*+              +--------------+             +*/
    //        /*|*/ SPACE31, /* */ SPACE32, /* */ SPACE33 /*|*/,
    //        /*+-------------------------------------------+*/

    private final Space[][] field = new Space[][] {
            new Space[] { SPACE11, SPACE12, SPACE13 },
            new Space[] { SPACE21, SPACE22, SPACE23 },
            new Space[] { SPACE31, SPACE32, SPACE33 }
    };

    private static final int EXIT_X_COORD = -1;
    private static final int EXIT_Y_COORD = 1;

    private final SetMultimap<Space, Direction> permittedDirections;

    public MazeConfig() {
        this.permittedDirections = MultimapBuilder.enumKeys(Space.class).enumSetValues(Direction.class).build();
        this.permittedDirections.putAll(SPACE11, EnumSet.of(EAST));
        this.permittedDirections.putAll(SPACE12, EnumSet.of(EAST, WEST, SOUTH));
        this.permittedDirections.putAll(SPACE13, EnumSet.of(WEST));
        this.permittedDirections.putAll(SPACE21, EnumSet.of(SOUTH, WEST));
        this.permittedDirections.putAll(SPACE22, EnumSet.of(NORTH, EAST));
        this.permittedDirections.putAll(SPACE23, EnumSet.of(WEST, SOUTH));
        this.permittedDirections.putAll(SPACE31, EnumSet.of(NORTH, EAST));
        this.permittedDirections.putAll(SPACE32, EnumSet.of(EAST, WEST));
        this.permittedDirections.putAll(SPACE33, EnumSet.of(NORTH, WEST));
    }

    public StateMachineConfig<Space, Direction> configure(final Action1<Transition<Space, Direction>> entryAction) {
        final StateMachineConfig<Space, Direction> config = new StateMachineConfig<>();
        for (final Space space : Space.values()) {
            final StateConfiguration<Space,Direction> conf = config.configure(space);
            for (final Direction dir : this.permittedDirections.get(space)) {
                conf.permit(dir, getNeighbor(space, dir));
            }
            conf.onEntry(entryAction);
        }
        return config;
    }

    public int getFieldSizeX() {
        return this.field[0].length;
    }

    public int getFieldSizeY() {
        return this.field.length;
    }

    public Space getSpaceAt(final int x, final int y) {
        if (x == EXIT_X_COORD && y == EXIT_Y_COORD) {
            return Space.EXIT;
        }
        if (x < 0 || x > getFieldSizeX() - 1) {
            throw new IllegalArgumentException("x is invalid");
        }
        if (y < 0 || y > getFieldSizeY() - 1) {
            throw new IllegalArgumentException("y is invalid");
        }
        return this.field[y][x];
    }

    public Set<Direction> getPermittedDirectionsFor(final Space space) {
        return this.permittedDirections.get(space);
    }

    private int[] getSpaceCoordinates(final Space s) {
        for (int y = 0; y < getFieldSizeY(); y++) {
            for (int x = 0; x < getFieldSizeX(); x++) {
                if (getSpaceAt(x, y) == s) {
                    return new int[] {x, y};
                }
            }
        }
        return null;
    }

    private Space getNeighbor(final Space s, final Direction dir) {
        final int[] coord = getSpaceCoordinates(s);
        switch (dir) {
        case EAST: coord[0]++; break;
        case NORTH: coord[1]--; break;
        case SOUTH: coord[1]++; break;
        case WEST: coord[0]--; break;
        }
        try {
            return getSpaceAt(coord[0], coord[1]);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException("Invalid configuration. Cannot get neighbor of " + s + " in direction of " + dir);
        }
    }

    public Space getInitialSpace() {
        return getSpaceAt(0, 0);
    }


}
