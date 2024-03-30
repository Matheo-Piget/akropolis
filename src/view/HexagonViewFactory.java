package view;

import model.Hexagon;
import model.Place;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * This class is used to create the hexagon views
 * It's a convienent way to create hexagon views based on the model
 */
public class HexagonViewFactory {
    private static final Map<Class<? extends Hexagon>, BiFunction<Hexagon, Integer, HexagonView>> hexagonViewCreators = new HashMap<>();

    static {
        hexagonViewCreators.put(model.Quarrie.class,
                (hexagon, size) -> new QuarrieView(hexagon.getX(), hexagon.getY(), hexagon.getZ(), size));
        hexagonViewCreators.put(model.Place.class, (hexagon, size) -> new PlaceView(hexagon.getX(), hexagon.getY(),
                hexagon.getZ(), (model.Place) hexagon, size));
        hexagonViewCreators.put(model.District.class, (hexagon, size) -> new DistrictView(hexagon.getX(),
                hexagon.getY(), hexagon.getZ(), (model.District) hexagon, size));
    }

    private static final Map<Class<? extends HexagonView>, BiFunction<HexagonView, Integer, HexagonView>> hexagonViewCopyCreators = new HashMap<>();

    static {
        hexagonViewCopyCreators.put(QuarrieView.class, (hexagonView, size) -> new QuarrieView(hexagonView.getX(),
                hexagonView.getY(), hexagonView.getZ(), size));
        hexagonViewCopyCreators.put(PlaceView.class, (hexagonView, size) -> {
            if (hexagonView instanceof PlaceView) {
                Place place = ((PlaceView) hexagonView).getPlace();
                return new PlaceView(hexagonView.getX(), hexagonView.getY(), hexagonView.getZ(), place, size);
            }
            return null;
        });
        hexagonViewCopyCreators.put(DistrictView.class,(hexagonView, size) -> {
            if (hexagonView instanceof DistrictView) {
                return new DistrictView(hexagonView.getX(), hexagonView.getY(), hexagonView.getZ(), (model.District) ((DistrictView) hexagonView).getDistrict(), size);
            }
            return null;
        });
    }

    /**
     * This method is used to create a hexagon view based on the model
     * 
     * @param hexagon The hexagon model
     * @return The hexagon view
     */
    public static HexagonView createHexagonView(Hexagon hexagon, int size) {
        BiFunction<Hexagon, Integer, HexagonView> creator = hexagonViewCreators.get(hexagon.getClass());
        if (creator != null) {
            return creator.apply(hexagon, size);
        }
        return null;
    }

    public static HexagonView createHexagonView(HexagonView hexagonView, int size) {
        BiFunction<HexagonView, Integer, HexagonView> creator = hexagonViewCopyCreators.get(hexagonView.getClass());
        if (creator != null) {
            return creator.apply(hexagonView, size);
        }
        return null;
    }
}
