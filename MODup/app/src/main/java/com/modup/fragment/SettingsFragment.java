package com.modup.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.modup.app.MainActivity;
import com.modup.app.R;
import com.modup.view.WorkoutView;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button btnBack;
    private TextView tvChangePassword, tvLinkFacebook;
    private View view;
    private EditText etPassword1, etPassword2;
    private ParseUser currentUser;

    private MaterialDialog mChangePasswordDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
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
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        init();
        return view;
    }

    private void init() {
        currentUser = ParseUser.getCurrentUser();

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        tvChangePassword = (TextView) view.findViewById(R.id.textViewChangePassword);
        tvChangePassword.setOnClickListener(this);

        tvLinkFacebook = (TextView) view.findViewById(R.id.textViewLinkFacebook);
        tvLinkFacebook.setOnClickListener(this);


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
            case R.id.buttonBack:
                getActivity().getFragmentManager().popBackStack();
                break;

            case R.id.textViewChangePassword:
                Log.e("BUTTON", "BUTTON PRESSED");
                boolean wrapInScrollView = false;
                mChangePasswordDialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_change_password, wrapInScrollView)
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                String pass1 = etPassword1.getText().toString().trim();
                                String pass2 = etPassword2.getText().toString().trim();

                                if (!pass1.equals("") && !pass2.equals("") && pass1.equals(pass2)) {
                                    currentUser.put("password", pass1);
                                    currentUser.saveInBackground(new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e != null) {
                                            }
                                        }
                                    });
                                } else {
                                    etPassword1.getText().clear();
                                    etPassword2.getText().clear();
                                    Toast.makeText(getActivity(), "Passwords do not match!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                            }
                        }).build();

                View changePasswordDialogView = mChangePasswordDialog.getCustomView();
                etPassword1 = (EditText) changePasswordDialogView.findViewById(R.id.etPassword1);
                etPassword2 = (EditText) changePasswordDialogView.findViewById(R.id.etPassword2);
                mChangePasswordDialog.show();
                break;

            case R.id.textViewLinkFacebook:
                //TODO: ADD PERMISSION WHERE NULL

                if (!ParseFacebookUtils.isLinked(currentUser)) {
                    ParseFacebookUtils.linkWithReadPermissionsInBackground(currentUser, getActivity(), null, new SaveCallback() {
                        @Override
                        public void done(ParseException ex) {
                            if (ParseFacebookUtils.isLinked(currentUser)) {
                                Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                            }
                        }
                    });
                }
                break;
        }
    }

/*    private void notifyUser() {
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);

        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.logo);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getActivity(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.notification_logo)
                        .setLargeIcon(icon)
                        .setContentTitle("WODup")
                        .setContentText("You have workouts waiting! Go get 'em!")
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);


        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }*/

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
