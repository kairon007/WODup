package com.modup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by Sean on 2/28/2015.
 */
public class UserFavoriteAdapter extends ParseQueryAdapter<SingleWorkout> {
    String TAG = ParseWorkoutAdapter.class.getCanonicalName();
    private LayoutInflater mInflater;
    private Gson gson;


    public UserFavoriteAdapter(Context context) {
        super(context, new QueryFactory<SingleWorkout>() {
            public ParseQuery<SingleWorkout> create() {
/*                ParseQuery<SingleWorkout> query = new ParseQuery<SingleWorkout>(
                        "SingleWorkout");*/
                ParseRelation relation = ParseUser.getCurrentUser().getRelation("user_favorites");
                ParseQuery query = relation.getQuery();
                query.setLimit(50);
                query.orderByDescending("updatedAt");
                return query;
            }
        });
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getItemView(SingleWorkout workout, View convertView, ViewGroup parent) {
        super.getItemView(workout, convertView, parent);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_recent_favorites, parent, false);
            holder = new ViewHolder();
            holder.tvWorkoutTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvWorkoutTitle.setText(workout.get_title());


        return convertView;

    }

    private static class ViewHolder {
        public TextView tvWorkoutTitle;
    }


}