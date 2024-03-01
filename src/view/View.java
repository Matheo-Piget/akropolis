package view;

import java.beans.PropertyChangeListener;

/**
 * This class is the template that all views must follow
 */
public interface View {
    void addPropertyChangeListener(PropertyChangeListener pcl);
    void removePropertyChangeListener(PropertyChangeListener pcl);
}
