package controller;

import model.Site;
import model.Tile;
import view.SiteView;
import view.TileView;
import view.TileViewFactory;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.beans.PropertyChangeEvent;

public class SiteController extends Controller {
    private ArrayList<TileController> tileControllers = new ArrayList<>();
    private final BoardController boardController;

    public SiteController(Site model, SiteView view, BoardController boardController) {
        super(model, view);
        this.boardController = boardController;
        // Create a listener
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private TileView hoveredTile = null;
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get which tile was clicked by searching for the tile that contains the point
                System.out.println("Mouse clicked on site");
                for (TileController tileController : tileControllers) {
                    TileView tileView = (TileView) tileController.view;
                    Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), tileView);
                    if (tileView.contains(p)) {
                        boardController.selectedTile(tileController);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Get which tile is hovered by searching for the tile that contains the point
                for (TileController tileController : tileControllers) {
                    TileView tileView = (TileView) tileController.view;
                    Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), tileView);
                    if (tileView.contains(p)) {
                        if (hoveredTile != tileView) {
                            if (hoveredTile != null) {
                                hoveredTile.setHovered(false);
                            }
                            hoveredTile = tileView;
                            hoveredTile.setHovered(true);
                        }
                        return;
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredTile != null) {
                    hoveredTile.setHovered(false);
                    hoveredTile = null;
                }
            }
        };
        view.addMouseListener(mouseAdapter);
        view.addMouseMotionListener(mouseAdapter);
    }

    /**
     * update the siteView from the model
     * 
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("tileUpdated")) {
            // Update the view with the updated tile
            Tile[] tiles = ((Site) model).getTiles();
            ArrayList<TileView> tileViews = new ArrayList<>();
            // Then we create the tile controllers
            tileControllers = new ArrayList<>();
            SiteView siteView = (SiteView) view;
            for (Tile tile : tiles) {
                if (tile == null)
                    continue;
                TileController tileController = new TileController(tile, TileViewFactory.createTileView(tile),
                        boardController);
                tileControllers.add(tileController);
                tileViews.add((TileView) tileController.view);
            }
            // Then we update the view
            siteView.update(tileViews);
            // Update also the UI
            boardController.sendEventToUIController(new PropertyChangeEvent(this, "tilesRemainingUpdated", null, null));
        }
    }
}
