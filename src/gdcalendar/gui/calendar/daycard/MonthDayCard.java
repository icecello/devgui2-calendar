/**
 * 
 */
package gdcalendar.gui.calendar.daycard;

import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.model.Day;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.view.AbstractViewPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BoxLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Håkan
 * @author James
 *
 * This is the implementation of a day card that can be used in the standard
 * calendar if we omit the week view for now. It's quite simple to change if 
 * we want to support week view later. It's a self-contained day view panel.
 * 
 * One possibility is to switch to detailed view on clicking the indicator
 * triangle in the corner...
 * 
 * Update 21/4-10: This comment is outdated, the class has changed quite a bit
 * since it was made... 
 * 
 */
@SuppressWarnings("serial")
public class MonthDayCard extends AbstractViewPanel implements IDayCard {

    /**
     * A collection of constant used for defining how a MonthDayCard should
     * visually represent it's data
     */
    public enum CardView {

        /**
         * The contained data should not be shown at all
         */
        NONE,
        /**
         * The contained data should be shown in a simplistic way. Only textual
         * information will be shown
         */
        SIMPLE,
        /**
         * The contained data should be shown in a sophisticated way. Graphical components,
         * such as images texts will be shown
         */
        DETAILED
    }
    /*
     * member variables
     */
    private Calendar calendar = Calendar.getInstance();
    private Date filter;
    private CardView view = CardView.SIMPLE;
    private JPanel simpleView = new JPanel();   //The simple visual representation of the day
    private JPanel detailedView = new JPanel(); //The advanced visual representation of the day
    private String newEventName;        //temporary storage for a new event name
    private JLabel titleLabel;          //Title label, showing the day of the month
    private JLabel addEventLabel;       //Label, used for adding new events
    private JLabel removeEventLabel;    //Label, used for removing events
    private CalendarController controller;   //The controller, responsible for updating the
    //connected models
    private ArrayList<JLabel> eventLabels;  //The visual representation of the day events
    private ArrayList<DayEvent> events;     //The events of the day

    /**
     * Default constructor, creating an empty MonthDayCard
     */
    private MonthDayCard() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        eventLabels = new ArrayList<JLabel>();
        events = new ArrayList<DayEvent>();
        add(titleLabel, BorderLayout.PAGE_START);
    }

    /**
     * Create an empty MonthDayCard. It's visual apperence is decided by
     * the view.
     *
     * @param view	which layout to use for the drawing of the component
     */
    public MonthDayCard(CardView view, CalendarController controller) {
        //Call MonthDayCard() for basic set up code
        this();
        this.view = view;

        addEventLabel = new JLabel("+");
        addEventLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        removeEventLabel = new JLabel("-");
        removeEventLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        //If the view is set to none we don't wanna show the add and remove
        //event labels
        if (view == CardView.NONE) {
            addEventLabel.setVisible(false);
            removeEventLabel.setVisible(false);
        }

        JPanel eventModifyContainer = new JPanel(new BorderLayout());
        eventModifyContainer.add(removeEventLabel, BorderLayout.LINE_START);
        eventModifyContainer.add(addEventLabel, BorderLayout.LINE_END);
        add(eventModifyContainer, BorderLayout.PAGE_END);

        simpleView.setLayout(new BoxLayout(simpleView, BoxLayout.PAGE_AXIS));
        detailedView.setLayout(new BoxLayout(detailedView, BoxLayout.PAGE_AXIS));
        //Make the monthcard show the correct view
        if (view == CardView.SIMPLE) {
            add(simpleView, BorderLayout.CENTER);
        } else {
            add(detailedView, BorderLayout.CENTER);
        }
    }

    /**
     * Create a MonthCard showing a visual representation of a given day. The day
     * is connected to a contoller, which is responsible to updating connected models
     *
     * @param day   The day to visually represent
     * @param view  The visual representation of the day depend on which view
     *              that is connected to it
     * @param controller    The controller which is responsible for connecting the
     *                      MonthCardDay to corresponing models
     */
    public MonthDayCard(Date filter, CardView view, CalendarController controller) {
        /*
         * Call MonthDayCard with current date and the given view
         */

        this(view, controller);
        //Make the necessary adjustments, so that the monthcard reflectes
        //the data in the given day
        this.controller = controller;
        calendar.setTime(filter);

        titleLabel.setText("" + calendar.get(Calendar.DAY_OF_MONTH));

        if (view == CardView.NONE) {
            titleLabel.setVisible(false);
        }
        controller.setFilter(filter);
    }

    /**
     *
     * @see gdcalendar.gui.calendar.daycard.IDayCard#getDate()
     */
    @Override
    public Calendar getDate() {
        return calendar;
    }

    public void setDate(Calendar date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CardView getView() {
        return view;
    }

    /**
     * Change the visual apperence of the MonthDayCard. See
     * CardView for more details
     * @see gdcalendar.gui.calendar.daycard.MonthDayCard#view
     * @param newView the new view to show
     */
    public void changeView(CardView newView) {
        this.view = newView;
        remove(simpleView);
        remove(detailedView);
        if (view == CardView.SIMPLE) {
            add(simpleView, BorderLayout.CENTER);
            titleLabel.setVisible(true);
            addEventLabel.setVisible(true);
            removeEventLabel.setVisible(true);
        } else if (view == CardView.DETAILED) {
            add(detailedView, BorderLayout.CENTER);
            titleLabel.setVisible(true);
            addEventLabel.setVisible(true);
            removeEventLabel.setVisible(true);
        } else {
            titleLabel.setVisible(false);
            addEventLabel.setVisible(false);
            removeEventLabel.setVisible(false);

        }
    }

    public boolean displayImage() {
        throw new UnsupportedOperationException("Not supported yet.");


    }

    public void hideImage() {
        throw new UnsupportedOperationException("Not supported yet.");


    }

    /**
     * handle drawing of the day card, delegating the different ways
     * of drawing based on active view to private methods
     *
     *
     */
    @Override
    protected void paintChildren(Graphics g) {

        super.paintChildren(g);
        switch (view) {
            case SIMPLE:
                paintSimple(g);
                break;
            case DETAILED:
                paintDetailed(g);
                break;
        }
    }

    /**
     * Delegated method that handles the simple painting of the component.
     * This should never be called from outside the component.
     *
     */
    private void paintSimple(Graphics g) {
        //only draw if there are any events for this day card
        if (eventLabels.size() > 0) {
            /*
             * we may want to move these calculations into a resize event instead of paint
             */
            int x[] = {
                (int) (getWidth() * 0.8),
                getWidth(),
                getWidth()
            };


            int y[] = {
                0,
                (int) (getHeight() * 0.2),
                0
            };


            Polygon triangle = new Polygon(x, y, 3);

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(100, 230, 100));
            g2.fillPolygon(triangle);
            g2.setColor(new Color(30, 30, 100));
            g2.setStroke(new BasicStroke(1));
            g2.drawPolygon(triangle);

            //draw string showing how many events are available for this day
            //very crude, needs refinement
            //probably easier to switch to a label for this...
            String str = "Events: " + Integer.toString(eventLabels.size());

            int stringY = Math.max(getHeight() - g2.getFontMetrics().getHeight(), getHeight() - 10);
            g2.drawString(str, 10, stringY);
        }

    }

    /**
     * Delegated method that handles the detailed painting of this day card.
     * This should never be called from outside the component.
     *
     */
    private void paintDetailed(Graphics g) {
        //draw on glass pane
    }

    /**
     * @see gdcalendar.mvc.view.AbstractViewPanel#modelPropertyChange(java.beans.PropertyChangeEvent)
     * @param evt the event responsible for the call
     */
    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {
        String evtName = evt.getPropertyName();


        if (evt.getNewValue() != null) {
            newEventName = evt.getNewValue().toString();
        }

        //Add an event label to the DayCard, invoked in EDT
        if (evtName.equals(CalendarController.ADD_EVENT)) {

            DayEvent newEvent = (DayEvent)evt.getNewValue();
            String name = newEventName;
            JLabel event = new JLabel(newEvent.getEventName());
            simpleView.add(event);
            eventLabels.add(event);
            events.add(new DayEvent(name));

            //Remove an event (the one at the bottom) from the DayCard, invoked in EDT
        } else if (evtName.equals(CalendarController.REMOVE_EVENT)) {

            simpleView.remove(eventLabels.get(eventLabels.size() - 1));
            eventLabels.remove(eventLabels.size() - 1);
            events.remove(events.get(events.size() - 1));

        } else if (evtName.equals(CalendarController.FILTER)) {
            filter = (Date) evt.getNewValue();
            calendar.setTime(filter);
            System.out.println(filter);
            titleLabel.setText("" + calendar.get(Calendar.DAY_OF_MONTH));

        } else if (evtName.equals(CalendarController.FILTERED_EVENTS)) {
            simpleView.removeAll();
            eventLabels.clear();
            events.clear();
            DayEvent[] filteredEvents = (DayEvent[]) evt.getNewValue();

            for (int i = 0; i < filteredEvents.length; i++) {
                events.add(filteredEvents[i]);
                JLabel eventLabel = new JLabel(filteredEvents[i].getEventName());
                simpleView.add(eventLabel);
                eventLabels.add(eventLabel);
            }
        }
        revalidate();
        repaint();


    }

    /**
     * Add a listener for the add new event component
     * Note that this is still internal within the CalendarContainer.
     * @param l
     */
    public void addAddEventListener(MouseListener l) {
        if (addEventLabel == null) {
            //don't add listners if empty card
        } else {
            addEventLabel.addMouseListener(l);
        }
    }

    /**
     * Add a listener for the remove event component
     * Note that this is still internal within the CalendarContainer.
     * @param l
     */
    public void addRemoveEventListener(MouseListener l) {
        if (removeEventLabel == null) {
            //don't add listners if empty card
        } else {
            removeEventLabel.addMouseListener(l);

        }
    }
}
