/*JobListFragment:
*(1) Displays list of potential jobs
*(2) Allows user to sort jobs according to various categories
*(3) Receives updated list of jobs from parent Activity
*(4) Sends job selected from List to parent Activity*/

package edu.temple.mobiledevgroupproject.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.temple.mobiledevgroupproject.Objects.Job;
import edu.temple.mobiledevgroupproject.R;

public class JobListFragment extends Fragment implements RecyclerViewItemClicked {
    //layout objects
    RecyclerView recyclerView;
    ListAdapter listAdapter;

    //other objects
    private ArrayList<Job> jobList;
    public JobSelectedInterface jobSelectedListener;

    public JobListFragment() {
        // Required empty public constructor
    }

    public interface JobSelectedInterface {
        void getSelectedJob(Job selectedJob);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        jobSelectedListener = (JobSelectedInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        jobSelectedListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);

        Bundle args = getArguments();
        if (args != null) {
            jobList = args.getParcelableArrayList("job_list");
        }

        Log.d("FragmentJobList", jobList.toString());

        recyclerView = view.findViewById(R.id.recycler_view);
        listAdapter = new ListAdapter(jobList, this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        return view;
    }
    
    /**
     * Passes job corresponding to list item clicked by user to parent activity.
     * @param view
     * @param position Position of item clicked in the dataset
     */
    @Override
    public void userItemClick(View view, int position) {
        jobSelectedListener.getSelectedJob(jobList.get(position));
    }
}
