package com.modup.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;


public class SignUpActivity extends ActionBarActivity implements OnClickListener {
    TextView tvBack, tvSignUp, tvRecover;
    EditText etEmail, etUsername, etPassword1, etPassword2, etResetEmail;
    String email, username, password1, password2;
    String TAG = SignUpActivity.class.getCanonicalName();
    MaterialDialog mDialog;
    private MaterialDialog mRecoverPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void init() {
        tvSignUp = (TextView) findViewById(R.id.textViewSignUp);
        tvSignUp.setOnClickListener(this);
        tvBack = (TextView) findViewById(R.id.textViewBack);
        tvBack.setOnClickListener(this);
        tvRecover = (TextView) findViewById(R.id.textViewRecover);
        tvRecover.setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword1 = (EditText) findViewById(R.id.etPassword1);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewBack:
                goToMainActivity();
                break;
            case R.id.textViewSignUp:
                if (checkFields()) {
                    //create parse user
                    Log.e(TAG, "Check Fields Passed");
                    mDialog = new MaterialDialog.Builder(this)
                            .content("Maximizing Gains..")
                            .progress(true, 0)
                            .show();
                    ParseUser mUser = new ParseUser();
                    mUser.setUsername(username);
                    mUser.setEmail(email);
                    mUser.setPassword(password1);
                    mUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                mDialog.dismiss();
                                goToNavigationActivity();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.textViewRecover:
                boolean wrapInScrollView = false;
                mRecoverPasswordDialog = new MaterialDialog.Builder(this)
                        .customView(R.layout.dialog_recover_password, wrapInScrollView)
                        .title("Reset Password")
                        .positiveText("HURRY!")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                String email = etEmail.getText().toString().trim();
                                ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "Password reset request sent",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error with password reset request",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                            }
                        }).build();

                View recoverPasswordDialogView = mRecoverPasswordDialog.getCustomView();
                etResetEmail = (EditText) recoverPasswordDialogView.findViewById(R.id.etEmail);
                mRecoverPasswordDialog.show();


                break;
        }

    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToNavigationActivity() {
        Intent intent = new Intent(this, LeftMenusActivity.class);
        startActivity(intent);
    }

    public Boolean checkFields() {
        email = etEmail.getText().toString().trim();
        username = etUsername.getText().toString().trim();
        password1 = etPassword1.getText().toString().trim();
        password2 = etPassword2.getText().toString().trim();
        if (!(isAnyEmpty(email, username, password1, password2))) {
            if (isPasswordsMatching(password1, password2)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public Boolean isAnyEmpty(String email, String username, String password1, String password2) {
        if (email.equals("") || email.isEmpty()) {
            setEmptyError(etEmail, "email");
            return true;
        }
        if (username.equals("") || username.isEmpty()) {
            setEmptyError(etUsername, "username");
            return true;
        }
        if (password1.equals("") || password1.isEmpty()) {
            setEmptyError(etPassword1, "password1");
            return true;
        }
        if (password2.equals("") || password2.isEmpty()) {
            setEmptyError(etPassword2, "password2");
            return true;
        }
        return false;
    }

    public Boolean isPasswordsMatching(String password1, String password2) {
        if (password1.equals(password2)) {
            return true;
        } else {
            setPasswordError(etPassword1);
            setPasswordError(etPassword2);
            return false;
        }
    }

    public void setEmptyError(EditText et, String etName) {
        if (etName.equals("email")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Email");
        } else if (etName.equals("username")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Username");
        } else if (etName.equals("password1")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Password");
        }
        if (etName.equals("password2")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Password");
        }
    }

    public void setPasswordError(EditText et) {
        et.getText().clear();
        et.setHintTextColor(getResources().getColor(R.color.material_red_300));
        et.setHint("Unmatched Passwords");
    }

}
