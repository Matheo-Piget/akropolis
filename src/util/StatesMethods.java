package util;

public interface StatesMethods{

    // This is the contract that all states must follow

    public void exit();

    public void transitionTo(State s);

    public void enter();
}

