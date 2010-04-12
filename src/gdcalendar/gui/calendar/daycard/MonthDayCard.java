/**
 * 
 */
package gdcalendar.gui.calendar.daycard;






import gdcalendar.data.DayEvent;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Håkan
 *
 * This is the implementation of a day card that can be used in the standard
 * calendar if we omit the week view for now. It's quite simple to change if 
 * we want to support week view later. It's a self-contained day view panel.
 * 
 * One possibility is to switch to detailed view on clicking the indicator
 * triangle in the corner...
 * 
 */
public class MonthDayCard extends JPanel implements IDayCard {
	
	public enum CardView {
		SIMPLE,
		DETAILED
	}
	
	/*
	 * member variables
	 */
	private Calendar calendar;
	private CardView view = CardView.SIMPLE; 
	private Collection<DayEvent> dayEvents;
	
	private Polygon eventIndicatorPolygon;
	/*
	 * member components
	 */
	private JLabel titleLabel;
	
	/**
	 * default constructor
	 */
	public MonthDayCard() {
		titleLabel = new JLabel();
		titleLabel.setAlignmentY(CENTER_ALIGNMENT);
		this.add(titleLabel, BorderLayout.CENTER);
		dayEvents = new ArrayList<DayEvent>();
	}
	
	/**
	 * constructor
	 * 
	 * @param day		the day of month to display in this day card
	 * @param view	which layout to use for the drawing of the component
	 */
	public MonthDayCard(Calendar calendar, CardView view) {
		/*
                 * Replaced Date argument with a Calendar argument, same
                 * parameters and functionality.
                 */
            
                // this.date = date;
                this.calendar = calendar;
		this.view = view;
		
		// calendar = Calendar.getInstance();
		// calendar.setTime(date);
		
		titleLabel = new JLabel();
		String title = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		titleLabel.setText(title);
		titleLabel.setAlignmentY(CENTER_ALIGNMENT);
		this.add(titleLabel, BorderLayout.CENTER);
		
		dayEvents = new ArrayList<DayEvent>();
		
	}
	
	/**
	 * 
	 * @see gdcalendar.gui.calendar.daycard.IDayCard#getDate()
	 */
	@Override
	public Calendar getDate() {
		return calendar;
	}

	/**
	 * 
	 * @see gdcalendar.gui.calendar.daycard.IDayCard#setDate()
	 */
	@Override
	public void setDate(Calendar date) {
		this.calendar = date;
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
    
	/**
	 * handle drawing of the day card, delegating the different ways
	 * of drawing based on active view to private methods
	 * 
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		switch (view) {
			case SIMPLE:
				paintSimple(g);
				break;
			case DETAILED:
				paintDetailed(g);
				break;
		}
		
	}
	
	/**
	 * Delegated method that handles the simple painting of the component.
	 * This should never be called from outside the component.
	 * 
	 */
	private void paintSimple(Graphics g) {
		//only draw if there are any events for this day card
		if (dayEvents.size() > 0) {
		
			/*
			 * we may want to move these calculations into a resize event instead of paint
			 * 
			 */
			int x[] = { 
					(int) (getWidth() * 0.8),
					getWidth(),
					getWidth()
			};
			int y[] = {
					getHeight(),
					(int) (getHeight() * 0.8),
					getHeight()
			};
			
			
			Polygon triangle = new Polygon(x, y, 3);
		
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(new Color(30,30,220));
			g2.fillPolygon(triangle);
			g2.setColor(new Color(30,30,100));
			g2.setStroke(new BasicStroke(3));
			g2.drawPolygon(triangle);
			
			//draw string showing how many events are available for this day
			//very crude, needs refinement
			//probably easier to switch to a label for this...
			String str = "Events: " + Integer.toString(dayEvents.size());
			int stringY = Math.max(getHeight() - g2.getFontMetrics().getHeight(), getHeight() - 10);
			g2.drawString(str, 10, stringY);
		}
		
	}
	
	/**
	 * Delegated method that handles the detailed painting of this day card.
	 * This should never be called from outside the component.
	 * 
	 */
	private void paintDetailed(Graphics g) {
		
	}
	
	/**
	 * Add a new event to display in this day card.
	 * This method is temporary and should probably be removed in favor of
	 * using a CalendarModel to provide event data.
	 * 
	 * @param newEvent		a new event
	 * @return true if the event was added successfully
	 */
	@Deprecated
	public boolean addEvent(DayEvent newEvent) {
        return dayEvents.add(newEvent);
    }
}
