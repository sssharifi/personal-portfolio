package com.cmsc436.final_project.lostandfoundapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*@author: Philip Gouldman, Fall 2018 CMSC436
 * created on: 11/5/2018
 * */


public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    // The necessary instance variables to interact with the text fields and buttons.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView forgotpassword;

    private static final int REQUEST_READ_CONTACTS = 0;

    // This object authenticates the login credentials. To use this I needed to use the library
    // found at this link: https://firebase.google.com/docs/android/setup?authuser=0
    // and put it in the gradle file for the app module. You should see it in there with a
    // detailed comment.

    /*Information for the FirebaseAuth object can be found here:
     * https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth.html#getCurrentUser()
     * */
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mEmailView = findViewById(R.id.email);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // If there is a current user, they'll be taken to their home page.
        if (mFirebaseAuth.getCurrentUser() != null) {
            Log.i(TAG, "Current user is still logged in");
            Intent intent = new Intent(this, NavTabsActivity.class);

            // Finish this and since a user is logged in go to the users Home page.
            finish();
            startActivity(intent);

        }

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        // The logic for the 'Sign in' button.
        Button mEmailSignInButton = (Button) findViewById(R.id.Login_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        // The logic for the 'Register' button.
        Button mRegisterButton = findViewById(R.id.SignUp_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


        // The logic for the 'forgot password' textview.
        forgotpassword = (TextView)findViewById(R.id.ForgotPW);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PasswordActivity.class));
            }
        });

    }

    protected void attemptLogin() {

        // Store values at the time of the login attempt.

        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        // Check if text fields are empty. If so just alert user with a toast to stop being
        // stupid.

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter a valid email or password", Toast.LENGTH_LONG).show();
            return;
        }

        // Start the authentication process with firebase!
        Log.i(TAG, "Trying to login with credentials" + email + " : " +password);
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Verify if the login was successful.
                if (task.isSuccessful()) {
                   // Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();

                    // Upon successful login, Start the next activity to go to next screen
                    // TODO: make an intent to start the next activity to take user to next screen.
                    // This may require us to pass in some extra information. I'm not sure yet.
                    Log.i(TAG, "Login was successful, terminating the login activity");
                    finish();

                    // Start the navtabs activity, this is the users homescreen.
                    Intent intent = new Intent(getApplicationContext(), NavTabsActivity.class);
                    startActivity(intent);

                } else {
                    // Login failed. Alert with a toast and clear the fields.
                    Toast.makeText(LoginActivity.this, "Login failed. Invalid email or password.", Toast.LENGTH_LONG).show();

                    mEmailView.getText().clear();
                    mPasswordView.getText().clear();
                }
            }
        });
    }

    protected void registerUser() {
        //Log.i(TAG, "Starting Registration activty");

        // TODO: When the activity created for registering a user is created, replace
        // LoginActivity.class with that activity name instead.
        Intent intent = new Intent(this, CreateAccountActivity.class);

        //finish();
        startActivity(intent);
    }

}
