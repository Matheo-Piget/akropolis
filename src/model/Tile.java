package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Tile {
    private Point position;
    private Grid grid;

    private Tile trio1;
    private Tile trio2;

    public Tile(int x, int y, Grid grid) {
        this.position = new Point(x, y);
        this.grid = grid;
    }

    public void rotate() {
        // Rotate the tile by 90 degrees by exchanging trio1 and trio2 positions in the grid and updating their positions
        grid.addTile(trio1, trio2.getX(), trio2.getY());
    }

    public void setTrio(Tile trio1, Tile trio2) {
        this.trio1 = trio1;
        this.trio2 = trio2;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
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
}
