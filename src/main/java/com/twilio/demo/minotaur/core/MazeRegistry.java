package com.twilio.demo.minotaur.core;

import java.util.concurrent.ConcurrentHashMap;

public class MazeRegistry {

    private final MazeConfig mazeConfig;
    private final ConcurrentHashMap<String, Maze> registry = new ConcurrentHashMap<>();

    public MazeRegistry(final MazeConfig mazeConfig) {
        this.mazeConfig = mazeConfig;
    }

    public Maze get(final String phone) {
        return this.registry.get(phone);
    }

    public Maze start(final String phone) {
        return this.registry.compute(phone, (final String u, final Maze m) -> new Maze(this.mazeConfig));
    }

    public boolean remove(final String phone) {
        return this.registry.remove(phone) != null;
    }

    public int getCount() {
        return this.registry.size();
    }

}
