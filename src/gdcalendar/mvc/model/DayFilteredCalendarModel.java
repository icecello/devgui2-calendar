package gdcalendar.mvc.model;

import gdcalendar.gui.calendar.CalendarContainer;
import gdcalendar.mvc.controller.CalendarController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
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

    private CalendarModel realModel;
    private Date filter;
    protected transient PropertyChangeSupport propertyChange;
    private PropertyChangeListener pcl;

    public DayFilteredCalendarModel() {
    }

    /**
     * Add a new event to the specified date.
     *
     * @param newDay		a new event
     */
    public void addDayEvent(DayEvent event) {
        firePropertyChange(CalendarController.ADD_EVENT, null, event);
        realModel.addDayEvent(event);
    }

    /**
     * Remove specified event from a certain day.
     *
     * @param date		which date to remove the event from
     * @param day		which event to remove
     * @return 		boolean, if list contained this event
     */
    public DayEvent removeDayEvent(UUID eventID) {
        firePropertyChange(CalendarController.REMOVE_EVENT, realModel.getDayEvent(eventID), null);
        return realModel.removeDayEvent(eventID);
    }

    /**
     * @param i		indexed DayEvent to get from this model
     * @return		the DayEvent found at position i
     */
    public DayEvent getDayEvent(int index) {
        return getEvents()[index];
    }

    /**
     * Attach a new filter to the model. Only DayEvents fullfilling the
     * filters' preferences will be shown
     * @param filter the day filter. Only DayEvents taking place this day will be shown
     */
    public void setDayFilter(Date filter) {
        Date oldVaule = this.filter;
        this.filter = filter;
        firePropertyChange(CalendarController.FILTER, oldVaule, filter);
        firePropertyChange(CalendarController.FILTERED_EVENTS, null, getEvents());
    }

    public Date getFilter() {
        return filter;
    }

    public void setRealCalendarModel(final CalendarModel realModel) {
        final CalendarModel oldValue = this.realModel;
        // if we used to have a value, remove the
        //   old property change listener
        if (oldValue != null) {
            oldValue.removePropertyChangeListener(pcl);
        }

        // set the property value
        this.realModel = realModel;

        // set up listener to delegate events
        if (pcl == null) {
            pcl = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent e) {
                    boolean updated = false;
                    DayEvent[] oldData = oldValue.getEvents();
                    DayEvent[] newData = realModel.getEvents();
                    //Filter the DayEvents that fullfills the filter
                    Collection<DayEvent> filteredData = new ArrayList<DayEvent>();
                    for (int i = 0; i < oldData.length; i++) {
                        if (oldData[i].isActiveDuringDay(filter)) {
                            filteredData.add(oldData[i]);
                        }
                    }
                    newData = getEvents();
                    if (newData.length != oldData.length) {
                        updated = true;
                    } else {
                        for (int i = 0; i < oldData.length; i++) {
                            if (!oldData[i].equals(newData[i])) {
                                updated = true;
                                break;
                            }
                        }
                    }

                    if (updated) {
                        firePropertyChange(CalendarController.FILTERED_EVENTS, null,
                                getEvents());
                    }
                }
            };
        }

        if (!realModel.propertyChangeSupport.hasListeners(null)) {
            realModel.addPropertyChangeListener(pcl);
        }
        firePropertyChange(CalendarController.FILTERED_EVENTS, null, getEvents());
    }

    /**
     * Retrieve all DayEvents passing through the filter
     * @return the complete set of DayEvents passing through the filter
     */
    public DayEvent[] getEvents() {
        DayEvent[] data = realModel.getEvents();
        //If no filter, just return the data
        if (filter == null) {
            return data;
        }


        //Filter the DayEvents that fullfills the filter
        Collection<DayEvent> filteredData = new ArrayList<DayEvent>();
        for (int i = 0; i < data.length; i++) {
            if (data[i].isActiveDuringDay(filter)) {
                filteredData.add(data[i]);
            }
        }

        return (DayEvent[]) filteredData.toArray();
    }
}
