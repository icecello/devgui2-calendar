package gdcalendar.mvc.controller;

import gdcalendar.mvc.model.DayEvent;
import java.util.Date;


/**
 * This controller implements the required methods and provides the properties
 * necessary to work with the DisplayViewPanel and PropertyViewPanel views. Each of
 * methods in this class can be called upon by the views to update to state of the
 * registered models.
 *
 * @author H�kan
 * @author Tomas
 * @author James
 * 
 */
public class CalendarController extends AbstractController
{

    //  Properties this controller expects to be stored in one or more registered models
    
    
    public static final String ADD_EVENT = "addDayEvent";
    public static final String REMOVE_EVENT = "removeDayEvent";
    public static final String FILTERED_EVENTS = "getFilteredEvents";
    public static final String FILTER= "setDayFilter";
    
    
    /**
     * 
     * @param event The event to add
     */
    public void addDayEvent(DayEvent event) {
        setModelProperty(ADD_EVENT, event);
    }
    
    /**
     * 
     * @param event The event to remove
     */
    public void removeEvent(DayEvent event) {
        setModelProperty(REMOVE_EVENT, event);
    }

    public void setFilter(Date filter){
        setModelProperty(FILTER, filter);
    }

    
}
