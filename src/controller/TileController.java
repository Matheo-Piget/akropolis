package controller;

import model.Tile;
import view.TileView;
import java.beans.PropertyChangeEvent;

public class TileController extends Controller{
    
    public TileController(Tile model, TileView view) {
        super(model, view);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("tileSelected")) {
            // Update the model with the selected tile
        }
    }
}
