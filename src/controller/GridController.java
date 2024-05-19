package controller;

import model.Grid;
import model.Hexagon;
import view.component.HexagonView;
import view.component.HexagonViewFactory;
import view.panel.ScrollableGridView;
import java.beans.PropertyChangeEvent;

/**
 * Represents the controller for a grid in the game.
 * Wraps the Grid model and the GridView view.
 */
public class GridController extends Controller {

    public GridController(Grid grid, ScrollableGridView gridView) {
        super(grid, gridView);
        // Just to make the first hexagon appear
        ((Grid) model).gridInitialized();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("hexagonAdded")) {
            Hexagon hexagon = (Hexagon) evt.getNewValue();
            ScrollableGridView gridView = (ScrollableGridView) view;
            HexagonView hexagonView = HexagonViewFactory.createHexagonView(hexagon, ScrollableGridView.hexagonSize);
            gridView.addHexagon(hexagonView);
        }
    }
}
