package controller;

import model.Site;
import model.Tile;
import view.SiteView;
import view.TileView;

import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SiteController extends Controller {
    private ArrayList<TileController> tileControllers; // Useful for converting
    
    public SiteController(Site model, SiteView view) {
        super(model, view);
        // Then we setup the listener to inform the model when we click on a tile
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // We get the tile that was clicked
                TileView tile = ((SiteView) view).getTileClicked(e.getX(), e.getY());
                // We can obtain the model from the tile controller
                for (TileController tileController : tileControllers) {
                    if (tileController.view == tile) {
                        // We inform the model that the tile was clicked
                        model.selectedTile((Tile) tileController.model);
                    }
                }
            }
        };
        ((SiteView) view).addMouseListener(mouseAdapter);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("tileUpdated")) {
            // Update the view with the updated tile
            ArrayList<Tile> tiles = ((Site) model).getTiles();
            System.out.println("Updating the site with the tiles: " + tiles);
            // Then we create the tile controllers
            tileControllers = new ArrayList<TileController>();
            for (Tile tile : tiles) {
                TileController tileController = new TileController(tile, ((SiteView) view).obtainCorrespondingView(tile));
                tileControllers.add(tileController);
            }
            // Then we update the view
            ((SiteView) view).update(tiles);
        }
    }
}
