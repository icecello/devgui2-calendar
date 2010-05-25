package gdcalendar.gui.calendar.daycard;

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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
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


/**
 * @author Håkan
 * @author James
 *
 * This is the implementation of a day card that can be used in the standard
 * calendar. It's a self-contained day view panel.
 * 
 * It also handles animation by implementing the IAnimatedComponent interface.
 * Note: this comment probably needs lots of development...
 * TODO: update IDayCard interface to not be useless
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
    private GradientLabel titleLabel;          //Title label, showing the day of the month
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
        titleLabel = new GradientLabel();
        
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

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

    public Date getFilter() {
        return filter;
    }

    /**
     * set the normal color for the triangle
     * @param color
     */
    public void setTriangleColor(Color color) {
    	triangleStartColor = color;
    }
    
    /** 
     * set the color the triangle fades into
     * during animation
     * @param color
     */
    public void setTriangleFadeColor(Color color) {
    	triangleEndColor = color;
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
     * Set the second color of the daycard's title background.
     * If this color differs from the first background color
     * chosen, a gradient will be produced between them.
     * 
     * @param color
     */
    public void setTitleBackground2(Color color) {
    	titleLabel.setBackground2(color);
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
    	//AnimationDriver.getInstance().run(this, category.getName());
    }
    
    /**
     * highlight this daycard if contained events match the
     * specified category
     * 
     * @param prio    priority to match against
     */
    public void addHighlight(Priority prio) {
    	arrayPrioritiesHighlight.add(prio);
    	//AnimationDriver.getInstance().run(this);
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
    
    class GradientLabel extends JLabel {
    	private Color color1;
    	private Color color2;
    	GradientLabel() {
    		color1 = SystemColor.textText;
    		color2 = SystemColor.textText;
    	}
    	
    	@Override
    	public void setBackground(Color color) {
    		color1 = color;
    		this.repaint();
    		super.setBackground(color);
    	}
    	
    	public void setBackground2(Color color) {
    		color2 = color;
    		this.repaint();
    	}
    	
    	@Override
    	public void paintComponent(Graphics graphics) {
    		super.paintComponent(graphics);
    		
    		/*
    		 * if we have two different colors, let's
    		 * produce a nice gradient between them
    		 */
    		if (!color1.equals(color2)) {
    			Graphics2D g2d = (Graphics2D)graphics;
    			Rectangle r = getBounds();
    			Point pt1 = new Point(1,1);
    			Point pt2 = new Point((int)r.getWidth(), (int)r.getHeight());
    			GradientPaint gp = new GradientPaint(pt1, color1, pt2, color2);
    			
    			g2d.setPaint(gp);
    			g2d.fillRect(pt1.x, pt1.y, pt2.x, pt2.y);
    			g2d.setColor(this.getForeground());
    			g2d.setFont(this.getFont());
    			//get font metrics to properly place the label text
    			int height = this.getFontMetrics(this.getFont()).getHeight();
    			int width = 0;
    			int widths[] = getFontMetrics(getFont()).getWidths();
    			for (int i = 0; i < getText().length(); i++) {
    				width += widths[getText().charAt(i)];
    			}
    			/*
    			 * Draw the actual text of the label, this has to be done manually
    			 * after drawing the background, since calling super.paintComponent()
    			 * draws both background and text, which would overwrite the custom
    			 * drawing.
    			 */
    			g2d.drawString(this.getText(), (int)r.getWidth()/2 - width/2, (int)r.getHeight()/2 + height/2-1);
    			
    		}
    		
    		
    	}
    }
    
    
    /*
     * these variables are placed down here for the sole reason that it's much
     * easier to keep them close to the actual code that uses them, it makes sense
     * to have them nearby
     */
    private Color triangleStartColor = new Color(0,100,0);
    private Color triangleEndColor = new Color(0,210,0);
    private Color triangleColor = triangleStartColor;
    private float colorStep = 0.0f;
    private boolean hasMarker = true;
    private float colorInc = 0.2f;
    private boolean shouldAnimate = false;
    private boolean cleanupDone = false;
    
	@Override
	public boolean animationFinished() {
		return false;
	}

	/*
	 * Reset the state of the animation to it's starting state.
	 */
	@Override
	public void cleanup() {
		if (!cleanupDone) {
			triangleColor = triangleStartColor;
			colorStep = 0.2f;
			this.repaint();
			cleanupDone = true;
		}
	}
	
	/*
	 * perform a simple animation that smoothly changes between two colors
	 * in the triangle, any other animation is not yet supported
	 */
	@Override
	public void computeAnimatation() {
		shouldAnimate = false;
		/*
		 * Do nothing if we have no specified marker for the daycard
		 * if we have a marker:
		 * Look through the list of priorities and categories to highlight
		 * and see if this daycard has events matching these. Animation
		 * will only happen if we find a match.
		 */
		if (hasMarker) {
			for (int j = 0; j < events.size(); j++) {
				for (int i = 0; i < arrayPrioritiesHighlight.size(); i++) {
					if (arrayPrioritiesHighlight.get(i).toString().equals(events.get(j).getPriority().toString())) {
						shouldAnimate = true;
					}
				}
				
				for (int i = 0; i < arrayCategoriesHighlight.size(); i++) {
					if (arrayCategoriesHighlight.get(i).getName().equals(events.get(j).getCategory().getName())) {
						shouldAnimate = true;
					}
				}
			}
		}
		
		if (shouldAnimate) {
			cleanupDone = false;
			switch (highlightMarker) {
			
			
			case TRIANGLE_FADING:
				int r = (int) ((1 - colorStep) * triangleStartColor.getRed() + colorStep * triangleEndColor.getRed());
				int g = (int) ((1 - colorStep) * triangleStartColor.getGreen() + colorStep * triangleEndColor.getGreen());
				int b = (int) ((1 - colorStep) * triangleStartColor.getBlue() + colorStep * triangleEndColor.getBlue());
				colorStep += colorInc;
				if (colorStep >= 1) {
					colorInc = -0.2f;
				} else if (colorStep <= 0.1f) {
					colorInc = 0.2f;
				}
				triangleColor = new Color(r, g, b);
			}
		} else {
			cleanup();
		}
	}

	@Override
	public void displayAnimatation() {
		if (shouldAnimate) {
			this.repaint();
		} 
	}

	@Override
	public double preferredFPS() {
		return 5;
	}
}
