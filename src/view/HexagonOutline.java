package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

/**
 * Implements a hexagon outline view.
 */
public class HexagonOutline extends HexagonView {
    private static BasicStroke stroke = new BasicStroke(size / 25);
    
    public HexagonOutline(int x, int y, int z) {
        super(x, y, z);
        setOpaque(false);
    }

    @Override
    public HexagonOutline copy(){
        return new HexagonOutline(pos.x, pos.y, z);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        // Draw a blue border around the hexagon
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.draw(hexagon);
        g2d.dispose();
    }
}
