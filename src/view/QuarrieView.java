package view;

public class QuarrieView extends HexagonView {
    
    public QuarrieView(int x, int y, int side) {
        super(x, y, side, java.awt.Color.GRAY);
    }

    @Override
    public void paint(java.awt.Graphics2D g) {
        g.setPaint(this.texture);
        g.fill(this);

        // Draw the border of the hexagon
        g.setColor(java.awt.Color.BLACK);
        g.setStroke(new java.awt.BasicStroke(3));
        g.draw(this);
    }
}
