package controller;

import model.Board;
import model.Grid;
import model.Player;
import view.ScrollableGridView;

import java.beans.PropertyChangeEvent;

public class GridController extends Controller {

    private ScrollableGridView gridView;
    public GridController(Grid grid, ScrollableGridView gridView) {
        super(grid, gridView);
        this.gridView = gridView;

    }

    public void updateCurrentGrid() {
        gridView.updateGrid(((Grid)model).getHexagons()); //TODO : updateGrid
    }


    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("currentGridUpdated")) {
            updateCurrentGrid();
        }
    }
}
