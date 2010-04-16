package gdcalendar.gui.calendar;

import gdcalendar.gui.calendar.daycard.MonthDayCard;
import gdcalendar.mvc.controller.DefaultController;
import gdcalendar.mvc.model.Day;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.TimeStamp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Container for the calendar.
 * @author Tomas, H�kan, James
 */
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
    private JButton leftButton;
    private JButton rightButton;
    private JPanel monthTitle;
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

    public CalendarContainer() {



        setLayout(new BorderLayout());
        topPanel = new JPanel(new BorderLayout());

        // Create a calendar for current day
        Calendar cal = GregorianCalendar.getInstance();

        monthTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthTitle.add(new JLabel(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)));
        monthTitle.setBackground(new Color(220, 220, 220));
        monthTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Need to decide on Action/Listener pattern before making these work.
        // Should be simple though! (I've already been doing it by hand)
        leftButton = new JButton("<<");
        rightButton = new JButton(">>");

        monthNavPanel = new JPanel(new BorderLayout());
        monthNavPanel.add(leftButton, BorderLayout.LINE_START);
        monthNavPanel.add(monthTitle, BorderLayout.CENTER);
        monthNavPanel.add(rightButton, BorderLayout.LINE_END);
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

        // The number of days in the current month.
        int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // The start day of the month in integer form, so we know where to
        // start placing numbers in the grid.
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println(startDay);

        for (int i = 1; i <= 42; i++) {

            if (i >= (startDay) && i < (startDay + numDays)) {

                Calendar date = new GregorianCalendar(2010, 4, i - startDay + 1);
                //Create a event shared by all DayCards
                HashMap<String, DayEvent> dayEvents = new HashMap<String, DayEvent>();
                dayEvents.put("evt0", new DayEvent("Event 1"));
                Day day = new Day(date, dayEvents);
                MonthDayCard daycard = new MonthDayCard(day, MonthDayCard.CardView.SIMPLE);
                //Create a controller for each dayCard, and attach the view and model together
                DefaultController controller = new DefaultController();
                controller.addView(daycard);
                controller.addModel(day);
                daycard.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                //temporary code to make every other day card show contain
                //more events
                if (i % 2 == 1) {
                    day.addEvent("evt1" + i, new DayEvent("School", new TimeStamp(11, 15), new TimeStamp(15, 00)));
                    day.addEvent("evt3" + i, new DayEvent("Sleep", new TimeStamp(17, 15), new TimeStamp(21, 00)));
                }
                monthView.add(daycard);

            } else {
                monthView.add(new MonthDayCard());
            }
        }

        add(topPanel, BorderLayout.PAGE_START);
        add(monthView, BorderLayout.CENTER);
    }
}


