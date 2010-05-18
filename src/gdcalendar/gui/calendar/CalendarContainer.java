package gdcalendar.gui.calendar;

import gdcalendar.gui.calendar.daycard.MonthDayCard;
import gdcalendar.gui.calendar.undoredo.AddEventCommand;
//this import will be used later as we sort out the details of how
//to deal with removing events
//see adding of events for functioning undo/redo operations
//import gdcalendar.gui.calendar.undoredo.RemoveEventCommand;
import gdcalendar.logic.AnimationDriver;
import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.model.*;
import gdcalendar.mvc.model.DayEvent.Priority;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import commandmanager.CommandManager;
import gdcalendar.gui.calendar.daycard.MonthDayCard.CardView;
import gdcalendar.gui.calendar.daycard.MonthDayCard.Marker;

import java.util.ArrayList;

/**
 * Container for the calendar.
 * @author Tomas
 * @author Håkan
 * @author James
 */
@SuppressWarnings("serial")
public class CalendarContainer extends JPanel {

    // Container layout
    // _______________________________
    //|          topPanel             |
    //|_______________________________|
    //|                               |
    //|                               |
    //|          monthView            |
    //|                               |
    //|                               |
    //|_______________________________|
    private JPanel topPanel;
    // _____________________________
    //|       monthNavPanel         |
    //|_____________________________|
    //|         dayTitle            |
    //|_____________________________|
    private JPanel monthNavPanel;
    // ___________________________________
    //|leftButton| monthTitle |rightButton|
    //|__________|____________|___________|
    private JButton previousMonthButton;
    private JButton nextMonthButton;
    private JPanel monthTitle;
    private JLabel monthTitleLabel;
    // _____________________________
    //|         monthTitle          |
    //|_____________________________|
    private JPanel dayTitle;
    // ___________________________
    //|Mån|Tis|Ons|Tor|Fre|Lör|Sön|
    //|___|___|___|___|___|___|___|
    private JPanel monthView;
    //_____________________________
    //|_#_|_#_|_#_|_#_|_#_|_#_|_#_|
    //|_#_|_#_|_#_|_#_|_#_|_#_|_#_|
    //|_#_|_#_|_#_|_#_|_#_|_#_|_#_|
    //|_#_|_#_|_#_|_#_|_#_|_#_|_#_|
    //|_#_|_#_|_#_|_#_|_#_|_#_|_#_|
    private Calendar cal;
    private CommandManager undoManager;
    private CardView dayViews = CardView.SIMPLE;
    private ArrayList<CalendarController> controllers = new ArrayList<CalendarController>();
    private ArrayList<MonthDayCard> views = new ArrayList<MonthDayCard>();
    private ArrayList<CalendarDataChangedListener> dataChangedListeners = new ArrayList<CalendarDataChangedListener>();
    private CalendarModel calendarModel = new CalendarModel();

    /**
     * Construct the calendar, with all it's child components and data it needs.
     * For now, we still need to pass the command manager into the calendar, I'm
     * not sure if that is really necessary, but at least it works, and we don't
     * need the main window in the CalendarContainer anymore. That's a good thing.
     * 
     * @param undoManager		command manager to use for this calendar for handling all commands
     */
    public CalendarContainer(CommandManager undoManager, CalendarModel calendarModel) {
        this.undoManager = undoManager;
        this.calendarModel = calendarModel;
        setLayout(new BorderLayout());
        topPanel = new JPanel(new BorderLayout());

        // Create a calendar for current day
        cal = GregorianCalendar.getInstance();

        monthTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthTitleLabel = new JLabel();
        monthTitle.add(monthTitleLabel);
        monthTitle.setBackground(new Color(220, 220, 220));
        monthTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        previousMonthButton = new JButton("<<");
        nextMonthButton = new JButton(">>");     
        
        monthNavPanel = new JPanel(new BorderLayout());
        monthNavPanel.add(previousMonthButton, BorderLayout.LINE_START);
        monthNavPanel.add(monthTitle, BorderLayout.CENTER);
        monthNavPanel.add(nextMonthButton, BorderLayout.LINE_END);
        monthNavPanel.setBackground(new Color(220, 220, 220));


        dayTitle = new JPanel(new GridLayout(1, 7));
        dayTitle.setBackground(new Color(220, 220, 220));
        dayTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Calendar tempCalendar = GregorianCalendar.getInstance();
        for (int i = 1; i <= 7; i++) {
            tempCalendar.set(Calendar.DAY_OF_WEEK, i);
            dayTitle.add(new JLabel(tempCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)));
        }

        topPanel.add(monthNavPanel, BorderLayout.PAGE_START);
        topPanel.add(dayTitle, BorderLayout.CENTER);

        monthView = new JPanel(new GridLayout(6, 7));
        initMVC();

        add(topPanel, BorderLayout.PAGE_START);
        add(monthView, BorderLayout.CENTER);
        initListeners();
    }

    /**
     * 
     * @param marker
     */
    public void setMarker(Marker marker) {
    	for (int i = 0; i < views.size(); i++) {
    		views.get(i).setMarker(marker);
    	}
    }
    /**
     * Use some means of highlighting all items that belong to 
     * specified category
     * 
     * @param category		name of category to match against
     */
    public void highlight(Category category) {
    	for (int i = 0; i < views.size(); i++) {
    		views.get(i).highlight(category);
    	}
    }
    
    /**
     * Use some means of highlighting all items that have the
     * specified priority.
     * 
     * @param prio			priority to match against
     */
    public void highlight(Priority prio) {
    	for (int i = 0; i < views.size(); i++) {
    		views.get(i).highlight(prio);
    	}
    }
    
    /**
     * Initialize the calendar view for the current month
     */
    private void initMVC() {
        //Fill the whole grid with MonthDayCards
        for (int i = 1; i <= 42; i++) {
            final CalendarController controller = new CalendarController();
            final MonthDayCard daycard = new MonthDayCard(dayViews, controller);
            AnimationDriver.getInstance().add(daycard,"calendarcontainer");
            
            DayFilteredCalendarModel model = new DayFilteredCalendarModel();
            daycard.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            /*
             * as mentioned in MonthDayCard previously, this is a temporary way of adding new events
             * we would like a method for the user to specify his data...
             */
            daycard.addAddEventListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    DayEvent newEvent = new DayEvent("New Event", new Date(110,4,13,0,0), new Date(110,4,16,0,0));
                    CalendarChangeEvent changeEvent = new CalendarChangeEvent(newEvent, 0, CalendarChangeEvent.EventAdd, 0);
                    undoManager.execute(new AddEventCommand(controller, newEvent));
                    fireDataChangedEvent(changeEvent);
                }
            });

            daycard.addRemoveEventListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        //undoManager.execute(new RemoveEventCommand(controller))
                        CalendarChangeEvent changeEvent = new CalendarChangeEvent(null, 0, CalendarChangeEvent.EventRemove, 0);

                        undoManager.removeLast();
                        fireDataChangedEvent(changeEvent);
                    } catch (Exception e1) {
                    }
                }
            });


            model.setRealCalendarModel(calendarModel);

            controller.addView(daycard);
            controller.addModel(model);
            views.add(daycard);
            controllers.add(controller);
            monthView.add(daycard);


        }
        //Call switchToMonth to attach the correct days (models) to the newly created MonthCards
        switchToMonth(cal);
    }

    /*
     * Update the calendar view to show another month
     */
    private void switchToMonth(final Calendar calendar) {
        Calendar tempCal = (Calendar) calendar.clone();
        monthTitleLabel.setText(tempCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
                + " " + tempCal.get(Calendar.YEAR));

        int numDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // The start day of the month in integer form, so we know where to
        // start placing numbers in the grid.
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = tempCal.get(Calendar.DAY_OF_WEEK);


        for (int i = 1; i <= 42; i++) {
            final CardView currentView;     //variable to keep track what apperence the daycard should have
            final int index = i - 1;

            if (i >= (startDay) && i < (startDay + numDays)) {
                currentView = dayViews;
                tempCal.set(Calendar.DAY_OF_MONTH, i - startDay +1);
                Date filter = tempCal.getTime();
                controllers.get(index).setFilter(filter);
            } else {
                currentView = CardView.NONE;
                controllers.get(index).setFilter(new Date(0,0,0));
            }
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        views.get(index).changeView(currentView);
                    }
                });

            } else {
                views.get(index).changeView(currentView);
            }
            views.get(index).revalidate();
            views.get(index).repaint();
        }
    }

    private void initListeners() {
        previousMonthButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                CalendarContainer.this.previousMonthMouseClicked(e);
            }
        });

        nextMonthButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                CalendarContainer.this.nextMonthMouseClicked(e);
            }
        });
    }

    private void nextMonthMouseClicked(MouseEvent e) {

        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        switchToMonth(cal);
    }

    private void previousMonthMouseClicked(MouseEvent e) {

        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        switchToMonth(cal);
    }

    /**
     * 
     * @param l
     */
    public void addDayMouseListener(MouseListener l) {
    	for (int i = 0; i < views.size(); i++) {
    		views.get(i).addMouseListener(l);
    	}
    }
    
    /**
     * 
     * @param l	
     */
    public void addEventMouseListener(MouseListener l) {
    	for (int i = 0; i < views.size(); i++) {
    		views.get(i).addEventMouseListener(l);
    	}
    }
    
    /**
     * Add a listener that will be invoked whenever the data of this
     * calendar has changed in some way.
     * 
     * Changes that will trigger this event are:
     * - Adding new data
     * - Removing data
     * - Editing data (TODO)
     * 
     * @param listener
     */
    public void addDataChangeListener(CalendarDataChangedListener listener) {
        dataChangedListeners.add(listener);
    }

    /*
     * Internal method that handles firing of data changed events.
     */
    private void fireDataChangedEvent(CalendarChangeEvent e) {
        Iterator<CalendarDataChangedListener> it = dataChangedListeners.iterator();

        while (it.hasNext()) {
            CalendarDataChangedListener listener = it.next();
            listener.dataChanged(e);
        }
    }
}

