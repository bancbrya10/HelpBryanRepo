package edu.temple.mobiledevgroupproject.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Set;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private static Context ctx;
    //User keys
    private static final String SHARED_PREF_NAME = "mySharedPref12";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_BIRTHDAY= "birthday";
    private static final String KEY_PREVIOUS_JOBS = "previousJobs";
    private static final String KEY_CURRENT_ENROLLED_JOBS = "currentEnrolledJobs";
    private static final String KEY_CURRENT_POSTED_JOBS = "currentPostedJobs";
    private static final String KEY_RATING = "rating";
    //Job keys
    /*
    private static final String KEY_JOB_TITLE = "jobTitle";
    private static final String KEY_JOB_DESCRIPTION = "jobDescription";
    private static final String KEY_DATE_POSTED = "datePosted";
    private static final String KEY_DATE_OF_JOB = "dateOfJob";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_POSTED_BY = "postedBy";
    private static final String KEY_COMMENTS= "comments";
    */

    private static final String KEY_JOB_LIST = "jobList";

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean addJob(String jobListStr){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        /*
        editor.putString(KEY_JOB_TITLE, jobTitle);
        editor.putString(KEY_JOB_DESCRIPTION, jobDescription);
        editor.putString(KEY_DATE_POSTED, datePosted);
        editor.putString(KEY_DATE_OF_JOB, dateOfJob);
        editor.putString(KEY_START_TIME, startTime);
        editor.putString(KEY_END_TIME, endTime);
        editor.putFloat(KEY_LATITUDE, (float)latitude);
        editor.putFloat(KEY_LONGITUDE, (float)longitude);
        editor.putString(KEY_POSTED_BY, postedBy);
        editor.putString(KEY_COMMENTS, comments);
        */

        editor.putString(KEY_JOB_LIST, jobListStr);

        editor.apply();

        return true;
    }

    public String getJobList(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_JOB_LIST, null);
    }

    /*
    public String getJobTitle(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_JOB_TITLE, null);
    }

    public String getJobDescription(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_JOB_DESCRIPTION, null);
    }

    public String getDatePosted(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DATE_POSTED, null);
    }

    public String getDateOfJob(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DATE_OF_JOB, null);
    }

    public String getStartTime(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_START_TIME, null);
    }

    public String getEndTime(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_END_TIME, null);
    }

    public double getLatitude(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (double)sharedPreferences.getFloat(KEY_LATITUDE, 0);
    }

    public double getLongitude(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (double)sharedPreferences.getFloat(KEY_LONGITUDE, 0);
    }

    public String getPostedBy(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_POSTED_BY, null);
    }

    public String getComments(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_COMMENTS, null);
    }
    */

    public boolean userLogin(int id, String name, String userName, String birthday,
                             String previousJobs, String currentEnrolledJobs,
                             String currentPostedJobs, double rating){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_BIRTHDAY, birthday);
        editor.putString(KEY_PREVIOUS_JOBS, previousJobs);
        editor.putString(KEY_CURRENT_ENROLLED_JOBS, currentEnrolledJobs);
        editor.putString(KEY_CURRENT_POSTED_JOBS, currentPostedJobs);
        editor.putFloat(KEY_RATING, (float)rating);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME, null) != null){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getName(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null);
    }

    public String getUserName(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getBirthday(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_BIRTHDAY, null);
    }

    public String getPreviousJobs(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PREVIOUS_JOBS, null);
    }

    public String getCurrentEnrolledJobs(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CURRENT_ENROLLED_JOBS, null);
    }

    public String getCurrentPostedJobs(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CURRENT_POSTED_JOBS, null);
    }

    public double getRating(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(KEY_RATING, (float)0.0);
    }
}
