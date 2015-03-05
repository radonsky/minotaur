package com.twilio.demo.minotaur.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.demo.minotaur.core.Maze;
import com.twilio.demo.minotaur.core.MazeConfig.Direction;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@Path("/sms")
@Produces(MediaType.APPLICATION_XML)
public class SmsResource {

    private static final Logger log = LoggerFactory.getLogger(SmsResource.class);

    private final MazeRegistry registry;

    public SmsResource(final MazeRegistry registry) {
        this.registry = registry;
    }

    @GET
    public String getSms(
            @QueryParam("From") final String from,
            @QueryParam("Body") final String body) {
        try {
            return smsReply(createMessageFrom(from, body));
        } catch (final Exception e) {
            return "<Error>" + e.getMessage() + "<Error>";
        }
    }

    private String createMessageFrom(final String user, final String command) {
        Maze maze = this.registry.get(user);
        if (maze == null) {
            maze = this.registry.start(user);
        }
        final StringBuilder bldr = new StringBuilder();
        boolean valid = true;
        switch (command.trim().toUpperCase()) {
        case "START":
            maze = this.registry.start(user);
            bldr.append("Welcome to the Labyrinth! Type N, S, E, W to go NORTH, SOUTH, EAST and WEST and find your way out. ");
            break;
        case "W":
            valid = maze.move(Direction.WEST);
            break;
        case "S":
            valid = maze.move(Direction.SOUTH);
            break;
        case "N":
            valid = maze.move(Direction.NORTH);
            break;
        case "E":
            valid = maze.move(Direction.EAST);
            break;
        }
        if (!valid) {
            bldr.append("I'm sorry, you can't go there! ");
        }
        if (maze.isInExitState()) {
            bldr.append("Congratulations, you found your way out! Type START to start over.");
        } else {
            bldr.append(getDirections(maze.getPermittedDirections()));
        }
        return bldr.toString();
    }

    private String getDirections(final List<Direction> permittedDirections) {
        return "You can go to the " + permittedDirections + ".";
    }

    private String smsReply(final String text) throws TwiMLException {
        final TwiMLResponse twiml = new TwiMLResponse();
        final Message message = new Message(text);
        try {
            twiml.append(message);
            return twiml.toXML();
        } catch (final TwiMLException e) {
            log.error("Cannot create SMS", e);
            throw e;
        }
    }

}
