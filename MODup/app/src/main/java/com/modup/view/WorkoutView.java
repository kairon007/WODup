package com.modup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.modup.model.SingleWorkout;

/**
 * Created by Sean on 3/2/2015.
 */
public class WorkoutView extends LinearLayout {


    public TextView tvMuscleGroup, tvWorkoutName, tvSets, tvReps;
    public Button btnRemoveWorkout;
    String workoutName, muscleGroup, sets, reps;
    int id;
    LayoutInflater inflater;
    Context mContext;

    public WorkoutView(Context context) {
        super(context);
        mContext = context;

    }

    public WorkoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public WorkoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public SingleWorkout getAll() {
        //to be designed
        return null;
    }
}
