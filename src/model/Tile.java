package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Tile {
    private Point position;
    private Grid grid;
    private TileTrio tileTrio;

    private Tile above;
    private Tile below;

    public Tile(int x, int y, Grid grid) {
        this.position = new Point(x, y);
        this.grid = grid;
    }

    public Tile(int x, int y) {
        this.position = new Point(x, y);
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setTileTrio(TileTrio tileTrio) {
        this.tileTrio = tileTrio;
    }

    public TileTrio getTileTrio() {
        return tileTrio;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public Tile getAbove() {
        return above;
    }

    public Tile getBelow() {
        return below;
    }

    public void setAbove(Tile above) {
        this.above = above;
    }

    public void setBelow(Tile below) {
        this.below = below;
    }

    public List<Tile> getNeighbors() {
        List<Tile> neighbors = new ArrayList<>();

        // Define the directions for the 6 neighbors in a hexagonal grid
        int[][] directions;
        if (position.x % 2 == 0) {
            directions = new int[][]{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, 1}};
        } else {
            directions = new int[][]{{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}};
        }

        for (int[] direction : directions) {
            int nx = position.x + direction[0];
            int ny = position.y + direction[1];
            Tile neighbor = grid.getTile(nx, ny);

            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    public boolean hasAbove() {

        return above != null;
    }

    public boolean hasBelow(){

        return below != null;

    }
}
