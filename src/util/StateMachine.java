package util;

public interface StateMachine {

    State getState();
    void changeState(State s);
}
