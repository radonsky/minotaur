package com.twilio.demo.minotaur.core;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

    private final Set<String> users;

    public Status(final Set<String> users) {
        this.users = users;
    }

    @JsonProperty
    public Set<String> getUsers() {
        return this.users;
    }

}
