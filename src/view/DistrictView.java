package view;

import java.awt.Color;
import model.District;

public class DistrictView extends HexagonView {
    private final District district;
    private static final Color strokeColor = Color.BLACK;

    public DistrictView(int x, int y, int z, District d, int size) {
        super(x, y, z, size);
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

    public District getDistrict() {
        return district;
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
