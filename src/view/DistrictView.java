package view;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import model.District;

public class DistrictView extends HexagonView {
    private BasicStroke stroke = new BasicStroke(size / 25);
    private District district;

    public DistrictView(int x, int y, int z, District d) {
        super(x, y, z);
        this.district = d;
        switch (d.getType()) {
            case "Barrack":
                this.texture = TextureFactory.getTexture("barrack");
                break;
            case "Building":
                this.texture = TextureFactory.getTexture("building");
                break;
            case "Garden":
                this.texture = TextureFactory.getTexture("garden");
                break;
            case "Market":
                this.texture = TextureFactory.getTexture("market");
                break;
            case "Temple":
                this.texture = TextureFactory.getTexture("temple");
                break;
            default:
                break;
        }
    }

    @Override
    public DistrictView copy(){
        return new DistrictView((int) getPosition().getX(), (int) getPosition().getY(), (int) getPosition().getZ(), district);
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
