package test;

import model.Board;
import model.Grid;
import model.Quarrie;
import model.TileTrio;
import util.Point3D;

public class Test {
    public static void main(String[] args) {
        Board b1 = new Board();
        Quarrie t1 = new Quarrie(new Point3D(0, 0, 1));t1.setGrid(b1.getGrid());
        Quarrie t2 = new Quarrie(new Point3D(0, -1, 1));t2.setGrid(b1.getGrid());
        Quarrie t3 = new Quarrie(new Point3D(1, -1, 1));t3.setGrid(b1.getGrid());
        TileTrio trio = new TileTrio(t1, t2, t3);
        System.out.println(Grid.tuileValide(trio));
        
    }
}
