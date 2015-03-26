package com.modup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.modup.app.R;
import com.modup.view.WorkoutView;

import java.util.List;


public class WorkoutCardsAdapter2 extends ArrayAdapter<WorkoutView> {

    private LayoutInflater mInflater;
    String TAG = WorkoutCardsAdapter2.class.getCanonicalName();
    List<WorkoutView> mViews;

    public WorkoutCardsAdapter2(Context context, List<WorkoutView> views) {
        super(context, 0, views);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViews = views;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        WorkoutView workoutView = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_workout_layout_2, parent, false);
            holder = new ViewHolder();
            holder.tvMuscleGroupItem = (TextView) convertView.findViewById(R.id.textViewMuscleGroupItem);
            holder.tvSetsItem = (TextView) convertView.findViewById(R.id.textViewSetsItem);
            holder.tvRepsItem = (TextView) convertView.findViewById(R.id.textViewRepsItem);
            holder.tvWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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

    private static class ViewHolder {
        public TextView tvMuscleGroupItem;
        public TextView tvSetsItem;
        public TextView tvRepsItem;
        public TextView tvWorkoutName;
    }
}
