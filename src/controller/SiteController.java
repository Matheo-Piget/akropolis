package controller;

import model.Site;
import view.SiteView;
import java.beans.PropertyChangeEvent;

public class SiteController extends Controller{
    
    public SiteController(Site model, SiteView view) {
        super(model, view);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("tileSelected")) {
            // Update the model with the selected tile
        }
    }
}
