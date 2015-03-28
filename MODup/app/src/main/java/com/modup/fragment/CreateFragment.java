package com.modup.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.modup.adapter.TextSpinnerAdapter;
import com.modup.adapter.WorkoutCardsAdapter;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.modup.model.SingleWorkoutItem;
import com.modup.view.WorkoutView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG = CreateFragment.class.getCanonicalName();

    private OnFragmentInteractionListener mListener;

    private Button btnAddWorkout, btnAcceptWorkout, btnCancelWorkout;
    private Spinner spinnerDifficulty, spinnerTime, spinnerSets, spinnerReps, spinnerExerciseType, spinnerExerciseCategoryStrength, spinnerExerciseCategoryEndurance;
    private EditText etWorkoutName, etTitle;
    private View view;
    private String[] array;
    private ArrayList<WorkoutView> mArrayList = new ArrayList<WorkoutView>();
    private ListView mListView;
    private WorkoutCardsAdapter mWorkoutCardsAdapter;

    private Animation animFadeOut, animFadeIn;
    private Handler mHandler;
    private Boolean isTimerRunning = false;
    private Boolean isDialogShowing = false;
    private CountDownTimer mCountDownTimer;
    private MaterialDialog mDialog;
    private WorkoutView workoutView;
    private LinearLayout linearLayoutDistance, linearLayoutExerciseCategoryStrength, linearLayoutExerciseCategoryEndurance,
            linearLayoutExerciseType, linearLayoutWeightlifting, linearLayoutOther, linearLayoutTraditional, linearLayoutCrossfit;
    private Spinner spinnerDistance, spinnerCrossfitType, spinnerForRepsReps, spinnerForRepsTime, spinnerForReps, spinnerForRepsRounds, spinnerForTime;
    private RadioButton rbtnTraditional, rbtnCrossFit;

    //crossfit stuff
    private LinearLayout linearLayoutForReps, linearLayoutForRepsTime, linearLayoutForTime, linearLayoutForWeight, linearLayoutCrossfitOther;

    private EditText etWorkoutWeight, etCrossfitWeight, etTraditionalDesc, etCrossfitDesc;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateFragment() {
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
        view = inflater.inflate(R.layout.fragment_create, container, false);
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
        mHandler = new Handler();

        btnAddWorkout = (Button) view.findViewById(R.id.btnAddWorkout);
        btnAddWorkout.setOnClickListener(this);
        btnAcceptWorkout = (Button) view.findViewById(R.id.buttonAccept);
        btnAcceptWorkout.setOnClickListener(this);
        btnCancelWorkout = (Button) view.findViewById(R.id.buttonCancel);
        btnCancelWorkout.setOnClickListener(this);

        etTitle = (EditText) view.findViewById(R.id.textViewTitle);


        mWorkoutCardsAdapter = new WorkoutCardsAdapter(getActivity(), mArrayList, new WorkoutCardsAdapter.Callback() {
            @Override
            public void onPressed(int pos) {
                mWorkoutCardsAdapter.remove(mWorkoutCardsAdapter.getItem(pos));
                mWorkoutCardsAdapter.notifyDataSetChanged();
            }
        });

        mListView = (ListView) view.findViewById(R.id.listViewWorkouts);
        mListView.setAdapter(mWorkoutCardsAdapter);
        mListView.setOnScrollListener(this);


        spinnerDifficulty = (Spinner) view.findViewById(R.id.spinnerDifficulty);
        array = getActivity().getResources().getStringArray(R.array.string_array_difficulty);
        TextSpinnerAdapter difficultyAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
        // Specify the layout to use when the list of choices appears
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDifficulty.setAdapter(difficultyAdapter);

        spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
        array = getActivity().getResources().getStringArray(R.array.string_array_time);
        TextSpinnerAdapter timeAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
        // Specify the layout to use when the list of choices appears
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTime.setAdapter(timeAdapter);

        animFadeOut = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        animFadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnAcceptWorkout.startAnimation(animFadeOut);
                btnAcceptWorkout.setVisibility(View.GONE);
                btnCancelWorkout.startAnimation(animFadeOut);
                btnCancelWorkout.setVisibility(View.GONE);
            }
        }, 1000);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddWorkout:
                //defining dialog
                boolean wrapInScrollView = false;
                mDialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_workout_layout, wrapInScrollView)
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Log.e(TAG, "Positive Pressed");
                                isDialogShowing = false;
                                if (rbtnTraditional.isChecked()) {
                                    //define the workout group based on traditional
                                    String mainCategory = "Traditional";
                                    String activityNameStr = (etWorkoutName.getText().toString().trim());
                                    String workoutType = (String) spinnerExerciseType.getSelectedItem();
                                    if (workoutType.equals("Strength")) {
                                        //check other strength spinner values
                                        String workoutCategory = (String) spinnerExerciseCategoryStrength.getSelectedItem();
                                        if (workoutCategory.equals("Weightlifting")) {
                                            String weight = etWorkoutWeight.getText().toString().trim();
                                            String setsStr = (String) spinnerSets.getSelectedItem();
                                            String repsStr = (String) spinnerReps.getSelectedItem();

                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutWeight(weight);
                                            workoutView.setSets(setsStr);
                                            workoutView.setReps(repsStr);
                                        } else if (workoutCategory.equals("Isometric")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);


                                        } else if (workoutCategory.equals("Circuit")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);

                                        } else if (workoutCategory.equals("Other")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);

                                        }

                                    } else if (workoutType.equals("Endurance")) {
                                        //check other endurance spinner values
                                        String workoutCategory = (String) spinnerExerciseCategoryEndurance.getSelectedItem();
                                        if (workoutCategory.equals("Running")) {
                                            String distance = (String) spinnerDistance.getSelectedItem();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutDistance(distance);
                                        } else if (workoutCategory.equals("Other")) {
                                            String desc = etTraditionalDesc.getText().toString().trim();
                                            workoutView = new WorkoutView(getActivity());
                                            workoutView.setWorkoutMainCategory(mainCategory);
                                            workoutView.setWorkoutName(activityNameStr);
                                            workoutView.setWorkoutType(workoutType);
                                            workoutView.setWorkoutCategory(workoutCategory);
                                            workoutView.setWorkoutDesc(desc);
                                        }

                                    } else if (workoutType.equals("Other")) {
                                        String desc = etTraditionalDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutDesc(desc);
                                    }

                                    //define crossfit items
                                } else if (rbtnCrossFit.isChecked()) {
                                    String mainCategory = "CrossFit";
                                    String activityNameStr = etWorkoutName.getText().toString().trim();
                                    String workoutType = (String) spinnerCrossfitType.getSelectedItem();
                                    if (workoutType.equals("For Reps & Time")) {
                                        String repsStr = (String) spinnerForReps.getSelectedItem();
                                        String timeStr = (String) spinnerForRepsTime.getSelectedItem();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setReps(repsStr);
                                        workoutView.setWorkoutTime(timeStr);
                                        workoutView.setWorkoutDesc(desc);


                                    } else if (workoutType.equals("For Reps")) {
                                        String repsStr = (String) spinnerForRepsReps.getSelectedItem();
                                        String setsStr = (String) spinnerForRepsRounds.getSelectedItem();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setReps(repsStr);
                                        workoutView.setSets(setsStr);
                                        workoutView.setWorkoutDesc(desc);

                                    } else if (workoutType.equals("For Time")) {
                                        String time = (String) spinnerForTime.getSelectedItem();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutTime(time);
                                        workoutView.setWorkoutDesc(desc);

                                    } else if (workoutType.equals("For Weight")) {
                                        String weight = etCrossfitWeight.getText().toString().trim();
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutWeight(weight);
                                        workoutView.setWorkoutDesc(desc);

                                    } else if (workoutType.equals("Other")) {
                                        String desc = etCrossfitDesc.getText().toString().trim();
                                        workoutView = new WorkoutView(getActivity());
                                        workoutView.setWorkoutMainCategory(mainCategory);
                                        workoutView.setWorkoutName(activityNameStr);
                                        workoutView.setWorkoutType(workoutType);
                                        workoutView.setWorkoutDesc(desc);
                                    }


                                }

                                mWorkoutCardsAdapter.add(workoutView);
                                mWorkoutCardsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                isDialogShowing = false;
                                Log.e(TAG, "Negative Pressed");
                            }
                        })
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                isDialogShowing = false;
                            }
                        })
                        .build();

                View dialogView = mDialog.getCustomView();
                etWorkoutName = (EditText) dialogView.findViewById(R.id.etWorkoutName);
                etWorkoutWeight = (EditText) dialogView.findViewById(R.id.editTextWeight);
                etCrossfitWeight = (EditText) dialogView.findViewById(R.id.editTextForWeight);
                etTraditionalDesc = (EditText) dialogView.findViewById(R.id.editTextDesc);
                etCrossfitDesc = (EditText) dialogView.findViewById(R.id.editTextCrossfitDesc);


                rbtnTraditional = (RadioButton) dialogView.findViewById(R.id.radioButtonTraditional);
                rbtnCrossFit = (RadioButton) dialogView.findViewById(R.id.radioButtonCrossFit);

                linearLayoutDistance = (LinearLayout) dialogView.findViewById(R.id.linearLayoutDistance);
                linearLayoutWeightlifting = (LinearLayout) dialogView.findViewById(R.id.linearLayoutWeightlifting);
                linearLayoutExerciseType = (LinearLayout) dialogView.findViewById(R.id.linearLayoutExerciseType);
                linearLayoutExerciseCategoryStrength = (LinearLayout) dialogView.findViewById(R.id.linearLayoutExerciseCategoryStrength);
                linearLayoutExerciseCategoryEndurance = (LinearLayout) dialogView.findViewById(R.id.linearLayoutExerciseCategoryEndurance);
                linearLayoutOther = (LinearLayout) dialogView.findViewById(R.id.linearLayoutOther);
                linearLayoutTraditional = (LinearLayout) dialogView.findViewById(R.id.linearLayoutTraditional);


                //crossfit layouts
                linearLayoutCrossfit = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCrossfit);
                linearLayoutForRepsTime = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForRepsTime);
                linearLayoutForReps = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForReps);
                linearLayoutForTime = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForTime);
                linearLayoutForWeight = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForWeight);
                linearLayoutCrossfitOther = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCrossfitOther);


                //set starting visibilities
                linearLayoutOther.setVisibility(View.GONE);
                linearLayoutDistance.setVisibility(View.GONE);
                linearLayoutCrossfit.setVisibility(View.GONE);
                linearLayoutForReps.setVisibility(View.GONE);
                linearLayoutForTime.setVisibility(View.GONE);
                linearLayoutForWeight.setVisibility(View.GONE);

                //TODO: ADD MORE CROSSFIT LAYOUT SPECIFIC ITEMS

                rbtnTraditional.setChecked(true);

                rbtnTraditional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //TODO: show the traditonal layout, hide the crossfit
                            linearLayoutTraditional.setVisibility(View.VISIBLE);
                            linearLayoutCrossfit.setVisibility(View.GONE);
                        }

                    }
                });

                rbtnCrossFit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //TODO: show the crossfit layout, hide the traditional
                            linearLayoutCrossfit.setVisibility(View.VISIBLE);
                            linearLayoutTraditional.setVisibility(View.GONE);
                        }

                    }
                });


                spinnerExerciseType = (Spinner) dialogView.findViewById(R.id.spinnerExerciseType);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_type);
                TextSpinnerAdapter extAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                extAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerExerciseType.setAdapter(extAdapter);
                spinnerExerciseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO: If statement to check strings and change the spinnerExerciseCategory array
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("Strength")) {
                            linearLayoutExerciseCategoryStrength.setVisibility(View.VISIBLE);
                            spinnerExerciseCategoryStrength.setSelection(0);
                            linearLayoutWeightlifting.setVisibility(View.VISIBLE);
                            linearLayoutExerciseCategoryEndurance.setVisibility(View.GONE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Endurance")) {
                            linearLayoutExerciseCategoryEndurance.setVisibility(View.VISIBLE);
                            spinnerExerciseCategoryEndurance.setSelection(0);
                            linearLayoutDistance.setVisibility(View.VISIBLE);
                            linearLayoutExerciseCategoryStrength.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                            linearLayoutExerciseCategoryStrength.setVisibility(View.GONE);
                            linearLayoutExerciseCategoryEndurance.setVisibility(View.GONE);
                            linearLayoutDistance.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                spinnerExerciseCategoryStrength = (Spinner) dialogView.findViewById(R.id.spinnerExerciseCategoryStrength);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_category_strength);
                TextSpinnerAdapter excsAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                excsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerExerciseCategoryStrength.setAdapter(excsAdapter);
                spinnerExerciseCategoryStrength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO: HIDE/SHOW layouts associated with selection
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("Weightlifting")) {
                            linearLayoutWeightlifting.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Isometric")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Circuit")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinnerExerciseCategoryEndurance = (Spinner) dialogView.findViewById(R.id.spinnerExerciseCategoryEndurance);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_category_endurance);
                TextSpinnerAdapter exceAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                exceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerExerciseCategoryEndurance.setAdapter(exceAdapter);
                spinnerExerciseCategoryEndurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO: HIDE/SHOW layouts associated with selection
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("Running")) {
                            linearLayoutDistance.setVisibility(View.VISIBLE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);
                            linearLayoutOther.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutOther.setVisibility(View.VISIBLE);
                            linearLayoutDistance.setVisibility(View.GONE);
                            linearLayoutWeightlifting.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                spinnerCrossfitType = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitType);
                array = getActivity().getResources().getStringArray(R.array.string_array_workout_crossfit_type);
                TextSpinnerAdapter ctAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                ctAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerCrossfitType.setAdapter(ctAdapter);
                spinnerCrossfitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String spinnerSelectionStr = String.valueOf(parent.getSelectedItem());
                        if (spinnerSelectionStr.equals("For Reps & Time")) {
                            linearLayoutForRepsTime.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);
                        } else if (spinnerSelectionStr.equals("For Reps")) {
                            linearLayoutForReps.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);

                        } else if (spinnerSelectionStr.equals("For Time")) {
                            linearLayoutForTime.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);

                        } else if (spinnerSelectionStr.equals("For Weight")) {
                            linearLayoutForWeight.setVisibility(View.VISIBLE);
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);

                        } else if (spinnerSelectionStr.equals("Other")) {
                            linearLayoutCrossfitOther.setVisibility(View.VISIBLE);
                            linearLayoutForRepsTime.setVisibility(View.GONE);
                            linearLayoutForReps.setVisibility(View.GONE);
                            linearLayoutForTime.setVisibility(View.GONE);
                            linearLayoutForWeight.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                //crossfit
                spinnerForRepsReps = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitReps);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter frrAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForRepsReps.setAdapter(frrAdapter);


                spinnerForRepsTime = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitTime);
                array = getActivity().getResources().getStringArray(R.array.string_array_cross_fit_time);
                TextSpinnerAdapter frtAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForRepsTime.setAdapter(frtAdapter);


                spinnerForRepsRounds = (Spinner) dialogView.findViewById(R.id.spinnerForRounds);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter frroAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForRepsRounds.setAdapter(frroAdapter);


                spinnerForReps = (Spinner) dialogView.findViewById(R.id.spinnerForReps);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter frAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                frAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForReps.setAdapter(frAdapter);

                spinnerForTime = (Spinner) dialogView.findViewById(R.id.spinnerCrossfitForTime);
                array = getActivity().getResources().getStringArray(R.array.string_array_cross_fit_time);
                TextSpinnerAdapter ftAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                ftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerForTime.setAdapter(ftAdapter);


                //traditional
                spinnerDistance = (Spinner) dialogView.findViewById(R.id.spinnerDistance);
                array = getActivity().getResources().getStringArray(R.array.string_array_distance);
                TextSpinnerAdapter dAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerDistance.setAdapter(dAdapter);

                spinnerSets = (Spinner) dialogView.findViewById(R.id.spinnerSets);
                array = getActivity().getResources().getStringArray(R.array.string_array_sets);
                TextSpinnerAdapter setsAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                setsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerSets.setAdapter(setsAdapter);

                spinnerReps = (Spinner) dialogView.findViewById(R.id.spinnerReps);
                array = getActivity().getResources().getStringArray(R.array.string_array_reps);
                TextSpinnerAdapter repsAdapter = new TextSpinnerAdapter(getActivity(), R.layout.spinner_text_item, array);
                // Specify the layout to use when the list of choices appears
                repsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerReps.setAdapter(repsAdapter);
                if (!(isDialogShowing)) {
                    mDialog.show();
                    isDialogShowing = true;
                }

                break;
            case R.id.buttonAccept:
                //grab data from fields, create the ParseObject
                String title = etTitle.getText().toString().trim();
                String difficulty = spinnerDifficulty.getSelectedItem().toString();
                String time = spinnerTime.getSelectedItem().toString();

                HashSet<SingleWorkoutItem> mSingleWorkoutItemHashset = new HashSet<SingleWorkoutItem>();
                HashSet<String> mMGHashset = new HashSet<String>();
                Gson gson = new Gson();

                //loop through listview and pull out info from views
                if (!(title.equals(""))) {
                    if (mArrayList.size() > 0) {
                        for (WorkoutView wv : mArrayList) {
                            SingleWorkoutItem mSingleWorkoutItem = new SingleWorkoutItem();
                            mSingleWorkoutItem.set_muscleGroup(wv.getMuscleGroup());
                            mSingleWorkoutItem.set_workoutName(wv.getWorkoutName());
                            mSingleWorkoutItem.set_sets(wv.getSets());
                            mSingleWorkoutItem.set_reps(wv.getReps());

/*                            String mg = wv.getMuscleGroup();
                            // check the name of the workout groups chosen, store the muscle group to later decide what icons to show on the main screen
                            if (mg.equals("Traps") || mg.equals("Shoulders") || mg.equals("Neck")) {
                                mMGHashset.add("Upperbody");
                            } else if (mg.equals("Abdominal") || mg.equals("Lats") || mg.equals("Back") || mg.equals("Chest") || mg.equals("Glutes")) {
                                mMGHashset.add("Chest");
                            } else if (mg.equals("Biceps") || mg.equals("Triceps") || mg.equals("Arms") || mg.equals("Forearms")) {
                                mMGHashset.add("Arms");
                            } else if (mg.equals("Quadriceps") || mg.equals("Legs") || mg.equals("Calves")) {
                                mMGHashset.add("Legs");
                            } else if (mg.equals("Cardio")) {
                                mMGHashset.add("Cardio");
                            }*/
                            mSingleWorkoutItemHashset.add(mSingleWorkoutItem);

                        }

                        String singleWorkoutItemHashsetJSONStr = gson.toJson(mSingleWorkoutItemHashset);


                        //create the single workout object
                        SingleWorkout mSingleWorkout = new SingleWorkout();
                        mSingleWorkout.set_title(title);
                        mSingleWorkout.set_difficulty(difficulty);
                        mSingleWorkout.set_time(time);
                        mSingleWorkout.set_singleWorkoutItemArrayJSON(singleWorkoutItemHashsetJSONStr);

                        mSingleWorkout.set_parseUser(ParseUser.getCurrentUser());

                        mDialog = new MaterialDialog.Builder(getActivity())
                                .content("Lifting Weights..")
                                .progress(true, 0)
                                .show();

                        //save the object in DB, and go back to feed fragment
                        mSingleWorkout.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                mDialog.dismiss();
                                getActivity().getFragmentManager().popBackStack();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Add workout item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etTitle.getText().clear();
                    etTitle.setHintTextColor(getResources().getColor(R.color.material_red_300));
                    etTitle.setHint("Enter Title");
                }
                break;

            case R.id.buttonCancel:
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                isTimerRunning = false;
            }
            if (btnAcceptWorkout.getVisibility() == View.VISIBLE) {
                btnAcceptWorkout.startAnimation(animFadeOut);
                btnAcceptWorkout.setVisibility(View.GONE);
            }
            if (btnCancelWorkout.getVisibility() == View.VISIBLE) {
                btnCancelWorkout.startAnimation(animFadeOut);
                btnCancelWorkout.setVisibility(View.GONE);
            }
        }
        if (scrollState == SCROLL_STATE_IDLE) {
            if (!(isTimerRunning)) {
                isTimerRunning = true;
                mCountDownTimer = new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //do nothing
                    }

                    public void onFinish() {
                        isTimerRunning = false;
                        if ((btnAcceptWorkout.getVisibility() == View.GONE) && (btnCancelWorkout.getVisibility() == View.GONE)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    btnAcceptWorkout.startAnimation(animFadeIn);
                                    btnAcceptWorkout.setVisibility(View.VISIBLE);

                                    btnCancelWorkout.startAnimation(animFadeIn);
                                    btnCancelWorkout.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }.start();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
