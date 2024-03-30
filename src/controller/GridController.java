package controller;

import model.Grid;
import model.Hexagon;
import view.GridView;
import view.HexagonView;
import view.HexagonViewFactory;
import view.ScrollableGridView;
import java.beans.PropertyChangeEvent;

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
            // Convert the hexagon to a view and add it to the grid view
            Hexagon hexagon = (Hexagon) evt.getNewValue();
            HexagonView hexagonView = HexagonViewFactory.createHexagonView(hexagon, GridView.hexagonSize);
            ((ScrollableGridView) view).addHexagon(hexagonView);
            System.out.println("Hexagon added to the grid view");
        }
    }
}
