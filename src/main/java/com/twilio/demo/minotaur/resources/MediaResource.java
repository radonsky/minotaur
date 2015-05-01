package com.twilio.demo.minotaur.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.png.PNGMazeRenderer;


@Path("/media")
@Produces("image/png")
public class MediaResource {

    private static final Logger log = LoggerFactory.getLogger(MediaResource.class);

    private final MazeRegistry mazeRegistry;

    public MediaResource(final MazeRegistry mazeRegistry) {
        this.mazeRegistry = mazeRegistry;
    }

    @GET
    public Response getPNG(@QueryParam("From") final String from) throws Exception {
        try {
            final Maze maze = this.mazeRegistry.get(from);
            if (maze == null) {
                return Response.serverError()
                        .type(MediaType.TEXT_PLAIN)
                        .entity("A maze for this user doesn't exist")
                        .build();
            }
            final StreamingOutput stream = new PNGMazeRenderer(maze, false, maze.isMinotaurVisible());
            return Response.ok(stream).build();
        } catch (final Exception e) {
            log.error("getPNG failed", e);
            return Response.serverError()
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

}
