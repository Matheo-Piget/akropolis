package controller;

import java.beans.PropertyChangeEvent;
import model.Board;
import view.BoardView;
import view.TileViewFactory;
import view.TileView;

public class BoardController extends Controller {
    private SiteController siteController;
    private UIController uiController;
    private GridController gridController;
    private TileController selectedTile;

    public BoardController(Board model, BoardView view) {
        super(model, view);
        // Create the site controller
        siteController = new SiteController(model.getSite(), view.getSiteView());
        uiController = new UIController(model, view.getBoardUI());
        gridController = new GridController(model.getCurrentGrid(), view.getGridView());
        // Then we can start the game
        ((Board) (model)).startGame();
    }

    /**
     * This method is used to react to the signals send by the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("tileSelected")) {
            Board board = (Board) model;
            BoardView boardView = (BoardView) view;
            TileView tileView = TileViewFactory.createTileView(board.getCurrentPlayer().getSelectedTile());
            selectedTile = new TileController(board.getCurrentPlayer().getSelectedTile(), tileView);
            boardView.setSelectedTile((TileView) selectedTile.view);
        }
        if (evt.getPropertyName().equals("nextTurn")) {
            // Change the current grid to the next player's grid
            ((BoardView) view).nextTurn();
        }
        handleUiUpdates(evt);
    }

    private void handleUiUpdates(PropertyChangeEvent evt){
        if (evt.getPropertyName().equals("playerUpdated")) {
            // Update the player info in the view
            uiController.updatePlayerInfo();
        }
        if (evt.getPropertyName().equals("tilesRemainingUpdated")) {
            // Update the remaining tiles info in the view
            uiController.updateRemainingTilesInfo();
        }
    }
}
