/*User:
*(1) Represents a user of the application
*(2) Implemented using Builder pattern*/

package edu.temple.mobiledevgroupproject.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.FileNameMap;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User implements Parcelable {
    public static final double DEFAULT_RATING = 3.0;
    public static final double MIN_RATING = 0.0;
    public static final double MAX_RATING = 5.0;
    public static final String FILENAME = "prof_img";

    private String name;
    private String userName;
    private SimpleDate userBirthDay;
    private Record<Job> previousJobs;
    private double userRating;

    public User() {

    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public User setUserBirthDay(SimpleDate userBirthDay) {
        this.userBirthDay = userBirthDay;
        return this;
    }

    public User setPreviousJobs(Record<Job> previousJobs) {
        this.previousJobs = previousJobs;
        return this;
    }

    public User setUserRating(double userRating) {
        this.userRating = userRating;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public SimpleDate getUserBirthDay() {
        return userBirthDay;
    }

    public Record<Job> getPreviousJobs() {
        return previousJobs;
    }

    public double getUserRating() {
        return userRating;
    }

    public void updatePreviousJobs(Job newJob) {
        if (previousJobs == null) {
            previousJobs = new Record<>("PREVIOUS_JOBS", Record.JOB_RECORD);
        }
        previousJobs.addDataToRecord(newJob);
    }

    public static String hashAndSaltPassword(String password) {
        String hashedPassword = null;
        byte[] salt = new byte[16];
        new Random().nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte [] hash = factory.generateSecret(spec).getEncoded();
            hashedPassword = Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    protected User(Parcel in) {
        name = in.readString();
        userName = in.readString();
        userBirthDay = (SimpleDate) in.readValue(SimpleDate.class.getClassLoader());
        previousJobs = (Record<Job>) in.readValue(Record.class.getClassLoader());
        userRating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(userName);
        dest.writeValue(userBirthDay);
        dest.writeValue(previousJobs);
        dest.writeDouble(userRating);
    }

    /**
     * Helper method.
     * translate Base64 String representation of a user's profile image into a Bitmap.
     * @param profileImage Base64 String
     * @return a Bitmap representing a user's profile image.
     */
    public static Bitmap decodeToBitmap(String profileImage) {
        byte [] data = Base64.decode(profileImage, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * Helper method.
     *
     * @param profileImage
     * @return a Base64 representation of a user's Bitmap profile image.
     */
    public static String encodeToString(Bitmap profileImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        profileImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte [] data = os.toByteArray();
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * Helper method.
     * Read user profile image String from file.
     * @param filePath
     * @return Base64 String representing a user's profile image.
     */
    public static String fetchProfImg(File filePath) {
        String profImg = null;
        File file = new File(filePath, FILENAME);
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String currLine;
                while ((currLine = br.readLine()) != null) {
                    sb.append(currLine);
                }
                br.close();
                profImg = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return profImg;
    }

    /**
     * Helper method.
     * Write user profile image to storage.
     * @param profImg Base64 encoded profile image to be saved.
     * @param filePath Path to the save file in internal storage.
     */
    public static void writeProfImg(String profImg, File filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filePath, FILENAME));
            fos.write(profImg.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a JSONObject based on a User instance's fields.
     * FORMAT: {"full_name":<full name>,"name":<username>,"password":<password>,"age":<age>,"rating":<rating>,"posted_jobs":<record id>,"curr_jobs":<record id>,"prev_jobs":<record id>}
     * @return a User instance's fields in JSONObject format.
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("full_name", name);
            jsonObject.put("name", userName);
            jsonObject.put("age", userBirthDay);
            jsonObject.put("rating", userRating);
            jsonObject.put("prev_jobs", previousJobs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}