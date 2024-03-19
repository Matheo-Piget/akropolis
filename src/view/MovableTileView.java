package view;

import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

/**
 * Implements a draggable tile view.
 * This class is responsible for the visual representation of a draggable tile.
 */
public class MovableTileView extends TileView {

    public MovableTileView(TileView tile) {
        super(tile);
        setOpaque(false);
        // Then remove all mouseListener from tile to make sure they don't interfere
        // with the movement
        for (MouseListener listener : this.getMouseListeners()) {
            this.removeMouseListener(listener);
        }
        int hexHeight = (int) (Math.sqrt(3) / 2 * HexagonView.size * 2);
        this.setSize((int) (Math.sqrt(3) * HexagonView.size), hexHeight);
    }

    public Point2D getCenter() {
        // The center is based on the center of hex1
        return new Point2D.Double(hex1.getX() + hex1.getWidth() / 2, hex1.getY() + hex1.getHeight() / 2);
    }
}
