package com.modup.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.modup.fragment.CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.modup.fragment.CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String TAG = CalendarFragment.class.getCanonicalName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private Button btnAddDate;
    ParseQuery<SingleWorkout> query;

    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        init();
        return view;
    }

    public void init() {
        btnAddDate = (Button) view.findViewById(R.id.buttonAddDate);
        btnAddDate.setOnClickListener(this);


        query = new ParseQuery<SingleWorkout>(
                "SingleWorkout");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.setLimit(50);
        query.orderByDescending("createdAt");


        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        CalendarPickerView calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();


        calendar.init(todate1, nextYear.getTime())
                .withSelectedDate(today);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //do a query and find out if there are any dates that exist, if there are open a different fragment

                query.whereEqualTo("event_date", date);

                try {
                    if(query.count() != 0){
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("DATE", date);
                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment mFragment = new CalendarFeedFragment();
                        mFragment.setArguments(mBundle);
                        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack("CALENDARFEEDFRAGMENT").commit();

                    } else {
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("DATE", date);
                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment mFragment = new CreatePrivateFragment();
                        mFragment.setArguments(mBundle);
                        fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack("CREATEPRIVATEFRAGMENT").commit();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddDate:
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new CreatePrivateFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack("CREATEPRIVATEFRAGMENT").commit();
               break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
