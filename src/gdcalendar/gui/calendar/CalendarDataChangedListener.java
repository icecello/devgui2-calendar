package gdcalendar.gui.calendar;

import java.util.EventListener;

/**
 * @author Håkan
 *
 */
public interface CalendarDataChangedListener extends EventListener {

	/**
	 * Override this method to do something appropriate whenever a change
	 * in the calendar's data has occurred.
	 * 
	 * @param e		CalendarEvent object with information for the change
	 * 				that has taken place
	 */
	public void dataChanged(CalendarChangeEvent e);
}
