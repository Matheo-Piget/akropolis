package view;

import java.awt.Color;

/**
 * Implements a hexagon outline view.
 */
public class HexagonOutline extends HexagonView {
    private static Color strokeColor = Color.BLUE;

    public HexagonOutline(int x, int y, int z) {
        super(x, y, z);
        setOpaque(false);
    }

    @Override
    public HexagonOutline copy() {
        return new HexagonOutline(pos.x, pos.y, z);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (render == null) {
            renderHexagon(strokeColor);
        }
        g.drawImage(render, 0, 0, null);
    }
}
