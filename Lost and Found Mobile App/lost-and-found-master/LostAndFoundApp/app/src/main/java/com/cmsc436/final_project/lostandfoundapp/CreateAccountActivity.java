package com.cmsc436.final_project.lostandfoundapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.autofill.AutofillManager;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mconfirmPass;
    private EditText mUserName;
    private CircleImageView mProfileImage;
    private DatabaseReference database;
    private static final String TAG = "CreateAccount";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mEmailView =  findViewById(R.id.createEmail);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mPasswordView = findViewById(R.id.createPassword);
        mconfirmPass = findViewById(R.id.confirmPassword);
        mUserName = findViewById(R.id.username);
        mProfileImage = findViewById(R.id.profileImage);
        database = FirebaseDatabase.getInstance().getReference("users");
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        Button createAccount = (Button)findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

    }

    private void createUser(){
        final String email = mEmailView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();
        String confirm = mconfirmPass.getText().toString().trim();
        final String username = mUserName.getText().toString().trim();
        final String imageUrl = "default";

        // check to make sure every field is filled in
        if(email.equals("") || password.equals("") || username.equals("")){
            Toast.makeText(this, "Enter a valid email, username, or password", Toast.LENGTH_LONG).show();
            return;
        }
        // password and username must be minimum length
        if(password.length() < 6 ) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if(username.length() < 4) {
            Toast.makeText(this, "Username must be at least 4 characters", Toast.LENGTH_LONG).show();
            return;
        }
        // password and confirm password must match
        if(!password.equals(confirm)){
            Toast.makeText(this, "Make sure password fields match!", Toast.LENGTH_LONG).show();
            return;
        }
        Query usernameQuery = database.orderByChild("username").equalTo(username);

        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean usernameExists = false;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    usernameExists = true;
                }
                if(!usernameExists) {
                    // create entity in firebase authentication
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Verify if the account creation was successful.
                            if (task.isSuccessful()) {
                                // create key in real time database
                                mProfileImage.setImageResource(R.mipmap.ic_launcher);
                                String id = database.push().getKey();
                                database.child(id).setValue(new Users(email, 0, username, imageUrl, id));
                                FirebaseUser newUser = mFirebaseAuth.getCurrentUser();
                                // This fragment of code will set the display name of the logged in user so it is
                                // easily retrievable using the method for a firebase user, getDisplayName().
                                if (newUser != null) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username).build();
                                    newUser.updateProfile(profileUpdates);
                                }
                                Toast.makeText(CreateAccountActivity.this, "Account Created", Toast.LENGTH_LONG).show();

                                // Upon successful login, Start the next activity to go to next screen
                                // TODO: make an intent to start the next activity to take user to next screen.
                                // This may require us to pass in some extra information. I'm not sure yet.
                                Log.i(TAG, "Account successfully created");
                                Intent intent = new Intent(CreateAccountActivity.this, NavTabsActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(CreateAccountActivity.this, "Account already exists, try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(CreateAccountActivity.this, "Username already in use, try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

