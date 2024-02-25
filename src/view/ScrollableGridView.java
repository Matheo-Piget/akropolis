package view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;

public class ScrollableGridView extends JScrollPane {

    private GridView grid;

    public ScrollableGridView(GridView grid) {
        super(grid); // Set the grid as the viewport view
        this.grid = grid;

        // Remove the border of the scroll pane
        setBorder(null);
        // And the bar
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Customize the bar appearance
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);
        // Create a custom UI that doesn't paint the track and buttons
        BasicScrollBarUI customUI = new BasicScrollBarUI() {
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                // Don't paint anything
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                // Paint the thumb as usual
                super.paintThumb(g, c, thumbBounds);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                // Create an invisible button
                return createInvisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                // Create an invisible button
                return createInvisibleButton();
            }

            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        };

        // Set the custom UI to the scroll bars
        getVerticalScrollBar().setUI(customUI);
        getHorizontalScrollBar().setUI(customUI);

        // Set the preferred size of the scrollable area
        setPreferredSize(new Dimension(1500, 844));
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
        // Repaint only the viewport
        Rectangle r = getViewport().getViewRect();
        getViewport().scrollRectToVisible(r);
    }
}
