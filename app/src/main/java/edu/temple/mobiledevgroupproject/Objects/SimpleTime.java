package edu.temple.mobiledevgroupproject.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SimpleTime implements Serializable {
    public static String ANTE_MERIDIEM = "AM";
    public static String POST_MERIDIEM = "PM";

    private int hours;
    private int minutes;
    private String timePeriod;

    public SimpleTime(int hours, int minutes, String timePeriod) {
        this.hours = hours;
        this.minutes = minutes;
        this.timePeriod = timePeriod;
    }

    public SimpleTime(String timeStr){
        this.hours = Integer.valueOf(timeStr.substring(0,2));
        this.minutes = Integer.valueOf(timeStr.substring(3,5));
        if(timeStr.contains("a") || timeStr.contains("A")){
            this.timePeriod = ANTE_MERIDIEM;
        }
        else if(timeStr.contains("p") || timeStr.contains("P")){
            this.timePeriod = POST_MERIDIEM;
        }
        else{
            this.timePeriod = "";
        }
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    /**
     * Helper Method.
     * Checks if a date with the attributes passed in as args. is a valid date.
     * @return True if the params. match a valid time.
     */
    public static boolean isValidTime(int hours, int minutes, String timePeriod) {
        //check for simple invalidity first.
        if (!timePeriod.equals(ANTE_MERIDIEM) || !timePeriod.equals(POST_MERIDIEM)) {
            return false;
        }

        if ((hours > 12 || hours < 1) || (minutes > 59 || minutes < 0)) {
            return false;
        }



        //TODO determine whether this class is needed. Implement this method if so.

        return false;
    }

    /**
     * Constructs a JSONObject based on a SimpleDate instance's fields.
     * FORMAT: {"hours":<hours>,"minutes","time_period":<time period>}
     * @return a SimpleTime instance's fields in JSONObject format.
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("hours", hours);
            jsonObject.put("minutes", minutes);
            jsonObject.put("time_period", timePeriod);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "" + getHours() + ":" + getMinutes();
    }
}
