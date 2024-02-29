package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import view.GridView;

public class GridMouseListener extends MouseAdapter {
    private GridView gridView;

    public GridMouseListener(GridView gridView) {
        this.gridView = gridView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point2D pixelPosition = new Point2D.Double(e.getX(), e.getY());
        Point2D gridPosition = gridView.convertPixelPositionToGridPosition(pixelPosition);
        //System.out.println("Clic en coordonnées de grille : " + gridPosition);
    }
}
