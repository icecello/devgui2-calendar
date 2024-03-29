package gdcalendar.mvc.model;

import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.controller.DayFilteredCalendarController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.UUID;

/**
 * This is a filtered model that only reflects a sub set of the data contained in
 * a CalendarModel. All data passes through a Day filter, and only events taking place
 * during the day specified in the filter is visisble to the user.
 * 
 * @author Tomas
 */
public class DayFilteredCalendarModel extends AbstractModel {

    //The backbone model
    private CalendarModel realModel;
    private Date filter;
    //Responsible for sending notifications to the controller, when
    //when the model has changed
    protected transient PropertyChangeSupport propertyChange;
    //PropertyChangeLister that is connected to the backbone model.
    //This listner notifies the filteredModel when the backbone model has changed
    //The filtered model will then take appropriate actions, and thus the illusion of
    // the controller will only see the filtered model sending notfications
    private PropertyChangeListener pcl;

    public DayFilteredCalendarModel() {
    }

    /**
     * Attach a new filter to the model. Only DayEvents fullfilling the
     * filters' preferences will be shown
     * @param filter the day filter. Only DayEvents taking place this day will be shown
     */
    public void setDayFilter(Date filter) {
        Date oldVaule = this.filter;
        this.filter = filter;
        //Notify the connected controllers that the filter has been updated, and
        //also send the events matching the new filter
        firePropertyChange(DayFilteredCalendarController.FILTER, oldVaule, filter);
        firePropertyChange(CalendarController.FILTERED_EVENTS, null, getFilteredEvents());

    }

    /**
     * Get the filter connected to the model
     * @return the filter
     */
    public Date getFilter() {
        return filter;
    }

    /**
     * Attach a backbone model to the filteredCalendarModel. Changes to the filtered Model
     * and vice versa will be reflected to the corresponing controllers
     * @param realModel the backbone model
     */
    public void setRealCalendarModel(final CalendarModel realModel) {
        final CalendarModel oldValue = this.realModel;
        // if we used to have a value, remove the
        //   old property change listener
        if (oldValue != null) {
            oldValue.removePropertyChangeListener(pcl);
        }
        this.realModel = realModel;

        // set up listener to delegate events
        if (pcl == null) {
            //If the backbone model has been updated and the changes effect
            //the filtered model, the filtered model should send a notification
            //to the connected controllers
            pcl = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {

                    boolean updated = false;
                    DayEvent updatedValue = (DayEvent) evt.getNewValue();
                    if (evt.getPropertyName().equals(CalendarModel.EVENT_REPLACED)) {
                        DayEvent oldValue = (DayEvent) evt.getOldValue();
                        if (oldValue != null) {
                            if (oldValue.isActiveDuringDay(filter) || updatedValue.isActiveDuringDay(filter));
                            updated = true;
                        }
                    } else {
                        if (updatedValue != null) {
                            if (updatedValue.isActiveDuringDay(filter)) {
                                updated = true;
                            }
                        }
                    }

                    //If the filtered data has changed, notify the connected controllers
                    if (updated) {
                        if (evt.getPropertyName().equals(CalendarModel.EVENT_REMOVED)) {
                            firePropertyChange(CalendarController.REMOVE_EVENT, null,
                                    getFilteredEvents());
                        } else if (evt.getPropertyName().equals(CalendarModel.EVENT_ADDED)) {
                            firePropertyChange(CalendarController.ADD_EVENT, null,
                                    getFilteredEvents());
                        } else if (evt.getPropertyName().equals(CalendarModel.EVENT_REPLACED)) {
                            firePropertyChange(CalendarController.FILTERED_EVENTS, null,
                                    getFilteredEvents());
                        }

                    }
                }
            };
        }
        realModel.addPropertyChangeListener(pcl);
    }

    /**
     * Retrieve all DayEvents passing through the filter
     * @return the complete set of DayEvents passing through the filter
     */
    public DayEvent[] getFilteredEvents() {
        DayEvent[] data = realModel.getEvents();
        //If no filter, just return the data
        if (filter == null) {
            return data;
        }

        //Get the DayEvents that fullfills the filter
        int filteredSize = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].isActiveDuringDay(filter)) {
                filteredSize++;
            }
        }
        DayEvent[] filteredData = new DayEvent[filteredSize];
        for (int i = 0, j = 0; i < data.length; i++) {
            if (data[i].isActiveDuringDay(filter)) {
                filteredData[j++] = data[i];
            }
        }
        return filteredData;
    }
}
