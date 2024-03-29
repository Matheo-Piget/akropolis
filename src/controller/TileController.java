package controller;

import model.Tile;
import view.TileView;
import java.beans.PropertyChangeEvent;


/**
 * Represents the controller for a tile in the game.
 * Wraps the model and the view of the tile.
 */
public class TileController extends Controller{
    BoardController boardController;
    
    public TileController(Tile model, TileView view, BoardController boardController) {
        super(model, view);
        this.boardController = boardController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
