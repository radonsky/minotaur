package com.twilio.demo.minotaur;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import com.twilio.demo.minotaur.core.Game;
import com.twilio.demo.minotaur.core.MazeConfig;
import com.twilio.demo.minotaur.core.MazeRegistry;
import com.twilio.demo.minotaur.core.UserRegistry;

public class MinotaurConsole {

    public static void main(final String[] args) throws IOException {
        final UserRegistry userRegistry = new UserRegistry();
        final MazeRegistry mazeRegistry = new MazeRegistry(new MazeConfig());
        final Game game = new Game(userRegistry, mazeRegistry);
        println("What is your name?");
        String command = readln();
        while (command != null && !command.equalsIgnoreCase("exit")) {
            println(game.parseCommand("User", command).action("User").getMessage());
            command = readln();
        }
    }

    private static void println(final String str) {
        final Console c = System.console();
        if (c != null) {
            c.printf("%s%n", str);
        } else {
            System.out.println(str);
        }
    }

    private static String readln() throws IOException {
        final Console c = System.console();
        if (c != null) {
            c.printf(">");
            return c.readLine();
        } else {
            System.out.print(">");
            final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            return br.readLine();
        }
    }
}