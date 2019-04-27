package edu.temple.mobiledevgroupproject.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SimpleDate implements Serializable {
    private int year;
    private int month;
    private int day;

    public SimpleDate(int month, int day, int year) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public SimpleDate(String dateStr){
        this.year = Integer.valueOf(dateStr.substring(0,4));
        this.month = Integer.valueOf(dateStr.substring(5,7));
        this.day = Integer.valueOf(dateStr.substring(8,10));
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    /**
     * Helper method.
     * Checks if a date with the attributes passed in as args. is a valid date.
     * Accounts for leap year and number of days in a month.
     * @return True if the params. match a valid date.
     */
    public static boolean isValidDate(int month, int day, int year) {
        //check simple invalidity first
        if ((month <= 0 || month > 12) || (day <= 0)) {
            return false;
        }

        if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day > 30) {
                return false;
            }
        } else if (month == 2) {
            if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                if (day > 29) {
                    return false;
                }
            } else {
                if (day > 28) {
                    return false;
                }
            }
        } else {
            if (day > 31) {
                return false;
            }
        }
        return true;
    }



    /**
     * Constructs a JSONObject based on a SimpleDate instance's fields.
     * FORMAT: {"month":<month>,"day":<day>,"year":<year>}
     * @return a SimpleDate instance's fields in JSONObject format.
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("month", month);
            jsonObject.put("day", day);
            jsonObject.put("year", year);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "" + getYear() + "-" + getMonth() + "-" + getDay();
    }
}
