package gdcalendar.mvc.controller;

import gdcalendar.mvc.model.DayEvent;
import java.util.Date;
import java.util.UUID;


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
    
    public static final String VIEW_DAY = "viewDayEvent";
    public static final String ADD_EVENT = "addDayEvent";
    public static final String REMOVE_EVENT = "removeDayEvent";
    public static final String FILTERED_EVENTS = "getFilteredEvents";
    
    
    /**
     * View a particular day
     * @param day The Date of the day to view
     */
    public void viewDayEvent(Date day) {
        setModelProperty(VIEW_DAY, day);
    }

    /**
     * Add a DayEvent to all models connected to the controller
     * @param event The DayEvent to add<
     */
    public void addDayEvent(DayEvent event) {
        setModelProperty(ADD_EVENT, event);
    }
    
    /**
     * Remove the DayEvent uniquely specified by the eventID from the
     * connected models
     * @param eventID The ID that uniquely determines a DayEvent
     */
    public void removeDayEvent(UUID eventID) {
        setModelProperty(REMOVE_EVENT, eventID);
    }
}
