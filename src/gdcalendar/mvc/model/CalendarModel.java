package gdcalendar.mvc.model;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


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
public class CalendarModel {

    private ArrayList<DayEvent> dayMap = new ArrayList<DayEvent>();

    public CalendarModel() {
    	
    }

    /**
     * Add a new event to the specified date.
     *
     * @param newDay		a new event
     */
    public void addDayEvent(DayEvent event) {
        dayMap.add(event);
    }

    /**
     * Remove specified event from a certain day.
     *
     * @param date		which date to remove the event from
     * @param day		which event to remove
     * @return 		boolean, if list contained this event
     */
    public boolean removeDayEvent(DayEvent event) {
        return dayMap.remove(event);
    }

    /**
     * 
     * 
     * @param i		indexed DayEvent to get from this model
     * @return		the DayEvent found at position i
     */
    public DayEvent getDayEvent(int i) {
    	return dayMap.get(i);
    }

}
