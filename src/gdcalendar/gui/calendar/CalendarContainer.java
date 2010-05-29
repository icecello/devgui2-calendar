package gdcalendar.gui.calendar;

import gdcalendar.gui.calendar.daycard.MonthDayCard;
import gdcalendar.logic.AnimationDriver;
import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.model.*;
import gdcalendar.mvc.model.DayEvent.Priority;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;

import gdcalendar.gui.calendar.daycard.MonthDayCard.CardView;
import gdcalendar.gui.calendar.daycard.MonthDayCard.Marker;
import java.util.ArrayList;

/**
 * Container for the calendar. This is a stand-alone component that could
 * be used anywhere.
 * 
 * Right now it demands sending a CommandManager in at construction to support
 * undo/redo when pressing the +/- "buttons", but this should be possible to
 * move outside by adding listeners for add/remove events or something.
 * 
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
    private CardView dayViews = CardView.SIMPLE;
    private ArrayList<CalendarController> controllers = new ArrayList<CalendarController>();
    private ArrayList<MonthDayCard> views = new ArrayList<MonthDayCard>();
    private ArrayList<CalendarDataChangedListener> dataChangedListeners = new ArrayList<CalendarDataChangedListener>();
    private CalendarModel calendarModel = new CalendarModel();
    private ArrayList<JLabel> dayTitleLabels = new ArrayList<JLabel>();

    /**
     * Construct the calendar, with all it's child components and data it needs.
     * 
     * @param calendarModel calendarModel containing all events shown in the calendar
     */
    public CalendarContainer(CalendarModel calendarModel) {
        this.calendarModel = calendarModel;
        setLayout(new BorderLayout());
        topPanel = new JPanel(new BorderLayout());
        // Create a calendar for current day
        cal = GregorianCalendar.getInstance();

        monthTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthTitleLabel = new JLabel();
        monthTitle.add(monthTitleLabel);

        //monthTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        previousMonthButton = new JButton("<<");
        nextMonthButton = new JButton(">>");

        monthNavPanel = new JPanel(new BorderLayout());
        monthNavPanel.add(previousMonthButton, BorderLayout.LINE_START);
        monthNavPanel.add(monthTitle, BorderLayout.CENTER);
        monthNavPanel.add(nextMonthButton, BorderLayout.LINE_END);

        dayTitle = new JPanel(new GridLayout(1, 7));

        //dayTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Calendar tempCalendar = GregorianCalendar.getInstance();
        for (int i = 1; i <= 7; i++) {
            tempCalendar.set(Calendar.DAY_OF_WEEK, i);
            JLabel newLabel = new JLabel(tempCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
            newLabel.setHorizontalAlignment(JLabel.CENTER);
            dayTitle.add(newLabel);
            dayTitleLabels.add(newLabel);
        }

        topPanel.add(monthNavPanel, BorderLayout.PAGE_START);
        topPanel.add(dayTitle, BorderLayout.CENTER);

        monthView = new JPanel(new GridLayout(6, 7));
        initMVC();

        add(topPanel, BorderLayout.PAGE_START);
        add(monthView, BorderLayout.CENTER);
        initListeners();

        /*
         * set some default properties of the calendar
         */
        setBackground(SystemColor.white);
        setComponentBackground(SystemColor.control);
        setDayNameBackground(SystemColor.window);
        setDayFont(new Font("Arial", Font.BOLD, 16));
        
        setDayTitleBackground(new Color(240, 240, 240));
        setDayTitleBackground2(new Color(210,210,210));
        
        setGridBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        
    }

    /**
     * Set the border type to use between days in
     * the grid.
     * 
     * @param gridborder
     */
    public void setGridBorder(Border gridborder) {
    	for (int i = 0; i < views.size(); i++) {
    		views.get(i).setBorder(gridborder);
    	}
    }
    /**
     * Set the default color for the triangle in all
     * daycards.
     * @param color
     */
    public void setTriangleColor(Color color) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setTriangleColor(color);
        }
    }

    /**
     * Set the color the triangle indicator fades
     * into for all daycards.
     * 
     * @param color
     */
    public void setTriangleFadeColor(Color color) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setTriangleFadeColor(color);
        }
    }

    /**
     * Set the background color for the calendar, which means
     * the background of all days.
     * @param color
     */
    @Override
    public void setBackground(Color color) {
        super.setBackground(color);

        //avoid doing this if we got called before views has been created
        //this happens in Swing automatically, and is a problem since 
        //this is an overload of the default setBackground()
        if (views != null) {
            for (int i = 0; i < views.size(); i++) {
                views.get(i).setBackground(color);
            }
        }
    }
    
    /**
     * Get the background color of the days of the calendar container.
     * 
     * @return		background color of the days, or their parent's
     * 				background color if none is specified.
     */
    public Color getBackground() {
    	if (views != null && views.size() > 0)
    		return views.get(0).getBackground();
    	else
    		return super.getBackground();
    }

    /**
     * Set the font for the Month title. This is just the name
     * of the current month.
     * 
     * @param font
     */
    public void setMonthFont(Font font) {
        monthTitleLabel.setFont(font);
    }

    /**
     * Set the font to use for the day titles, which refers to
     * the 7 names of days listed on the top of the calendar.
     * 
     * @param font
     */
    public void setDayTitleFont(Font font) {
        for (int i = 0; i < dayTitleLabels.size(); i++) {
            dayTitleLabels.get(i).setFont(font);
        }
    }

    /**
     * Set the font to use for each individual day's title.
     * 
     * @param font
     */
    public void setDayFont(Font font) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setTitleFont(font);
        }
    }
    
    /**
     * Set the background color of the area listing
     * the days, Sun-Mon
     * 
     * @param color
     */
    public void setDayNameBackground(Color color) {
    	for (int i = 0; i < dayTitleLabels.size(); i++) {
            dayTitleLabels.get(i).setBackground(color);
        }
    }

    /**
     * Set the foreground color of the area listing
     * the days, Sun-Mon
     * @param color
     */
    public void setDayNameForeground(Color color) {
        for (int i = 0; i < dayTitleLabels.size(); i++) {
            dayTitleLabels.get(i).setForeground(color);
        }

    }

    /**
     * Set the background color for the month title area and
     * the filler space between navigation components.
     * @param color
     */
    public void setComponentBackground(Color color) {
        super.setBackground(color);
        monthTitle.setBackground(color);
        monthNavPanel.setBackground(color);
    }

    /**
     * Set the background color of each day's title.
     * 
     * @param color
     */
    public void setDayTitleBackground(Color color) {
    	for (int i = 0; i < views.size(); i++) {
            views.get(i).setTitleBackground(color);
        }
    }
    
    /**
     * Set the second background color of each day's title.
     * This can be used to produce a gradient effect on the
     * background.
     * 
     * @param color
     */
    public void setDayTitleBackground2(Color color) {
    	for (int i = 0; i < views.size(); i++) {
            views.get(i).setTitleBackground2(color);
        }
    }

    /**
     * Set the color of auxiliary text inside each day, like the
     * title.
     * 
     * @param color
     */
    public void setDayForeground(Color color) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setTitleForeground(color);
        }
    }

    /**
     * Set the color for text displaying event names.
     * 
     * @param color
     */
    public void setEventForeground(Color color) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setEventForeground(color);
        }
    }

    /**
     * Set which kind of marker will be used for highlighting
     * days that contain events matching specified criteria.
     * 
     * @param marker		marker to use
     * 
     * @see #addHighlight(Category)
     * @see #addHighlight(Priority)
     */
    public void setMarker(Marker marker) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setMarker(marker);
        }
    }

    /**
     * Add a category to the list of categories that will be
     * highlighted in the calendar by the specified marker
     * 
     * @param category		category to match against
     * 
     * @see #setMarker(Marker)
     */
    public void addHighlight(Category category) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).addHighlight(category);
        }
    }

    /**
     * Remove the specified category from the list of
     * categories to highlight
     * 
     * @param category		category to remove
     */
    public void removeHighlight(Category category) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).removeHighlight(category);
        }
    }

    /**
     * Add a priority to the list of priorities that will be
     * highlighted in the calendar by the specified marker.
     * 
     * @param prio			priority to match against
     * 
     * @see #setMarker(Marker)
     */
    public void addHighlight(Priority prio) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).addHighlight(prio);
        }
    }

    /**
     * remove specified priority from the list of priorities
     * to highlight
     * @param prio
     */
    public void removeHighlight(Priority prio) {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).removeHighlight(prio);
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
            //add each daycard into the animation driver under the thread "calendarcontainer"
            AnimationDriver.getInstance().add(daycard, "calendarcontainer");

            DayFilteredCalendarModel model = new DayFilteredCalendarModel();

            model.setRealCalendarModel(calendarModel);

            controller.addView(daycard);
            controller.addModel(model);
            views.add(daycard);
            controllers.add(controller);
            monthView.add(daycard);
        } //Call switchToMonth to attach the correct days (models) to the newly created MonthCards
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

        for (int i = 1; i
                <= 42; i++) {
            final CardView currentView;     //variable to keep track what apperence the daycard should have
            final int index = i - 1;

            if (i >= (startDay) && i < (startDay + numDays)) {
                currentView = dayViews;
                tempCal.set(Calendar.DAY_OF_MONTH, i - startDay + 1);
                Date filter = tempCal.getTime();
                controllers.get(index).setFilter(filter);

            } else {
                currentView = CardView.NONE;
                controllers.get(index).setFilter(new Date(0, 0, 0));
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
        switchToMonth(
                cal);
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
    } /*
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

