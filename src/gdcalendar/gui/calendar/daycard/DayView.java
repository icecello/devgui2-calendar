/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui.calendar.daycard;

import gdcalendar.mvc.model.DayEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author James
 * @author Tomas
 */
public class DayView extends JPanel {
    JPanel view = new JPanel(new GridBagLayout());
    JScrollPane scroll = new JScrollPane(view,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    ArrayList<EventCard> eventCards = new ArrayList<EventCard>();

    public DayView() {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new BorderLayout());
        scroll.setMinimumSize(new Dimension(100, 500));
        initTimeLabels();

        //Add some events, used for debugging
        for (int i = 0; i < 24*60; i++) {
            if (i % 60 == 0) {
                Date date1 = new Date();
                date1.setHours(i/60);
                date1.setMinutes(0);
                Date date2 = new Date();
                date2.setHours(i/60);
                date2.setMinutes(30);

                DayEvent event = new DayEvent("Event", date1, date2); // new TimeStamp(i/60, 0), new TimeStamp(i/60, 30));
                addEvent(event);
            }
        }
        add(scroll, BorderLayout.CENTER);
    }

    public DayView(ArrayList<DayEvent> events) {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new BorderLayout());
        scroll.setMinimumSize(new Dimension(100, 500));
        initTimeLabels();

        for (int i=0; i < events.size(); i++) {
            addEvent(events.get(i));
        }

        add(scroll, BorderLayout.CENTER);
    }

    public DayView(DayEvent[] events) {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new BorderLayout());
        scroll.setMinimumSize(new Dimension(100, 500));
        initTimeLabels();

        System.out.println("length: " + events.length);
        for (int i=0; i < events.length; i++) {
            addEvent(events[i]);
        }

        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Initialize the time labels shown to the right.
     * Every even hour is displayed, and a event have a resolution of 30 min
     * in this implementation
     */
    private void initTimeLabels() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;

        for (int i = 0; i < 24*15; i++) {
            //Even hours
            c.gridy = i;
            if (i % 15 == 0) {
                JLabel timeLabel = new JLabel((i / 15 <= 9 ? "0" + i / 15 : i / 15) + ":00  ");
                timeLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
                view.add(timeLabel, c);
                //Half hours
            } else {
                //Add an empty box, making the half hours take place as well
                Box box = Box.createVerticalBox();
                box.add(Box.createVerticalStrut(1));
                box.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
                view.add(box, c);
            }
        }
    }

    public void addEvent(final DayEvent event) {
        //Events overlapping with the new event is put into an array, and will
        //later be removed
        ArrayList<EventCard> toRemove = new ArrayList<EventCard>();
        //
        final JLabel newEvent = new JLabel(event.getEventName());
        newEvent.setVerticalAlignment(SwingConstants.TOP);
        newEvent.setHorizontalAlignment(SwingConstants.CENTER);
        newEvent.setOpaque(true);   //Make the label show it's background
        newEvent.setBackground(new Color(200, 240, 200));
        newEvent.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.lightGray));

        int labelSize = event.getTimeSpan() / 4; // 1 min = 1/4 px height

        for (EventCard eventCard : eventCards) {
            if (event.isActiveDuringTimeStamp(eventCard.event.getStartTime())
                    || event.isActiveDuringTimeStamp(eventCard.event.getEndTime())) {
                toRemove.add(eventCard); //Which events conflict with the new one

            }
        }
        for (final EventCard eventCard : toRemove) {
            eventCards.remove(eventCard);
            SwingUtilities.invokeLater(new Runnable() {
                //Remove the conflicting events
                public void run() {
                    view.remove(eventCard.eventLabel);
                }
            });
        }
        final GridBagConstraints cLocal = new GridBagConstraints();
        //Determine where to put the new event
        cLocal.gridy = 15 * event.getStartTime().getHours() + event.getStartTime().getMinutes();
        cLocal.gridheight = labelSize;  cLocal.fill = GridBagConstraints.BOTH;
        cLocal.gridx = 1; cLocal.weightx = 1; cLocal.weighty = 1;
        SwingUtilities.invokeLater(new Runnable() {
            //Just add the new event at the correct position
            public void run() {
                view.add(newEvent, cLocal);
            }
        });
        //Keep track of the new event, and revalidate/repaint the view
        eventCards.add(new EventCard(event, newEvent));
        revalidate();
        repaint();
    }

    /**
     * Simple innner class for connecting events with it's corresponding
     * visual representation
     */
    private class EventCard {

        private DayEvent event;
        private JLabel eventLabel;

        public EventCard(DayEvent event, JLabel eventLabel) {
            this.event = event;
            this.eventLabel = eventLabel;
        }
    }

    //Test the detailed view, adding some new events
//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        DayView d = new DayView();
//
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.add(d);
//        d.addEvent(new DayEvent("Innebandy", new Date("May 30, 2010, 10:00:00 GMT+1"), new Date("May 30, 2010, 10:30:00 GMT+1"))); //new TimeStamp(21, 0), new TimeStamp(22, 00)));
//        d.addEvent(new DayEvent("Städa", new Date("May 30, 2010, 14:30:00 GMT+1"), new Date("May 30, 2010, 16:00:00 GMT+1"))); //new TimeStamp(10, 00), new TimeStamp(14, 25)));
//        frame.pack();
//        frame.setVisible(true);
//
//    }
}
