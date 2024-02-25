package view;

import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class QuarrieView extends HexagonView {
    private BasicStroke stroke = new BasicStroke(size / 25);
    
    public QuarrieView(int x, int y, int z) {
        super(x, y, z, java.awt.Color.GRAY);
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(darkenTexturePaint(texture, getPosition().getZ()));
        g2d.fill(hexagon);

        // Draw the border of the hexagon
        if (isHovered) {
            g2d.setColor(java.awt.Color.YELLOW);
        }
        else {
            g2d.setColor(java.awt.Color.BLACK);
        }

        g2d.setStroke(stroke);
        g2d.draw(hexagon);
        g2d.dispose();
    }
}
