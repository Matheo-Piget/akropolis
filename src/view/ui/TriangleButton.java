package view.ui;

import javax.swing.JButton;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Polygon;
import java.awt.Color;

/**
 * Represents a button that has a triangle shape.
 */
public class TriangleButton extends JButton{
    private Polygon triangle;
    private Color originalColor = getBackground();
    private boolean isHovered = false;
    
    public TriangleButton(String text) {
        super(text);
        triangle = new Polygon();
        triangle.addPoint(0, getHeight()/2);
        triangle.addPoint(getWidth(), 0);
        triangle.addPoint(getWidth(), getHeight());
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        originalColor = bg;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        triangle = new Polygon();
        triangle.addPoint(0, height/2);
        triangle.addPoint(width, 0);
        triangle.addPoint(width, height);
    }

    // Receive only the input when the polygon is clicked
    @Override
    public boolean contains(int x, int y) {
        return triangle.contains(x, y);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Enable antialiasing
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(isHovered ? originalColor.brighter() : originalColor);
        g.fillPolygon(triangle);
        // Draw the text inside the triangle
        g.setColor(getForeground());
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() / 2) + (fm.getAscent() / 2) - (fm.getDescent() / 2);
        g2d.drawString(getText(), x, y);
    }
}
