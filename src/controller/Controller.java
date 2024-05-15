package controller;

import model.Model;
import view.View;

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
