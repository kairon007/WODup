package com.modup.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Sean on 3/20/2015.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {

    String _comment;
    ParseUser _user;

    public Comment() {
    }

    public String get_comment() {
        return _comment;
    }

    public void set_comment(String _comment) {
        this._comment = _comment;
    }

    public ParseUser get_user() {
        return _user;
    }

    public void set_user(ParseUser _user) {
        this._user = _user;
    }
}
