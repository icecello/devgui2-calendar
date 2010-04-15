
package gdcalendar.gui.calendar;

import gdcalendar.data.DayEvent;
import gdcalendar.gui.calendar.daycard.MonthDayCard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
        /*
         * Debug calendar to manually set the date
         * Confusing! Set as (Year, Month(0-11), Day(1-*))
         */
        // Calendar cal = new GregorianCalendar(2010, 4, 5);

        //The days contained in the dayTitle
//        String[] days = {"Söndag","Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag"};

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
            /*
             * JPanel day = new JPanel();
            // Add date label for days in the current month. No label for
            // days not in the month.
            if (i > startDay && i < (startDay + numDays + 1)) {
                day.add(new JLabel("" + (i - startDay)));
            }
            day.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            day.setMinimumSize(new Dimension(100, 100));
            day.setForeground(Color.lightGray);
            monthView.add(day);
            */

        	/*
        	 * This is pretty much the same code as above, I made a small
        	 * change to the test against day, since the previous version
        	 * seemed to be one day off.
        	 *
        	 * This uses the current implementation of day cards, which can
        	 * be seen in MonthDayCard. The interface is a combination of
        	 * the proposal Tomas had and the version that I had built in
        	 * parallel.
        	 */
            if (i >= (startDay) && i < (startDay + numDays)) {
				/*
				 * Good call, let's use Calendar's instead.
				 *
				 * Note: this still shows dates incorrectly for me.
				 * For instance, 1st of April shows up on a Tuesday, while
				 * it should be a Thursday.
				 */

            	Calendar date = new GregorianCalendar(2010, 4, i - startDay + 1);
	        	MonthDayCard daycard = new MonthDayCard(date, MonthDayCard.CardView.SIMPLE);
	        	daycard.setBorder(BorderFactory.createLineBorder(Color.lightGray));
	        	//temporary code to make every other day card show the
	        	//event indicator
	        	if (i % 2 == 1)
	        		daycard.addEvent(new DayEvent());
	        	monthView.add(daycard);

            } else
            	monthView.add(new MonthDayCard());
        }

        add(topPanel, BorderLayout.PAGE_START);
        add(monthView, BorderLayout.CENTER);
    }
}


