package gdcalendar.gui.calendar;

import java.util.Calendar;

/**
 * The purpose of this new interface is to act as an abstract model that is used
 * by calendar components or similar that require events specified by dates rather
 * than being indexed by integers.
 * 
 * It still requires a bunch of work, the current look of it hardly justifies
 * a separate interface.
 * @author Håkan
 *
 */
public interface CalendarModel {
	
	
	
	public Object getValueAt(Calendar date);
	

}
