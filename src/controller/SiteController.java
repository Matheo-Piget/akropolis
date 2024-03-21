package controller;

import model.Site;
import model.Tile;
import view.SiteView;
import view.TileView;
import view.TileViewFactory;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;

public class SiteController extends Controller {
    private ArrayList<TileController> tileControllers = new ArrayList<TileController>();
    private BoardController boardController;
    
    public SiteController(Site model, SiteView view, BoardController boardController) {
        super(model, view);
        this.boardController = boardController;
    }

    /**
     * update the siteView from the model
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("tileUpdated")) {
            // Update the view with the updated tile
            ArrayList<Tile> tiles = ((Site) model).getTiles();
            ArrayList<TileView> tileViews = new ArrayList<TileView>();
            // Then we create the tile controllers
            tileControllers = new ArrayList<TileController>();
            for (Tile tile : tiles) {
                TileController tileController = new TileController(tile, TileViewFactory.createTileView(tile), boardController);
                tileControllers.add(tileController);
                tileViews.add((TileView) tileController.view);
            }
            // Then we update the view
            ((SiteView) view).update(tileViews);
        }
    }
}
