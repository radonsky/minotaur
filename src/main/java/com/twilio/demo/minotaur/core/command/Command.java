package com.twilio.demo.minotaur.core.command;

/**
 * A function that transforms the MazeRegistry from one state to another and generates response.
 *
 */
public interface Command {

    Response action(String userId);

}
