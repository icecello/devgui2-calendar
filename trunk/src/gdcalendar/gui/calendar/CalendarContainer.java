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


/**
 * Container for the calendar.
 * @author Tomas
 * @author H�kan
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
    private MainWindow parent;
    
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
        displayMonth();

        add(topPanel, BorderLayout.PAGE_START);
        add(monthView, BorderLayout.CENTER);

        initListeners();
    }

    /*
     * utility function used to construct and arrange everything so that
     * the currently specified month is displayed properly
     * 
     * called by: constructor, nextMonthMouseClicked, previousMonthMouseClicked
     */
    private void displayMonth() {
        // Clear month view
        monthView.removeAll();

        // Display the month title
        monthTitleLabel.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
                + " " + cal.get(Calendar.YEAR));

        // The number of days in the current month.
        int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // The start day of the month in integer form, so we know where to
        // start placing numbers in the grid.
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i <= 42; i++) {

            if (i >= (startDay) && i < (startDay + numDays)) {

                Calendar date = new GregorianCalendar(2010, 4, i - startDay + 1);
                final DefaultController controller = new DefaultController();
                //Create a event shared by all DayCards
                Collection <DayEvent> dayEvents = new ArrayList<DayEvent>();
                dayEvents.add(new DayEvent("Event 1"));
                Day day = new Day(date, dayEvents);
                MonthDayCard daycard = new MonthDayCard(day, MonthDayCard.CardView.SIMPLE, controller);
                /*
                 * as mentioned in MonthDayCard previously, this is a temporary way of adding new events
                 * we would like a method for the user to specify his data...
                 */
                daycard.addAddEventListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						DayEvent newEvent = new DayEvent("New Event",new TimeStamp(10, 00), new TimeStamp(12, 30));
			            
						parent.setUndoEnabled(true);
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
                
                //Create a controller for each dayCard, and attach the view and model together

                controller.addView(daycard);
                controller.addModel(day);
                daycard.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                //temporary code to make every other day card show
                //more events
                if (i % 2 == 1) {
                    day.addEvent(new DayEvent("School", new TimeStamp(11, 15), new TimeStamp(15, 00)));
                    day.addEvent(new DayEvent("Sleep", new TimeStamp(17, 15), new TimeStamp(21, 00)));
                }
                monthView.add(daycard);
                

            } else {
                monthView.add(new MonthDayCard());
            }
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
        displayMonth();
    }

    private void previousMonthMouseClicked(MouseEvent e) {
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        displayMonth();
    }
}


