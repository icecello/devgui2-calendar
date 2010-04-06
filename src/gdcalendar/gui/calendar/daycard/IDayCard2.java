package gdcalendar.gui.calendar.daycard;

import gdcalendar.logic.Event;

import java.awt.Image;
import java.util.Collection;
import java.util.Date;

/**
 * @author Håkan
 * @author Tomas
 * 
 * Abstract interface for day cards.
 * 
 * Håkan:
 * Note that I had already done an IDayCard during essentially the same time,
 * I noticed while trying to commit that Tomas has committed one earlier.
 * This is a combination of both these versions. 
 * See the MonthDayCard for an implementation which works as a self-contained
 * day card component that is supposed to draw and display all relevant data
 * that we want for the day cards.
 * 
 * The event collections are a nice touch, I'm not sure if each day card will receive
 * it's own collection or if the intention is for a more central store and using
 * the model-view paradigm. I'd vouch for the latter...
 * 
 * 
 * We should discuss how to proceed with this at some point.
 */
public interface IDayCard2 {
	
	/**
	 * Set which day of month this day card is associated with,
	 * as a full date to make it aware of year and month.
	 * 
	 * @param date		date that this day card should display
	 */
	public void setDate(Date date);
	
	/**
	 * Get which date this day card is associated with.
	 * 
	 * @return date associated with this day card, integer
	 */
	public Date getDate();
	
	/*
	 * TODO: add comments to these methods
	 */
	public boolean addEvent(Event newEvent);
    public boolean removeEvent(Event event);
    public Collection<Event> getEvents();

    public void setImage(Image image);
    public Image getImage();
    public boolean  displayImage();
    public void hideImage();
	
}