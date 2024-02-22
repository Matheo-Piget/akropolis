package view;

import model.District;

public class DistrictView extends HexagonView {
    
    public DistrictView(int x, int y, int side, District d) {
        super(x, y, side);
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

    public DistrictView(int x, int y, int z, int side, District d) {
        super(x, y, z, side);
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
    public void paint(java.awt.Graphics2D g) {
        g.setPaint(darkenTexturePaint(this.texture, this.position.getZ()));
        g.fill(this);

        // Draw the border of the hexagon
        g.setColor(java.awt.Color.BLACK);
        g.setStroke(new java.awt.BasicStroke(3));
        g.draw(this);
    }
}
