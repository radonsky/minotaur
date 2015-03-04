package com.twilio.demo.minotaur.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

    private final long users;

    public Status(final long users) {
        this.users = users;
    }

    @JsonProperty
    public long getUsers() {
        return this.users;
    }

}
