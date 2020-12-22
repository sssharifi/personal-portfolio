package com.cmsc436.final_project.lostandfoundapp;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.net.URI;
import java.util.ArrayList;
import android.net.Uri;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private String TAG = "User Profile Fragment";
    Users mUser;
    DatabaseReference databaseUsers;
    String mEmail,mImage;
    TextView Username,Email;
    RatingBar ratingBar;
    Button updateProfileButton;
    FirebaseUser user;
    CircleImageView profile_image;
    Uri uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragview = inflater.inflate(R.layout.fragment_user_profile, container, false);
        // Inflate the layout for this fragment
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        Username = fragview.findViewById(R.id.username);
        Email = fragview.findViewById(R.id.email);
        ratingBar = fragview.findViewById(R.id.ratingBar);
        user = firebaseAuth.getCurrentUser();
        mEmail = user.getEmail();
        uri = user.getPhotoUrl();
        profile_image = fragview.findViewById(R.id.userProfileImage);


        updateProfileButton = (Button) fragview.findViewById(R.id.updateProfile);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.tab_fragment_container, new UpdateProfileFragment());
                ft.commit();
            }
        });
        return fragview;
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

                        Username.setText(mUser.getUsername());
                        Email.setText(mUser.getEmail());
                        ratingBar.setRating(mUser.getRating());

                        if (mUser.getImageURL().equals("default")) {
                            profile_image.setImageResource(R.mipmap.ic_launcher);

                        } else {
                            Glide.with(getContext()).load(mUser.getImageURL()).into(profile_image);
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
