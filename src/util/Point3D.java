package util;

import java.awt.Point;
/**
 * Represents a point in 3D space.
 */
public class Point3D extends Point{
    public int z;

    public Point3D(int x, int y, int z){
        super(x, y);
        this.z = z;
    }
    public Point3D(Point p, int z){
        super(p);
        this.z = z;
    }
    public Point3D(){
        super();
        this.z = 0;
    }
    public Point3D(Point3D p){
        super(p);
        this.z = p.z;
    }
    public boolean equals(Point3D p){
        return super.equals(p) && this.z == p.z;
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
}
