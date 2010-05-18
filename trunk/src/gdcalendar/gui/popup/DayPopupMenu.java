/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui.popup;

import actionmanager.Action;
import actionmanager.ActionManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 *
 * @author James
 */

public class DayPopupMenu extends JPopupMenu {

    protected DayPopupMenu popup = this;

    private ActionManager actionManager;
    private ResourceBundle resource;

    private JMenuItem viewItem;
    private JMenuItem addItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;

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
    	System.out.println("Viewing daycard...");
    }

    /**
     * Info here.
     */
    @Action
    public void addEvent() {
    	System.out.println("Adding event...");
    }

    /**
     * Info here.
     */
    @Action
    public void editEvent() {
    	System.out.println("Editing event...");
    }

    /**
     * Info here.
     */
    @Action
    public void deleteEvent() {
    	System.out.println("Deleting event...");
    }
}
