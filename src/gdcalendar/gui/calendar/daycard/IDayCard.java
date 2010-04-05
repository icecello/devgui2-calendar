package gdcalendar.gui.calendar.daycard;

import gdcalendar.logic.Event;
import java.awt.Image;
import java.util.Collection;
import java.util.Date;
import javax.swing.JPanel;

/**
 * a proposition of an dayCard interface...
 * Please comment on this.
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
