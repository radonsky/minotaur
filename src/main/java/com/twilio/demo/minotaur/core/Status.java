package com.twilio.demo.minotaur.core;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

    private final List<URI> users;

    public Status(final List<URI> users) {
        this.users = users;
    }

    @JsonProperty
    public List<URI> getUsers() {
        return this.users;
    }

}
