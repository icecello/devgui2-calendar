package gdcalendar.data;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Representation of a day, containing events taking place that day. Every event
 * is identified by a unique String value
 * @author Tomas
 */
public class Day {

    private Map<String, DayEvent> events = new HashMap<String, DayEvent>();
    private Calendar cal = Calendar.getInstance();

    /**
     * Create a day containing no events, identified by it´s date
     * @param dayId the Date identifying the Day
     */
    public Day(Calendar dayId) {
        this.cal = dayId;
    }

    /**
     * Create a Day containig a set of events, identified by it´s date
     * @param dayId the Date identifying the Day
     * @param dayEvents the set of events that taking place for this day
     */
    public Day(Calendar dayId, Map<String, DayEvent> dayEvents) {
        this(dayId);
        events = dayEvents;
    }

    /**
     * add an event to the day
     * @param event the new event that is supposed to take place this day
     */
    public void addEvent(String eventId, DayEvent event) {
        events.put(eventId, event);
    }

    /**
     * Remove the specified Event from the day. Returns the DayEvent that is removed.
     * NULL is returned if there is no matching event contained in the Day
     * @param event the event to be removed
     * @return The DayEvent removed from the Day, NLLL is returned if no matching DayEvent is
     * contained in the Day
     */
    public DayEvent removeEvent(String eventId) {
        return events.remove(eventId);
    }

    /**
     * Get the complete set of Events taking place during the Day
     * @return the set of Events for the Day
     */
    public Collection<DayEvent> getEvents() {
        return events.values();
    }

    /**
     * Get the complete set of event Id:s (unique Strings) of the Day
     * @return All of the DayEvent id:s of the Day
     */
    public Set<String> getEventIds() {
        return events.keySet();
    }

    @Override
    public String toString() {
        StringBuffer sBuffer = new StringBuffer();

        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        String year = "" + y;
        String month = m < 10 ? "" + 0 + m : "" + m;
        String day = d < 10 ? "" + 0 + d : "" + d;
        sBuffer.append(year + "-" + month + "-" + day + "\n");

        for (DayEvent dEvent : events.values()) {
            sBuffer.append(dEvent.toString() + "\n");
        }

        return sBuffer.toString();
    }

    public static void main(String[] args) {
        Day d = new Day(Calendar.getInstance());
        DayEvent event1 = new DayEvent("Event1");
        DayEvent event2 = new DayEvent("Event2");
        DayEvent event3 = new DayEvent("Event3");
        DayEvent event4 = new DayEvent("Event4", new TimeStamp(12, 00), new TimeStamp(13, 45));
        d.addEvent("e1",event1);
        d.addEvent("e2",event2);
        d.addEvent("e3",event3);
        d.addEvent("e4",event4);
        System.out.println(d);
    }
}
