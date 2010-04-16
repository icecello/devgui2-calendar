/**
 * 
 */
package gdcalendar.gui.calendar.daycard;

import gdcalendar.mvc.controller.DefaultController;
import gdcalendar.mvc.model.Day;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.view.AbstractViewPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import javax.swing.BoxLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Håkan
 *
 * This is the implementation of a day card that can be used in the standard
 * calendar if we omit the week view for now. It's quite simple to change if 
 * we want to support week view later. It's a self-contained day view panel.
 * 
 * One possibility is to switch to detailed view on clicking the indicator
 * triangle in the corner...
 * 
 */
public class MonthDayCard extends AbstractViewPanel implements IDayCard {

    public enum CardView {

        SIMPLE,
        DETAILED
    }
    /*
     * member variables
     */
    private Calendar calendar;
    private CardView view = CardView.SIMPLE;
    private Collection<DayEvent> dayEvents = new ArrayList<DayEvent>();
    private JPanel simpleView = new JPanel();
    private JPanel detailedView = new JPanel();
    private String newEventName;
    private JLabel titleLabel;
    /*
     * member components
     */

    /**
     * Default constructor, creating an empty MonthDayCard
     */
    public MonthDayCard() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel();
        dayEvents = new ArrayList<DayEvent>();

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleContainer.add(titleLabel);
        add(titleContainer, BorderLayout.CENTER);
    }

    /**
     * Create a MonthDayCard showing the day of the month formated for the given view
     *
     * @param day	the day of month to display in this day card
     * @param newView	which layout to use for the drawing of the component
     */
    public MonthDayCard(Calendar calendar, CardView view) {
        //Call MonthDayCard() for basic set up code
        this();
        this.view = view;
        this.calendar = calendar;

        simpleView.setLayout(new BoxLayout(simpleView, BoxLayout.PAGE_AXIS));
        detailedView.setLayout(new BoxLayout(detailedView, BoxLayout.PAGE_AXIS));
        //The title is set to the current day of the month
        String title = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        titleLabel.setText(title);

        //Make the monthcard show the correct view
        if (view == CardView.SIMPLE) {
            add(simpleView, BorderLayout.PAGE_END);
        } else {
            add(detailedView, BorderLayout.PAGE_END);
        }
    }

    public MonthDayCard(Day day, CardView view) {
        /*
         * Call MonthDayCard with current date and the given view
         */
        this(Calendar.getInstance(), view);
        //Make the necessary adjustments, so that the monthcard reflectes
        //the data in the given day
        dayEvents = day.getEvents();
        calendar.setTime(day.getDate());
        titleLabel.setText("" + calendar.get(Calendar.DAY_OF_MONTH));

        for (DayEvent events : dayEvents) {
            simpleView.add(new JLabel(events.toString()));
        }

    }

    /**
     *
     * @see gdcalendar.gui.calendar.daycard.IDayCard#getDate()
     */
    @Override
    public Calendar getDate() {
        return calendar;
    }

    public CardView getView() {
        return view;
    }

    public void setView(CardView newView) {
        this.view = newView;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (view == CardView.SIMPLE) {
                    add(simpleView, BorderLayout.PAGE_END);
                } else {
                    add(detailedView, BorderLayout.PAGE_END);
                }
            }
        });

    }

    /**
     *
     * @see gdcalendar.gui.calendar.daycard.IDayCard#setDate()
     */
    @Override
    public void setDate(Calendar date) {
        this.calendar = date;
    }

    public void setImage(Image image) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Image getImage() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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
        if (dayEvents.size() > 0) {

            /*
             * we may want to move these calculations into a resize event instead of paint
             *
             */
            int x[] = {
                (int) (getWidth() * 0.8),
                getWidth(),
                getWidth()
            };
            int y[] = {
                getHeight(),
                (int) (getHeight() * 0.8),
                getHeight()
            };


            Polygon triangle = new Polygon(x, y, 3);

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(30, 30, 220));
            g2.fillPolygon(triangle);
            g2.setColor(new Color(30, 30, 100));
            g2.setStroke(new BasicStroke(3));
            g2.drawPolygon(triangle);

            //draw string showing how many events are available for this day
            //very crude, needs refinement
            //probably easier to switch to a label for this...
            String str = "Events: " + Integer.toString(dayEvents.size());
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
     * Add a new event to display in this day card.
     * This method is temporary and should probably be removed in favor of
     * using a CalendarModel to provide event data.
     *
     * @param newEvent		a new event
     * @return true if the event was added successfully
     */
    @Deprecated
    public boolean addEvent(DayEvent newEvent) {
        return dayEvents.add(newEvent);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String evtName = evt.getPropertyName();
        newEventName = evt.getNewValue().toString();
        if (evtName.equals(DefaultController.ADD_EVENT_PROPERTY)) {
            SwingUtilities.invokeLater(new Runnable() {
                String name = newEventName;
                public void run() {
                    simpleView.add(new JLabel(name));
                }
            });


        } else if (evtName.equals(DefaultController.REMOVE_EVENT_PROPERTY)) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    //TODO: Do something here!
                }
            });

        }
        repaint();
    }
}
