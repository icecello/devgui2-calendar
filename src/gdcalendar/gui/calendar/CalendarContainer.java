package gdcalendar.gui.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
        //The days contained in the dayTitle
        String[] days = {"Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag", "Söndag"};

        monthTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthTitle.add(new JLabel("This month"));
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


        monthView = new JPanel(new GridLayout(5, 7));
        for (int i = 0; i < 35; i++) {
            JPanel day = new JPanel();
            day.add(new JLabel("" + (i + 1))); //Add date to every panel
            day.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            day.setMinimumSize(new Dimension(100, 100));
            day.setForeground(Color.lightGray);
            monthView.add(day);
        }

        add(topPanel, BorderLayout.PAGE_START);
        add(monthView, BorderLayout.CENTER);
    }
}
