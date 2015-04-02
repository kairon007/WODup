package com.modup.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.*;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.modup.adapter.UserFavoriteAdapter;
import com.modup.adapter.UserRecentAdapter;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.modup.utils.ImageUtil;
import com.parse.*;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG = UserFragment.class.getCanonicalName();

    private TextView tvUsername, tvRecent, tvFavorites;

    private OnFragmentInteractionListener mListener;
    private View view;

    private ParseUser currentUser;
    private ListView listViewRecent, listViewFavorites;
    private UserRecentAdapter mRecentAdapter;
    private UserFavoriteAdapter mFavoriteAdapter;

    private ImageView ivProfilePic;

    final int PHOTO_WIDTH = 300;
    final int PHOTO_HEIGHT = 300;
    private final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private Handler mHandler;
    private ParseFile profilePicture;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "OnCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "OnCreateView");
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_user, container, false);
        init();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void init() {
        currentUser = ParseUser.getCurrentUser();
        tvUsername = (TextView) view.findViewById(R.id.textViewTitle);
        tvUsername.setText(currentUser.getUsername());

        ivProfilePic = (ImageView) view.findViewById(R.id.imageViewProfile);
        ivProfilePic.setOnClickListener(this);
        try {
            currentUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    byte[] mBytes = parseObject.getBytes("photo");
                    setPhoto(mBytes);
                }
            });
        } catch (Exception p) {
            Log.e(TAG, p.getMessage());
        }


        tvRecent = (TextView) view.findViewById(R.id.textViewRecent);
        tvRecent.setOnClickListener(this);
        tvFavorites = (TextView) view.findViewById(R.id.textViewFavorites);
        tvFavorites.setOnClickListener(this);

        tvFavorites.setTextColor(getActivity().getResources().getColor(R.color.main_color_grey_500));
        tvRecent.setTextColor(getActivity().getResources().getColor(R.color.primary_blue));


        listViewRecent = (ListView) view.findViewById(R.id.listViewRecent);
        listViewFavorites = (ListView) view.findViewById(R.id.listViewFavorites);
        listViewFavorites.setVisibility(View.GONE);

        mRecentAdapter = new UserRecentAdapter(getActivity());
        mFavoriteAdapter = new UserFavoriteAdapter((getActivity()));
        listViewRecent.setAdapter(mRecentAdapter);
        listViewFavorites.setAdapter(mFavoriteAdapter);

        listViewRecent.setOnItemClickListener(this);
        listViewFavorites.setOnItemClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewRecent:
                listViewRecent.setVisibility(View.VISIBLE);
                listViewFavorites.setVisibility(View.GONE);
                tvFavorites.setTextColor(getActivity().getResources().getColor(R.color.main_color_grey_500));
                tvRecent.setTextColor(getActivity().getResources().getColor(R.color.primary_blue));
                break;
            case R.id.textViewFavorites:
                listViewFavorites.setVisibility(View.VISIBLE);
                listViewRecent.setVisibility(View.GONE);
                tvRecent.setTextColor(getActivity().getResources().getColor(R.color.main_color_grey_500));
                tvFavorites.setTextColor(getActivity().getResources().getColor(R.color.primary_blue));
                break;

            case R.id.imageViewProfile:
                //TODO: Need to choose a picture
                choosePicture();

                break;
        }

    }

    public void choosePicture() {
        Intent cropIntent = new Intent();
        cropIntent.setType("image/*");
        cropIntent.setAction(Intent.ACTION_PICK);
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("outputX", PHOTO_WIDTH);
        cropIntent.putExtra("outputY", PHOTO_HEIGHT);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void setPhoto(final byte[] encodedByteArray) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (encodedByteArray != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);
                        ivProfilePic.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == getActivity().RESULT_OK) {
                final ParseUser currentUser = ParseUser.getCurrentUser();
                Bundle extras = data.getExtras();
                Bitmap bitmap = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bmp_data = stream.toByteArray();

                currentUser.remove("photo");
                currentUser.put("photo", bmp_data);
                currentUser.saveEventually();

/*                profilePicture = new ParseFile(
                        "profilePicture", bmp_data);*/

/*                profilePicture.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e != null) {

                        } else {

                            // check to see if ParseFile exists, if it does
                            // delete it and set new ParseFile
                            currentUser.remove("photo");
                            currentUser.put("photo", profilePicture);
                            currentUser.saveEventually();
                        }
                    }
                });*/

                ivProfilePic.setImageBitmap(bitmap);


            } else if (resultCode == getActivity().RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SingleWorkout mSingleWorkout = (SingleWorkout) parent.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("SINGLEWORKOUT", mSingleWorkout);
                FragmentManager fragmentManager = getFragmentManager();
                Fragment mFragment = new DetailFragment();
                mFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).addToBackStack("DETAILFRAGMENT").commit();

            }
        }, 200);
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
