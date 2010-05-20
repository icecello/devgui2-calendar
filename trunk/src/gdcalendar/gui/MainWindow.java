package gdcalendar.gui;

import gdcalendar.gui.calendar.*;
import gdcalendar.logic.AnimationDriver;
import gdcalendar.mvc.model.*;

import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import actionmanager.Action;
import actionmanager.ActionManager;

import commandmanager.CommandManager;
import gdcalendar.gui.calendar.daycard.DayPopupMenu;
import gdcalendar.gui.calendar.daycard.MonthDayCard.Marker;
import gdcalendar.mvc.model.Category;
import gdcalendar.mvc.model.DayEvent.Priority;
import javax.swing.UIManager;

/**
 * MainWindow for a Calendar application. Events are loaded from XML at initialization
 * and events can be added/removed and modified to/from/in the calendar while running.
 * At window close, all events present in the calendar is stored to XML.
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
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);

        if (System.getProperties().get("os.name").toString().contains("Windows")) {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }

        ArrayList<DayEvent> events = null;
        ArrayList<Category> categories = null;
        try {
            events = XMLUtils.loadDayEvents(Configuration.getProperty("calendar"));
        } catch (Exception e) {
            System.err.println("In MainWindow: Error loading events from file");
        }
        for(DayEvent event : events){
            System.out.println(event.getCategory().getDescription());
        }
        calendarModel = new CalendarModel(events);
        calendarContainer = new CalendarContainer(cm, calendarModel);

        initMenuBar();
        initListners();

        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.EAST);
        collapsiblePanel.setLayout(new BorderLayout());
        collapsiblePanel.setCollapsButtonSize(5);
        collapsiblePanel.add(new LeftItemPanel(), BorderLayout.PAGE_START, -1);
        add(calendarContainer, BorderLayout.CENTER);
        add(collapsiblePanel, BorderLayout.LINE_START);
        pack();
    }

    private void initMenuBar() {
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
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);

        JMenu helpMenu = new JMenu(resource.getString("menu.help.text"));
        aboutItem = new JMenuItem(actionManager.getAction("about"));

        helpMenu.add(aboutItem);

        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(helpMenu);
        this.setJMenuBar(mb);
    }

    /**
     * Initialize listners for the main window. This include listners
     * for the calendar container (pop-up menu & datachange listner) and
     * a window listner
     */
    private void initListners() {
        calendarContainer.addEventMouseListener(new MouseAdapter() {
            //TODO: add commandmananger as parameter to daypopupmenu

            private DayPopupMenu popupMenu = new DayPopupMenu();

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(),
                            e.getX(), e.getY());
                }
            }
        });
        /*
         * Add listener for data change events from the calendar
         * to set undo/redo properly
         *
         * Not much else is done currently but it's possible
         * to see what kind of change was made if we need that
         * for anything...
         */
        calendarContainer.addDataChangeListener(new CalendarDataChangedListener() {

            @Override
            public void dataChanged(CalendarChangeEvent e) {
                undoItem.setEnabled(cm.canUndo());
                redoItem.setEnabled(cm.canRedo());
            }
        });




        /*
         * Stop animation driver and store the calendar to
         * XML file
         */
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                AnimationDriver.getInstance().stopAll();
                ArrayList<DayEvent> events = new ArrayList<DayEvent>();
                DayEvent[] temp = calendarModel.getEvents();
                for (int i = 0; i < temp.length; i++) {
                    events.add(calendarModel.getEvents()[i]);
                }
                XMLUtils.saveDayEvents(events);
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
