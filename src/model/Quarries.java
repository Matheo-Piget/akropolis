package model;

import util.Point3D;

/**
 * Represents a quarries in the game.
 * They give you a stone if you overlap one of them.
 */
public class Quarries extends Hexagon{

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