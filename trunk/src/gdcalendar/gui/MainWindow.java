package gdcalendar.gui;

import gdcalendar.Main;
import gdcalendar.gui.calendar.*;
import gdcalendar.logic.AnimationDriver;
import gdcalendar.mvc.model.*;

import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
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
import gdcalendar.gui.calendar.daycard.DayView;
import gdcalendar.gui.calendar.daycard.MonthDayCard;
import gdcalendar.gui.calendar.daycard.MonthDayCard.Marker;
import gdcalendar.gui.calendar.undoredo.AddEventCommand;
import gdcalendar.gui.calendar.undoredo.RemoveEventCommand;
import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.model.DayEvent.Priority;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.UUID;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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

    static private HelpGlassPane helpGlassPane;
    private ResourceBundle resource;
    private JMenuItem quitItem;
    private JMenuItem undoItem;
    private JMenuItem redoItem;
    private JMenu onlineHelpItem;
    private JMenuItem addEventHelpItem;
    private JMenuItem transparencyHelpItem;
    private JMenuItem aboutItem;

    /*
     * note that we would want an array here if we need more undo managers
     * for multiple calendars or whatever...
     */
    private final CommandManager cm = new CommandManager(0);
    private ActionManager actionManager;
    private CalendarContainer calendarContainer;
    private CalendarModel calendarModel;
    private CalendarController calendarController;
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
        setPreferredSize(new Dimension(700, 530));

        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);



        ArrayList<DayEvent> events = null;
        try {
            events = XMLUtils.loadDayEvents(Configuration.getProperty("calendar"));
        } catch (Exception e) {
            System.err.println("In MainWindow: Error loading events from file");
        }
        calendarModel = new CalendarModel(events);
        calendarController = new CalendarController();
        calendarController.addModel(calendarModel);

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

        onlineHelpItem = new JMenu(resource.getString("menu.help.onlinehelp.text"));

        addEventHelpItem = new JMenuItem(actionManager.getAction("addEventHelp"));
        transparencyHelpItem = new JMenuItem(actionManager.getAction("transparencyHelp"));
        onlineHelpItem.add(addEventHelpItem);
        onlineHelpItem.add(transparencyHelpItem);

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

            private Color oldColor = SystemColor.white;
            private JPanel panel = null;
            private boolean mouseDown = false;

            @Override
            public void mouseEntered(MouseEvent e) {
                panel = (JPanel) e.getSource();
                doHoverColor();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                /*
                 * only return original background color
                 * if the popupmenu is no longer visible
                 */
                if (!mouseDown || !popMenu.isVisible()) {
                    mouseDown = false;
                    panel = (JPanel) e.getSource();
                    doReturnColor();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDown = true;
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void doHoverColor() {
                oldColor = calendarContainer.getBackground();

                panel.setOpaque(true);
                panel.setBackground(SystemColor.controlHighlight);
            }

            private void doReturnColor() {
                panel.setOpaque(false);
                panel.setBackground(oldColor);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    /*
                     * add a listener to detect popup menu events, which
                     * will affect the background color of the currently
                     * selected calendar event
                     */
                    popMenu.addPopupMenuListener(new PopupMenuListener() {

                        @Override
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                            doHoverColor();
                        }

                        @Override
                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                            doReturnColor();
                        }

                        @Override
                        public void popupMenuCanceled(PopupMenuEvent e) {
                            doReturnColor();
                        }
                    });

                    popMenu.setEditEnabled(true);
                    popMenu.setDeleteEnabled(true);
                    popMenu.setAddEnabled(false);
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
                    popMenu.setAddEnabled(true);
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
                    showAddEventWindow(((MonthDayCard) popMenu.getInvoker()).getFilter());


                } else if (evtName.equals(DayPopupMenu.DELETE)) {
                    String ID = ((JPanel) popMenu.getInvoker()).getName();
                    DayEvent event = calendarModel.getDayEvent(UUID.fromString(ID));
                    cm.execute(new RemoveEventCommand(calendarController, event));
                    actionManager.getAction("doRedo").setEnabled(cm.canRedo());
                    actionManager.getAction("doUndo").setEnabled(cm.canUndo());

                } else if (evtName.equals(DayPopupMenu.EDIT)) {
                    //Create a new addEvent window
                    String ID = ((JPanel) popMenu.getInvoker()).getName();
                    final DayEvent d = calendarModel.getDayEvent(UUID.fromString(ID));
                    showEditEventWindow(d);
                } else if (evtName.equals(DayPopupMenu.VIEW)) {
                    System.out.println("Viewing day...");
                    Date day = ((MonthDayCard) popMenu.getInvoker()).getFilter();
                    DayFilteredCalendarModel model = new DayFilteredCalendarModel();
                    model.setRealCalendarModel(calendarModel);
                    model.setDayFilter(day);
                    JFrame frame = new JFrame();
                    DayView d = new DayView(model.getFilteredEvents());

                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.add(d);
                    frame.pack();
                    frame.setVisible(true);
                }
            }
        });
    }

    public void showAddEventWindow(Date eventDate) {
        //Create a new addEvent window

        EventWindow addEventWindow = new EventWindow(eventDate, null, DayPopupMenu.ADD);

        addEventWindow.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CategoryWindow.CATEGORY_EDITED)) {
                    Category oldCategory = (Category) evt.getOldValue();
                    Category newCategory = (Category) evt.getNewValue();
                    updateDisplayedCategories(oldCategory, newCategory);
                } else {
                    cm.execute(new AddEventCommand(calendarController, (DayEvent) evt.getNewValue()));
                    actionManager.getAction("doRedo").setEnabled(cm.canRedo());
                    actionManager.getAction("doUndo").setEnabled(cm.canUndo());
                }
            }
        });
        addEventWindow.setVisible(true);
    }

    public void showEditEventWindow(final DayEvent event) {
        //Create a new addEvent window
        EventWindow editEventWindow = new EventWindow(new Date(), event, DayPopupMenu.EDIT);

        editEventWindow.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CategoryWindow.CATEGORY_EDITED)) {
                    Category oldCategory = (Category) evt.getOldValue();
                    Category newCategory = (Category) evt.getNewValue();
                    updateDisplayedCategories(oldCategory, newCategory);
                } else {
                    DayEvent tempEvent = (DayEvent) evt.getNewValue();
                    //TODO: This should be done via the controller. 
                    //however, the current implementation of the controller doesn't
                    //support methods with more than one argument
                    calendarModel.replaceDayEvent(event.getID(), tempEvent);
                }
            }
        });
        editEventWindow.setVisible(true);
    }

    public void updateDisplayedCategories(Category oldCategory, Category newCategory) {
        DayEvent[] events = calendarModel.getEvents();
        for (int i = 0; i < events.length; i++) {
            if (events[i].getCategory().getName().equals(oldCategory.getName())) {
                DayEvent d = events[i];
                d.setCategory(newCategory);
                //TODO: This should be done via the controller.
                //however, the current implementation of the controller doesn't
                //support methods with more than one argument
                calendarModel.replaceDayEvent(events[i].getID(), d);
            }
        }
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
     * Shows Online Help animation for Add Event.
     */
    @Action
    public void addEventHelp() {
        showGlassPane("addevent");
    }

    /**
     * Shows Online Help animation for changing transparency.
     */
    @Action
    public void transparencyHelp() {
        showGlassPane("transparency");
    }

    private void showGlassPane(String helpType) {
        helpGlassPane = new HelpGlassPane(this, helpType);
        this.setGlassPane(helpGlassPane);
        helpGlassPane.setVisible(true);
    }

    public void doAction(String helpType) {
        if (helpType.equals("addevent")) {
            showAddEventWindow(new Date());
        } else if (helpType.equals("transparency")) {
            new PreferencesWindow(this);
        }
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
