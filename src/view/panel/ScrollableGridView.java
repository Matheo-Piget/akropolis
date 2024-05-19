package view.panel;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.geom.Point2D;
import util.Point3D;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import view.View;
import view.component.HexagonView;
import view.component.TileView;
import view.main.App;

/**
 * Represents a scrollable grid view.
 * This class is used to display the game grid in a scrollable view.
 */
public class ScrollableGridView extends JScrollPane implements View {
    public static int hexagonSize = 90;
    /**
     * Represents the game grid displayed in game.
     */
    public static class GridView extends JPanel {

        private final int xOffset;
        private final int yOffset;

        private final HashMap<Point, HexagonView> hexagons = new HashMap<>();

        public GridView(int maxHexagons) {
            setDoubleBuffered(true);
            int panelWidth = (int) (maxHexagons * 3.0 / 2 * hexagonSize);

            int panelHeight = (int) (maxHexagons * Math.sqrt(3) * hexagonSize);
            setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setLayout(null); // We will manually set the position of the hexagons
            xOffset = getPreferredSize().width / 2;// Offset for centering the (0, 0)
            yOffset = getPreferredSize().height / 2; // Offset for centering the (0, 0)
        }

        /**
         * Convert a grid position to a pixel position
         *
         * @param gridPosition The grid position
         * @return The pixel position
         */
        public Point2D convertGridPositionToPixelPosition(Point gridPosition) {
            int q = gridPosition.x; // column index
            int r = gridPosition.y; // row index
            int size = hexagonSize / 2; // size of the hexagon
            int pixelX = (int) (size * 3.0 / 2 * q) + xOffset;
            int pixelY = (int) (size * Math.sqrt(3) * (r + q / 2.0)) + yOffset;
            return new Point2D.Double(pixelX, pixelY);
        }

        /**
         * Get the hexagon at the given pixel position
         * Be careful, it will return null if there is no hexagon at the given mouse
         * position
         *
         * @param pixelPosition The pixel position
         * @return The hexagon at the given pixel position
         */
        public HexagonView getHexagonAtPixelPos(Point2D pixelPosition) {
            // We just need to use getComponentAt to get the hexagon at the pixel position
            Component component = getComponentAt((int) pixelPosition.getX(), (int) pixelPosition.getY());
            if (component instanceof HexagonView) {
                return (HexagonView) component;
            }
            return null;
        }

        public HexagonView getHexagonAtGridPos(int x, int y) {
            return hexagons.get(new Point(x, y));
        }

        /**
         * Round the axial coordinates to the nearest hexagon
         *
         * @param q The q coordinate
         * @param r The r coordinate
         * @return The rounded coordinates
         */
        private Point3D axialRound(double q, double r) {
            double s = -q - r;
            double rq = Math.round(q);
            double rr = Math.round(r);
            double rs = Math.round(s);

            double qDiff = Math.abs(rq - q);
            double rDiff = Math.abs(rr - r);
            double sDiff = Math.abs(rs - s);

            if (qDiff > rDiff && qDiff > sDiff) {
                rq = -rr - rs;
            } else if (rDiff > sDiff) {
                rr = -rq - rs;
            }
            return new Point3D((int) rq, (int) rr, 0);
        }

        /**
         * Add a hexagon to the grid
         *
         * @param hexagon The hexagon to add
         */
        public void addHexagon(HexagonView hexagon) {
            boolean contains = hexagons.containsKey(hexagon.getPosition());
            if (contains && hexagon.getZ() == 0) {
                // It's an outline that will overlap
                return;
            }
            // Find the position of the hexagon in pixels
            Point2D position = convertGridPositionToPixelPosition(hexagon.getPosition());
            // If there is already a hexagon with the same x and y, remove it
            if (contains) {
                this.remove(hexagons.get(hexagon.getPosition()));
                hexagons.remove(hexagon.getPosition());
            }
            hexagon.setLocation((int) Math.round(position.getX()), (int) Math.round(position.getY()));
            hexagons.put(hexagon.getPosition(), hexagon);
            // Add it to the JPanel
            this.add(hexagon);
            // Repaint the area where the hexagon is
            this.repaint(hexagon.getBounds());
        }
    }

    //Fields of the ScrollableGridView class

    private final GridView grid;
    private final JScrollBar horizontalScrollBar;
    private final JScrollBar verticalScrollBar;
    private TileView selectedTile;
    private HexagonView[] filledHexagonViews = new HexagonView[3];
    private HexagonView hoveredHexagon = null;
    private final MouseAdapter ma;

    public ScrollableGridView(GridView grid) {
        super(grid);
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
        ma = new MouseAdapter() {
            private Point origin;

            @Override
            public void mouseMoved(MouseEvent e) {
                if (selectedTile != null) {
                    // Get the hexagon under the mouse which is a component of the grid
                    HexagonView actualHoveredHexagon = grid.getHexagonAtPixelPos(e.getPoint());
                    if (actualHoveredHexagon != null && actualHoveredHexagon.equals(hoveredHexagon)) {
                        return;
                    }
                    if (actualHoveredHexagon != null && !actualHoveredHexagon.equals(hoveredHexagon)) {
                        // Remove the fill from the previous hovered hexagon
                        if (hoveredHexagon != null) {
                            unfilledEachHexagons();
                        }
                        hoveredHexagon = actualHoveredHexagon;
                        // Fill the new hovered hexagon
                        fillEachHexagons(selectedTile, hoveredHexagon);
                        // Determine which tile to fill based on the tile rotation
                    } else if (actualHoveredHexagon == null && hoveredHexagon != null) {
                        // Remove the fill from the previous hovered hexagon
                        unfilledEachHexagons();
                        hoveredHexagon = null;
                    }
                } else {
                    if (hoveredHexagon != null) {
                        unfilledEachHexagons();
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
        setPreferredSize(new Dimension(App.getInstance().getWidth(), App.getInstance().getHeight()));
    }

    public GridView getGrid() {
        return grid;
    }

    /**
     * Unfilled the hexagons
     */
    public void unfilledEachHexagons() {
        for (HexagonView hexagon : filledHexagonViews) {
            if (hexagon != null) {
                hexagon.unfilled();
            }
        }
        // Reset the filled hexagons
        filledHexagonViews = new HexagonView[3];
    }

    /**
     * Get the filled hexagons
     * 
     * @return The filled hexagons
     */
    public HexagonView[] getFilledHexagons() {
        return filledHexagonViews;
    }

    /**
     * Fill the hexagons with the tile
     * 
     * @param tile           The tile to fill the hexagons with
     * @param hoveredHexagon The hovered hexagon
     */
    public void fillEachHexagons(TileView tile, HexagonView hoveredHexagon) {
        int rotation = tile.getRotation();
        HexagonView hex2 = null;
        HexagonView hex3;
        int x = hoveredHexagon.getPosition().x;
        int y = hoveredHexagon.getPosition().y;
        hex3 = switch (rotation) {
            case 0 -> {
                hex2 = grid.getHexagonAtGridPos(x - 1, y);
                yield grid.getHexagonAtGridPos(x - 1, y + 1);
            }
            case 90 -> {
                hex2 = grid.getHexagonAtGridPos(x, y - 1);
                yield grid.getHexagonAtGridPos(x + 1, y - 1);
            }
            case 180 -> {
                hex2 = grid.getHexagonAtGridPos(x + 1, y);
                yield grid.getHexagonAtGridPos(x + 1, y - 1);
            }
            case 270 -> {
                hex2 = grid.getHexagonAtGridPos(x, y + 1);
                yield grid.getHexagonAtGridPos(x - 1, y + 1);
            }
            default -> null;
        };
        if (hex2 != null && hex3 != null) {
            ArrayList<HexagonView> hexagons = tile.getHexagons();
            hoveredHexagon.fill(hexagons.get(0));
            hex2.fill(hexagons.get(1));
            hex3.fill(hexagons.get(2));
            filledHexagonViews[0] = hoveredHexagon;
            filledHexagonViews[1] = hex2;
            filledHexagonViews[2] = hex3;
        }
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

    /**
     * Enable the listeners
     */
    public void enableListeners() {
        grid.addMouseListener(ma);
        grid.addMouseMotionListener(ma);
        grid.setEnabled(true);
    }

    /**
     * Disable the listeners
     */
    public void disableListeners() {
        grid.removeMouseListener(ma);
        grid.removeMouseMotionListener(ma);
        grid.setEnabled(false);
    }

    /**
     * Set the selected tile
     * 
     * @param tile The selected tile
     */
    public void setSelectedTile(TileView tile) {
        if (selectedTile != null) {
            // Unfilled the hexagons
            unfilledEachHexagons();
        }
        // Reset also the previous rotation
        tile.resetRotation();
        selectedTile = tile;
    }

    /**
     * Rotate the selected tile
     */
    public void rotateSelectedTile() {
        if (selectedTile != null) {
            selectedTile.rotate();
            if (hoveredHexagon != null) {
                unfilledEachHexagons();
                fillEachHexagons(selectedTile, hoveredHexagon);
            }
        }
    }

    /**
     * Center the view of the scroll pane
     */
    public void centerView() {
        int x = (grid.getPreferredSize().width - getPreferredSize().width) / 2;
        int y = (grid.getPreferredSize().height - getPreferredSize().height) / 2;
        getViewport().setViewPosition(new Point(x, y));
    }

    /**
     * Remove the selected tile
     */
    public void removeSelectedTile() {
        if (selectedTile != null) {
            // Unfilled the hexagons
            unfilledEachHexagons();
            hoveredHexagon = null;
            selectedTile = null;
        }
    }
}
