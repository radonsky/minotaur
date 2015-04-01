package com.twilio.demo.minotaur.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.demo.minotaur.core.Game;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@Path("/sms")
@Produces(MediaType.APPLICATION_XML)
public class SmsResource {

    private static final Logger log = LoggerFactory.getLogger(SmsResource.class);

    private final Game game;

    public SmsResource(final Game game) {
        this.game = game;
    }

    @GET
    public String getSms(
            @QueryParam("From") final String from,
            @QueryParam("Body") final String body) {
        try {
            return smsReply(this.game.command(from, body));
        } catch (final Exception e) {
            return "<Error>" + e.getMessage() + "<Error>";
        }
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
