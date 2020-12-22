package com.cmsc436.final_project.lostandfoundapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchResults extends AppCompatActivity {

    ArrayList<ItemReport> myReports;
    RecyclerView myReportsRecyclerView;
    ReportAdapter mReportAdapter;
    DatabaseReference databaseResultItems;
    Query myReportsQuery;

    boolean locationSelected = false;
    double curr_Lat = 0.0;
    double curr_Lng = 0.0;

    double latMax, latMin, lngMax, lngMin = 0.0;
    double radiusMiles = 0.25;

    boolean isEarlyBoundSelected;
    Date earlyBound;
    boolean isLateBoundSelected;
    Date lateBound;

    boolean isFound = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();

        myReportsRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearchResults);
        myReportsRecyclerView.setHasFixedSize(true);
        myReportsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        myReports = new ArrayList<ItemReport>();
        mReportAdapter = new ReportAdapter(getApplicationContext(), myReports);
        myReportsRecyclerView.setAdapter(mReportAdapter);

        locationSelected = (intent.getIntExtra("locationSelected", 0) == 1);
        isFound = intent.getIntExtra("isFound", 0) == 1 ? true : false;
        isEarlyBoundSelected = intent.getIntExtra("earlyBoundSelected", 0) == 1 ? true : false;
        isLateBoundSelected = intent.getIntExtra("lateBoundSelected", 0) == 1 ? true : false;
        latMax = intent.getDoubleExtra("latMax", 0.0);
        latMin = intent.getDoubleExtra("latMin", 0.0);
        lngMax = intent.getDoubleExtra("lngMax", 0.0);
        lngMin = intent.getDoubleExtra("lngMin", 0.0);
        earlyBound = (Date) intent.getSerializableExtra("earlyBound");
        lateBound = (Date) intent.getSerializableExtra("lateBound");


        if(isFound){ // searching found
            databaseResultItems = FirebaseDatabase.getInstance().getReference("found_reports");
        }else{ // searching lost
            databaseResultItems = FirebaseDatabase.getInstance().getReference("lost_reports");
        }

        if(locationSelected){
            myReportsQuery = databaseResultItems.orderByChild("latitude");
        }else{
            myReportsQuery = databaseResultItems.orderByKey();
        }

        myReportsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myReports.clear();
                //Log.i( "testing","did my author "+currentAuthor+" get here in time");
                //Log.i("testing","hasChildren: "+dataSnapshot.hasChildren());
                int i = 0;
                //Toast.makeText(getApplicationContext(), "early date chosen " +earlyBound.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "later date chosen " +lateBound.toString(), Toast.LENGTH_LONG).show();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    i++;
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

                    String authorEmailAddress = snapshot.child("authorEmailAddress").getValue().toString();

                    // Make a new latLng object. We can't deserialize the LatLng object inside our object
                    // So the work around is to get the double values, make a new LatLng object
                    // and pass it into the constructor. Note that since this is the found fragment
                    // "true" will be passed in as the argument...duh!
                    LatLng latLng = new LatLng(lat, longitude);

                    ItemReport report = new ItemReport(title, description, author, dateOccurred,
                            dateAuthored, latLng, address, type.equals("FOUND"), id, authorEmailAddress);

                    Calendar lowBound = Calendar.getInstance();
                    lowBound.setTime(earlyBound);
                    Calendar calRipkin = Calendar.getInstance();
                    lowBound.setTime(dateOccurred);
                    Calendar highBound = Calendar.getInstance();
                    lowBound.setTime(lateBound);


                    if(locationSelected && (longitude<lngMin || lngMax<longitude || lat<latMin || latMax<lat)){

                        continue;
                    }else if(isEarlyBoundSelected
                            && earlyBound.after(dateOccurred)) {
                        continue;
                    }else if(isLateBoundSelected && lateBound.before(dateOccurred)) {

                        continue;
                    } else {
                        myReports.add(report);
                    }
                }//for
                mReportAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(),"i = "+i, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
