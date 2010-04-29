package gdcalendar.gui.calendar;

import javax.swing.event.ChangeEvent;

/**
 * A simple event object that encapsulates information regarding
 * a change that has taken place in the calendar.
 * 
 * @author Håkan
 *
 */
@SuppressWarnings("serial")
public class CalendarChangeEvent extends ChangeEvent {

	public static int EventAdd = 0;
	public static int EventRemove = 1;
	public static int EventEdit = 2;
	
	
	private int day;
	private int eventType;
	/**
	 * 
	 * Note: "int day" should probably be a Calendar object or something...
	 * the idea is that the change event should tell listeners which event
	 * was involved in the actual change taking place.
	 * 
	 * Another possibility is to remove this altogether and let the change
	 * event simply do less.
	 * 
	 * @param source
	 * @param id
	 * @param day
	 */
	public CalendarChangeEvent(Object source, int id, int eventType, int day) {
		super(source);
		this.eventType = eventType;
		this.day = day;
	}

	/**
	 * 
	 * @return
	 */
	public int getDay() {
		return day;
	}
	
	public int getEventType() {
		return eventType;
	}
}
