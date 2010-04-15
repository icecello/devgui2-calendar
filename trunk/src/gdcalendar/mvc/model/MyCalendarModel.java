package gdcalendar.mvc.model;


import java.util.Calendar;
import java.util.HashMap;


/**
 * This class is an implementation of the CalendarModel interface, which
 * is a WIP. It stores events as arraylists for specified dates, using
 * a hash map.
 * 
 * @author Håkan, Tomas
 *
 */
public class MyCalendarModel {

    private HashMap<Calendar, Day> dayMap;

    MyCalendarModel() {
    }

    /**
     * Add a new event to the specified date.
     *
     * @param date			which date to add the new event to
     * @param newEvent		a new event 
     */
    public void addDay(Calendar date, Day newDay) {
        dayMap.put(date, newDay);
    }

    /**
     * Remove specified event from a certain day.
     *
     * @param date		which date to remove the event from
     * @param event		which event to remove
     * @return the day which is supposed to be removed. NULL is returned
     * if no matchin Day is contained in the model
     */
    public Day removeEvent(Calendar date, Day day) {
        return dayMap.remove(date);
    }

   
    public Object getValueAt(Calendar date) {
        // TODO Auto-generated method stub
        return null;
    }
}
