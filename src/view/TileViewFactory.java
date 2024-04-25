package view;

import java.util.ArrayList;

import model.Tile;

/**
 * This class is used to create the tile views
 * It's a convenient way to create tile views based on the model
 */
public class TileViewFactory {
    
    /**
     * This method is used to create a tile view based on the model
     * @param tile The tile model
     * @return The tile view
     */
    public static TileView createTileView(Tile tile, int size) {
        // We need to iterate through all the hexagons of the tile to build the view
        ArrayList<HexagonView> hexagonViews = new ArrayList<>();
        for (int i = 0; i < tile.getHexagons().size(); i++) {
            hexagonViews.add(HexagonViewFactory.createHexagonView(tile.getHexagons().get(i), size));
        }
        return new TileView(hexagonViews.get(0), hexagonViews.get(1), hexagonViews.get(2));
    }
}
