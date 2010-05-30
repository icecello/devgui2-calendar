package gdcalendar.gui.calendar.daycard;

import actionmanager.Action;
import actionmanager.ActionManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * A pop-up menu showing modifications that can be done
 * for a DayCard, such as adding of an event, editing an event
 * and removing an event.
 *
 * The pop-up menu sends notifications to all who are listning
 * when are certain action is performed
 * @author James
 * @author Tomas
 */
@SuppressWarnings("serial")
public class DayPopupMenu extends JPopupMenu {

    public static final String ADD = "Add";
    public static final String VIEW = "View";
    public static final String EDIT = "Edit";
    public static final String DELETE = "Delete";

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
     * Sends a notification that view day action should be called
     * in the classes listening
     */
    @Action
    public void viewDay() {
        propSupport.firePropertyChange(VIEW, null, null);
    }

    /**
     * Sends a notification that add action should be called
     * in the classes listening
     */
    @Action
    public void addEvent() {
        propSupport.firePropertyChange(ADD, null, null);
    }

    /**
     * Sends a notification that edit action should be called
     * in the classes listening
     */
    @Action
    public void editEvent() {
        propSupport.firePropertyChange(EDIT, null, null);
    }

    /**
     * Sends a notification that delete action should be called
     * in the classes listening
     */
    @Action
    public void deleteEvent() {
        propSupport.firePropertyChange(DELETE, null, null);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propSupport.addPropertyChangeListener(l);
    }

    /**
     * Set if the edit item should be enabled or not
     * @param value true if the item should be enables, and false otherwise
     */
    public void setEditEnabled(boolean value){
        actionManager.getAction("editEvent").setEnabled(value);
    }

    /**
     * Set if the delete item should be enabled or not
     * @param value true if the item should be enables, and false otherwise
     */
    public void setDeleteEnabled(boolean value){
        actionManager.getAction("deleteEvent").setEnabled(value);
    }
}
