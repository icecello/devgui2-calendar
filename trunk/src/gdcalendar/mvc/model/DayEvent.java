package gdcalendar.mvc.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * A DayEvent is an event taking place during a specified time slot. The DayEvent
 * contains information such as name, start and end time, priority and is associated
 * with a certain category.
 *
 * @author Tomas
 */
public class DayEvent extends AbstractModel{

    /**
     * Priority for a given DayEvent
     */
    public static enum Priority{
        VERY_LOW("Very Low"), LOW("Low"), MEDIUM("Medium"),
        HIGH("High"), VERY_HIGH("Very high");

        private final String name;
        private Priority(String name) {
            this.name = name;
        }
    }

    private String eventName = "No_Name";
    private Date startTime, endTime;

    //Default values for category and priority
    private String category = "None";
    private Priority priority = Priority.LOW;
    private UUID id;

    public DayEvent() {
        id = UUID.randomUUID();
        startTime = new Date();
        endTime = new Date();
    }

    public DayEvent(String eventName) {
        this();
        this.eventName = eventName;
    }

    public DayEvent(String eventName, Date startTime, Date endTime) {
        if (startTime.compareTo(endTime) > 0) {
            throw new IllegalArgumentException("Event start time must be <= end time");
        }
        id = UUID.randomUUID();
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayEvent(String eventName, Date startTime, Date endTime, String category, Priority priority) {
        this(eventName,startTime,endTime);
        this.category = category;
        this.priority = priority;
    }



    /**
     * Get the time when the event ends
     * @return the ending time of the event
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Determine if the DayEvent is active during a certain time stamp
     * @param when the time stamp
     * @return true if active during the time stamp, false otherwise
     */
    public boolean isActiveDuringTimeStamp(Date when) {
        if (when.before(startTime) || when.after(endTime)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Determine if the event is active during a given day
     * @param day the day
     * @return true if the event is active during that day, false otherwise
     */
    public boolean isActiveDuringDay(Date day){
        Date startDay, endDay;
        Calendar cal = new GregorianCalendar();
        //Set the startDay to the start of the day when the
        //event is active
        cal.setTime(startTime);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE, 0);
        startDay = cal.getTime();
        //Set the endDay to the end of the day pointed by endTime
        cal.setTime(endTime);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE, 59);
        endDay = cal.getTime();

       return startDay.before(day) && endDay.after(day);
    }


    /**
     * Set the time when the event should end
     * @param endTime The time when the event should end
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Get the name of the event
     * @return The name of the event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Set a new name for the event
     * @param eventName The new name of the event
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Get the time when the event starts
     * @return the starting time of the event
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Set the time when the event should start
     * @param startTime The time when the event should start
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public UUID getID(){
        return id;
    }



    @Override
    public String toString() {
        return id + "\n"+ startTime + "-" + endTime + " " + eventName;
    }



    public static void main(String[] args) {
        Calendar cal = new GregorianCalendar(2010, 5, 13, 13, 00);
        Date d1 = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 15);
        Date d2 = cal.getTime();
        DayEvent e1 = new DayEvent("event",d1,d2);
        DayEvent e2 = new DayEvent("event",d1,d2);

        cal.set(Calendar.DAY_OF_MONTH, 13);
        d1 = cal.getTime();
        System.out.println(e1.isActiveDuringDay(d1));
        System.out.println("First dayEvent: " + e1);
        System.out.println("Second dayEvent: " + e2);
    }
}
