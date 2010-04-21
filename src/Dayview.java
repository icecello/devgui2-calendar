
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.TimeStamp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A proposition of the detailed view of a DayCard
 * @author Tomas
 */
public class Dayview extends JPanel {

    JPanel view = new JPanel(new GridBagLayout());
    JScrollPane scroll = new JScrollPane(view,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    ArrayList<EventCard> eventCards = new ArrayList<EventCard>();

    public Dayview() {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new BorderLayout());
        scroll.setMinimumSize(new Dimension(100, 500));
        initTimeLabels();
        //Add some events, used for debugging
        for (int i = 0; i < 24; i++) {
            if (i % 2 == 0) {
                DayEvent event = new DayEvent("Event", new TimeStamp(i, 0), new TimeStamp(i, 30));
                addEvent(event);
            }
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

        for (int i = 0; i < 48; i++) {
            //Even hours
            c.gridy = i;
            if (i % 2 == 0) {
                JLabel timeLabel = new JLabel((i / 2 <= 9 ? "0" + i / 2 : i / 2) + ":00  ");
                timeLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
                view.add(timeLabel, c);
                //Half hours
            } else {
                //Add an empty box, making the half hours take place as well
                Box box = Box.createVerticalBox();
                box.add(Box.createVerticalStrut(10));
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

        int[] timeSpan = event.timeSpan();
        int labelSize = 2 * timeSpan[0];
        labelSize += timeSpan[1] / 30;  //Every hour = 2 gridheight

        for (EventCard eventCard : eventCards) {
            if (event.isActive(eventCard.event.getStartTime())
                    || event.isActive(eventCard.event.getEndTime())) {
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
        cLocal.gridy = 2 * event.getStartTime().getHour() + event.getStartTime().getMin() / 30;
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
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Dayview d = new Dayview();

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(d);
        d.addEvent(new DayEvent("Innebandy", new TimeStamp(21, 0), new TimeStamp(22, 00)));
        d.addEvent(new DayEvent("Städa", new TimeStamp(10, 00), new TimeStamp(15, 00)));
        frame.pack();
        frame.setVisible(true);

    }
}
