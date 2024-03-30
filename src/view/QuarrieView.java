package view;

public class QuarrieView extends HexagonView {
    private static java.awt.Color strokeColor = java.awt.Color.BLACK;
    
    public QuarrieView(int x, int y, int z, int size) {
        super(x, y, z, java.awt.Color.GRAY, size);
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (render == null) {
            renderHexagon(strokeColor, texture);
        }
        g.drawImage(render, 0, 0, null);
    }
}
