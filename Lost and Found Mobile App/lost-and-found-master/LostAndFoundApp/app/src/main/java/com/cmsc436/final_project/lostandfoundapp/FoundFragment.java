package com.cmsc436.final_project.lostandfoundapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoundFragment extends Fragment {

    private static final String TAG = "FoundFragment";

    ArrayList<ItemReport> myFoundReports;
    RecyclerView myReportsRecyclerView;
    DatabaseReference databaseFoundItems;
    ReportAdapter mReportAdapter;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseUsers;
    Query myAuthorQuery;
    Query myReportsQuery;

    //Buttons
    Button newFoundReportButton;
    Button searchFoundReportsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.fragment_found, container, false);
        myReportsRecyclerView = (RecyclerView) fragView.findViewById(R.id.recyclerViewMyFoundReports);
        myReportsRecyclerView.setHasFixedSize(true);
        myReportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myFoundReports = new ArrayList<ItemReport>();

        mReportAdapter = new ReportAdapter(getContext(), myFoundReports);
        myReportsRecyclerView.setAdapter(mReportAdapter);

        databaseFoundItems = FirebaseDatabase.getInstance().getReference("found_reports");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        // Get the button to start the Create a new Found Item Report
        newFoundReportButton = fragView.findViewById(R.id.buttonAddFound);
        mFirebaseAuth = FirebaseAuth.getInstance();

        String displayName = mFirebaseAuth.getCurrentUser().getDisplayName();

        newFoundReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement the logic for the button that makes a new Found Item Report
                // This will involve simply packaging an intent to start the appropriate activity.
                Intent intent = new Intent(getContext(), PostNewFoundReport.class);
                startActivity(intent);
            }
        });

        // Search Found Report Button logic
        searchFoundReportsButton = fragView.findViewById(R.id.buttonSearchFound);
        searchFoundReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Entering onClick for Searching found reports");
                Intent intent = new Intent(getContext(), SearchFoundReportsActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return fragView;
    }


    @Override
    public void onStart() {
        super.onStart();

        mFirebaseAuth = FirebaseAuth.getInstance();
        String mEmail = mFirebaseAuth.getCurrentUser().getEmail();
        myAuthorQuery = databaseUsers.orderByChild("email").equalTo(mEmail);
        myAuthorQuery.addListenerForSingleValueEvent(authorValueEventListener);

    }

    ValueEventListener authorValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String currentAuthor = "";
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                currentAuthor = snapshot.child("username").getValue(String.class);
            }
            Log.i( "testing","where is my author "+currentAuthor);
            myReportsQuery = databaseFoundItems.orderByChild("author").equalTo(currentAuthor);
            myReportsQuery.addValueEventListener(reportValueEventListener);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            throw databaseError.toException();
        }
    };

    ValueEventListener reportValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            myFoundReports.clear();
            //Log.i( "testing","did my author "+currentAuthor+" get here in time");
            //Log.i("testing","hasChildren: "+dataSnapshot.hasChildren());
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                String address = snapshot.child("address").getValue().toString();
                String author = snapshot.child("author").getValue().toString();
                Date dateAuthored = snapshot.child("dateAuthored").getValue(Date.class);
                Date dateOccurred = snapshot.child("dateOccurred").getValue(Date.class);
                String description = snapshot.child("description").getValue().toString();
                Double lat = snapshot.child("latLng").child("latitude").getValue(Double.class);
                Double longitude = snapshot.child("latLng").child("longitude").getValue(Double.class);
                String method = snapshot.child("method").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String title = snapshot.child("title").getValue().toString();
                String type = snapshot.child("type").getValue().toString();
                String id = snapshot.child("id").getValue().toString();
                // Make a new latLng object. We can't deserialize the LatLng object inside our object
                // So the work around is to get the double values, make a new LatLng object
                // and pass it into the constructor. Note that since this is the found fragment
                // "true" will be passed in as the argument...duh!
                LatLng latLng = new LatLng(lat, longitude);
                String authorEmailAddress = snapshot.child("authorEmailAddress").getValue().toString();


                ItemReport report = new ItemReport(title, description, author, dateOccurred,
                        dateAuthored, latLng, address, true, id, authorEmailAddress);

                myFoundReports.add(report);

            }//for
            mReportAdapter.notifyDataSetChanged();

        }//onDataChanged()

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            throw databaseError.toException();
        }
    };


}
