package gdcalendar.gui.calendar.daycard;


/**
 * Note that I had already done an IDayCard during essentially the same time,
 * I noticed while trying to commit that Tomas had committed one earlier.
 * This is a combination of both these versions. 
 * See the MonthDayCard for an implementation which works as a self-contained
 * day card component that is supposed to draw and display all relevant data
 * that we want for the day cards.
 * 
 * 
 * 
 * We should discuss how to proceed with this at some point.
 */
public interface IDayCard {
	
    public boolean  displayImage();
    public void hideImage();
	
}
