package gdcalendar.gui.calendar.daycard;


import java.util.Calendar;

/**
 * Note that I had already done an IDayCard during essentially the same time,
 * I noticed while trying to commit that Tomas had committed one earlier.
 * This is a combination of both these versions. 
 * See the MonthDayCard for an implementation which works as a self-contained
 * day card component that is supposed to draw and display all relevant data
 * that we want for the day cards.
 * 
 * The event collections are a nice touch. I'm not sure if each day card will receive
 * it's own collection or if the intention is for a more central store and using
 * the model-view paradigm however. I'd vouch for the latter...
 * 
 * 
 * We should discuss how to proceed with this at some point.
 */
public interface IDayCard {
	
	/**
	 * Set which day of month this day card is associated with,
	 * as a full date to make it aware of year and month.
	 * 
	 * @param date		date that this day card should display
	 */
	public void setDate(Calendar date);
	
	/**
	 * Get which date this day card is associated with.
	 * 
	 * @return date associated with this day card, integer
	 */
	public Calendar getDate();
	

    public boolean  displayImage();
    public void hideImage();
	
}
