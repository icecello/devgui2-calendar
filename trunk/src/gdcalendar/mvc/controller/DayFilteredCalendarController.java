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
 * @author Håkan
 * @author Tomas
 * @author James
 * 
 */
public class DayFilteredCalendarController extends AbstractController
{

    //  Properties this controller expects to be stored in one or more registered models
    public static final String FILTER= "setDayFilter";

    /**
     * Set a filter for all the connected models
     * @param filter The new filter
     */
    public void setFilter(Date filter){
        setModelProperty(FILTER, filter);
    }


}
