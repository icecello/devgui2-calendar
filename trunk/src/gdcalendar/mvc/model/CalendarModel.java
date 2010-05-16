package gdcalendar.mvc.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * This is a model that should contain all DayEvents associated with a CalendarContainer.
 * It is accessed through a filter by the CalendarContainer.
 * 
 * 
 * 
 * 
 * @author Håkan, Tomas
 *
 */
public class CalendarModel extends AbstractModel {

    private HashMap<UUID, DayEvent> dayMap = new HashMap<UUID, DayEvent>();

    public CalendarModel() {
    }

    /**
     * Add a new event to the specified date.
     *
     * @param newDay		a new event
     */
    public void addDayEvent(DayEvent event) {
        firePropertyChange("realModelChanged", null, null);
        dayMap.put(event.getID(), event);
    }

    /**
     * Remove specified event from a certain day.
     *
     * @param date		which date to remove the event from
     * @param day		which event to remove
     * @return 		boolean, if list contained this event
     */
    public DayEvent removeDayEvent(UUID eventID) {
        firePropertyChange("realModelChanged", null, null);
        return dayMap.remove(eventID);
    }

    /**
     * 
     * 
     * @param i		indexed DayEvent to get from this model
     * @return		the DayEvent found at position i
     */
    public DayEvent getDayEvent(UUID eventID) {
        return dayMap.get(eventID);
    }

    public DayEvent[] getEvents() {
       Collection<DayEvent> events = dayMap.values();
       DayEvent[] ev = new DayEvent[events.size()];
       events.toArray(ev);
       return  ev;
    }
}
