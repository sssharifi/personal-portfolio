package com.cmsc436.final_project.lostandfoundapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class PostNewLostReportActivity extends AppCompatActivity {

    private final static String TAG = "Lost Report Activity";

    protected EditText mTitleTextField; // The text field for the title of the post.
    protected EditText mDescriptionTextField;   // The text field for a description.
    protected EditText mAddressTextField;
    protected FirebaseAuth mFirebaseAuth;
    protected DatabaseReference mDatabaseReference;
    protected String currentUserName;
    protected TextView mDateLostView;
    protected TextView mPostLostCoor;
    protected TextView mAddressView;
    protected EditText mDescription;
    private DatePickerDialog datePicker;

    // Buttons
    protected Button submitLostReportButton;
    protected Button cancelLostReportButton;
    protected Button mdateLostPicker;
    protected Button mLostLocationButton;

    private Double curr_Lat, curr_Lng;
    private String curr_address;

    private static final int MAP_ACTIVITY_RESULT_CODE = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_lost_report);
        mTitleTextField = findViewById(R.id.postLostReportTitle);
        mDateLostView = findViewById(R.id.postLostDateLost);
        mPostLostCoor = findViewById(R.id.postLostCoordinates);
        mAddressView = findViewById(R.id.postLostAddress);
        mDescription = findViewById(R.id.postLostReportDescription);


        // TODO: Implement the logic to get the necessary information from the
        // EditText elements and push a new foundReport into the Firebase.
        // This requires us to have an xml that has the necessary id's. So Whoever
        // makes the xml and choose to do this, they can fill in the missing pieces.

        /* To push the new item report, we need to get the current users username.
         * this might involve more querying...Hope this skeleton helps.
         *
         */


        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserName = mFirebaseAuth.getCurrentUser().getDisplayName();  // Get the username of the logged in user.

        // Get reference to the 'users' tree in the Firebase Database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("lost_reports");

        // Getting the Views for the buttons on the page
        submitLostReportButton = findViewById(R.id.submitLost);
        cancelLostReportButton = findViewById(R.id.cancelLost);
        mdateLostPicker = findViewById(R.id.pickDateLostButton);
        mLostLocationButton = findViewById(R.id.getLostLocationButton);
        mdateLostPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Date picker button was clicked");
                Calendar c = Calendar.getInstance();
                int currMonth = c.get(Calendar.MONTH);
                int currDay = c.get(Calendar.DAY_OF_MONTH);
                int currYear = c.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(PostNewLostReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mDateLostView.setText((month + 1) + "/" + day + "/" + year);
                    }
                }, currYear, currMonth, currDay);
                datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePicker.show();
            }
        });

        mLostLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Onclick inside of get location dude" );
                Intent inten = new Intent(getApplicationContext(), MapActivity.class);
                startActivityForResult(inten, MAP_ACTIVITY_RESULT_CODE);
            }
        });

        submitLostReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Diarre, implement the same stuff we did for the found report
                // only use the reference for the "lost_reports" child.
                if(mTitleTextField.getText().toString().trim().equals("") ||
                        mDateLostView.getText().toString().trim().equals("unselected") ||
                        mPostLostCoor.getText().toString().equals("no location selected") ||
                        mAddressView.getText().toString().equals("no address selected")
                        || mDescription.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill in all appropriate fields", Toast.LENGTH_LONG).show();

                } else {
                    pushToFireBase();
                    finish();
                }
            }
        });

        cancelLostReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If they cancel then just end the activity and go back to the lost fragment
                finish();
            }
        });

    }
    public void pushToFireBase() {
        String mTitle = mTitleTextField.getText().toString().trim();
        String dateFound = mDateLostView.getText().toString().trim();
        String coor = mPostLostCoor.getText().toString();
        String[] coorSeperated = coor.split(",");
        LatLng latLongFound = new LatLng(curr_Lat, curr_Lng);
        String address = mAddressView.getText().toString();
        String descript = mDescription.getText().toString();
        String reportId = mDatabaseReference.push().getKey();
        String authorEmailAddress = mFirebaseAuth.getCurrentUser().getEmail();

        ItemReport reportFiled = new ItemReport(mTitle, descript, mFirebaseAuth.getCurrentUser().getDisplayName(),
                new Date(dateFound), new Date(dateFound), latLongFound, address, false, reportId,
                authorEmailAddress);
        mDatabaseReference.child(reportId).setValue(reportFiled);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: Enter");
//TODO: CHECK REQUEST CODE;


        if(MAP_ACTIVITY_RESULT_CODE == requestCode && RESULT_OK == resultCode){
            ItemReport foundReport = new ItemReport();
            curr_Lat = data.getDoubleExtra("curr_lat",0.0);
            curr_Lng = data.getDoubleExtra("curr_lng", 0.0);
            curr_address = data.getStringExtra("curr_address");

            Log.i(TAG, "onActivityResult: curr_lng: "+ curr_Lng + " curr_lat: " + curr_Lat + " curr_address: "+ curr_address);
            mPostLostCoor = findViewById(R.id.postLostCoordinates);

            mPostLostCoor.setText(new DecimalFormat("###.##").format(curr_Lat).toString() + ", " +
                    new DecimalFormat("###.##").format(curr_Lng).toString());
            mAddressView = findViewById(R.id.postLostAddress);
            mAddressView.setText(curr_address);

        }

    }

}
