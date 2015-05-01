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

    private final Space[][] field;

    private static final int EXIT_X_COORD = -1;
    private static final int EXIT_Y_COORD = 1;

    private static final int MINOTAUR_Y_COORD = 1;
    private static final int MINOTAUR_X_COORD = 4;


    private final SetMultimap<Space, Direction> permittedDirections;

    private static final char SPACE = ' ';

    private final String[] layout = new String[] {
            "+-+-+-+-+-+",
            "|   |     |",
            "+-+ + + +-+",
            "  |   | | |",
            "+ +-+-+ + +",
            "| |   | | |",
            "+ + + + + +",
            "| |   |   |",
            "+ + + +-+ +",
            "|   |     |",
            "+-+-+-+-+-+",
    };

    public MazeConfig() {
        final int sizeX = (this.layout[0].length() - 1) / 2;
        final int sizeY = (this.layout.length - 1) / 2;
        this.field = createField(sizeX, sizeY);
        this.permittedDirections = MultimapBuilder.hashKeys().enumSetValues(Direction.class).<Space, Direction>build();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                this.permittedDirections.putAll(getSpaceAt(y, x), getPermittedDirectionsAt(y, x));
            }
        }
    }

    private char getLayoutCharAt(final int row, final int col) {
        final String line = this.layout[row];
        return line.charAt(col);
    }

    private EnumSet<Direction> getPermittedDirectionsAt(final int y, final int x) {
        final int row = y * 2 + 1;
        final int col = x * 2 + 1;
        if (getLayoutCharAt(row, col) != SPACE) {
            throw new IllegalArgumentException("Invalid layout: There should be an empty space at row = " + row + " and col = " + col);
        }
        final EnumSet<Direction> result = EnumSet.noneOf(Direction.class);
        if (getLayoutCharAt(row - 1, col) == SPACE) {
            result.add(NORTH);
        }
        if (getLayoutCharAt(row + 1, col) == SPACE) {
            result.add(SOUTH);
        }
        if (getLayoutCharAt(row, col - 1) == SPACE) {
            result.add(WEST);
        }
        if (getLayoutCharAt(row, col + 1) == SPACE) {
            result.add(EAST);
        }
        return result;
    }

    public StateMachineConfig<Space, Direction> configure(final Action1<Transition<Space, Direction>> entryAction) {
        final StateMachineConfig<Space, Direction> config = new StateMachineConfig<>();
        for (final Space[] row : this.field) {
            for (final Space space : row) {
                final StateConfiguration<Space,Direction> conf = config.configure(space);
                for (final Direction dir : this.permittedDirections.get(space)) {
                    conf.permit(dir, getNeighbor(space, dir));
                }
                if (entryAction != null) {
                    conf.onEntry(entryAction);
                }
            }
        }
        return config;
    }

    private Space[][] createField(final int sizeX, final int sizeY) {
        final Space[][] rows = new Space[sizeY][];
        for (int y = 0; y < sizeY; y++) {
            final Space[] columns = new Space[sizeX];
            for (int x = 0; x < sizeX; x++) {
                final Space space = new Space("SPACE" + y + x);
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

    public Space getSpaceAt(final int y, final int x) {
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
                if (getSpaceAt(y, x) == s) {
                    return new int[] {x, y};
                }
            }
        }
        return null;
    }

    public Space getNeighbor(final Space s, final Direction dir) {
        final int[] coord = getSpaceCoordinates(s);
        switch (dir) {
        case EAST: coord[0]++; break;
        case NORTH: coord[1]--; break;
        case SOUTH: coord[1]++; break;
        case WEST: coord[0]--; break;
        }
        try {
            return getSpaceAt(coord[1], coord[0]);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException("Invalid configuration. Cannot get neighbor of " + s + " in direction of " + dir);
        }
    }

    public Space getInitialSpace() {
        return getSpaceAt(0, 0);
    }

    public Space getFinalSpace() {
        return getSpaceAt(EXIT_Y_COORD, EXIT_X_COORD);
    }

    public Space getMinotaurInitialSpace() {
        return getSpaceAt(MINOTAUR_Y_COORD, MINOTAUR_X_COORD);
    }

    public boolean arePermittedNeighbors(final Space space1, final Space space2) {
        for (final Direction dir : getPermittedDirectionsFor(space1)) {
            if (getNeighbor(space1, dir) == space2) {
                return true;
            }
        }
        return false;
    }


}
