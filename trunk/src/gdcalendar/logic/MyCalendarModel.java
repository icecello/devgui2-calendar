package gdcalendar.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import gdcalendar.gui.calendar.CalendarModel;
import gdcalendar.gui.calendar.Event;

/**
 * This class is an implementation of the CalendarModel interface, which
 * is a WIP. It stores events as arraylists for specified dates, using
 * a hash map.
 * 
 * @author Håkan
 *
 */
public class MyCalendarModel implements CalendarModel {

	private HashMap<Calendar, ArrayList<Event> > eventMap;
	
	MyCalendarModel() {
		
	}
	
	/**
	 * Add a new event to the specified date.
	 * 
	 * @param date			which date to add the new event to
	 * @param newEvent		a new event
	 * @return true if the event was added successfully
	 */
	public boolean addEvent(Calendar date, Event newEvent) {
		ArrayList<Event> list = eventMap.get(date);
		
		if (list == null)
			list = new ArrayList<Event>();
		
		boolean ret = list.add(newEvent);
		eventMap.put(date, list);
		
		return ret;
    }

	/**
	 * Remove specified event from a certain day.
	 * 
	 * @param date		which date to remove the event from
	 * @param event		which event to remove
	 * @return true if an event was removed
	 */
    public boolean removeEvent(Calendar date, Event event) {
    	ArrayList<Event> list = eventMap.get(date);
    	
    	if (list == null)
    		return false;
    	
        return list.remove(event);
    }
    

	@Override
	public Object getValueAt(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}
}
