package com.twilio.demo.minotaur.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.demo.minotaur.core.Game;
import com.twilio.sdk.verbs.Media;
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
            @QueryParam("Body") final String body,
            @Context final UriInfo uriInfo) {
        try {
            final String mediaUrl = uriInfo.getBaseUriBuilder().path(MediaResource.class).queryParam("From", from).build().toString();
            return mmsReply(this.game.command(from, body), mediaUrl);
        } catch (final Exception e) {
            return "<Error>" + e.getMessage() + "<Error>";
        }
    }

    private String mmsReply(final String text, final String mediaUrl) throws TwiMLException {
        final TwiMLResponse twiml = new TwiMLResponse();
        final Message message = new Message(text);
        final Media media = new Media(mediaUrl);
        try {
            message.append(media);
            twiml.append(message);
            return twiml.toXML();
        } catch (final TwiMLException e) {
            log.error("Cannot create SMS", e);
            throw e;
        }
    }

}
