package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.Point3D;

public class Site extends Model {
      private ArrayList<Tile> tiles;

      public Site(int nbTiles) {
            tiles = new ArrayList<Tile>(nbTiles);
      }

      public void add(Tile tile) {
            tiles.add(tile);
      }
      public int size() {
          return tiles.size();
      }

      public Tile get(int index) {
          return tiles.get(index);
      }

      public void remove(int index) {
          tiles.remove(index);
      }

      public void remove(Tile tile) {
          tiles.remove(tile);
      }

      public boolean isEmpty() {
          return tiles.isEmpty();
      }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}

