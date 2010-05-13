package gdcalendar.gui;

import gdcalendar.gui.calendar.CalendarChangeEvent;
import gdcalendar.gui.calendar.CalendarContainer;
import gdcalendar.gui.calendar.CalendarDataChangedListener;
import gdcalendar.logic.AnimationDriver;
import gdcalendar.logic.IAnimatedComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

	
	private ResourceBundle resource;
	private JMenuItem undoItem;
	private JMenuItem redoItem;

	/*
	 * note that we would want an array here if we need more undo managers
	 * for multiple calendars or whatever...
	 */
	private final CommandManager cm = new CommandManager(0);
	private ActionManager actionManager;
	
	class animateColor extends JPanel implements IAnimatedComponent {
		int r = 0, g = 200, b = 50;
		boolean val = false;
		@Override
		public boolean animationFinished() {
			return false;
		}

		@Override
		public void computeAnimatation() {
			r = (r + 1) % 255;
			g = (g + 1) % 255;
			b = (b + 1) % 255;
		}

		@Override
		public void displayAnimatation() {
			this.setBackground(new Color(r, g, b));
		}

		@Override
		public int preferredFPS() {
			return 60;
		}
		
	}
	
    public MainWindow() throws Exception {
        setLayout(new BorderLayout());

        //load the resource file
        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);
        
        
        AnimationDriver ad = AnimationDriver.getInstance();
        //add dummy animation to animation driver
        animateColor jp = new animateColor();
        ad.add(jp);
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
        add(jp, BorderLayout.AFTER_LAST_LINE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        
        //start animation driver
        ad.runAnimations();
        
        this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
        		AnimationDriver.getInstance().stopAnimations();
        	}
		});
    }


    /**
     * Create and show a preferences window that
     * currently only supports changing the main
     * window's opacity.
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
