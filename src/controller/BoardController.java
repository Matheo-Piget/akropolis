package controller;

import java.beans.PropertyChangeEvent;
import model.Board;
import view.BoardView;

public class BoardController extends Controller {
    private SiteController siteController;

    public BoardController(Board model, BoardView view) {
        super(model, view);
        // Create the site controller
        siteController = new SiteController(model.getSite(), view.getSiteView());
        // Then we can start the game
        ((Board)(model)).startGame();
    }

    /**
     * This method is used to react to the signals send by the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("tileSelected")) {
            // Draw the selected tile in the view and make it draggable
        }
    }
}
