package controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Grid;
import util.Pair;
import util.SoundManager;
import view.component.HexagonView;
import view.component.TileView;
import view.panel.BoardView;
import view.panel.ScrollableGridView;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Tile;

/**
 * Represents the controller for the board in the game.
 * Wraps the Board model and the BoardView view.
 */
public class BoardController extends Controller {
    private final UIController uiController;
    private List<GridController> gridControllers;
    private TileController selectedTile;

    /**
     * Constructor for the BoardController class.
     * Initializes the board controller with the given model and view.
     * @param model The model to associate with the board controller.
     * @param view The view to associate with the board controller.
     */
    public BoardController(Board model, BoardView view) {
        super(model, view);
        new SiteController(model.getSite(), view.getSiteView(), this);
        uiController = new UIController(model, view.getBoardUI());
        initializeGridControllers(model, view);
        initializeListeners();
        // Then we can start the game
        model.startGame();
    }

    /**
     * Initialize the grid controllers
     * @param model the model
     * @param view the view
     */
    private void initializeGridControllers(Board model, BoardView view) {
        gridControllers = new ArrayList<>();
        for (int i = 0; i < model.getGrids().size(); i++) {
            Grid grid = model.getGrids().get(i);
            ScrollableGridView gridView = view.getGridViews().get(i); // Utiliser la bonne GridView
            GridController gridController = new GridController(grid, gridView);
            gridControllers.add(gridController);
        }

    }

    /**
     * Initialize the listeners
     */
    private void initializeListeners() {
        for (GridController gridController : gridControllers) {
            MouseAdapter ms = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
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
                                SoundManager.playSound("tilePlaced");
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
        if (evt.getPropertyName().equals("addTile")) {
            // Change the current grid to the next player's grid
            ((BoardView) view).nextTurn(gridControllers.size() == 1);
        }
        if (evt.getPropertyName().equals("gameOver")) {
            // To avoid a ClassCastException, we need to check the type of the winner
            Object winner = evt.getNewValue();
            List<Pair<String, Integer>> players = new ArrayList<Pair<String, Integer>>();
            if (winner instanceof List<?>) {
                List<?> unknowList = (List<?>) winner;
                for (Object obj : unknowList) {
                    if (obj instanceof Pair<?, ?>) {
                        Pair<?, ?> pair = (Pair<?, ?>) obj;
                        if (pair.getKey() instanceof String && pair.getValue() instanceof Integer) {
                            String name = (String) pair.getKey();
                            Integer score = (Integer) pair.getValue();
                            players.add(new Pair<String, Integer>(name, score));
                        }
                    }
                }
            }
            ((BoardView) view).showGameOver(players);
        }
        handleUiUpdates(evt);
    }

    /**
     * Controller for the selected current tile
     * @param tileController the tile controller
     */
    public void selectedTile(TileController tileController) {
        Board board = (Board) model;
        BoardView boardView = (BoardView) view;
        Tile tile = (Tile) tileController.model;
        if(board.setSelectedTile(tile)){
            selectedTile = tileController;
            boardView.setSelectedTile((TileView) tileController.view);
        }
    }

    public void sendEventToUIController(PropertyChangeEvent evt){
        uiController.propertyChange(evt);
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
            selectedTile = null;
        }
    }
}
