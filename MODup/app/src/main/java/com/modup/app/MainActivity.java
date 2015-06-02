package com.modup.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.*;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    TextView tvSignIn, tvSignUp, tvRecover;
    EditText etUsername, etPassword1, etEmail;
    String username, password1;
    MaterialDialog mDialog;
    private MaterialDialog mRecoverPasswordDialog;
    String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().getThemedContext();
        //getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (!(ParseUser.getCurrentUser() == null)) {
            goToNavigationActivity();
        } else {
            tvSignUp = (TextView) findViewById(R.id.textViewSignUp);
            tvSignUp.setOnClickListener(this);
            tvSignIn = (TextView) findViewById(R.id.textViewSignIn);
            tvSignIn.setOnClickListener(this);
            tvRecover = (TextView) findViewById(R.id.textViewRecover);
            tvRecover.setOnClickListener(this);
            etUsername = (EditText) findViewById(R.id.etUsername);
            etPassword1 = (EditText) findViewById(R.id.etPassword1);
        }
    }

    public void goToRegisterActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void goToNavigationActivity() {
        Intent intent = new Intent(this, LeftMenusActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewSignUp:
                goToRegisterActivity();
                break;
            case R.id.textViewSignIn:
                if (checkFields()) {
                    Log.e(TAG, "Check Fields Passed");
                    mDialog = new MaterialDialog.Builder(this)
                            .content("Maximizing Gains..")
                            .progress(true, 0)
                            .show();

                    ParseUser.logInInBackground(username, password1, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (!(parseUser == null)) {
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
                etEmail = (EditText) recoverPasswordDialogView.findViewById(R.id.etEmail);
                mRecoverPasswordDialog.show();
                break;
        }

    }

    public Boolean checkFields() {
        username = etUsername.getText().toString().trim();
        password1 = etPassword1.getText().toString().trim();
        if (!(isAnyEmpty(username, password1))) {
            return true;
        }
        return false;
    }

    public Boolean isAnyEmpty(String username, String password1) {
        if (username.equals("") || username.isEmpty()) {
            setEmptyError(etUsername, "username");
            return true;
        }
        if (password1.equals("") || password1.isEmpty()) {
            setEmptyError(etPassword1, "password1");
            return true;
        }
        return false;
    }

    public void setEmptyError(EditText et, String etName) {
        if (etName.equals("username")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Username");
        } else if (etName.equals("password1")) {
            et.getText().clear();
            et.setHintTextColor(getResources().getColor(R.color.material_red_300));
            et.setHint("Enter Password");
        }
    }


}
