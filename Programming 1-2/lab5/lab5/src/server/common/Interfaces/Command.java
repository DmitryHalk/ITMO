package server.common.Interfaces;

public interface Command {
    void execute();
    default void execute(String args) {
        execute();
    };
}
