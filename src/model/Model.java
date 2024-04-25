package model;

import java.beans.PropertyChangeSupport;

/**
 * This class is the template that all models must follow
 */
public abstract class Model {
    protected PropertyChangeSupport propertyChangeSupport;

    public Model() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
