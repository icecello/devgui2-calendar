package gdcalendar.data;

/**
 * A DayEvent is an event taking place during a specified time slot. The DayEvent
 * contains information such as name, start and end time, etc.
 *
 * @author Tomas
 */
public class DayEvent {

    private String eventName;
    private TimeStamp startTime, endTime;

    public DayEvent() {
        eventName = "No name";
        startTime = new TimeStamp();
        endTime = new TimeStamp();
    }

    public DayEvent(String eventName) {
        this();
        this.eventName = eventName;
    }

    public DayEvent(String eventName, TimeStamp startTime, TimeStamp endTime) {
        if (startTime.compareTo(endTime) > 0) {
            throw new IllegalArgumentException("Event start time must be <= end time");
        }
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Get the time when the event ends
     * @return the ending time of the event
     */
    public TimeStamp getEndTime() {
        return endTime;
    }

    /**
     * Set the time when the event should end
     * @param The time when the event should end
     */
    public void setEndTime(TimeStamp endTime) {
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
    public TimeStamp getStartTime() {
        return startTime;
    }

    /**
     * Set the time when the event should start
     * @param The time when the event should start
     */
    public void setStartTime(TimeStamp startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return eventName + " " + startTime + "-" + endTime;
    }

    public static void main(String[] args) {
        TimeStamp ts1 = new TimeStamp(9, 59);
        TimeStamp ts2 = new TimeStamp(10, 59);
        DayEvent e = new DayEvent();
        System.out.println(e);
    }
}
