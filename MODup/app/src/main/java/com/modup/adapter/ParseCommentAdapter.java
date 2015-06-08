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
import com.modup.model.Comment;
import com.modup.model.SingleWorkout;
import com.parse.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Sean on 3/21/2015.
 */
public class ParseCommentAdapter extends ParseQueryAdapter<Comment> {
    String TAG = ParseCommentAdapter.class.getCanonicalName();
    private LayoutInflater mInflater;
    private Gson gson;


    public ParseCommentAdapter(Context context, final SingleWorkout workout) {
        super(context, new ParseQueryAdapter.QueryFactory<Comment>() {
            public ParseQuery<Comment> create() {
                ParseRelation relation = workout.getRelation("comments");
                ParseQuery query = relation.getQuery();
                query.orderByAscending("createdAt");
                return query;
            }
        });
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getItemView(Comment comment, View convertView, ViewGroup parent) {
        super.getItemView(comment, convertView, parent);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_comment, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.imageViewProfilePic);
            holder.tvComment = (TextView) convertView.findViewById(R.id.textViewComment);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvComment.setText(comment.getComment());

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", comment.getUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    ParseUser mUser = objects.get(0);

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
                            } catch (Exception f) {
                                Log.e(TAG, f.getMessage());
                            }
                        }
                    });


                } else {
                    // Something went wrong.
                }
            }
        });
        return convertView;

    }

    private static class ViewHolder {
        public ImageView ivImage;
        public TextView tvComment;
    }
}
