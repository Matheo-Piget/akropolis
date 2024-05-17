package controller;

import model.Model;
import view.View;

/**
 * Represents the controller base class for each controller in the game.
 * Wraps the model and the view and sets up the listener.
 */
public abstract class Controller implements ControllerInterface {
    // The casted model and view
    protected Model model;
    protected View view;
    
    public Controller(Model model, View view) {
        setupListener(model, view);
        this.model = model;
        this.view = view;
    }

    @Override
    public void setupListener(Model model, View view) {
        model.addPropertyChangeListener(this);
    }
}
