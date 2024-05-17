package util;

/**
 * This class is the template that all state machines must follow
 */
public interface StateMachine {

    State getState();
    void changeState(State s);
}
