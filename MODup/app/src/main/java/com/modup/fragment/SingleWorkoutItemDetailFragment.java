package com.modup.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.modup.model.SingleWorkoutItem;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleWorkoutItemDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleWorkoutItemDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleWorkoutItemDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    private SingleWorkoutItem choosenSingleWorkoutItem;
    private LinearLayout linearLayoutType, linearLayoutCategory, linearLayoutWeightSetsReps, linearLayoutDistance, linearLayoutForRepsTime, linearLayoutForReps,
            linearLayoutForTime, linearLayoutForWeight, linearLayoutDesc, linearLayoutCardDiv3, linearLayoutCardDiv4, linearLayoutCardDiv5, linearLayoutCardDiv6,
            linearLayoutCardDiv7, linearLayoutCardDiv8;

    private TextView tvType, tvCategory, tvWeight, tvSets, tvReps, tvDistance, tvForRepsTimeReps, tvForRepsTimeTime, tvForRepsReps, tvForRepsSets, tvForTime, tvForWeight, tvDesc, tvTitle;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingleWorkoutItemDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleWorkoutItemDetailFragment newInstance(String param1, String param2) {
        SingleWorkoutItemDetailFragment fragment = new SingleWorkoutItemDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SingleWorkoutItemDetailFragment() {
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
        view = inflater.inflate(R.layout.fragment_single_workout_item_detail, container, false);
        init();
        return view;
    }

    public void init() {
        choosenSingleWorkoutItem = (SingleWorkoutItem) getArguments().getSerializable("SINGLEWORKOUTITEM");

        linearLayoutType = (LinearLayout) view.findViewById(R.id.linearLayoutType);
        linearLayoutCategory = (LinearLayout) view.findViewById(R.id.linearLayoutCategory);
        linearLayoutWeightSetsReps = (LinearLayout) view.findViewById(R.id.linearLayoutWeightSetsReps);
        linearLayoutDistance = (LinearLayout) view.findViewById(R.id.linearLayoutDistance);
        linearLayoutForRepsTime = (LinearLayout) view.findViewById(R.id.linearLayoutForRepsTime);
        linearLayoutForReps = (LinearLayout) view.findViewById(R.id.linearLayoutForReps);
        linearLayoutForTime = (LinearLayout) view.findViewById(R.id.linearLayoutForTime);
        linearLayoutForWeight = (LinearLayout) view.findViewById(R.id.linearLayoutForWeight);
        linearLayoutDesc = (LinearLayout) view.findViewById(R.id.linearLayoutWorkoutDesc);
        linearLayoutCardDiv3 = (LinearLayout) view.findViewById(R.id.cardDivider3);
        linearLayoutCardDiv4 = (LinearLayout) view.findViewById(R.id.cardDivider4);
        linearLayoutCardDiv5 = (LinearLayout) view.findViewById(R.id.cardDivider5);
        linearLayoutCardDiv6 = (LinearLayout) view.findViewById(R.id.cardDivider6);
        linearLayoutCardDiv7 = (LinearLayout) view.findViewById(R.id.cardDivider7);
        linearLayoutCardDiv8 = (LinearLayout) view.findViewById(R.id.cardDivider8);


        tvTitle = (TextView) view.findViewById(R.id.textViewTitle);
        tvType = (TextView) view.findViewById(R.id.textViewWorkoutTypeItem);
        tvCategory = (TextView) view.findViewById(R.id.textViewWorkoutCategoryItem);
        tvWeight = (TextView) view.findViewById(R.id.textViewWorkoutWeightItem);
        tvSets = (TextView) view.findViewById(R.id.textViewWorkoutSetsItem);
        tvReps = (TextView) view.findViewById(R.id.textViewWorkoutRepsItem);
        tvDistance = (TextView) view.findViewById(R.id.textViewWorkoutDistanceItem);
        tvForRepsTimeReps = (TextView) view.findViewById(R.id.textViewWorkoutForRepsTimeRepsItem);
        tvForRepsTimeTime = (TextView) view.findViewById(R.id.textViewWorkoutForRepsTimeTimeItem);
        tvForRepsReps = (TextView) view.findViewById(R.id.textViewWorkoutForRepsRepsItem);
        tvForRepsSets = (TextView) view.findViewById(R.id.textViewWorkoutForRepsSetsItem);
        tvForTime = (TextView) view.findViewById(R.id.textViewWorkoutForTimeItem);
        tvForWeight = (TextView) view.findViewById(R.id.textViewWorkoutForWeightItem);
        tvDesc = (TextView) view.findViewById(R.id.textViewWorkoutDescriptionItem);

        setupUI();


    }

    public void setupUI() {
        linearLayoutCategory.setVisibility(View.GONE);
        linearLayoutWeightSetsReps.setVisibility(View.GONE);
        linearLayoutDistance.setVisibility(View.GONE);
        linearLayoutForRepsTime.setVisibility(View.GONE);
        linearLayoutForReps.setVisibility(View.GONE);
        linearLayoutForTime.setVisibility(View.GONE);
        linearLayoutForWeight.setVisibility(View.GONE);
        linearLayoutDesc.setVisibility(View.GONE);
        linearLayoutCardDiv3.setVisibility(View.GONE);
        linearLayoutCardDiv4.setVisibility(View.GONE);
        linearLayoutCardDiv5.setVisibility(View.GONE);
        linearLayoutCardDiv6.setVisibility(View.GONE);
        linearLayoutCardDiv7.setVisibility(View.GONE);
        linearLayoutCardDiv8.setVisibility(View.GONE);

        tvTitle.setText(choosenSingleWorkoutItem.get_workoutName());
        tvType.setText(choosenSingleWorkoutItem.get_workoutType());

        if (choosenSingleWorkoutItem.get_workoutType().equals("Strength")) {
            if(choosenSingleWorkoutItem.get_workoutCategory().equals("Weightlifting")){
                linearLayoutCategory.setVisibility(View.VISIBLE);
                linearLayoutWeightSetsReps.setVisibility(View.VISIBLE);
                tvCategory.setText(choosenSingleWorkoutItem.get_workoutCategory());
                tvWeight.setText(choosenSingleWorkoutItem.get_workoutWeight());
                tvSets.setText(choosenSingleWorkoutItem.get_sets());
                tvReps.setText(choosenSingleWorkoutItem.get_reps());

            } else if (choosenSingleWorkoutItem.get_workoutCategory().equals("Isometric") || choosenSingleWorkoutItem.get_workoutCategory().equals("Circuit")
                    || choosenSingleWorkoutItem.get_workoutCategory().equals("Other")){
                 linearLayoutCategory.setVisibility(View.VISIBLE);
                 linearLayoutDesc.setVisibility(View.VISIBLE);
                 tvCategory.setText(choosenSingleWorkoutItem.get_workoutCategory());
                 tvDesc.setText(choosenSingleWorkoutItem.get_workoutDesc());
            }

        } else if (choosenSingleWorkoutItem.get_workoutType().equals("Endurance")) {
            if(choosenSingleWorkoutItem.get_workoutCategory().equals("Running")){
                linearLayoutCategory.setVisibility(View.VISIBLE);
                linearLayoutDistance.setVisibility(View.VISIBLE);
                tvCategory.setText(choosenSingleWorkoutItem.get_workoutCategory());
                tvDistance.setText(choosenSingleWorkoutItem.get_workoutDistance());

            } else if (choosenSingleWorkoutItem.get_workoutCategory().equals("Other")){
                linearLayoutCategory.setVisibility(View.VISIBLE);
                linearLayoutDesc.setVisibility(View.VISIBLE);
                tvCategory.setText(choosenSingleWorkoutItem.get_workoutCategory());
                tvDesc.setText(choosenSingleWorkoutItem.get_workoutDesc());
            }

        } else if (choosenSingleWorkoutItem.get_workoutType().equals("Other")) {
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(choosenSingleWorkoutItem.get_workoutDesc());

        } else if (choosenSingleWorkoutItem.get_workoutType().equals("For Reps & Time")) {
            linearLayoutForRepsTime.setVisibility(View.VISIBLE);
            linearLayoutCardDiv5.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForRepsTimeReps.setText(choosenSingleWorkoutItem.get_reps());
            tvForRepsTimeTime.setText(choosenSingleWorkoutItem.get_workoutTime());
            tvDesc.setText(choosenSingleWorkoutItem.get_workoutDesc());

        } else if (choosenSingleWorkoutItem.get_workoutType().equals("For Reps")) {
            linearLayoutForReps.setVisibility(View.VISIBLE);
            linearLayoutCardDiv6.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForRepsReps.setText(choosenSingleWorkoutItem.get_reps());
            tvForRepsSets.setText(choosenSingleWorkoutItem.get_sets());
            tvDesc.setText(choosenSingleWorkoutItem.get_workoutDesc());

        } else if (choosenSingleWorkoutItem.get_workoutType().equals("For Time")) {
            linearLayoutForTime.setVisibility(View.VISIBLE);
            linearLayoutCardDiv7.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForTime.setText(choosenSingleWorkoutItem.get_workoutTime());
            tvDesc.setText(choosenSingleWorkoutItem.get_workoutDesc());

        } else if (choosenSingleWorkoutItem.get_workoutType().equals("For Weight")) {
            linearLayoutForWeight.setVisibility(View.VISIBLE);
            linearLayoutCardDiv8.setVisibility(View.VISIBLE);
            linearLayoutDesc.setVisibility(View.VISIBLE);
            tvForWeight.setText(choosenSingleWorkoutItem.get_workoutWeight());
            tvDesc.setText(choosenSingleWorkoutItem.get_workoutDesc());

        }


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
