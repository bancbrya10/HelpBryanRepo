/*Job:
*(1) Represents a job to be posted by User*/

package edu.temple.mobiledevgroupproject.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Job implements Parcelable {
    private String jobTitle;
    private String jobDescription;
    private SimpleDate datePosted;
    private SimpleDate dateOfJob;
    private SimpleTime startTime;
    private SimpleTime endTime;
    private LatLng location;
    private User user;
    private Record<Comment> commentList;

    public Job() {

    }

    public Job setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public Job setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public Job setDatePosted(SimpleDate datePosted) {
        this.datePosted = datePosted;
        return this;
    }

    public Job setDateOfJob(SimpleDate dateOfJob) {
        this.dateOfJob = dateOfJob;
        return this;
    }

    public Job setStartTime(SimpleTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Job setEndTime(SimpleTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public Job setLocation(LatLng location) {
        this.location = location;
        return this;
    }

    public Job setUser(User user) {
        this.user = user;
        return this;
    }

    public Job setCommentList(Record<Comment> commentList) {
        this.commentList = commentList;
        return this;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public SimpleDate getDatePosted() {
        return datePosted;
    }

    public SimpleDate getDateOfJob() {
        return dateOfJob;
    }

    public SimpleTime getStartTime() {
        return startTime;
    }

    public SimpleTime getEndTime() {
        return endTime;
    }

    public LatLng getLocation() {
        return location;
    }

    public User getUser() {
        return user;
    }

    public Record<Comment> getCommentList() {
        return commentList;
    }

    public void updateCommentList(Comment newComment) {
        commentList.addDataToRecord(newComment);
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    protected Job(Parcel in) {
        jobTitle = in.readString();
        jobDescription = in.readString();
        datePosted = (SimpleDate) in.readValue(SimpleDate.class.getClassLoader());
        dateOfJob = (SimpleDate) in.readValue(SimpleDate.class.getClassLoader());
        startTime = (SimpleTime) in.readValue(SimpleTime.class.getClassLoader());
        endTime = (SimpleTime) in.readValue(SimpleTime.class.getClassLoader());
        location = (LatLng) in.readValue(SimpleDate.class.getClassLoader());
        user = (User) in.readValue(User.class.getClassLoader());
        commentList = (Record<Comment>) in.readValue(Record.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobTitle);
        dest.writeString(jobDescription);
        dest.writeValue(datePosted);
        dest.writeValue(dateOfJob);
        dest.writeValue(startTime);
        dest.writeValue(endTime);
        dest.writeValue(location);
        dest.writeValue(user);
        dest.writeValue(commentList);
    }

    /**
     * Constructs a JSONObject based on a Job instance's fields.
     * FORMAT: {"title":<job title>,"desc":<job description>,"date":[<month>,<day>,<year>],"date_posted":[<month>,<day>,<year>],
     * "start_time:[<hour>,<minute>,<time_period>], "end_time":[<hour>,<minute>,<time_period>],
     * "loc_data":[<latitude>,<longitude>],"user_id":<job's user's ID>, "record_id""<job's record's ID>}
     * @return a Job instance's fields in JSONObject format.
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", jobTitle);
            jsonObject.put("desc", jobDescription);

            JSONArray dateArray = new JSONArray();
            dateArray.put(dateOfJob.getMonth());
            dateArray.put(dateOfJob.getDay());
            dateArray.put(dateOfJob.getYear());
            jsonObject.put("date", dateArray);

            JSONArray dateArray2 = new JSONArray();
            dateArray2.put(datePosted.getMonth());
            dateArray2.put(datePosted.getDay());
            dateArray2.put(datePosted.getYear());
            jsonObject.put("date_posted", dateArray2);

            JSONArray startArray = new JSONArray();
            startArray.put(startTime.getHours());
            startArray.put(startTime.getMinutes());
            startArray.put(startTime.getTimePeriod());
            jsonObject.put("start_time", startArray);

            JSONArray endArray = new JSONArray();
            endArray.put(endTime.getHours());
            endArray.put(endTime.getMinutes());
            endArray.put(endTime.getTimePeriod());
            jsonObject.put("end_time", endArray);

            JSONArray locArray = new JSONArray();
            locArray.put(location.latitude);
            locArray.put(location.longitude);
            jsonObject.put("loc_data", locArray);

            jsonObject.put("user_id", user);
            jsonObject.put("record_id", commentList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "Title: " + getJobTitle() + "\nDescription: " + getJobDescription() + "\nDate Posted:" + getDatePosted().toString() +
                "\nDate Of Job: " + getDateOfJob().toString() + "\nStart Time: " + getStartTime().toString() + "\nEnd Time: " + getEndTime().toString() +
                "\nLatitude: " + getLocation().latitude + "\nLongitude: " + getLocation().longitude;
    }
}