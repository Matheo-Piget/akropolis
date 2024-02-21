package model;

import java.util.ArrayList;
import java.util.Map;

import util.Point3D;

public class Site {
      private Map<Point3D, Tile> siteMap;

      public void add_Tiles(ArrayList<Tile> tiles){
        for (Tile tile:tiles){
            siteMap.put(null, tile);
        }
    }
}

