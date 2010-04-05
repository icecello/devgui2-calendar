package gdcalendar.gui.calendar.daycard;

import gdcalendar.logic.Event;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A simple day card, used as a mock up
 * @author Tomas
 */
public class MockUpDayCard implements IDayCard{

    private JPanel simpleView = new JPanel(new BorderLayout());
    private JPanel normalView;
    private JPanel detailedView;
    private Collection<Event> dayEvents;
    private int dayCardId;

    public MockUpDayCard(Collection<Event> dayEvents, int dayCardId){
        this.dayEvents = dayEvents;
        this.dayCardId = dayCardId;
        simpleView.add(new JLabel(""+dayCardId), BorderLayout.CENTER);
    }
    public void setId(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JPanel getSimpleView() {
        return simpleView;
    }

    public JPanel getNormalView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JPanel getDetailedView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addEvent(Event newEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeEvent(Event event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Event> getEvents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setImage(Image image) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Image getImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean displayImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void hideImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
