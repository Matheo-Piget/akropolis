package view.component;

import java.awt.Color;

/**
 * Implements a hexagon outline view.
 * This class is used to draw the outline of a hexagon.
 */
public class HexagonOutline extends HexagonView {
    private Color strokeColor = Color.BLUE;

    public HexagonOutline(int x, int y, int z, int size, Color strokeColor) {
        super(x, y, z, size);
        this.strokeColor = strokeColor;
        setOpaque(false);
    }

    @Override
    public void unfilled() {
        renderHexagon(strokeColor, texture);
        repaint();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (render == null) {
            renderHexagon(strokeColor, null);
        }
        g.drawImage(render, 0, 0, null);
    }
}
