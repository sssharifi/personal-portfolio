package com.cmsc436.final_project.lostandfoundapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.firebase.auth.EmailAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private String TAG = "Update Profile Fragment";
    //private EditText mPasswordView;

    DatabaseReference databaseUsers;
    String mEmail,mPassword,mCpassword,oldPassword;
    EditText username, password,cPassword,currentPassword;
    TextView email, editImage;
    FirebaseUser user;
    Button cancel, update;
    Users mUser;
    CircleImageView profileImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragview = inflater.inflate(R.layout.fragment_update_profile, container, false);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mEmail = user.getEmail();

        email = fragview.findViewById(R.id.updateUserEmail);
        password = fragview.findViewById(R.id.changePassword);
        cPassword = fragview.findViewById(R.id.confirmPasswordChanged);
        currentPassword = fragview.findViewById(R.id.oldPassword);

        cancel = fragview.findViewById(R.id.cancel);
        update = fragview.findViewById(R.id.btProfileUpdateSubmit);
        profileImage = fragview.findViewById(R.id.updateProfileImage);
        email.setText(mEmail);




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.tab_fragment_container,new UserProfileFragment());
                ft.commit();
            }
        });

        editImage = (TextView) fragview.findViewById(R.id.editProfileImage);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.tab_fragment_container,new EditProfileFragment());
                ft.commit();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPassword = password.getText().toString().trim();
                mCpassword = cPassword.getText().toString().trim();
                oldPassword = currentPassword.getText().toString().trim();

                AuthCredential credential = EmailAuthProvider.getCredential(mEmail,oldPassword);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if(!mPassword.equals(mCpassword)){
                                        Toast.makeText(getContext(),"Please check if both passwords are the same", Toast.LENGTH_LONG).show();
                                    }else if(mPassword.length() < 6 ) {
                                        Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
                                    }else{
                                        user.updatePassword(mPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Password updated");
                                                    Toast.makeText(getContext(), "Password has been updated, Please login with your new password", Toast.LENGTH_LONG).show();
                                                    password.getText().clear();
                                                    cPassword.getText().clear();
                                                    firebaseAuth.signOut();
                                                    moveToNewActivity();
                                                } else {
                                                    Log.d(TAG, "Error password not updated");
                                                    Toast.makeText(getContext(), "Password could not be changed", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }else{
                                    Log.d(TAG, "Error auth failed");
                                    Toast.makeText(getContext(), "Old Password is incorrect!! Please Check", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        return fragview;
    }

    private void moveToNewActivity() {
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        ((Activity) getActivity()).overridePendingTransition(0,0);
    }


    public void onStart(){
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String username = snapshot.child("username").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String id = snapshot.child("id").getValue().toString();

                    int rating = snapshot.child("rating").getValue(Integer.class);
                    String imageUrl = snapshot.child("imageURL").getValue(String.class);
                    if(email.equals(mEmail)) {
                        mUser = new Users(email, rating, username,imageUrl,id);
                        Log.i(TAG, "onDataChange: " + mUser.getUsername() + " " +
                                mUser.getRating() + " " + mUser.getEmail());

                        if (mUser.getImageURL().equals("default")) {
                            profileImage.setImageResource(R.mipmap.ic_launcher);

                        } else {
                            Glide.with(getContext()).load(mUser.getImageURL()).into(profileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
