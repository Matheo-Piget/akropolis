package util;

import controller.BoardController;

public class State implements StatesMethods {

    // This is the default state class, it is used to create new states

    public final static State INSTANCE = new State();

    public State() {

    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void transitionTo(State s) {

    }

    public BoardController getBoardController() {
        return null;
    }
}
