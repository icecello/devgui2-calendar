package gdcalendar.gui.calendar.daycard;

import gdcalendar.gui.calendar.Event;

import java.awt.Image;
import java.util.Collection;
import java.util.Date;
import javax.swing.JPanel;

/**
 * a proposition of an dayCard interface...
 * Please comment on this.
 * 
 * Update from H�kan: have a look at IDayCard2, this is a combination of IDayCard from SVN
 * and the one I had done in parallel
 * 
 * @author Tomas
 */
public interface IDayCard {

    public void setId(int id);
    public Date getId();

    public JPanel getSimpleView();
    public JPanel getNormalView();
    public JPanel getDetailedView();

    public boolean addEvent(Event newEvent);
    public void removeEvent(Event event);
    public Collection<Event> getEvents();

    public void setImage(Image image);
    public Image getImage();
    public boolean  displayImage();
    public void hideImage();
}
