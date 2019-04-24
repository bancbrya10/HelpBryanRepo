/*ProfileFragment:
*(1) Receives User from parent activity
*(2) Sets and Displays its UI elements using User object received*/

package edu.temple.mobiledevgroupproject.UI;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import edu.temple.mobiledevgroupproject.Objects.Job;
import edu.temple.mobiledevgroupproject.Objects.Record;
import edu.temple.mobiledevgroupproject.Objects.SimpleDate;
import edu.temple.mobiledevgroupproject.Objects.User;
import edu.temple.mobiledevgroupproject.R;

public class ProfileFragment extends Fragment {
    //layout objects
    private TextView userNameView;
    private TextView nameView;
    private TextView ageView;
    private TextView ratingView;
    private TextView currentlyEnrolledView;
    private TextView previouslyEnrolledView;
    private ImageView profileImgView;

    //other objects
    User userToDisplay;
    View mView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        Bundle args = getArguments();
        if (args != null) {
            userToDisplay = args.getParcelable("user_to_display");
        }

        userNameView = mView.findViewById(R.id.username_view);
        nameView = mView.findViewById(R.id.fullname_view);
        ageView = mView.findViewById(R.id.age_view);
        ratingView = mView.findViewById(R.id.rating_view);
        currentlyEnrolledView = mView.findViewById(R.id.currently_enrolled_view);
        previouslyEnrolledView = mView.findViewById(R.id.past_enrolled_view);
        profileImgView = mView.findViewById(R.id.profile_img);

        if (userToDisplay != null) {
            userNameView.setText(userToDisplay.getUserName());
            nameView.setText(userToDisplay.getName());
            String ageString = getResources().getString(R.string.age);
            ageView.setText(ageString + " " + getAgeString(userToDisplay.getUserBirthDay()));
            ratingView.setText(String.valueOf(userToDisplay.getUserRating()));

            String opportunityString = getResources().getString(R.string.opportunities);
            currentlyEnrolledView.setText(getJobCount(userToDisplay.getCurrentEnrolledJobs()) + " " + opportunityString);
            previouslyEnrolledView.setText(getJobCount(userToDisplay.getPreviousJobs()) + " " + opportunityString);

            String profImgString = User.fetchProfImg(getContext().getFilesDir());
            if (profImgString != null) {
                Bitmap profImgBitmap = User.decodeToBitmap(profImgString);
                profileImgView.setImageBitmap(profImgBitmap);
            }
        }

        return mView;
    }

    /**
     * Helper method.
     * Get count of jobs in a User instance's job Records fields.
     * @param currentEnrolledJobs User instance's field representing currently enrolled jobs.
     * @return String representing the count of jobs in Record.
     */
    private String getJobCount(Record<Job> currentEnrolledJobs) {
        return String.valueOf(currentEnrolledJobs.getRecordData().size());
    }

    /**
     * Helper method.
     * Calculate a user's age based on the current date and the userBirthDay param.
     * @param userBirthDay User instance's field representing their D.O.B.
     * @return A string representing the user's age relative to the current date.
     */
    private String getAgeString(SimpleDate userBirthDay) {
        Calendar cal = Calendar.getInstance();
        int currDay = cal.get(Calendar.DAY_OF_MONTH);
        int currMonth = cal.get(Calendar.MONTH);
        int currYear = cal.get(Calendar.YEAR);

        int yearDiff = currYear - userBirthDay.getYear();
        int monthDiff = currMonth - userBirthDay.getMonth();

        if (monthDiff < 0) {
            yearDiff -= 1;
        } else if (monthDiff == 0) {
            int dayDiff = currDay - userBirthDay.getDay();
            if (dayDiff < 0) {
                yearDiff -= 1;
            }
        }
        return String.valueOf(yearDiff);
    }
}
