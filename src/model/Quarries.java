package model;

import util.Point3D;

// Quarries are a type of tile that can be placed on the board that allow you to earn stones useful for buying tiles.
public class Quarries extends Hexagon{

    public Quarries(Point3D p, Grid grid) {
        super(p, grid);
    }

    public Quarries(int x, int y){
        super(x, y);
    }

    public Quarries(Point3D p) {
        super(p);
    }

    @Override
    public String getType() {
        return "Quarries";
    }
}