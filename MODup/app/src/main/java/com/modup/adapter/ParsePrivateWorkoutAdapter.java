package com.modup.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.parse.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

/**
 * Created by Sean on 3/21/2015.
 */
public class ParsePrivateWorkoutAdapter extends ParseQueryAdapter<SingleWorkout> {
    String TAG = ParsePrivateWorkoutAdapter.class.getCanonicalName();
    private LayoutInflater mInflater;
    private Gson gson;


    public ParsePrivateWorkoutAdapter(Context context, final Date eventDate) {
        super(context, new QueryFactory<SingleWorkout>() {
            public ParseQuery<SingleWorkout> create() {
                ParseQuery<SingleWorkout> query = new ParseQuery<SingleWorkout>(
                        "SingleWorkout");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereEqualTo("event_date", eventDate);
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
            holder.ivImage = (ImageView) convertView.findViewById(R.id.imageViewProfilePic);
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

        holder.tvTitle.setText(workout.get_title());
        holder.tvTime.setText(workout.get_time());
        holder.tvDifficulty.setText(workout.get_difficulty());

        try {
            JSONArray mJsonArray = new JSONArray(workout.get_pictureArrayJSON());
            String pictureVal1 = "w" +mJsonArray.getString(0);
            String pictureVal2 = "w" + mJsonArray.getString(1);
            String pictureVal3 = "w" + mJsonArray.getString(2);

            holder.ivPeek1.setImageResource(getContext().getResources().getIdentifier(pictureVal1, "drawable", getContext().getPackageName()));
            holder.ivPeek2.setImageResource(getContext().getResources().getIdentifier(pictureVal2, "drawable", getContext().getPackageName()));
            holder.ivPeek3.setImageResource(getContext().getResources().getIdentifier(pictureVal3, "drawable", getContext().getPackageName()));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //needed to get User profile pic for parse feed
        try{
            ParseUser mUser = workout.getParseUser("user");
            mUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    try {
                        final byte[] mBytes = parseObject.getBytes("photo");
                        if (mBytes != null) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
                                    holder.ivImage.setImageBitmap(bitmap);
                                }
                            });
                        }
                    } catch (Exception f){
                        Log.e(TAG, f.getMessage());
                    }
                }
            });
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        //still need to populate the ivPeek with icons

        return convertView;

    }

    private static class ViewHolder {
        public ImageView ivImage, ivPeek1, ivPeek2, ivPeek3;
        public TextView tvTitle, tvDifficulty, tvTime;
    }
}
