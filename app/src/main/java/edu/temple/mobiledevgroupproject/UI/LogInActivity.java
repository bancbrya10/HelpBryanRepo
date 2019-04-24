/*LogInActivity
 *(1) Serves as the entry point of the application
 *(2) Listens for user selection to login/signup
 *(3) Signs user in automatically if sign-in info has been saved locally
 *(4) Creates and launches fragments according to login/signup selection
 *(5) Launches MainActivity.java via Intent upon successful login/signup*/

package edu.temple.mobiledevgroupproject.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import edu.temple.mobiledevgroupproject.MainActivity;
import edu.temple.mobiledevgroupproject.Objects.User;
import edu.temple.mobiledevgroupproject.R;

public class LogInActivity extends AppCompatActivity implements LogInFragment.LogInInterface, SignUpFragment.SignUpInterface {
    private static final String FILENAME = "saved_credentials";

    //layout objects
    FrameLayout fragmentContainer;
    TextView newTextView;

    User thisUser;
    String retrievedUserName = null;
    Intent launchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        fragmentContainer = findViewById(R.id.login_frag_container);

        if (savedDataExists()) {
            startMainActivity(thisUser);
        } else {
            LogInFragment logInFragment = new LogInFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.login_frag_container, logInFragment).commit();
        }
    }

    /**
     * Write username, password pair to save file with JSON format [<username> , <password>]
     * Send existing user User object to database.
     * Launch MainActivity.
     * @param existingUser validated User object received from LogInFragment.java
     * @param saveData boolean data specifying whether user wishes to create a save file for automatic login
     */
    @Override
    public void sendExistingUser(User existingUser, boolean saveData) {
        //write to file
        if (saveData) {
            File file = new File(getFilesDir(), FILENAME);
            JSONArray jsonArray = null;
            jsonArray.put(existingUser.getUserName());
            jsonArray.put(existingUser.getPassword());
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(jsonArray.toString().getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendUserToDataBase(existingUser);
        startMainActivity(existingUser);
    }

    /**
     * Triggered in child fragment, when user requests to create new account.
     * Launches new fragment
     */
    @Override
    public void signUpClick() {
        SignUpFragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_frag_container, signUpFragment).commit();
        //Toast.makeText(LogInActivity.this, "clicked", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sends new user information to database.
     * Launches MainActivity.
     * @param newUser User object constructed and received from SignUpFragment.java
     */
    @Override
    public void sendNewUser(User newUser) {
        sendUserToDataBase(newUser);
        startMainActivity(newUser);
    }

    /**
     * Checks device's internal storage for a username, password save file.
     * If so, retrieves pair from file, queries database to retrieve User info matching pair in save file.
     * @return true if a username, password pair in device's internal storage.
     */
    private boolean savedDataExists() {
        File file = new File(getFilesDir(), FILENAME);
        boolean fileExists = false;

        if (file.exists()) {
            fileExists = true;
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String currLine;
                while ((currLine = br.readLine()) != null) {
                    sb.append(currLine);
                    sb.append('\n');
                }
                br.close();
                JSONArray jsonArray = new JSONArray(sb.toString());
                /*username/password written into json array with format:
                 *["<username>" , "<password>"]*/
                retrievedUserName = jsonArray.getString(0);

                //TODO GET USER DATA FROM DATABASE, CREATE NEW USER OBJECT.

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return fileExists;
    }

    /**
     * Send signup/login user information to database
     * @param user User object to be sent to database
     */
    private void sendUserToDataBase(User user) {

    }

    /**
     * Create a new Intent; use it to launch MainActivity.class
     * @param extraData User object to be passed to MainActivity.class as Intent extra
     */
    private void startMainActivity(User extraData) {
        launchIntent = new Intent(this, MainActivity.class);
        launchIntent.putExtra("this_user", extraData);
        startActivity(launchIntent);
    }

}
