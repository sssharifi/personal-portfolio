package com.cmsc436.final_project.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
public class LostFragment extends Fragment {

    private static final String TAG = "LostFragment";

    protected RecyclerView myReportsRecyclerView;
    protected ArrayList<ItemReport> myLostReports;
    protected Button submitLostReportButton;
    protected Button searchLostReportsButton;
    protected FirebaseAuth mFirebaseAuth;
    protected DatabaseReference mDatabaseReference;
    protected Query mReportsQuery;
    protected ReportAdapter mReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_lost, container, false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("lost_reports");

        myReportsRecyclerView = (RecyclerView) fragView.findViewById(R.id.recyclerViewMyLostReports);
        myReportsRecyclerView.setHasFixedSize(true);
        myReportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myLostReports = new ArrayList<ItemReport>();
        mReportAdapter = new ReportAdapter(getContext(), myLostReports);

        myReportsRecyclerView.setAdapter(mReportAdapter);

        submitLostReportButton = fragView.findViewById(R.id.buttonAddLost);
        // When a user clicks add new lost report button they will be taken to that
        // activity
        submitLostReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Add lost report button was clicked. Entering PostNewLostReport Activity");
                Intent intent = new Intent(getContext(), PostNewLostReportActivity.class);
                startActivity(intent);
            }
        });

        // Search Lost Report Button logic
        searchLostReportsButton = fragView.findViewById(R.id.buttonSearchLost);
        searchLostReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Entering onClick for Searching lost reports");
                Intent intent = new Intent(getContext(), SearchLostReportsActivity.class);
                startActivity(intent);
            }
        });

        return fragView;
    }


    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        String displayName = mFirebaseAuth.getCurrentUser().getDisplayName();
        mReportsQuery = mDatabaseReference.orderByChild("author").equalTo(displayName);
        mReportsQuery.addValueEventListener(reportValueEventListener);
    }

    ValueEventListener reportValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            myLostReports.clear();

            Log.i(TAG, "onDataChange for reports");
            // Extract all information to make them into ItemReport objects to be displayed
            // in our adapter.
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
                String id = snapshot.child("reportId").getValue().toString();
                String authorEmail = snapshot.child("authorEmailAddress").getValue().toString();
                // Make a new latLng object. We can't deserialize the LatLng object inside our object
                // So the work around is to get the double values, make a new LatLng object
                // and pass it into the constructor. Note that since this is the lost fragment
                // "false" will be passed in as the argument...duh!
                LatLng latLng = new LatLng(lat, longitude);


                ItemReport report = new ItemReport(title, description, author, dateOccurred,
                        dateAuthored, latLng, address, false, id, authorEmail);

                myLostReports.add(report);

            }

            mReportAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            throw databaseError.toException();
        }
    };
}
