package util;

import java.awt.Point;
import java.util.Objects;

/**
 * Represents a point in 3D space.
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
    public void setLocation(int x, int y, int z){
        super.setLocation(x, y);
        this.z = z;
    }
    public void setLocation(Point p, int z){
        super.setLocation(p);
        this.z = z;
    }
    public void setLocation(Point3D p){
        super.setLocation(p);
        this.z = p.z;
    }
    public void move(int dx, int dy, int dz){
        super.move(dx, dy);
        this.z += dz;
    }
    public void translate(int dx, int dy, int dz){
        super.translate(dx, dy);
        this.z += dz;
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
