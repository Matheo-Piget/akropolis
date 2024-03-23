package controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Grid;
import view.*;
import model.Tile;

public class BoardController extends Controller {
    private SiteController siteController;
    private UIController uiController;
    private List<GridController> gridControllers;
    private GridController currentGridController;
    private TileController selectedTile;

    public BoardController(Board model, BoardView view) {
        super(model, view);
        // Create the site controller
        siteController = new SiteController(model.getSite(), view.getSiteView(), this);
        uiController = new UIController(model, view.getBoardUI());
        initializeGridControllers(model, view);
        // Then we can start the game
        ((Board) (model)).startGame();
    }

    private void initializeGridControllers(Board model, BoardView view) {
        gridControllers = new ArrayList<>();
        for (Grid grid : model.getGrids()) {
            GridController gridController = new GridController(grid, view.getGridView()); // Assurez-vous d'avoir un constructeur approprié dans GridController
            view.nextTurn();
            gridControllers.add(gridController);
        }
        // Sélectionnez le premier GridController comme currentGridController
        if (!gridControllers.isEmpty()) {
            currentGridController = gridControllers.get(0);
        }
    }

    /**
     * This method is used to react to the signals send by the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("nextTurn")) {
            // Change the current grid to the next player's grid
            ((BoardView) view).nextTurn();
        }
        handleUiUpdates(evt);
    }

    /**
     * Controller for the selected current tile
     * @param tileController
     */
    public void selectedTile(TileController tileController) {
        Board board = (Board) model;
        BoardView boardView = (BoardView) view;
        Tile tile = (Tile) tileController.model;
        if(board.setSelectedTile(tile)){
            selectedTile = tileController;
            boardView.setSelectedTile((TileView) tileController.view);
            System.out.println("Tile selected");
        }
    }

    /**
     * update the UI infos
     * @param evt the evt call
     */
    private void handleUiUpdates(PropertyChangeEvent evt){
        if (evt.getPropertyName().equals("nextTurn")) {
            // Update the player info in the view
            uiController.updatePlayerInfo();
            uiController.updateRemainingTilesInfo();
        }
    }

    /**
     * call next turn in the model
     */
    public void nextTurn() {
        ((Board) model).endTurn();
    }
}
