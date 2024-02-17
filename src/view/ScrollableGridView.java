package view;

import javax.swing.JScrollPane;
import java.awt.Point;

public class ScrollableGridView extends JScrollPane {

    public ScrollableGridView(GridView grid) {
        super(grid);
        setPreferredSize(new java.awt.Dimension(1500, 900));
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Calculate the center coordinates
        int x = (grid.getPreferredSize().width - getPreferredSize().width) / 2;
        int y = (grid.getPreferredSize().height - getPreferredSize().height) / 2;

        // Set the initial scroll position to the center
        getViewport().setViewPosition(new Point(x, y));
    }
}
