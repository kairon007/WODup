package com.modup.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.modup.adapter.CardsAdapter;
import com.modup.adapter.ParsePrivateWorkoutAdapter;
import com.modup.adapter.ParseWorkoutAdapter;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.Date;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.modup.fragment.CalendarFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.modup.fragment.CalendarFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFeedFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = CalendarFeedFragment.class.getCanonicalName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static final int INITIAL_DELAY_MILLIS = 300;
    private CardsAdapter mGoogleCardsAdapter;
    private View view;
    private MaterialDialog mDialog;

    //parse queries
    private ParsePrivateWorkoutAdapter mParseWorkoutAdapter;
    private SwipeRefreshLayout swipeLayout;
    private Date eventDate;
    private Button btnBack, btnAddContent;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFeedFragment newInstance(String param1, String param2) {
        CalendarFeedFragment fragment = new CalendarFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFeedFragment() {
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
        view = inflater.inflate(R.layout.fragment_calendar_feed, container, false);
        initValues();
        return view;
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

    public void initValues() {
        eventDate = (Date) getArguments().getSerializable("DATE");



        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mParseWorkoutAdapter.loadObjects();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);

                    }
                }, 3000);

            }
        });

        swipeLayout.setColorSchemeResources(R.color.primary_blue, R.color.secondary_blue, R.color.primary_purple, R.color.secondary_purple);

        btnAddContent = (Button) view.findViewById(R.id.buttonAdd);
        btnAddContent.setOnClickListener(this);

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        ListView listView = (ListView) view.findViewById(R.id.listViewFeed);
        listView.setOnItemClickListener(this);

        mParseWorkoutAdapter = new ParsePrivateWorkoutAdapter(getActivity(), eventDate);

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                mParseWorkoutAdapter);
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                INITIAL_DELAY_MILLIS);
        listView.setDivider(null);

        listView.setFadingEdgeLength(0);
        listView.setFitsSystemWindows(true);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setAdapter(swingBottomInAnimationAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("DATE", eventDate);
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new CreatePrivateFragment();
                mFragment.setArguments(mBundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack("CREATEPRIVATEFRAGMENT").commit();
                break;

            case R.id.buttonBack:
                getActivity().getFragmentManager().popBackStack();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SingleWorkout currentSingleWorkout = mParseWorkoutAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("SINGLEWORKOUT", currentSingleWorkout);
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new DetailFragment();
                mFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack("DETAILFRAGMENT").commit();

            }
        }, 200);
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
