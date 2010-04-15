package gdcalendar.mvc.model;

/**
 * Low weight time stamp class, representing a time 00:00 - 24:00 for a non
 * specified day.
 * 
 * @author Tomas
 */
public class TimeStamp implements Comparable<TimeStamp> {

    private int hour, min;

    public TimeStamp() {
        hour = 0;
        min = 0;
    }

    /**
     * Create a time stamp. Time stamp range from 00:00 to 24:00
     * @param hour Hour of the time stamp,  0<hour<24
     * @param min Minute of the time stamp, 0<min<60
     */
    TimeStamp(int hour, int min) {
        if (hour < 0 || hour > 24 || hour >= 24 && min > 0 || min < 0 || min > 60) {
            throw new IllegalArgumentException("Time stamp range from 00:00 - 24:00");
        }

        this.hour = hour;
        this.min = min;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    /**
     * Get the time stamp as a int[]
     * @return the time stamp, {hour, min}
     */
    public int[] getTimeStamp() {
        return new int[]{hour, min};
    }

    /**
     * Set a new time stamp
     * @param hour the hour of the time stamp,  0<hour<24
     * @param min the minute of the time stamp, 0<min<60
     */
    public void setTimeStamp(int hour, int min) {
        if (hour < 0 || hour > 24 || hour >= 24 && min > 0 || min < 0 || min > 60) {
            throw new IllegalArgumentException("Time stamp range from 00:00 - 24:00");
        }
        this.hour = hour;
        this.min = min;
    }

    @Override
    public String toString() {
        return formatTimeStamp(this);
    }

    // format the timestamp ##:##, where # is a int between 0 and 9
    private String formatTimeStamp(TimeStamp stamp) {
        String formatedString;
        formatedString = stamp.hour < 10 ? "" + 0 + stamp.hour : "" + stamp.hour;
        formatedString += ":";
        formatedString += stamp.min < 10 ? "" + 0 + stamp.min : "" + stamp.min;

        return formatedString;
    }

    public int compareTo(TimeStamp o) {
        int timeDifference;
        timeDifference = 60 * (hour - o.hour) + (min - o.min);
        return timeDifference;
    }
}

