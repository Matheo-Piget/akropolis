package view;

import java.awt.Color;
import model.District;

public class DistrictView extends HexagonView {
    private District district;
    private static Color strokeColor = Color.BLACK;

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
        return new DistrictView(pos.x, pos.y , z, district);
    }

    @Override 
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (render == null) {
            renderHexagon(strokeColor);
        }
        g.drawImage(render, 0, 0, null);
    }
}
