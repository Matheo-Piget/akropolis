package controller;

import java.beans.PropertyChangeEvent;
import model.Board;
import view.BoardView;

public class BoardController extends Controller {

    public BoardController(Board model, BoardView view) {
        super(model, view);
    }

    /**
     * This method is used to react to the signals send by the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        
    }
}
