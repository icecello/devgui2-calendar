/**
 * 
 */
package gdcalendar.gui.calendar.daycard;

import gdcalendar.logic.AnimationDriver;
import gdcalendar.logic.IAnimatedComponent;
import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.model.Category;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.DayEvent.Priority;
import gdcalendar.mvc.view.AbstractViewPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.SystemColor;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * @author Håkan
 * @author James
 *
 * This is the implementation of a day card that can be used in the standard
 * calendar if we omit the week view for now. It's quite simple to change if 
 * we want to support week view later. It's a self-contained day view panel.
 * 
 * One possibility is to switch to detailed view on clicking the indicator
 * triangle in the corner...
 * 
 * Update 21/4-10: This comment is outdated, the class has changed quite a bit
 * since it was made... 
 * 
 */
@SuppressWarnings("serial")
public class MonthDayCard extends AbstractViewPanel implements IDayCard, IAnimatedComponent {

    /**
     * A collection of constant used for defining how a MonthDayCard should
     * visually represent it's data
     */
    public static enum CardView {

        /**
         * The contained data should not be shown at all
         */
        NONE,
        /**
         * The contained data should be shown in a simplistic way. Only textual
         * information will be shown
         */
        SIMPLE,
        /**
         * The contained data should be shown in a sophisticated way. Graphical components,
         * such as images texts will be shown
         */
        DETAILED
    }
    
    public static enum Marker {
    	NONE, TRIANGLE_FADING, SPARKLE /* this one doesn't yet exist */
    }
    /*
     * member variables
     */
    private Calendar calendar = Calendar.getInstance();
    private Date filter;
    private CardView view = CardView.SIMPLE;
    private JPanel simpleView = new JPanel();   //The simple visual representation of the day
    private JPanel detailedView = new JPanel(); //The advanced visual representation of the day
    private String newEventName;        //temporary storage for a new event name
    private JLabel titleLabel;          //Title label, showing the day of the month
    private CalendarController controller;   //The controller, responsible for updating the
    //connected models
    private ArrayList<JLabel> eventLabels;  //The visual representation of the day events
    private ArrayList<DayEvent> events;     //The events of the day
    private ArrayList<MouseListener> eventMouseListeners = new ArrayList<MouseListener>();
    
    private Color eventForeground = SystemColor.textText;
    
    private Marker highlightMarker = Marker.NONE;
    /**
     * internal method to handle basic setup of the daycard
     */
    private void Init() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel();
        
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        /*
         * setup colors for the calendar based on system colors, so
         * the user's choices are respected
         */
        //titleLabel.setBackground(SystemColor.window);
        titleLabel.setOpaque(true);
        //this.setBackground(SystemColor.text);
        simpleView.setBackground(SystemColor.text);
        detailedView.setBackground(SystemColor.text);
        
        eventLabels = new ArrayList<JLabel>();
        events = new ArrayList<DayEvent>();
        add(titleLabel, BorderLayout.PAGE_START);
    }

    /**
     * Create an empty MonthDayCard. It's visual apperence is decided by
     * the view.
     *
     * @param view	which layout to use for the drawing of the component
     */
    public MonthDayCard(CardView view, CalendarController controller) {
        //Call Init() to handle basic setup
    	//just changed name to make more sense
        Init();
        
        this.view = view;

        simpleView.setLayout(new BoxLayout(simpleView, BoxLayout.PAGE_AXIS));
        detailedView.setLayout(new BoxLayout(detailedView, BoxLayout.PAGE_AXIS));
        //Make the monthcard show the correct view
        if (view == CardView.SIMPLE) {
            add(simpleView, BorderLayout.CENTER);
        } else {
            add(detailedView, BorderLayout.CENTER);
        }
    }

    /**
     * Create a MonthCard showing a visual representation of a given day. The day
     * is connected to a contoller, which is responsible to updating connected models
     *
     * @param day   The day to visually represent
     * @param view  The visual representation of the day depend on which view
     *              that is connected to it
     * @param controller    The controller which is responsible for connecting the
     *                      MonthCardDay to corresponing models
     */
    private  MonthDayCard(Date filter, CardView view, CalendarController controller) {
        /*
         * Call MonthDayCard with current date and the given view
         */
    	this(view, controller);
    	Init();
    	
        //Make the necessary adjustments, so that the monthcard reflectes
        //the data in the given day
        this.controller = controller;
        calendar.setTime(filter);


        titleLabel.setText("" + calendar.get(Calendar.DAY_OF_MONTH));

        if (view == CardView.NONE) {
            titleLabel.setVisible(false);
        }
        controller.setFilter(filter);
    }

    /**
     * set the current color of the indicator triangle in the
     * top right
     * @param color
     */
    public void setTriangleColor(Color color) {
    	triangleColor = color;
    }
    
    /**
     * set the font for the title of this daycard
     * 
     * @param font
     */
    public void setTitleFont(Font font) {
    	titleLabel.setFont(font);
    }
    
    /**
     * Set the color to be used for the daycards title text
     * 
     * @param color		color to use
     */
    public void setTitleForeground(Color color) {
    	titleLabel.setForeground(color);
    }
    
    /**
     * Set the color to use for the background of the daycards
     * title
     * 
     * @param color
     */
    public void setTitleBackground(Color color) {
    	titleLabel.setBackground(color);
    }
    
    /**
     * Set the background color of the daycard
     * 
     * @param color		which color to use for background
     */
    public void setBackground(Color color) {
    	super.setBackground(color);
    }
    
    /**
     * Set the color of text for events that are displayed in 
     * the daycard
     * 
     * @param color
     */
    public void setEventForeground(Color color) {
    	eventForeground = color;
    	
    	for (int i = 0; i < eventLabels.size(); i++) {
    		eventLabels.get(i).setForeground(color);
    	}
    }
    
    /**
     * Set which marker to use during highlighting of
     * priorities or categories.
     * There is no support for using different markers
     * for categories and priorites as of yet.
     * 
     * @param marker	which marker to use
     */
    public void setMarker(Marker marker) {
    	highlightMarker = marker;
    }
    
    
    
    
    private ArrayList<Priority> arrayPrioritiesHighlight = new ArrayList<Priority>();
    private ArrayList<Category> arrayCategoriesHighlight = new ArrayList<Category>();
    /**
     * highlight this daycard if contained events match the
     * specified category
     * 
     * @param category	name of category to match against
     */
    public void addHighlight(Category category) {
    	arrayCategoriesHighlight.add(category);
    	AnimationDriver.getInstance().run(this, category.getName());
    }
    
    /**
     * highlight this daycard if contained events match the
     * specified category
     * 
     * @param prio    priority to match against
     */
    public void addHighlight(Priority prio) {
    	arrayPrioritiesHighlight.add(prio);
    	AnimationDriver.getInstance().run(this, prio.toString());
    }
    
    public void removeHighlight(Category category) {
    	arrayCategoriesHighlight.remove(category);
    }
    
    public void removeHighlight(Priority prio) {
    	arrayPrioritiesHighlight.remove(prio);
    }
    
    /**
     * Get the currenty active view of this day card.
     * This is one of:
     * - NONE
     * - SIMPLE
     * - DETAILED
     * 
     * @return	currently active view
     */
    public CardView getView() {
        return view;
    }

    /**
     * Change the visual apperence of the MonthDayCard. See
     * CardView for more details
     * @see gdcalendar.gui.calendar.daycard.MonthDayCard#view
     * @param newView the new view to show
     */
    public void changeView(CardView newView) {
        this.view = newView;
        remove(simpleView);
        remove(detailedView);
        if (view == CardView.SIMPLE) {
            add(simpleView, BorderLayout.CENTER);
            titleLabel.setVisible(true);
        } else if (view == CardView.DETAILED) {
            add(detailedView, BorderLayout.CENTER);
            titleLabel.setVisible(true);
        } else {
            titleLabel.setVisible(false);
        }
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
     */
    @Override
    protected void paintChildren(Graphics g) {

        super.paintChildren(g);
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
        if (eventLabels.size() > 0) {
            /*
             * we may want to move these calculations into a resize event instead of paint
             */
        	int triangleX[] = { (getWidth() - (titleLabel.getHeight()*2)), getWidth(), getWidth() };
            int triangleY[] = {0, titleLabel.getHeight(), 0 };


            Polygon triangle = new Polygon(triangleX, triangleY, 3);

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(triangleColor);
            g2.fillPolygon(triangle);
            g2.setColor(new Color(30, 30, 100));
            g2.setStroke(new BasicStroke(1));
            g2.drawPolygon(triangle);

            //draw string showing how many events are available for this day
            //very crude, needs refinement
            //probably easier to switch to a label for this...
            String str = "Events: " + Integer.toString(eventLabels.size());

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
        //draw on glass pane
    }

    /**
     * @see gdcalendar.mvc.view.AbstractViewPanel#modelPropertyChange(java.beans.PropertyChangeEvent)
     * @param evt the event responsible for the call
     */
    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {
        String evtName = evt.getPropertyName();
        if (evt.getNewValue() != null) {
            newEventName = evt.getNewValue().toString();
        }

        //Add an event label to the DayCard, invoked in EDT
        if (evtName.equals(CalendarController.ADD_EVENT)|| evtName.equals(CalendarController.REMOVE_EVENT)) {
            simpleView.removeAll();
            eventLabels.clear();
            events.clear();
            DayEvent[] filteredEvents = (DayEvent[]) evt.getNewValue();
            for (int i = 0; i < filteredEvents.length; i++) {
                events.add(filteredEvents[i]);
                JLabel eventLabel = new JLabel(filteredEvents[i].getEventName());
                eventLabel.setName(""+filteredEvents[i].getID());
                eventLabel.setForeground(eventForeground);
                //add all event mouse listeners to the event label
                for (int j = 0; j < eventMouseListeners.size(); j++) {
                	eventLabel.addMouseListener(eventMouseListeners.get(j));
                }
                simpleView.add(eventLabel);
                eventLabels.add(eventLabel);
                
            }

            //Remove an event (the one at the bottom) from the DayCard, invoked in EDT
        } else if (evtName.equals(CalendarController.REMOVE_EVENT)) {
            simpleView.remove(eventLabels.get(eventLabels.size() - 1));
            eventLabels.remove(eventLabels.size() - 1);
            events.remove(events.get(events.size() - 1));

        } else if (evtName.equals(CalendarController.FILTER)) {
            filter = (Date) evt.getNewValue();
            calendar.setTime(filter);
            titleLabel.setText("" + calendar.get(Calendar.DAY_OF_MONTH));
            checkAndHighlightHoliday();
            
        } else if (evtName.equals(CalendarController.FILTERED_EVENTS)) {

            simpleView.removeAll();
            eventLabels.clear();
            events.clear();
            DayEvent[] filteredEvents = (DayEvent[]) evt.getNewValue();
            for (int i = 0; i < filteredEvents.length; i++) {
                events.add(filteredEvents[i]);
                JLabel eventLabel = new JLabel(filteredEvents[i].getEventName());
                eventLabel.setName(""+filteredEvents[i].getID());

                eventLabel.setForeground(eventForeground);
                //add all event mouse listeners to the event label
                for (int j = 0; j < eventMouseListeners.size(); j++) {
                	eventLabel.addMouseListener(eventMouseListeners.get(j));
                }
                simpleView.add(eventLabel);
                eventLabels.add(eventLabel);
            }

        }
        revalidate();
        repaint();


    }

    private void checkAndHighlightHoliday() {
    	
    	if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        	titleLabel.setForeground(SystemColor.red);
        } else {
        	titleLabel.setForeground(SystemColor.textText);
        }
    }
    
    /**
     * Add a MouseListener that will be fired whenever
     * a mouse event occurs on events.
     *     
     * @param l
     */
    public void addEventMouseListener(MouseListener l) {
    	for (int i = 0; i < eventLabels.size(); i++) {
    		eventLabels.get(i).addMouseListener(l);
    	}
    	
    	eventMouseListeners.add(l);
    }
    
    /*
     * these variables are placed down here for the sole reason that it's much
     * easier to keep them close to the actual code that uses them, it makes sense
     * to have them nearby
     */
    private Color startColor = new Color(0,100,0);
    private Color endColor = new Color(0,210,0);
    private Color triangleColor = startColor;
    private float step = 0.0f;
    private boolean isAnimationFinished = false;
    private boolean hasMarker = true;
    private float inc = 0.1f;
    
	@Override
	public boolean animationFinished() {
		//cancel the animation if it has finished or if this daycard has no marker
		//to animate
		isAnimationFinished = true;
		
		if (hasMarker) {
			for (int j = 0; j < events.size(); j++) {
				System.out.println("inside event loop");
				for (int i = 0; i < arrayPrioritiesHighlight.size(); i++) {
					if (arrayPrioritiesHighlight.get(i).toString().equals(events.get(j).getPriority().toString())) {
						isAnimationFinished = false;
					}		
				}
				
				for (int i = 0; i < arrayCategoriesHighlight.size(); i++) {
					if (arrayCategoriesHighlight.get(i).equals(events.get(j).getCategory())) {
						isAnimationFinished = false;
					}
				}
			}
		}
		System.out.println("finished=" + isAnimationFinished);
		return isAnimationFinished;
	}

	/*
	 * perform a simple animation that smoothly changes between two colors
	 * in the triangle, any other animation is not yet supported
	 */
	@Override
	public void computeAnimatation() {
		
		switch (highlightMarker) {
		
		case TRIANGLE_FADING:
			int r = (int) ((1 - step) * startColor.getRed() + step * endColor.getRed());
			int g = (int) ((1 - step) * startColor.getGreen() + step * endColor.getGreen());
			int b = (int) ((1 - step) * startColor.getBlue() + step * endColor.getBlue());
			step += inc;
			if (step >= 1) {
				inc = -0.1f;
			} else if (step <= 0.1f) {
				inc = 0.1f;
			}
			triangleColor = new Color(r, g, b);
			System.out.println("fading");
		}
	}

	@Override
	public void displayAnimatation() {
		this.repaint();
	}

	@Override
	public int preferredFPS() {
		return 10;
	}
}
