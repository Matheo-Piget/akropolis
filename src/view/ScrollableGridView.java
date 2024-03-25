package view;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class ScrollableGridView extends JScrollPane implements View {

    private GridView grid;
    private JScrollBar horizontalScrollBar;
    private JScrollBar verticalScrollBar;
    private TileView selectedTile;
    private HexagonView[] filledHexagonViews = new HexagonView[3];
    private HexagonView hoveredHexagon = null;

    public ScrollableGridView(GridView grid) {
        super(grid);
        setFocusable(true);
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
            public void mouseMoved(MouseEvent e) {
                if (selectedTile != null) {
                    // Get the hexagon under the mouse which is a component of the grid
                    HexagonView actualHoveredHexagon = grid.getHexagonAtPixelPos(e.getPoint());
                    if (actualHoveredHexagon != null && hoveredHexagon != null
                            && actualHoveredHexagon.equals(hoveredHexagon)) {
                        return;
                    }
                    if (actualHoveredHexagon != null && !actualHoveredHexagon.equals(hoveredHexagon)) {
                        // Remove the fill from the previous hovered hexagon
                        if (hoveredHexagon != null) {
                            unfillEachHexagons();
                        }
                        hoveredHexagon = actualHoveredHexagon;
                        // Fill the new hovered hexagon
                        fillEachHexagons(selectedTile, hoveredHexagon);
                        // Determine which tile to fill based on the tile rotation
                    } else if (actualHoveredHexagon == null && hoveredHexagon != null) {
                        // Remove the fill from the previous hovered hexagon
                        unfillEachHexagons();
                        hoveredHexagon = null;
                    }
                } else {
                    if (hoveredHexagon != null) {
                        unfillEachHexagons();
                        hoveredHexagon = null;
                    }
                }
            }

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

    public void unfillEachHexagons() {
        for (HexagonView hexagon : filledHexagonViews) {
            if (hexagon != null) {
                hexagon.unfill();
            }
        }
        // Reset the filled hexagons
        filledHexagonViews = new HexagonView[3];
    }

    public void fillEachHexagons(TileView tile, HexagonView hoveredHexagon) {
        int rotation = tile.getRotation();
        HexagonView hex1 = hoveredHexagon;
        HexagonView hex2 = null;
        HexagonView hex3 = null;
        int x = hex1.getPosition().x;
        int y = hex1.getPosition().y;
        switch (rotation) {
            case 0:
                hex2 = grid.getHexagonAtGridPos(x - 1, y);
                hex3 = grid.getHexagonAtGridPos(x - 1, y + 1);
                break;
            case 90:
                hex2 = grid.getHexagonAtGridPos(x, y - 1);
                hex3 = grid.getHexagonAtGridPos(x + 1, y - 1);
                break;
            case 180:
                hex2 = grid.getHexagonAtGridPos(x + 1, y);
                hex3 = grid.getHexagonAtGridPos(x + 1, y - 1);
                break;
            case 270:
                hex2 = grid.getHexagonAtGridPos(x, y + 1);
                hex3 = grid.getHexagonAtGridPos(x - 1, y + 1);
                break;
        }
        if(hex1 !=null){
            hex1.fill(tile.hex1);
        }
        if(hex2 !=null){
            hex2.fill(tile.hex2);
        }
        if(hex3 !=null){
            hex3.fill(tile.hex3);
        }
        filledHexagonViews[0] = hex1;
        filledHexagonViews[1] = hex2;
        filledHexagonViews[2] = hex3;
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

    public void setSelectedTile(TileView tile) {
        if (selectedTile != null) {
            // Unfill the hexagons
            unfillEachHexagons();
        }
        selectedTile = tile;
    }

    public void removeSelectedTile() {
        if (selectedTile != null) {
            // Unfill the hexagons
            unfillEachHexagons();
            hoveredHexagon = null;
            selectedTile = null;
        }
    }
}
