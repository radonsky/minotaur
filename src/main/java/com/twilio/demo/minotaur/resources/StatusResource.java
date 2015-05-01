package com.twilio.demo.minotaur.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.Lists;
import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.core.Status;
import com.twilio.demo.minotaur.core.UserRegistry;
import com.twilio.demo.minotaur.png.PNGMazeRenderer;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    @Context
    UriInfo uriInfo;

    private final UserRegistry userRegistry;
    private final MazeRegistry mazeRegistry;

    public StatusResource(final UserRegistry userRegistry, final MazeRegistry mazeRegistry) {
        this.userRegistry = userRegistry;
        this.mazeRegistry = mazeRegistry;
    }

    @GET
    public Status getStatus() {
        return new Status(Lists.transform(Lists.newArrayList(this.userRegistry.getUsers()), (final String user) -> {
            final UriBuilder ub = this.uriInfo.getAbsolutePathBuilder();
            return ub.path(user).build();
        }));
    }

    @Path("/{user}")
    @GET
    @Produces("image/png")
    public Response getUserStatus(@PathParam("user") final String user) {
        final String phone = this.userRegistry.getPhone(user);
        if (phone == null) {
            return Response.serverError()
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Can't find user of that name: " + user)
                    .build();
        }
        final Maze maze = this.mazeRegistry.get(phone);
        if (maze == null) {
            return Response.serverError()
                    .type(MediaType.TEXT_PLAIN)
                    .entity("A maze for this user doesn't exist")
                    .build();
        }
        final StreamingOutput stream = new PNGMazeRenderer(maze, true, true);
        return Response.ok(stream).build();
    }

}
