package controller;

import java.beans.PropertyChangeEvent;
import model.Board;
import view.BoardView;

public class BoardController extends Controller {
    private SiteController siteController;
    private UIController uiController;
    private GridController gridController;

    public BoardController(Board model, BoardView view) {
        super(model, view);
        // Create the site controller
        siteController = new SiteController(model.getSite(), view.getSiteView());
        uiController = new UIController(model, view.getBoardUI());
        gridController = new GridController(model.getCurrentGrid(), view.getGridView());
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
        } else if(evt.getPropertyName().equals("playerUpdated")) {
            // Update the player info in the view
            uiController.updatePlayerInfo();
        } else if(evt.getPropertyName().equals("tilesRemainingUpdated")) {
            // Update the remaining tiles info in the view
            uiController.updateRemainingTilesInfo();
        } else if(evt.getPropertyName().equals("currentGridUpdated")) {
            // Update the current grid in the view
            //updateCurrentGrid();
        }
    }
}
