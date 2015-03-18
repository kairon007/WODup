package com.modup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.modup.app.R;
import com.modup.view.WorkoutView;
import org.w3c.dom.Text;

import java.util.List;
import java.util.logging.Handler;


public class WorkoutCardsAdapter extends ArrayAdapter<WorkoutView> {

    private LayoutInflater mInflater;
    String TAG = WorkoutCardsAdapter.class.getCanonicalName();
    Callback mCallback;
    List<WorkoutView> mViews;

    public WorkoutCardsAdapter(Context context, List<WorkoutView> views, Callback callback) {
        super(context, 0, views);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCallback = callback;
        this.mViews = views;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        WorkoutView workoutView = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_workout_layout, parent, false);
            holder = new ViewHolder();
            holder.tvMuscleGroupItem = (TextView) convertView.findViewById(R.id.textViewMuscleGroupItem);
            holder.tvSetsItem = (TextView) convertView.findViewById(R.id.textViewSetsItem);
            holder.tvRepsItem = (TextView) convertView.findViewById(R.id.textViewRepsItem);
            holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
            holder.btnRemoveWorkout = (Button) convertView.findViewById(R.id.btnRemoveWorkout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btnRemoveWorkout.setTag(position);
        holder.btnRemoveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                mCallback.onPressed(position);
            }
        });

        holder.tvWorkoutName.setText(workoutView.getWorkoutName());
        holder.tvMuscleGroupItem.setText(workoutView.getMuscleGroup());
        holder.tvSetsItem.setText(workoutView.getSets());
        holder.tvRepsItem.setText(workoutView.getReps());


        return convertView;
    }

   @Override
    public WorkoutView getItem(final int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    public interface Callback {
        void onPressed(int pos);
    }

    private static class ViewHolder {
        public TextView tvMuscleGroupItem;
        public TextView tvSetsItem;
        public TextView tvRepsItem;
        public TextView tvWorkoutName;
        public Button btnRemoveWorkout;
    }
}
