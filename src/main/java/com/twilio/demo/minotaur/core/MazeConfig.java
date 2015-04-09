package com.twilio.demo.minotaur.core;

import static com.twilio.demo.minotaur.core.MazeConfig.Direction.EAST;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.NORTH;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.SOUTH;
import static com.twilio.demo.minotaur.core.MazeConfig.Direction.WEST;

import java.util.EnumSet;
import java.util.Set;

import com.github.oxo42.stateless4j.StateConfiguration;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;


public class MazeConfig {

    public static final class Space {
        public static final Space EXIT = new Space("EXIT");

        private final String name;

        private Space(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
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

    private final Space[][] field = createField(3, 3);

    private static final int EXIT_X_COORD = -1;
    private static final int EXIT_Y_COORD = 1;

    private final SetMultimap<Space, Direction> permittedDirections;

    public MazeConfig() {
        //        /*+--------------+--------------+-------------+*/
        //        /*|*/ SPACE11, /* */ SPACE12, /* */ SPACE13 /*|*/,
        //        /*+--------------+              +-------------+*/
        //        /* */ SPACE21, /*|*/ SPACE22, /* */ SPACE23 /*|*/,
        //        /*+              +--------------+             +*/
        //        /*|*/ SPACE31, /* */ SPACE32, /* */ SPACE33 /*|*/,
        //        /*+-------------------------------------------+*/

        this.permittedDirections = MultimapBuilder.hashKeys().enumSetValues(Direction.class).<Space, Direction>build();
        this.permittedDirections.putAll(getSpaceAt(0, 0), EnumSet.of(EAST));
        this.permittedDirections.putAll(getSpaceAt(1, 0), EnumSet.of(EAST, WEST, SOUTH));
        this.permittedDirections.putAll(getSpaceAt(2, 0), EnumSet.of(WEST));
        this.permittedDirections.putAll(getSpaceAt(0, 1), EnumSet.of(SOUTH, WEST));
        this.permittedDirections.putAll(getSpaceAt(1, 1), EnumSet.of(NORTH, EAST));
        this.permittedDirections.putAll(getSpaceAt(2, 1), EnumSet.of(WEST, SOUTH));
        this.permittedDirections.putAll(getSpaceAt(0, 2), EnumSet.of(NORTH, EAST));
        this.permittedDirections.putAll(getSpaceAt(1, 2), EnumSet.of(EAST, WEST));
        this.permittedDirections.putAll(getSpaceAt(2, 2), EnumSet.of(NORTH, WEST));
    }

    public StateMachineConfig<Space, Direction> configure(final Action1<Transition<Space, Direction>> entryAction) {
        final StateMachineConfig<Space, Direction> config = new StateMachineConfig<>();
        for (final Space[] row : this.field) {
            for (final Space space : row) {
                final StateConfiguration<Space,Direction> conf = config.configure(space);
                for (final Direction dir : this.permittedDirections.get(space)) {
                    conf.permit(dir, getNeighbor(space, dir));
                }
                conf.onEntry(entryAction);
            }
        }
        return config;
    }

    private Space[][] createField(final int sizeX, final int sizeY) {
        final Space[][] rows = new Space[sizeY][];
        for (int y = 0; y < sizeY; y++) {
            final Space[] columns = new Space[sizeX];
            for (int x = 0; x < sizeX; x++) {
                final Space space = new Space("SPACE" + (y + 1) + (x + 1));
                columns[x] = space;
            }
            rows[y] = columns;
        }
        return rows;
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

    public Space getFinalSpace() {
        return getSpaceAt(EXIT_X_COORD, EXIT_Y_COORD);
    }


}
