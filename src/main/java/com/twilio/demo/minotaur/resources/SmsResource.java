package com.twilio.demo.minotaur.resources;

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
import com.twilio.demo.minotaur.core.UserRegistry;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@Path("/sms")
@Produces(MediaType.APPLICATION_XML)
public class SmsResource {

    private static final Logger log = LoggerFactory.getLogger(SmsResource.class);

    private final UserRegistry userRegistry;
    private final MazeRegistry mazeRegistry;

    public SmsResource(final UserRegistry userRegistry, final MazeRegistry mazeRegistry) {
        this.mazeRegistry = mazeRegistry;
        this.userRegistry = userRegistry;
    }

    @GET
    public String getSms(
            @QueryParam("From") final String from,
            @QueryParam("Body") final String body) {
        try {
            if (this.userRegistry.getUser(from) == null) {
                try {
                    this.userRegistry.addUser(from, body);
                    final Maze maze = this.mazeRegistry.start(from);
                    return smsReply("Hello " + body + "! Welcome to the Labyrinth. Type N, S, E, W to move around this maze and find your way out. " + maze.getDirections());
                } catch (final IllegalArgumentException e) {
                    return smsReply("I'm sorry but user " + body + " has been already registered. Please choose a different name.");
                }
            }
            return smsReply(createMessageFrom(from, body));
        } catch (final Exception e) {
            return "<Error>" + e.getMessage() + "<Error>";
        }
    }

    private String createMessageFrom(final String phone, final String command) {
        Maze maze = this.mazeRegistry.get(phone);
        final StringBuilder bldr = new StringBuilder();
        boolean valid = true;
        switch (command.trim().toUpperCase()) {
        case "START":
            maze = this.mazeRegistry.start(phone);
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
        default:
            bldr.append("I'm sorry, I don't know where you want to go. Type N, S, E, W to move around this maze and find your way out. ");
        }
        if (!valid) {
            bldr.append("I'm sorry, you can't go there! ");
        } else {
            bldr.append(maze.getDescription());
            bldr.append(' ');
            bldr.append(maze.getDirections());
        }
        return bldr.toString();
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
