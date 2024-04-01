package util;

public interface StatesMethods{

    // This is the contract that all states must follow

    void exit();

    void transitionTo(State s);

    void enter();
}

