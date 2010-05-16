package gdcalendar.mvc.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * This is a model that should contain all DayEvents associated with a CalendarContainer.
 * It is accessed through a filter by the CalendarContainer.
 * 
 * @author Håkan, Tomas
 */
public class CalendarModel extends AbstractModel {

    //HashMap containing all DayEvents for the calendar
    private HashMap<UUID, DayEvent> dayMap = new HashMap<UUID, DayEvent>();

    public CalendarModel() {
    }

    /**
     * Add a new event to the specified date.
     * @param newDay		a new event
     */
    public void addDayEvent(DayEvent event) {     
        dayMap.put(event.getID(), event);
        //Tell all models connected to this model that it has been updated
        firePropertyChange("realModelChanged", null, null);
    }

    /**
     * Remove specified event from the CalendarModel.
     *
     * @param eventID	The ID that uniquely identifies a DayEvent
     * @return 		The event that has been removed
     */
    public DayEvent removeDayEvent(UUID eventID) {
        //Tell all models connected to this model that it has been updated
        firePropertyChange("realModelChanged", null, null);
        return dayMap.remove(eventID);
    }

    /**
     * Get the DayEvent determined by the given eventID. If eventID is not present, 
     * null is returned
     *
     * @param eventId	The ID that uniquely identifies a DayEvent
     * @return		the DayEvent that is connected to the ID
     */
    public DayEvent getDayEvent(UUID eventID) {
        return dayMap.get(eventID);
    }

    /**
     * Get the complete set of DaYEvents stored in the CalendarModel
     * @return the set of DayEvents
     */
    public DayEvent[] getEvents() {
       Collection<DayEvent> events = dayMap.values();
       DayEvent[] ev = new DayEvent[events.size()];
       events.toArray(ev);
       return  ev;
    }
}
