package com.twilio.demo.minotaur.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.core.Status;
import com.twilio.demo.minotaur.core.UserRegistry;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    private final UserRegistry userRegistry;
    private final MazeRegistry mazeRegistry;

    public StatusResource(final UserRegistry userRegistry, final MazeRegistry mazeRegistry) {
        this.userRegistry = userRegistry;
        this.mazeRegistry = mazeRegistry;
    }

    @GET
    public Status getStatus() {
        return new Status(this.userRegistry.getUsers());
    }
}
