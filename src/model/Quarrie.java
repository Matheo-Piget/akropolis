package model;

import util.Point3D;

// Quarries are a type of tile that can be placed on the board that allow you to earn stones useful for buying tiles.
public class Quarrie extends Tile{

    public Quarrie(Point3D p, Grid grid) {
        super(p, grid);
    }

    public Quarrie(Point3D p) {
        super(p);
    }

    @Override
    public String getType() {
        return "Quarrie";
    }
}