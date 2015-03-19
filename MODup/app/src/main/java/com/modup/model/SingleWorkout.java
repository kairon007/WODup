package com.modup.model;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashSet;

/**
 * Created by Sean on 3/2/2015.
 */
@ParseClassName("SingleWorkout")
public class SingleWorkout extends ParseObject {
    String _title, _difficulty, _time;
    HashSet<SingleWorkoutItem> _workoutHashSet;
    HashSet<String> _muscleGroupIcons;

    public SingleWorkout() {
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_difficulty() {
        return _difficulty;
    }

    public void set_difficulty(String _difficulty) {
        this._difficulty = _difficulty;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public HashSet<SingleWorkoutItem> get_workoutHashSet() {
        return _workoutHashSet;
    }

    public void set_workoutHashSet(HashSet<SingleWorkoutItem> _workoutHashSet) {
        this._workoutHashSet = _workoutHashSet;
    }

    public HashSet<String> get_muscleGroupIcons() {
        return _muscleGroupIcons;
    }

    public void set_muscleGroupIcons(HashSet<String> _muscleGroupIcons) {
        this._muscleGroupIcons = _muscleGroupIcons;
    }
}
