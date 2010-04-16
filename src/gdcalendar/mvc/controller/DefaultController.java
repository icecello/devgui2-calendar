package gdcalendar.mvc.controller;

import gdcalendar.mvc.model.DayEvent;


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
public class DefaultController extends AbstractController
{

    //  Properties this controller expects to be stored in one or more registered models
    
    
    public static final String ADD_EVENT_PROPERTY = "addEvent";
    public static final String REMOVE_EVENT_PROPERTY = "removeEvent";
    
    
    /**
     * 
     * @param 
     */
    public void addEvent(DayEvent event) {
        setModelProperty(ADD_EVENT_PROPERTY, event);
    }
    
    /**
     * 
     * @param 
     */
    public void removeEvent(DayEvent event) {
        setModelProperty(REMOVE_EVENT_PROPERTY, event);
    }
    
}
