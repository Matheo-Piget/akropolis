package view;

public class QuarrieView extends HexagonView {
    
    public QuarrieView(int x, int y, int side) {
        super(x, y, side, java.awt.Color.GRAY);
    }

    @Override
    public void paint(java.awt.Graphics2D g) {
        g.setPaint(this.texture);
        g.fill(this);
    }
}
