package gdcalendar.gui;

import gdcalendar.Main;
import gdcalendar.gui.calendar.*;
import gdcalendar.logic.AnimationDriver;
import gdcalendar.mvc.model.*;

import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import actionmanager.Action;
import actionmanager.ActionManager;

import commandmanager.CommandManager;
import gdcalendar.gui.calendar.daycard.DayPopupMenu;
import gdcalendar.gui.calendar.daycard.MonthDayCard;
import gdcalendar.gui.calendar.daycard.MonthDayCard.Marker;
import gdcalendar.gui.calendar.undoredo.AddEventCommand;
import gdcalendar.gui.calendar.undoredo.RemoveEventCommand;
import gdcalendar.mvc.model.DayEvent.Priority;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.UUID;
import javax.swing.JLabel;

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
    private JMenuItem onlineHelpItem;
    private JMenuItem aboutItem;

    /*
     * note that we would want an array here if we need more undo managers
     * for multiple calendars or whatever...
     */
    private final CommandManager cm = new CommandManager(0);
    private ActionManager actionManager;
    private CalendarContainer calendarContainer;
    private CalendarModel calendarModel;
    private DayPopupMenu popMenu;
    private JToolBar toolBar;
    private CollapsiblePanel collapsiblePanel;
    private JPanel highlightPanel;
    private JButton toolButtonCategory;
    private JButton toolButtonPriority;

    public MainWindow() throws Exception {
        super("GDCalendar");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(700, 500));

        
        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);



        ArrayList<DayEvent> events = null;
        try {
            events = XMLUtils.loadDayEvents(Configuration.getProperty("calendar"));
        } catch (Exception e) {
            System.err.println("In MainWindow: Error loading events from file");
        }
        calendarModel = new CalendarModel(events);
        calendarContainer = new CalendarContainer(calendarModel);

        initMenuBar();
        initListeners();
        initHighlightPanel();

        collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.NORTH);
        collapsiblePanel.setLayout(new BorderLayout());
        collapsiblePanel.setCollapsButtonSize(7);
        collapsiblePanel.add(highlightPanel);
        add(collapsiblePanel, BorderLayout.PAGE_END);
        add(calendarContainer, BorderLayout.CENTER);

        pack();

        
        AnimationDriver.getInstance().runThread("calendarcontainer");
    }

    private void initHighlightPanel() {
        highlightPanel = new JPanel();

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolButtonCategory = new JButton(actionManager.getAction("highlightCategoryPopup"));
        toolButtonPriority = new JButton(actionManager.getAction("highlightPriorityPopup"));
        toolBar.add(toolButtonCategory);
        toolBar.add(toolButtonPriority);

        highlightPanel.add(toolBar);

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

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.add(new JSeparator());
        editMenu.add(preferencesItem);
        actionManager.getAction("doRedo").setEnabled(false);
        actionManager.getAction("doUndo").setEnabled(false);

        JMenu helpMenu = new JMenu(resource.getString("menu.help.text"));

        onlineHelpItem = new JMenuItem(actionManager.getAction("onlineHelp"));
        aboutItem = new JMenuItem(actionManager.getAction("about"));
        helpMenu.add(onlineHelpItem);
        helpMenu.add(aboutItem);

        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(helpMenu);
        this.setJMenuBar(mb);
    }

    /**
     * Initialize listeners for the main window. This include listeners
     * for the calendar container (pop-up menu & datachange listener) and
     * a window listener
     */
    private void initListeners() {
        //Pop-menu shown when right click the daycards
        popMenu = new DayPopupMenu();

        //Make the pop-up show when the user press an event
        calendarContainer.addEventMouseListener(new MouseAdapter() {
            //TODO: add commandmananger as parameter to daypopupmenu

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
                    popMenu.setEditEnabled(true);
                    popMenu.setDeleteEnabled(true);
                    popMenu.show(e.getComponent(),
                            e.getX(), e.getY());
                }
            }
        });
        //Make the pop-up show when pressing a dayCard
        calendarContainer.addDayMouseListener(new MouseAdapter() {

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
                    //Gray out edit and delete functionality
                    popMenu.setEditEnabled(false);
                    popMenu.setDeleteEnabled(false);
                    popMenu.show(e.getComponent(),
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

        //Take care of propertychanges from the pop-up menu
        popMenu.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                String evtName = evt.getPropertyName();
                if (evtName.equals(DayPopupMenu.ADD)) {
                    //Create a new addEvent window
                    Date eventDate = ((MonthDayCard) popMenu.getInvoker()).getFilter();
                    EventWindow addEventWindow = new EventWindow(eventDate, null, DayPopupMenu.ADD);

                    addEventWindow.addPropertyChangeListener(new PropertyChangeListener() {

                        public void propertyChange(PropertyChangeEvent evt) {
                            cm.execute(new AddEventCommand(calendarModel, (DayEvent) evt.getNewValue()));
                            actionManager.getAction("doRedo").setEnabled(cm.canRedo());
                            actionManager.getAction("doUndo").setEnabled(cm.canUndo());
                        }
                    });
                    addEventWindow.setVisible(true);


                } else if (evtName.equals(DayPopupMenu.DELETE)) {
                    String ID = ((JLabel) popMenu.getInvoker()).getName();
                    DayEvent event = calendarModel.getDayEvent(UUID.fromString(ID));
                    cm.execute(new RemoveEventCommand(calendarModel, event));
                    actionManager.getAction("doRedo").setEnabled(cm.canRedo());
                    actionManager.getAction("doUndo").setEnabled(cm.canUndo());


                } else if (evtName.equals(DayPopupMenu.EDIT)) {
                    //Create a new addEvent window
                    String ID = ((JLabel) popMenu.getInvoker()).getName();
                    final DayEvent d = calendarModel.getDayEvent(UUID.fromString(ID));
                    EventWindow editEventWindow = new EventWindow(new Date(), d, DayPopupMenu.EDIT);

                    editEventWindow.addPropertyChangeListener(new PropertyChangeListener() {

                        public void propertyChange(PropertyChangeEvent evt) {
                            DayEvent tempEvent = (DayEvent) evt.getNewValue();
                            calendarModel.replaceDayEvent(d.getID(), tempEvent);
                        }
                    });
                    editEventWindow.setVisible(true);
                } else if (evtName.equals(DayPopupMenu.VIEW)) {
                    System.out.println("View");
                }
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
     * Shows Online Help window.
     */
    @Action
    public void onlineHelp() {
        new OnlineHelpWindow().setVisible(true);
        //System.out.println("Online Help");
    }

    /**
     * Shows About window.
     */
    @Action
    public void about() {
        new AboutWindow(this).setVisible(true);
    }
    private HashMap<Category, Boolean> checkedCategories = new HashMap<Category, Boolean>();
    private HashMap<Priority, Boolean> checkedPriorities = new HashMap<Priority, Boolean>();

    /**
     * Display a popupmenu with checkbox items to mark
     * which categories that we want to highlight by
     * animation
     */
    @Action
    public void highlightCategoryPopup() {
        final JPopupMenu menu = new JPopupMenu();

        Collection<Category> c = Main.categories.values();

        Iterator<Category> iter = c.iterator();
        while (iter.hasNext()) {

            final Category cat = iter.next();
            final JCheckBoxMenuItem item = new JCheckBoxMenuItem();
            if (checkedCategories.containsKey(cat)) {
                item.setState(checkedCategories.get(cat));
            }

            item.setAction(new AbstractAction(cat.getName()) {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    checkedCategories.put(cat, item.getState());
                    if (item.getState()) {
                        calendarContainer.addHighlight(cat);
                    } else {
                        calendarContainer.removeHighlight(cat);
                    }
                }
            });

            menu.add(item);
        }
        
        Rectangle rect = toolButtonCategory.getBounds();
        Point bounds = new Point(rect.x, rect.y + rect.height);
        Point toolButtonLocation = toolButtonCategory.getLocation();
        Point toolBarLocation = toolBar.getLocationOnScreen();
        Point windowDisplacement = this.getLocationOnScreen();
        menu.show(this, toolBarLocation.x + toolButtonLocation.x - windowDisplacement.x, 
        		toolBarLocation.y + toolButtonLocation.y + bounds.y - windowDisplacement.y);


    }

    /**
     * Display a popupmenu with checkbox items
     * for selecting priorities to highlight with
     * animations.
     */
    @Action
    public void highlightPriorityPopup() {
        final JPopupMenu menu = new JPopupMenu();
        final Priority[] p = Priority.values();

        for (int i = 0; i < p.length; i++) {
            final int ii = i;
            final JCheckBoxMenuItem item = new JCheckBoxMenuItem();
            if (checkedPriorities.containsKey(p[i])) {
                item.setState(checkedPriorities.get(p[i]));
            }

            item.setAction(new AbstractAction(p[i].toString()) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    checkedPriorities.put(p[ii], item.getState());
                    if (item.getState()) {
                        calendarContainer.addHighlight(p[ii]);
                    } else {
                        calendarContainer.removeHighlight(p[ii]);
                    }
                }
            });
            menu.add(item);
        }


        Rectangle rect = toolButtonPriority.getBounds();
        Point bounds = new Point(rect.x, rect.y + rect.height);
        Point toolButtonLocation = toolButtonPriority.getLocation();
        Point toolBarLocation = toolBar.getLocationOnScreen();
        Point windowDisplacement = this.getLocationOnScreen();
        menu.show(this, toolBarLocation.x + toolButtonLocation.x - windowDisplacement.x, 
        		toolBarLocation.y + toolButtonLocation.y + bounds.y - windowDisplacement.y);
    }
}
