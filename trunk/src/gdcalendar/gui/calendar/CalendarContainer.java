package gdcalendar.gui.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Container for the calendar.
 * @author Tomas, Håkan, James
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
    //|         monthTitle          |
    //|_____________________________|
    //|         dayTitle            |
    //|_____________________________|
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
        // Debug calendar to manually set the date
        // Confusing! Set as (Year, Month(0-11), Day(1-*))
        // Calendar cal = new GregorianCalendar(2010, 4, 5);
        //The days contained in the dayTitle
        String[] days = {"Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag", "Söndag"};

        monthTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthTitle.add(new JLabel(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)));
        monthTitle.setBackground(new Color(220, 220, 220));
        monthTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        dayTitle = new JPanel(new GridLayout(1, 7));
        dayTitle.setBackground(new Color(220, 220, 220));
        dayTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        for (int i = 0; i < 7; i++) {
            dayTitle.add(new JLabel("" + days[i]));
        }

        topPanel.add(monthTitle, BorderLayout.PAGE_START);
        topPanel.add(dayTitle, BorderLayout.CENTER);


        monthView = new JPanel(new GridLayout(6, 7));

        // The number of days in the current month.
        int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // The start day of the month in integer form, so we know where to
        // start placing numbers in the grid.
        int startDay = cal.get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < 42; i++) {
            JPanel day = new JPanel();
            // Add date label for days in the current month. No label for
            // days not in the month.
            if (i > startDay && i < (startDay + numDays + 1)) {
                day.add(new JLabel("" + (i - startDay)));
            }
            day.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            day.setMinimumSize(new Dimension(100, 100));
            day.setForeground(Color.lightGray);
            monthView.add(day);
        }

        add(topPanel, BorderLayout.PAGE_START);
        add(monthView, BorderLayout.CENTER);
    }
}
