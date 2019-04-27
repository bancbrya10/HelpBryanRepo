/*SignUpFragment:
*(1) Displays SignUp form to user
*(2) Gets sign up data from UI elements
*(3) Validates Sign Up info
*(3) Passes validated sign up data to parent activity*/

package edu.temple.mobiledevgroupproject.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.temple.mobiledevgroupproject.Objects.Constants;
import edu.temple.mobiledevgroupproject.Objects.RequestHandler;
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
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(getContext());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldsHaveInput() && signUpInfoValid()) {

                    String name = nameField.getText().toString();
                    String userName = userNameField.getText().toString();
                    String password = passwordField.getText().toString();
                    int month = Integer.valueOf(monthField.getText().toString());
                    int day = Integer.valueOf(dayField.getText().toString());
                    int year = Integer.valueOf(yearField.getText().toString());

                    if(isBirthdayValid(day, month, year)){
                        //construct new User object
                        User newUser = new User()
                                .setName(name)
                                .setUserName(userName)
                                .setUserBirthDay(new SimpleDate(month, day, year))
                                .setPreviousJobs(null)
                                .setCurrentEnrolledJobs(null)
                                .setCurrentPostedJobs(null)
                                .setUserRating(User.DEFAULT_RATING);
                        registerUser(newUser);

                        //pass signup data to parent activity
                        signUpListener.sendNewUser(newUser);
                    }
                    else{
                        Toast.makeText(getContext(), "Please enter valid values for birthday", Toast.LENGTH_LONG).show();
                    }

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
     * Get input from UI elements and query database to determine in signup info is unique.
     * Also check to ensure user birthday input from UI elements is valid.
     * @return true if signup info entered by user is unique
     */
    private boolean signUpInfoValid() {
        int month = Integer.valueOf(monthField.getText().toString().trim());
        int day = Integer.valueOf(dayField.getText().toString().trim());
        int year = Integer.valueOf(yearField.getText().toString().trim());

        if (!SimpleDate.isValidDate(month, day, year)) {
            return false;
        }

        return true;
    }

    //Post the data from the fields to the database
    private void registerUser(final User user){
        final String birthday = "" + user.getUserBirthDay().getYear() + "-" + user.getUserBirthDay().getMonth()+ "-" + user.getUserBirthDay().getDay();
        progressDialog.setMessage("Registering new user...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.SIGN_UP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("SignUpResponse", jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",user.getName());
                params.put("userName",user.getUserName());
                params.put("birthday",birthday);
                params.put("rating","" + user.getUserRating());
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    //TODO CHECK FOR INVALID BIRTHDATE

    private boolean isBirthdayValid(int day, int month, int year){
        if(year >= 1800 && month >= 1 && month <= 12 && day >=1 && day <= 31){
            return true;
        }
        return false;
    }
}