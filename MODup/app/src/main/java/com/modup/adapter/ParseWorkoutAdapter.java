package com.modup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.modup.app.R;
import com.modup.model.DummyModel;
import com.modup.model.SingleWorkout;
import com.modup.utils.ImageUtil;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashSet;

/**
 * Created by Sean on 3/21/2015.
 */
public class ParseWorkoutAdapter extends ParseQueryAdapter<SingleWorkout> {
    String TAG = ParseWorkoutAdapter.class.getCanonicalName();
    private LayoutInflater mInflater;
    private Gson gson;
    private HashSet<String> mMGHashset = new HashSet<String>();
    private Boolean isPeek1Set = false;
    private Boolean isPeek2Set = false;
    private Boolean isPeek3Set = false;


    public ParseWorkoutAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<SingleWorkout>() {
            public ParseQuery<SingleWorkout> create() {
                ParseQuery<SingleWorkout> query = new ParseQuery<SingleWorkout>(
                        "SingleWorkout");
                query.setLimit(50);
                query.orderByDescending("createdAt");
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
            convertView = mInflater.inflate(R.layout.list_item_cards, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.imageCards);
            holder.ivPeek1 = (ImageView) convertView.findViewById(R.id.imageViewPeek1);
            holder.ivPeek2 = (ImageView) convertView.findViewById(R.id.imageViewPeek2);
            holder.ivPeek3 = (ImageView) convertView.findViewById(R.id.imageViewPeek3);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.tvDifficulty = (TextView) convertView.findViewById(R.id.textViewDifficulty);
            holder.tvTime = (TextView) convertView.findViewById(R.id.textViewTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //needed to get User profile pic for parse feed
/*      // Add and download the image
        ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.ivMap);
        ParseFile imageFile = event.getParseFile("photo");
        if (imageFile != null) {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground();
        }*/

        holder.tvTitle.setText(workout.get_title());
        holder.tvTime.setText(workout.get_time());
        holder.tvDifficulty.setText(workout.get_difficulty());

        try {
            JSONArray mJsonArray = new JSONArray(workout.get_muscleGroupItemArrayJSON());
            int count = mJsonArray.length();
            for (int i = 0; i <= mJsonArray.length(); i++) {
                String mg = mJsonArray.getString(i);
                if (!(isPeek1Set)) {

                    if (mg.equals("Chest")) {
                        holder.ivPeek1.setImageResource(R.drawable.chest);
                        isPeek1Set = true;
                    } else if (mg.equals("Cardio")) {
                        holder.ivPeek1.setImageResource(R.drawable.cardio);
                        isPeek1Set = true;
                    } else if (mg.equals("Upperbody")) {
                        holder.ivPeek1.setImageResource(R.drawable.upperbody);
                        isPeek1Set = true;
                    } else if (mg.equals("Legs")) {
                        holder.ivPeek1.setImageResource(R.drawable.legs);
                        isPeek1Set = true;
                    } else if (mg.equals("Arms")) {
                        holder.ivPeek1.setImageResource(R.drawable.arm);
                        isPeek1Set = true;
                    }

                } else if (!(isPeek2Set)) {
                    if (mg.equals("Chest")) {
                        holder.ivPeek2.setImageResource(R.drawable.chest);
                        isPeek2Set = true;
                    } else if (mg.equals("Cardio")) {
                        holder.ivPeek2.setImageResource(R.drawable.cardio);
                        isPeek2Set = true;
                    } else if (mg.equals("Upperbody")) {
                        holder.ivPeek2.setImageResource(R.drawable.upperbody);
                        isPeek2Set = true;
                    } else if (mg.equals("Legs")) {
                        holder.ivPeek2.setImageResource(R.drawable.legs);
                        isPeek2Set = true;
                    } else if (mg.equals("Arms")) {
                        holder.ivPeek2.setImageResource(R.drawable.arm);
                        isPeek2Set = true;
                    }

                } else if (!(isPeek3Set)) {
                    if (mg.equals("Chest")) {
                        holder.ivPeek3.setImageResource(R.drawable.chest);
                        isPeek3Set = true;
                    } else if (mg.equals("Cardio")) {
                        holder.ivPeek3.setImageResource(R.drawable.cardio);
                        isPeek3Set = true;
                    } else if (mg.equals("Upperbody")) {
                        holder.ivPeek3.setImageResource(R.drawable.upperbody);
                        isPeek3Set = true;
                    } else if (mg.equals("Legs")) {
                        holder.ivPeek3.setImageResource(R.drawable.legs);
                        isPeek3Set = true;
                    } else if (mg.equals("Arms")) {
                        holder.ivPeek3.setImageResource(R.drawable.arm);
                        isPeek3Set = true;
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isPeek1Set = false;
        isPeek2Set  = false;
        isPeek3Set = false;

        //still need to populate the ivPeek with icons

        return convertView;

    }

    private static class ViewHolder {
        public ImageView ivImage, ivPeek1, ivPeek2, ivPeek3;
        public TextView tvTitle, tvDifficulty, tvTime;
    }


}
