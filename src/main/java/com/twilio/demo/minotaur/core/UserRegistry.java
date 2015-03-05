package com.twilio.demo.minotaur.core;

import java.util.Set;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

public class UserRegistry {

    private final BiMap<String, String> phone2user = Maps.synchronizedBiMap(HashBiMap.create());

    public String getUser(final String phone) {
        return this.phone2user.get(phone);
    }

    public String addUser(final String phone, final String user) throws IllegalArgumentException {
        return this.phone2user.put(phone, user);
    }

    public String removeUser(final String phone) {
        return this.phone2user.remove(phone);
    }

    public Set<String> getUsers() {
        return this.phone2user.values();
    }

}
