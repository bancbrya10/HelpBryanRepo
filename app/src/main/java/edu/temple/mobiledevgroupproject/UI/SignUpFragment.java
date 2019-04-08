/*SignUpFragment:
*(1) Displays SignUp form to user
*(2) Gets sign up data from UI elements
*(3) Validates Sign Up info
*(3) Passes validated sign up data to parent activity*/

package edu.temple.mobiledevgroupproject.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.spec.PBEKeySpec;

import edu.temple.mobiledevgroupproject.Objects.SimpleDate;
import edu.temple.mobiledevgroupproject.Objects.User;
import edu.temple.mobiledevgroupproject.R;

public class SignUpFragment extends Fragment {
    //layout objects
    EditText nameField;
    EditText userNameField;
    EditText passwordField;
    Button confirmButton;
    //for birthday picking
    EditText monthField;
    EditText dayField;
    EditText yearField;

    SignUpInterface signUpListener;
    View view;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public interface SignUpInterface {
        void sendNewUser(User newUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        signUpListener = (SignUpInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        signUpListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        nameField = view.findViewById(R.id.name_et_sign);
        userNameField = view.findViewById(R.id.user_et_sign);
        passwordField = view.findViewById(R.id.pass_et_sign);
        confirmButton = view.findViewById(R.id.confirm_sign);
        monthField = view.findViewById(R.id.month_et_sign);
        dayField = view.findViewById(R.id.day_et_sign);
        yearField = view.findViewById(R.id.year_et_sign);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldsHaveInput() && signUpInfoValid()) {

                    String name = nameField.getText().toString();
                    String userName = userNameField.getText().toString();
                    String hashedPassword = User.hashAndSaltPassword(passwordField.getText().toString());
                    int month = Integer.valueOf(monthField.getText().toString());
                    int day = Integer.valueOf(dayField.getText().toString());
                    int year = Integer.valueOf(yearField.getText().toString());

                    //construct new User object
                    User newUser = new User()
                            .setName(name)
                            .setUserName(userName)
                            .setPassword(hashedPassword)
                            .setUserBirthDay(new SimpleDate(year, month, day))
                            .setPreviousJobs(null)
                            .setCurrentEnrolledJobs(null)
                            .setCurrentPostedJobs(null)
                            .setUserRating(User.DEFAULT_RATING);

                    Toast.makeText(getContext(), R.string.signup_success, Toast.LENGTH_SHORT).show();
                    //pass signup data to parent activity
                    signUpListener.sendNewUser(newUser);
                } else {
                    if (allFieldsHaveInput()) {
                        Toast.makeText(getContext(), R.string.signup_fail, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    /**
     * Check the value of each UI input to ensure all signup fields have some input
     * @return true if all fields have some input
     */
    private boolean allFieldsHaveInput() {
        return (userNameField.getText().toString().trim().length() != 0) &&
                (nameField.getText().toString().trim().length() != 0) &&
                (passwordField.getText().toString().trim().length() != 0) &&
                (monthField.getText().toString().trim().length() != 0) &&
                (dayField.getText().toString().trim().length() != 0) &&
                (yearField.getText().toString().trim().length() != 0);
    }

    /**
     * Get input from UI elements and query database to determine in signup info is unique
     * @return true if signup info entered by user is unique
     */
    private boolean signUpInfoValid() {
        return false;
    }
}