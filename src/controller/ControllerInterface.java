package controller;

import java.beans.PropertyChangeListener;
import model.Model;
import view.View;

/**
 * This class is the template that all controllers must follow
 * 
 */
interface ControllerInterface extends PropertyChangeListener{

    /**
     * This method is used to set up the observer pattern between the view and the model
     */
    void setupListener(Model model, View view);
}
