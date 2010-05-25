package gdcalendar.gui.calendar.daycard;


/**
 * See the MonthDayCard for an implementation which works as a self-contained
 * day card component that is supposed to draw and display all relevant data
 * that we want for the day cards.
 * 
 * TODO: make this interface NOT useless... that would be a good thing...
 */
public interface IDayCard {
	
    public boolean  displayImage();
    public void hideImage();
	
}
