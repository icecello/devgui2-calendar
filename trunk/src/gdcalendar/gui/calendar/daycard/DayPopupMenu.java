/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gdcalendar.gui.calendar.daycard;

import actionmanager.Action;
import actionmanager.ActionManager;
import gdcalendar.gui.AddEventWindow;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 *
 * @author James
 */
public class DayPopupMenu extends JPopupMenu {

    public static final String ADD = "add";
    public static final String VIEW = "view";
    public static final String EDIT = "edit";
    public static final String DELETE = "delete";

    protected DayPopupMenu popup = this;
    private ActionManager actionManager;
    private ResourceBundle resource;
    private JMenuItem viewItem;
    private JMenuItem addItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;
    private PropertyChangeSupport propSupport = new PropertyChangeSupport(this);

    public DayPopupMenu() {
        //load the resource file
        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);

        viewItem = new JMenuItem(actionManager.getAction("viewDay"));
        addItem = new JMenuItem(actionManager.getAction("addEvent"));
        editItem = new JMenuItem(actionManager.getAction("editEvent"));
        deleteItem = new JMenuItem(actionManager.getAction("deleteEvent"));

        add(viewItem);
        add(new JSeparator());
        add(addItem);
        add(editItem);
        add(deleteItem);
    }

    /**
     * Info here.
     */
    @Action
    public void viewDay() {
        propSupport.firePropertyChange(VIEW, null, null);
    }

    /**
     * Info here.
     */
    @Action
    public void addEvent() {
        // Should send the date of the current DayCard.
        AddEventWindow addEventWindow = new AddEventWindow(new Date());
        addEventWindow.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                propSupport.firePropertyChange(ADD, evt.getOldValue(), evt.getNewValue());
            }
        });
        addEventWindow.setVisible(true);
    }

    /**
     * Info here.
     */
    @Action
    public void editEvent() {
        propSupport.firePropertyChange(EDIT, null, null);
    }

    /**
     * Info here.
     */
    @Action
    public void deleteEvent() {
        propSupport.firePropertyChange(DELETE, null, null);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propSupport.addPropertyChangeListener(l);
    }
}
