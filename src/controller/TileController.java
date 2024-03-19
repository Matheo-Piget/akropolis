package controller;

import model.Tile;
import view.TileView;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
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
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardController.selectedTile(TileController.this);
            }
        };
        ((TileView) view).addMouseListener(mouseAdapter);
        ((TileView) view).addMouseMotionListener(mouseAdapter);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
