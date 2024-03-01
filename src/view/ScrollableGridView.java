package view;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class ScrollableGridView extends JScrollPane {

    private GridView grid;
    private JScrollBar horizontalScrollBar;
    private JScrollBar verticalScrollBar;

    public ScrollableGridView(GridView grid) {
        super(grid); // Set the grid as the viewport view
        this.grid = grid;

        getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);

        this.horizontalScrollBar = getHorizontalScrollBar();
        this.verticalScrollBar = getVerticalScrollBar();

        // Remove the border of the scroll pane
        setBorder(null);
        // And the bar
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add a mouse adapter to handle right-click dragging
        MouseAdapter ma = new MouseAdapter() {
            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    origin = null;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null && SwingUtilities.isRightMouseButton(e)) {
                    int deltaX = origin.x - e.getX();
                    int deltaY = origin.y - e.getY();

                    horizontalScrollBar.setValue(horizontalScrollBar.getValue() + deltaX);
                    verticalScrollBar.setValue(verticalScrollBar.getValue() + deltaY);

                }
            }
        };

        grid.addMouseListener(ma);
        grid.addMouseMotionListener(ma);

        // Set the preferred size of the scrollable area
        setPreferredSize(new Dimension(1300, 844));
    }

    @Override
    public void addNotify() {
        super.addNotify();

        // Calculate the center coordinates
        int x = (grid.getPreferredSize().width - getPreferredSize().width) / 2;
        int y = (grid.getPreferredSize().height - getPreferredSize().height) / 2;

        // Set the initial scroll position to the center
        getViewport().setViewPosition(new Point(x, y));
    }

    public ScrollableGridView(int maxHexagons) {
        this(new GridView(maxHexagons));
    }

    public void addHexagon(HexagonView hexagon) {
        grid.addHexagon(hexagon);
    }
}
