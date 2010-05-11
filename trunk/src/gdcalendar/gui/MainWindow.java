package gdcalendar.gui;

import gdcalendar.gui.calendar.CalendarChangeEvent;
import gdcalendar.gui.calendar.CalendarContainer;
import gdcalendar.gui.calendar.CalendarDataChangedListener;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import actionmanager.Action;
import actionmanager.ActionManager;

import commandmanager.CommandManager;


/**
 *
 * @author Tomas
 * @author Håkan
 * @author James
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private ActionManager actionManager;
	private ResourceBundle resource;
	private JMenuItem undoItem;
	private JMenuItem redoItem;

	/*
	 * note that we would want an array here if we need more undo managers
	 * for multiple calendars or whatever...
	 */
	private final CommandManager cm = new CommandManager(0);
	
    public MainWindow() throws Exception {
        setLayout(new BorderLayout());

        //load the resource file
        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);
        
        
        /*
         * construct a simple menu
         * currently uses the ActionManager class to construct
         * AbstractAction objects...
         */
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu(resource.getString("menu.edit.text"));
        undoItem = new JMenuItem(actionManager.getAction("doUndo"));
        redoItem = new JMenuItem(actionManager.getAction("doRedo"));
        JMenuItem preferencesItem = new JMenuItem(actionManager.getAction("showPreferences"));
        
        menu.add(undoItem);
        menu.add(redoItem);
        menu.add(preferencesItem);
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);

        mb.add(menu);
        this.setJMenuBar(mb);
        
        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.EAST);
        collapsiblePanel.setLayout(new BorderLayout());
        collapsiblePanel.setCollapsButtonSize(5);
        collapsiblePanel.add(new LeftItemPanel(), BorderLayout.PAGE_START, -1);

        CalendarContainer cc = new CalendarContainer(cm);
        /*
         * Add listener for data change events from the calendar
         * to set undo/redo properly
         * 
         * Not much else is done currently but it's possible
         * to see what kind of change was made if we need that
         * for anything...
         */
        cc.addDataChangeListener(new CalendarDataChangedListener() {
			@Override
			public void dataChanged(CalendarChangeEvent e) {
				undoItem.setEnabled(cm.canUndo());
				redoItem.setEnabled(cm.canRedo());
			}
		});
        
        add(cc, BorderLayout.CENTER);
        
        add(collapsiblePanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
    
    /**
     * Create and show a preferences window that
     * currently only supports changing the main
     * windows opacity.
     */
    @Action
    public void showPreferences() {
    	new PreferencesWindow(this);
    }
    /**
     * This action is performed whenever an event executed on the calendar
     * is to be undone. This also updates the menu entries for undo/redo
     * so their enabled state is correct.
     */
    @Action
    public void doUndo() {
    	try {
			cm.undo(1);
			undoItem.setEnabled(cm.canUndo());
			redoItem.setEnabled(cm.canRedo());
		} catch (Exception e1) {
			// TODO add proper exception handling, stack trace
			// is good for debugging only
			e1.printStackTrace();
		}
    }
    
    /**
     * Action performed when an event previously executed on the calendar
     * is to be redone. This also updates the menu entries for undo/redo
     * so their enabled state is correct.
     */
    @Action
    public void doRedo() {
    	try {
			cm.redo(1);
			undoItem.setEnabled(cm.canUndo());
			redoItem.setEnabled(cm.canRedo());
		} catch (Exception e1) {
			// TODO add proper exception handling, stack trace
			// is good for debugging only
			e1.printStackTrace();
		}
    }
}
