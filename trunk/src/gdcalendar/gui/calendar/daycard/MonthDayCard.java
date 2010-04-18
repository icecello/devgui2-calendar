/**
 * 
 */
package gdcalendar.gui.calendar.daycard;

import gdcalendar.mvc.controller.DefaultController;
import gdcalendar.mvc.model.Day;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.TimeStamp;
import gdcalendar.mvc.view.AbstractViewPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Calendar;
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
    private JPanel simpleView = new JPanel();   //The simple visual representation of the day
    private JPanel detailedView = new JPanel(); //The advanced visual representation of the day
    private String newEventName;        //temporary storage for a new event name
    private JLabel titleLabel;          //Title label, showing the day of the month
    private JLabel addEventLabel;       //Label, used for adding new events
    private JLabel removeEventLabel;    //Label, used for removing events
    private DefaultController controller;   //The controller, responsible for updating the
                                            //connected models
    private ArrayList<JLabel> eventLabels;  //The visual representation of the day events
    private ArrayList<DayEvent> events;     //The events of the day
    /**
     * Default constructor, creating an empty MonthDayCard
     */
    public MonthDayCard() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel();
        eventLabels = new ArrayList<JLabel>();
        events = new ArrayList<DayEvent>();

        //Make the day of the month appear in the top center position of the card
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleContainer.add(titleLabel);
        add(titleLabel, BorderLayout.PAGE_START);

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

        addEventLabel = new JLabel("+");
        addEventLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        removeEventLabel = new JLabel("-");
        removeEventLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel eventModifyContainer = new JPanel(new BorderLayout());
        simpleView.setLayout(new BoxLayout(simpleView, BoxLayout.PAGE_AXIS));
        detailedView.setLayout(new BoxLayout(detailedView, BoxLayout.PAGE_AXIS));
        //The title is set to the current day of the month
        String title = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        titleLabel.setText(title);

        initListners();

        //Make the monthcard show the correct view
        if (view == CardView.SIMPLE) {
            add(simpleView, BorderLayout.CENTER);
        } else {
            add(detailedView, BorderLayout.CENTER);
        }
        eventModifyContainer.add(removeEventLabel, BorderLayout.LINE_START);
        eventModifyContainer.add(addEventLabel, BorderLayout.LINE_END);
        add(eventModifyContainer, BorderLayout.PAGE_END);


    }

    public MonthDayCard(Day day, CardView view, DefaultController controller) {
        /*
         * Call MonthDayCard with current date and the given view
         */
        this(Calendar.getInstance(), view);
        //Make the necessary adjustments, so that the monthcard reflectes
        //the data in the given day
        this.controller = controller;
        calendar.setTime(day.getDate());
        titleLabel.setText("" + calendar.get(Calendar.DAY_OF_MONTH));

        for (DayEvent event : day.getEvents()) {
            JLabel eventLabel = new JLabel(event.toString());
            eventLabels.add(eventLabel);
            this.events.add(event);
            simpleView.add(eventLabel);
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
        revalidate();
        repaint();
    }

    /**
     *
     * @see gdcalendar.gui.calendar.daycard.IDayCard#setDate()
     */
    @Override
    public void setDate(Calendar date) {
        this.calendar = date;
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
        if (eventLabels.size() > 0) {

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
        if (evtName.equals(DefaultController.ADD_EVENT_PROPERTY)) {
            SwingUtilities.invokeLater(new Runnable() {

                String name = newEventName; //Store a local copy of the event name
                                            //Since we don't know when it's called

                public void run() {
                    JLabel event = new JLabel(name);

                    simpleView.add(event);
                    eventLabels.add(event);
                    events.add(new DayEvent(name));
                }
            });

        //Remove an event (the one at the bottom) from the DayCard, invoked in EDT
        } else if (evtName.equals(DefaultController.REMOVE_EVENT_PROPERTY)) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    simpleView.remove(eventLabels.get(eventLabels.size()-1));
                    eventLabels.remove(eventLabels.size()-1);
                    events.remove(events.get(events.size()-1));
                }
            });

        }
        revalidate();
        repaint();
    }

    private void initListners() {
        addEventLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                MonthDayCard.this.addEventMouseClicked(e);
            }
        });

        removeEventLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                MonthDayCard.this.removeEventMouseClicked(e);
            }
        });
    }

    /**
     * Add an event to the by calling the controller
     * @param e the mouse event responsible for the call
     */
    private void addEventMouseClicked(MouseEvent e) {
        //For now, create a new event labeled with "New Event"
        try {
            DayEvent newEvent = new DayEvent("New Event",new TimeStamp(10, 00), new TimeStamp(12, 30));
            controller.addEvent(newEvent);
        } catch (Exception ex) {
            //  Handle exception
        }
    }

    /**
     * Remove the event appering at the bottom of the DayCard
     * @param e the mouse event responsible for the call
     */
    private void removeEventMouseClicked(MouseEvent e) {

        //Let the controller remove the event from the model, and update the view.
        try {   
            controller.removeEvent(events.get(events.size()-1));
        } catch (Exception ex) {
            // Handle exception
        }
    }
}
