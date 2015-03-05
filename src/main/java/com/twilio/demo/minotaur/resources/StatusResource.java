package com.twilio.demo.minotaur.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.core.Status;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    private final MazeRegistry registry;

    public StatusResource(final MazeRegistry registry) {
        this.registry = registry;
    }

    @GET
    public Status getStatus() {
        return new Status(this.registry.getCount());
    }
}
