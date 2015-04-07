package com.twilio.demo.minotaur.png;

import static com.twilio.demo.minotaur.core.MazeConfig.Direction.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeConfig.Direction;
import com.twilio.demo.minotaur.core.MazeConfig.Space;

public class PNGMazeRenderer implements StreamingOutput {

    private static final Logger log = LoggerFactory.getLogger(PNGMazeRenderer.class);

    private final Cell[][] cells;

    public PNGMazeRenderer(final Maze maze) {
        this.cells = new Cell[maze.getConfig().getFieldSizeY()][];
        for (int y = 0; y < this.cells.length; y++) {
            this.cells[y] = new Cell[maze.getConfig().getFieldSizeX()];
            for (int x = 0; x < this.cells[y].length; x++) {
                final Space space = maze.getConfig().getSpaceAt(x, y);
                this.cells[y][x] = new Cell(
                        maze.getConfig().getPermittedDirectionsFor(space),
                        maze.isInState(space),
                        maze.isVisitedState(space));
            }
        }
    }

    private static class Cell {

        private static final int SIZE = 40;
        private static final int WALL_SIZE = 2;
        private static final int PRESENCE_SIZE = 12;

        private static final int WALL_COLOR = 0x00;
        private static final int EMPTY_COLOR = 0xFF;
        private static final int PRESENCE_COLOR = 0x77;

        private final EnumSet<Direction> walls;
        private final boolean present;
        private final boolean visited;

        public Cell(final Set<Direction> permittedDirections, final boolean present, final boolean visited) {
            this.walls = EnumSet.allOf(Direction.class);
            this.walls.removeAll(permittedDirections);
            this.present = present;
            this.visited = visited;
        }

        public int getColorAt(final int x, final int y) {
            if (x < 0 || x >= SIZE) {
                throw new IllegalArgumentException("x is out of range");
            }
            if (y < 0 || y >= SIZE) {
                throw new IllegalArgumentException("y is out of range");
            }
            if (this.present) {
                if (x > SIZE / 2 - PRESENCE_SIZE / 2 && x <= SIZE / 2 + PRESENCE_SIZE / 2 &&
                        y > SIZE / 2 - PRESENCE_SIZE / 2 && y <= SIZE / 2 + PRESENCE_SIZE / 2) {
                    return PRESENCE_COLOR;
                }
            }
            if (this.visited) {
                if (x < WALL_SIZE && this.walls.contains(WEST)) {
                    return WALL_COLOR;
                }
                if (x > SIZE - WALL_SIZE - 1 && this.walls.contains(EAST)) {
                    return WALL_COLOR;
                }
                if (y < WALL_SIZE && this.walls.contains(NORTH)) {
                    return WALL_COLOR;
                }
                if (y > SIZE - WALL_SIZE - 1 && this.walls.contains(SOUTH)) {
                    return WALL_COLOR;
                }
            }
            return EMPTY_COLOR;
        }

    }

    @Override
    public void write(final OutputStream output) throws IOException, WebApplicationException {
        try {
            final int sizeX = this.cells[0].length * Cell.SIZE;
            final int sizeY = this.cells.length * Cell.SIZE;
            final ImageInfo imi = new ImageInfo(sizeX, sizeY, 8, false, true, false);
            // open image for writing to a output stream
            final PngWriter png = new PngWriter(output, imi);
            // add some optional metadata (chunks)
            png.getMetadata().setDpi(100.0);
            png.getMetadata().setTimeNow(0); // 0 seconds from now = now
            png.getMetadata().setText(PngChunkTextVar.KEY_Title, "just a text image");
            png.getMetadata().setText("my key", "my text");
            for (int row = 0; row < png.imgInfo.rows; row++) {
                final int cellY = row / Cell.SIZE;
                final int posY = row - cellY * Cell.SIZE;
                final ImageLineInt iline = new ImageLineInt(imi);
                final int[] scanline = iline.getScanline();
                for (int col = 0; col < imi.cols; col++) {
                    final int cellX = col / Cell.SIZE;
                    final int posX = col - cellX * Cell.SIZE;
                    scanline[col] = this.cells[cellY][cellX].getColorAt(posX, posY);
                }
                png.writeRow(iline);
            }
            png.end();
        } catch (final Exception e) {
            log.error("PNG rendering failed", e);
            throw new WebApplicationException(e);
        }
    }
}