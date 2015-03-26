package com.modup.model;

import java.util.HashSet;

/**
 * Created by Sean on 3/17/2015.
 */
public class SingleWorkoutItem {
    String _workoutName, _muscleGroup, _sets, _reps;


    public SingleWorkoutItem() {
    }

    public String get_workoutName() {
        return _workoutName;
    }

    public void set_workoutName(String _workoutName) {
        this._workoutName = _workoutName;
    }

    public String get_muscleGroup() {
        return _muscleGroup;
    }

    public void set_muscleGroup(String _muscleGroup) {
        this._muscleGroup = _muscleGroup;
    }

    public String get_sets() {
        return _sets;
    }

    public void set_sets(String _sets) {
        this._sets = _sets;
    }

    public String get_reps() {
        return _reps;
    }

    public void set_reps(String _reps) {
        this._reps = _reps;
    }

}
