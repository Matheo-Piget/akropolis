package controller;

import model.Model;
import view.View;

public abstract class Controller implements ControllerInterface {
    
    public Controller(Model model, View view) {
        setupListener(model, view);
    }

    @Override
    public void setupListener(Model model, View view) {
        model.addPropertyChangeListener(this);
        view.addPropertyChangeListener(this);
    }
}
