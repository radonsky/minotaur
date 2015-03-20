package com.twilio.demo.minotaur.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.sdk.verbs.Play;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@Path("/voice")
@Produces(MediaType.APPLICATION_XML)
public class VoiceResource {

    private static final Logger log = LoggerFactory.getLogger(VoiceResource.class);

    @GET
    public String getVoice() throws TwiMLException {
        final TwiMLResponse twiml = new TwiMLResponse();

        // Play an AIFF file for incoming callers.
        final Play play = new Play("http://minotaur.herokuapp.com/assets/secret.aif");
        try {
            twiml.append(play);
            return twiml.toXML();
        } catch (final TwiMLException e) {
            log.error("Cannot create SMS", e);
            throw e;
        }

    }

}
