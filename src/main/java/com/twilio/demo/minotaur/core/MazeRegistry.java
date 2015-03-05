package com.twilio.demo.minotaur.core;

import java.util.concurrent.ConcurrentHashMap;

public class MazeRegistry {

    private final MazeConfig mazeConfig;
    private final ConcurrentHashMap<String, Maze> registry = new ConcurrentHashMap<>();

    public MazeRegistry(final MazeConfig mazeConfig) {
        this.mazeConfig = mazeConfig;
    }

    public Maze get(final String user) {
        return this.registry.get(user);
    }

    public Maze start(final String user) {
        return this.registry.compute(user, (final String u, final Maze m) -> new Maze(this.mazeConfig));
    }

    public boolean remove(final String user) {
        return this.registry.remove(user) != null;
    }

    public int getCount() {
        return this.registry.size();
    }

}
