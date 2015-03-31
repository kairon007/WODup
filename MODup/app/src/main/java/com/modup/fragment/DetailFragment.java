package com.modup.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.Gson;
import com.modup.adapter.WorkoutCardsAdapter2;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.modup.model.SingleWorkoutItem;
import com.modup.view.WorkoutView;
import com.parse.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG = DetailFragment.class.getCanonicalName();

    private OnFragmentInteractionListener mListener;
    private ImageView ivLike, ivFavorite, ivChat;
    private TextView tvTitle, tvDifficulty, tvTime, tvLike, tvFavorite, tvComment;
    private View view;
    private Boolean isLikeChecked = false;
    private Boolean isFavoriteChecked = false;
    private ListView mListView;
    private WorkoutCardsAdapter2 mWorkoutCardsAdapter;
    private ArrayList<WorkoutView> mArrayList = new ArrayList<WorkoutView>();
    private static SingleWorkout currentSingleWorkout;
    private Gson gson;
    private ParseRelation<ParseUser> likeRelation, favoriteRelation;
    private ParseRelation<SingleWorkout> favoriteUserRelation, likeUserRelation;
    private ParseUser currentUser;
    private Boolean isStillLiked = false;
    private Boolean isOriginalLiked = false;
    private Boolean isStillFavorited = false;
    private Boolean isOriginalFavorited = false;
    private ParseQuery query;
    private ParseRelation relation;

    private int likeCount, favoriteCount;
    private Button btnBack;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        init();
        return view;
    }

    public void init() {
        currentUser = ParseUser.getCurrentUser();
        currentSingleWorkout = (SingleWorkout) getArguments().getSerializable("SINGLEWORKOUT");

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        ivFavorite = (ImageView) view.findViewById(R.id.imageViewFavorite);
        ivFavorite.setOnClickListener(this);
        ivLike = (ImageView) view.findViewById(R.id.imageViewLike);
        ivLike.setOnClickListener(this);
        ivChat = (ImageView) view.findViewById(R.id.imageViewComments);
        ivChat.setOnClickListener(this);

        tvTitle = (TextView) view.findViewById(R.id.textViewTitle);
        tvTime = (TextView) view.findViewById(R.id.textViewTime);
        tvDifficulty = (TextView) view.findViewById(R.id.textViewDifficulty);
        tvLike = (TextView) view.findViewById(R.id.textViewLikeCount);
        tvFavorite = (TextView) view.findViewById(R.id.textViewFavoriteCount);
        tvComment = (TextView) view.findViewById(R.id.textViewCommentCount);


        //set the fields from the currentSingleWorkout
        tvTitle.setText(currentSingleWorkout.get_title());
        tvTime.setText(currentSingleWorkout.get_time());
        tvDifficulty.setText(currentSingleWorkout.get_difficulty());

        //query the like/favorite count
        queryLikeCount();
        queryFavoriteCount();
        queryLikeRelation();
        queryFavoriteRelation();
        //TODO: QUERY COMMENT COUNT


        try {
            JSONArray mJsonArray = new JSONArray(currentSingleWorkout.get_singleWorkoutItemArrayJSON());
            for (int i = 0; i < mJsonArray.length(); i++) {
                WorkoutView mWorkoutView = new WorkoutView(getActivity());
                JSONObject mObj = new JSONObject(mJsonArray.get(i).toString());
                String workoutDesc = mObj.getString("_workoutDesc");
                String workoutName = mObj.getString("_workoutName");
                String sets = mObj.getString("_sets");
                String reps = mObj.getString("_reps");
                String workoutMainCategory = mObj.getString("_workoutMainCategory");
                String workoutType = mObj.getString("_workoutType");
                String workoutCategory = mObj.getString("_workoutCategory");
                String workoutWeight = mObj.getString("_workoutWeight");
                String workoutTime = mObj.getString("_workoutTime");
                String workoutDistance = mObj.getString("_workoutDistance");

                mWorkoutView.setWorkoutName(workoutName);
                mWorkoutView.setWorkoutMainCategory(workoutMainCategory);
                mWorkoutView.setWorkoutCategory(workoutCategory);
                mWorkoutView.setReps(reps);
                mWorkoutView.setSets(sets);
                mWorkoutView.setWorkoutDistance(workoutDistance);
                mWorkoutView.setWorkoutTime(workoutTime);
                mWorkoutView.setWorkoutType(workoutType);
                mWorkoutView.setWorkoutDesc(workoutDesc);
                mWorkoutView.setWorkoutWeight(workoutWeight);

                mArrayList.add(mWorkoutView);

            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }


        mWorkoutCardsAdapter = new WorkoutCardsAdapter2(getActivity(), mArrayList, new WorkoutCardsAdapter2.Callback() {
            @Override
            public void onPressed(int pos) {

            }
        });
        mListView = (ListView) view.findViewById(R.id.listViewWorkoutDetails);
        mListView.setAdapter(mWorkoutCardsAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WorkoutView choosenSingleWorkoutView = mWorkoutCardsAdapter.getItem(position);
                SingleWorkoutItem choosenSingleWorkoutItem = new SingleWorkoutItem();
                //TODO: Transfer data from workoutview to singleworkoutitem

                choosenSingleWorkoutItem.set_workoutMainCategory(choosenSingleWorkoutView.getWorkoutMainCategory());
                choosenSingleWorkoutItem.set_workoutType(choosenSingleWorkoutView.getWorkoutType());
                choosenSingleWorkoutItem.set_workoutCategory(choosenSingleWorkoutView.getWorkoutCategory());
                choosenSingleWorkoutItem.set_workoutDesc(choosenSingleWorkoutView.getWorkoutDesc());
                choosenSingleWorkoutItem.set_workoutDistance(choosenSingleWorkoutView.getWorkoutDistance());
                choosenSingleWorkoutItem.set_reps(choosenSingleWorkoutView.getReps());
                choosenSingleWorkoutItem.set_sets(choosenSingleWorkoutView.getSets());
                choosenSingleWorkoutItem.set_workoutName(choosenSingleWorkoutView.getWorkoutName());
                choosenSingleWorkoutItem.set_workoutTime(choosenSingleWorkoutView.getWorkoutTime());
                choosenSingleWorkoutItem.set_workoutWeight(choosenSingleWorkoutView.getWorkoutWeight());


                Bundle bundle = new Bundle();
                bundle.putSerializable("SINGLEWORKOUTITEM", choosenSingleWorkoutItem);
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new SingleWorkoutItemDetailFragment();
                mFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack("SINGLEWORKOUTITEMDETAILFRAGMENT").commit();

            }
        }, 200);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;


        //TODO: HANDLE STATE OF LIKES/FAVORITES
        if (!(isStillLiked)) {
            unlikeWorkout();
        } else if ((isStillLiked) && !(isStillLiked == isOriginalLiked)){
            likeWorkout();
        }

        if (!(isStillFavorited)) {
            unfavoriteWorkout();
        } else if ((isStillFavorited) && !(isStillFavorited == isOriginalFavorited)){
            favoriteWorkout();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewFavorite:
                if (isFavoriteChecked) {
                    ivFavorite.setImageResource(R.drawable.favorite_blank_content_selector);
                    isFavoriteChecked = false;
                    decreaseFavoriteCount();
                    //unfavorite
                    // unfavoriteWorkout();
                    isStillFavorited = false;

                } else {
                    ivFavorite.setImageResource(R.drawable.favorite_content_selector);
                    isFavoriteChecked = true;
                    increaseFavoriteCount();
                    //favorite
                    //favoriteWorkout();
                    isStillFavorited = true;
                }
                break;
            case R.id.imageViewLike:
                if (isLikeChecked) {
                    ivLike.setImageResource(R.drawable.like_blank_content_selector);
                    isLikeChecked = false;
                    decreaseLikeCount();
                    //unlike
                    //unlikeWorkout();
                    isStillLiked = false;
                } else {
                    ivLike.setImageResource(R.drawable.like_content_selector);
                    isLikeChecked = true;
                    increaseLikeCount();
                    //like
                    //likeWorkout();
                    isStillLiked = true;
                }
                break;

            case R.id.imageViewComments:
                //go to a comment section
                break;

            case R.id.buttonBack:
                getActivity().getFragmentManager().popBackStack();
                break;
        }

    }

    private void likeWorkout() {
        likeRelation = currentSingleWorkout.getRelation("likes");
        likeRelation.add(currentUser);
        currentSingleWorkout.saveEventually();

        likeUserRelation = currentUser.getRelation("user_likes");
        likeUserRelation.add(currentSingleWorkout);
        currentUser.saveEventually();
    }

    private void unlikeWorkout() {
        likeRelation = currentSingleWorkout.getRelation("likes");
        likeRelation.remove(currentUser);
        currentSingleWorkout.saveEventually();

        likeUserRelation = currentUser.getRelation("user_likes");
        likeUserRelation.remove(currentSingleWorkout);
        currentUser.saveEventually();
    }

    private void favoriteWorkout() {
        favoriteRelation = currentSingleWorkout.getRelation("favorites");
        favoriteRelation.add(currentUser);
        currentSingleWorkout.saveEventually();

        favoriteUserRelation = currentUser.getRelation("user_favorites");
        favoriteUserRelation.add(currentSingleWorkout);
        currentUser.saveEventually();

    }

    private void unfavoriteWorkout() {
        favoriteRelation = currentSingleWorkout.getRelation("favorites");
        favoriteRelation.remove(currentUser);
        currentSingleWorkout.saveEventually();

        favoriteUserRelation = currentUser.getRelation("user_favorites");
        favoriteUserRelation.remove(currentSingleWorkout);
        currentUser.saveEventually();
    }

    private void queryLikeCount() {
        relation = currentSingleWorkout.getRelation("likes");
        query = relation.getQuery();
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null) {
                    tvLike.setText(String.valueOf(i));
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void queryFavoriteCount() {
        relation = currentSingleWorkout.getRelation("favorites");
        query = relation.getQuery();
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null) {
                    tvFavorite.setText(String.valueOf(i));
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void queryCommentCount() {
        //TODO: NEED TO IMPLEMENT COMMENTS
    }

    //TODO: NEED TO FIX THESE METHODS

    private void queryLikeRelation() {

        relation = currentUser.getRelation("user_likes");
        query = relation.getQuery();
        query.whereEqualTo("objectId", currentSingleWorkout.getObjectId());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (i == 1) {
                    isStillLiked = true;
                    isOriginalLiked = true;
                    ivLike.setImageResource(R.drawable.like_content_selector);
                    isLikeChecked = true;
                } else {
                    isStillLiked = false;
                    isOriginalLiked = false;
                    ivLike.setImageResource(R.drawable.like_blank_content_selector);
                    isLikeChecked = false;
                }
            }
        });

    }

    private void queryFavoriteRelation() {
        relation = currentUser.getRelation("user_favorites");
        query = relation.getQuery();
        query.whereEqualTo("objectId", currentSingleWorkout.getObjectId());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (i == 1) {
                    isStillFavorited = true;
                    isOriginalFavorited = true;
                    ivFavorite.setImageResource(R.drawable.favorite_content_selector);
                    isFavoriteChecked = true;
                } else {
                    isStillFavorited = false;
                    isOriginalFavorited = false;
                    ivFavorite.setImageResource(R.drawable.favorite_blank_content_selector);
                    isFavoriteChecked = false;
                }
            }
        });

    }

    private void increaseLikeCount() {
        if(!(tvLike.getText().toString().trim().equals(""))) {
            likeCount = Integer.valueOf(tvLike.getText().toString().trim());
            likeCount++;
            tvLike.setText("" + likeCount);
        }
    }

    private void decreaseLikeCount() {
        if(!(tvLike.getText().toString().trim().equals(""))) {
            likeCount = Integer.valueOf(tvLike.getText().toString().trim());
            if (!(likeCount == 0)) {
                likeCount--;
            }
            tvLike.setText("" + likeCount);
        }
    }

    private void increaseFavoriteCount() {
        if(!(tvFavorite.getText().toString().trim().equals(""))) {
            favoriteCount = Integer.valueOf(tvFavorite.getText().toString().trim());
            favoriteCount++;
            tvFavorite.setText("" + favoriteCount);
        }
    }

    private void decreaseFavoriteCount() {
        if(!(tvFavorite.getText().toString().trim().equals(""))) {
            favoriteCount = Integer.valueOf(tvFavorite.getText().toString().trim());
            if (!(favoriteCount == 0)) {
                favoriteCount--;
            }
            tvFavorite.setText("" + favoriteCount);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
