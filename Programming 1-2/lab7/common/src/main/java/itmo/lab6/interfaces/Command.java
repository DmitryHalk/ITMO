package itmo.lab6.interfaces;

import itmo.lab6.Request;

public interface Command {
    void execute();
    default void execute(Request request) {
        execute();
    };
}

