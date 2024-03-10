package view;

import model.Hexagon;

/**
 * This class is used to create the hexagon views
 * It's a convienent way to create hexagon views based on the model
 */
public class HexagonViewFactory {

    /**
     * This method is used to create a hexagon view based on the model
     * @param hexagon The hexagon model
     * @return The hexagon view
     */
    public static HexagonView createHexagonView(Hexagon hexagon) {
        if (hexagon instanceof model.Quarrie) {
            return new QuarrieView(hexagon.getX(), hexagon.getY(), hexagon.getZ());
        } else if (hexagon instanceof model.Place) {
            return new PlaceView(hexagon.getX(), hexagon.getY(), hexagon.getZ(), (model.Place) hexagon);
        } else if (hexagon instanceof model.District) {
            return new DistrictView(hexagon.getX(), hexagon.getY(), hexagon.getZ(), (model.District) hexagon);
        }
        // If the hexagon is not of any of the known types, return null
        return null;
    }
}
