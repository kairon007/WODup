package com.modup.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.modup.adapter.ParseCommentAdapter;
import com.modup.app.R;
import com.modup.model.Comment;
import com.modup.model.SingleWorkout;
import com.parse.*;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Button btnAdd, btnBack;
    private View view;
    private MaterialDialog mCommentDialog;
    private EditText etComment;
    private ParseCommentAdapter commentAdapter;
    private SingleWorkout currentSingleWorkout;
    private ListView listView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentFragment() {
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

        view = inflater.inflate(R.layout.fragment_comment, container, false);
        init();
        return view;
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

    private void init() {
        btnAdd = (Button) view.findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(this);

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        currentSingleWorkout = (SingleWorkout) getArguments().getSerializable("SINGLEWORKOUT");
        listView = (ListView) view.findViewById(R.id.listViewComment);


        commentAdapter = new ParseCommentAdapter(getActivity(), currentSingleWorkout);
        listView.setAdapter(commentAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                getActivity().getFragmentManager().popBackStack();
                break;
            case R.id.buttonAdd:

                boolean wrapInScrollView = false;
                mCommentDialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_comment, wrapInScrollView)
                        .title("Add Comment")
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);

                                String commentStr = etComment.getText().toString();
                                final Comment mComment = new Comment();
                                mComment.setUser(ParseUser.getCurrentUser());
                                mComment.setComment(commentStr);

                                mComment.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParseRelation<Comment> relation = currentSingleWorkout
                                                .getRelation("comments");
                                        relation.add(mComment);
                                        currentSingleWorkout.saveInBackground(new SaveCallback() {

                                            @Override
                                            public void done(ParseException arg0) {
                                                // TODO Auto-generated method stub
                                                commentAdapter.notifyDataSetChanged();
                                                commentAdapter.loadObjects();
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        }).build();


                View mCommentDialogView = mCommentDialog.getCustomView();
                etComment = (EditText) mCommentDialogView.findViewById(R.id.etComment);
                mCommentDialog.show();


                break;
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
