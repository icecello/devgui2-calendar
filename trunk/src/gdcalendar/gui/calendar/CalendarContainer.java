package gdcalendar.gui.calendar;

import gdcalendar.gui.MainWindow;
import gdcalendar.gui.calendar.daycard.MonthDayCard;
import gdcalendar.gui.calendar.undoredo.AddEventCommand;
//this import will be used later as we sort out the details of how
//to deal with removing events
//see adding of events for functioning undo/redo operations
//import gdcalendar.gui.calendar.undoredo.RemoveEventCommand;
import gdcalendar.mvc.controller.DefaultController;
import gdcalendar.mvc.model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import commandmanager.CommandManager;
import gdcalendar.gui.calendar.daycard.MonthDayCard.CardView;
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
    private MainWindow parent;
    private ArrayList<DefaultController> controllers = new ArrayList<DefaultController>();
    private ArrayList<Day> models = new ArrayList<Day>();
    private ArrayList<MonthDayCard> views = new ArrayList<MonthDayCard>();

    /**
     * Construct the calendar, with all it's child components and data it needs.
     * 
     * 
     * @param undoManager		command manager to use for this calendar for handling all commands
     */
    public CalendarContainer(CommandManager undoManager, MainWindow parent) {
        this.undoManager = undoManager;
        this.parent = parent;

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
     * Initialize the calendar view for the current month
     */
    private void initMVC() {
        //Fill the whole grid with MonthDayCards
        for (int i = 1; i <= 42; i++) {
            final DefaultController controller = new DefaultController();
            final MonthDayCard daycard = new MonthDayCard(dayViews, controller);
            daycard.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            /*
             * as mentioned in MonthDayCard previously, this is a temporary way of adding new events
             * we would like a method for the user to specify his data...
             */
            daycard.addAddEventListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    DayEvent newEvent = new DayEvent("New Event", new TimeStamp(10, 00), new TimeStamp(12, 30));
                    undoManager.execute(new AddEventCommand(controller, newEvent));
                }
            });
            daycard.addRemoveEventListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        //undoManager.execute(new RemoveEventCommand(controller))
                        undoManager.removeLast();
                    } catch (Exception e1) {
                    }
                }
            });


            controller.addView(daycard);
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

        //Detach all models
        for (DefaultController controller : controllers) {
            controller.removeAllModels();
        }
        //Event for debugging, here we should fetch the correct days
        DayEvent event = new DayEvent("changed month");
        ArrayList<DayEvent> events = new ArrayList<DayEvent>();
        events.add(event);

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
                tempCal.set(Calendar.DAY_OF_MONTH, i - startDay + 1);
                Day day = new Day(tempCal, events);
                controllers.get(index).addModel(day);
                day.synchornize();
            } else {
                currentView = CardView.NONE;
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
}

