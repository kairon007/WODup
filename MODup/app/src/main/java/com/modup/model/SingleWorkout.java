package com.modup.model;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Sean on 3/2/2015.
 */
@ParseClassName("SingleWorkout")
public class SingleWorkout extends ParseObject implements Serializable {
    String _title, _difficulty, _time;
    ParseUser _parseUser;
    String _singleWorkoutItemArrayJSON, _muscleGroupItemArrayJSON;
    int _favoriteCount, _likeCount, _commentCount;

    public SingleWorkout() {
    }

    public String get_title() {
        return getString("title");
    }

    public void set_title(String _title) {
        this._title = _title;
        put("title", _title);
    }

    public String get_difficulty() {
        return getString("difficulty");
    }

    public void set_difficulty(String _difficulty) {
        this._difficulty = _difficulty;
        put("difficulty", _difficulty);
    }

    public String get_time() {
        return getString("time");
    }

    public void set_time(String _time) {
        this._time = _time;
        put("time", _time);
    }

    public String get_singleWorkoutItemArrayJSON() {
        return getString("singleWorkoutItemArrayJSON");
    }

    public void set_singleWorkoutItemArrayJSON(String _singleWorkoutItemArrayJSON) {
        this._singleWorkoutItemArrayJSON = _singleWorkoutItemArrayJSON;
        put("singleWorkoutItemArrayJSON", _singleWorkoutItemArrayJSON);
    }

    public String get_muscleGroupItemArrayJSON() {
        return getString("muscleGroupItemArrayJSON");
    }

    public void set_muscleGroupItemArrayJSON(String _muscleGroupItemArrayJSON) {
        this._muscleGroupItemArrayJSON = _muscleGroupItemArrayJSON;
        put("muscleGroupItemArrayJSON", _muscleGroupItemArrayJSON);
    }

    public ParseUser get_parseUser() {
        return getParseUser("user");
    }

    public void set_parseUser(ParseUser _parseUser) {
        this._parseUser = _parseUser;
        put("user", _parseUser);
    }


    public int get_favoriteCount() {
        return _favoriteCount;
    }

    public void set_favoriteCount(int _favoriteCount) {
        this._favoriteCount = _favoriteCount;
    }

    public int get_likeCount() {
        return _likeCount;
    }

    public void set_likeCount(int _likeCount) {
        this._likeCount = _likeCount;
    }

    public int get_commentCount() {
        return _commentCount;
    }

    public void set_commentCount(int _commentCount) {
        this._commentCount = _commentCount;
    }
}
