package controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Grid;
import view.*;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        initializeListeners();
        // Then we can start the game
        ((Board) (model)).startGame();
    }

    private void initializeGridControllers(Board model, BoardView view) {
        gridControllers = new ArrayList<>();
        for (Grid grid : model.getGrids()) {
            GridController gridController = new GridController(grid, view.getGridView());
            view.nextTurn();
            gridControllers.add(gridController);
        }
        if (!gridControllers.isEmpty()) {
            currentGridController = gridControllers.get(0);
        }
    }

    private void initializeListeners() {
        for (GridController gridController : gridControllers) {
            MouseAdapter ms = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Mouse clicked");
                    if (e.getButton() == MouseEvent.BUTTON1) { // Check if left button was clicked
                        if(selectedTile != null){
                            BoardView boardView = (BoardView) view;
                            HexagonView[] filledHexagons = boardView.getFilledHexagons();
                            if(filledHexagons != null){
                                // Make it buy the tile
                                Board board = (Board) model;
                                // Get the coordinates of each hexagon
                                ArrayList<Point> hexagonsCoordinates = new ArrayList<>();
                                for (HexagonView hexagonView : filledHexagons) {
                                    Point coordinates = hexagonView.getPosition();
                                    hexagonsCoordinates.add(coordinates);
                                }
                                // Set the model to those coordinates
                                Tile tile = (Tile) selectedTile.model;
                                tile.setCoordinates(hexagonsCoordinates);
                                // Place the tile on the board
                                board.addTileToGrid();
                            }
                        }
                    }
                }
            };
            ((ScrollableGridView) gridController.view).getGrid().addMouseListener(ms);
            ((ScrollableGridView) gridController.view).getGrid().addMouseMotionListener(ms);
        }
    }

    /**
     * This method is used to react to the signals send by the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("nextTurn")) {
            // Change the current grid to the next player's grid
            System.out.println("Next turn");
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
