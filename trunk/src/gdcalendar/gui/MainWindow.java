package gdcalendar.gui;

import actionmanager.Action;
import gdcalendar.gui.calendar.CalendarChangeEvent;
import gdcalendar.gui.calendar.CalendarContainer;
import gdcalendar.logic.AnimationDriver;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import actionmanager.ActionManager;

import commandmanager.CommandManager;
import gdcalendar.gui.calendar.CalendarDataChangedListener;
import gdcalendar.gui.calendar.daycard.MonthDayCard.Marker;
import gdcalendar.mvc.model.CalendarModel;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.DayEvent.Priority;
import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import javax.swing.JSeparator;
import javax.swing.UIManager;

/**
 *
 * @author Tomas
 * @author Håkan
 * @author James
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

    private ResourceBundle resource;
    private JMenuItem quitItem;
    private JMenuItem undoItem;
    private JMenuItem redoItem;
    private JMenuItem aboutItem;

    /*
     * note that we would want an array here if we need more undo managers
     * for multiple calendars or whatever...
     */
    private final CommandManager cm = new CommandManager(0);
    private ActionManager actionManager;
    private CalendarContainer calendarContainer;
    private CalendarModel calendarModel;
    public MainWindow() throws Exception {
        super("GDCalendar");


        if (System.getProperties().get("os.name").toString().contains("Windows")) {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }

        setLayout(new BorderLayout());


        ArrayList<DayEvent> events = null;
        try {
            events = XMLUtils.loadDayEvents(Configuration.getProperty("calendar"));
        } catch (Exception e) {
            System.err.println("In MainWindow: Error when trying to load events");
        }
        calendarModel = new CalendarModel(events);


        //load the resource file
        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);

        /*
         * construct a simple menu
         * currently uses the ActionManager class to construct
         * AbstractAction objects...
         */
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = new JMenu(resource.getString("menu.file.text"));
        quitItem = new JMenuItem(actionManager.getAction("quit"));

        fileMenu.add(quitItem);
        JMenu editMenu = new JMenu(resource.getString("menu.edit.text"));
        undoItem = new JMenuItem(actionManager.getAction("doUndo"));
        redoItem = new JMenuItem(actionManager.getAction("doRedo"));
        JMenuItem preferencesItem = new JMenuItem(actionManager.getAction("showPreferences"));
        JMenuItem showPrioItem = new JMenuItem(actionManager.getAction("showPrio"));

        editMenu.add(undoItem);

        editMenu.add(redoItem);

        editMenu.add(new JSeparator());

        editMenu.add(preferencesItem);
        editMenu.add(showPrioItem);
        undoItem.setEnabled(
                false);
        redoItem.setEnabled(
                false);

        JMenu helpMenu = new JMenu(resource.getString("menu.help.text"));
        aboutItem = new JMenuItem(actionManager.getAction("about"));

        helpMenu.add(aboutItem);

        mb.add(fileMenu);

        mb.add(editMenu);

        mb.add(helpMenu);
        this.setJMenuBar(mb);

        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.EAST);
        collapsiblePanel.setLayout(
                new BorderLayout());
        collapsiblePanel.setCollapsButtonSize(
                5);
        collapsiblePanel.add(
                new LeftItemPanel(),
                BorderLayout.PAGE_START, -1);

        calendarContainer = new CalendarContainer(cm, calendarModel);
        /*
         * Add listener for data change events from the calendar
         * to set undo/redo properly
         * 
         * Not much else is done currently but it's possible
         * to see what kind of change was made if we need that
         * for anything...
         */
        calendarContainer.addDataChangeListener(
                new CalendarDataChangedListener() {

                    @Override
                    public void dataChanged(CalendarChangeEvent e) {
                        undoItem.setEnabled(cm.canUndo());
                        redoItem.setEnabled(cm.canRedo());
                    }
                });

        add(calendarContainer, BorderLayout.CENTER);

        add(collapsiblePanel,
                BorderLayout.LINE_START);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();



        this.addWindowListener(
                new WindowAdapter() {

                    public void windowClosed(WindowEvent e) {
                        AnimationDriver.getInstance().stopAll();
                    }
                });

    }

    /**
     * Change which marker the CalendarContainer uses
     * for animations when highlight() is invoked.
     *
     * @param marker	marker to change to
     */
    public void changeMarker(Marker marker) {
        calendarContainer.setMarker(marker);


    }

    /**
     * Quits the program.
     */
    @Action
    public void quit() {
        System.exit(0);


    }

    /**
     * Action that invokes the highlight() method of the
     * CalendarContainer to animate any daycards that
     * contain events matching the specified priority.
     */
    @Action
    public void showPrio() {
        calendarContainer.highlight(Priority.LOW);


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

    /**
     * Shows About window.
     */
    @Action
    public void about() {
        new AboutWindow(this).setVisible(true);

    }
}
