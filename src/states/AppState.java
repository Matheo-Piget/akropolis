package states;
import util.State;
import util.StateMachine;

public enum AppState implements StateMachine  {
    // This is the enum that contains all the states of the application
    // Each state is a singleton
    
    START(StartState.getInstance());

    private State currentState;

    private AppState(State initial_state) {
        currentState = initial_state;
    }

    @Override
    public State getState() {
        return currentState;
    }

    @Override
    public void changeState(State s) {
        currentState.exit();
        currentState.transitionTo(s);
        currentState = s;
        currentState.enter();
    }
}
