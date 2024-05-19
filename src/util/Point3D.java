package util;

import java.awt.Point;
import java.util.Objects;

/**
 * Represents a point in 3D space.
 * @param x the x coordinate.
 * @param y the y coordinate.
 * @param z the z coordinate.
 */
public class Point3D extends Point{
    public int z;


    public Point3D(int x, int y){
        super(x, y);
        this.z = 1;
    }

    public Point3D(int x, int y, int z){
        super(x, y);
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point3D point3D = (Point3D) obj;
        return x == point3D.x && y == point3D.y && z == point3D.z;
    }

    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public void setLocation(int x, int y){
        super.setLocation(x, y);
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
