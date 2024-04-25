package view.main.states;

import util.State;
import util.StateMachine;

/**
 * Enumeration representing the different states of the application.
 * Each state is a unique instance (singleton) of the State class.
 */
public enum AppState implements StateMachine {

    // Definition of the application states with their initial instances
    START(StartState.getInstance()),
    LOGO(StartingLogoState.getInstance()),
    PLAYING(PlayingState.getInstance()),
    GAMEOVER(GameOverState.getInstance());



    // Current instance of the state
    private State currentState;

    /**
     * Private constructor for the enumeration.
     *
     * @param initialState The initial state of the application.
     */
    AppState(State initialState) {
        currentState = initialState;
    }

    /**
     * Returns the current state of the application.
     *
     * @return The current state.
     */
    @Override
    public State getState() {
        return currentState;
    }

    /**
     * Changes the application state to a specified new state.
     *
     * @param newState The new state to transition to.
     */
    @Override
    public void changeState(State newState) {
        // Exit the current state
        currentState.exit();

        // Transition to the new state
        currentState.transitionTo(newState);

        // Update the current state
        currentState = newState;

        // Enter the new state
        currentState.enter();
    }
}
