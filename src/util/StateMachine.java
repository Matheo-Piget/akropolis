package util;

public interface StateMachine {

    public State getState();

    public void changeState(State s);
}
