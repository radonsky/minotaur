package com.twilio.demo.minotaur.core.command;

import java.net.URI;

public class MultimediaResponse extends SimpleResponse {

    private final URI mediaUri;

    public MultimediaResponse(final String text, final URI mediaUri) {
        super(text);
        this.mediaUri = mediaUri;
    }

    public URI getMediaUri() {
        return this.mediaUri;
    }


}
