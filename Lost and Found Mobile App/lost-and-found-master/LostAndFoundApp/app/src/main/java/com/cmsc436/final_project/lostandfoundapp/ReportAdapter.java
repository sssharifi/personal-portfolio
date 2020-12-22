package com.cmsc436.final_project.lostandfoundapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<ItemReport> reportList;

    //getting the context and product list with constructor
    public ReportAdapter(Context mCtx, List<ItemReport> reportList) {
        this.mCtx = mCtx;
        this.reportList = reportList;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_item_report, null);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        //getting the product of the specified position
        final ItemReport report = reportList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(report.getTitle());
        holder.textViewDateOccur.setText(report.getDateOccurred().toString());
        holder.textViewAddress.setText(
                (report.getAddress()!=null) ? report.getAddress() : report.getLatLng().toString());
        holder.textViewDescription.setText(report.getDescription());

        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.lostfound_icon,null));

        // This will make our items that show up in this Recycler view 'clickable' and once
        // clicked they will go to the items detail report page.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ReportAdapter", "Starting intent to go to the item page for " + report.getTitle());

                String toastString = "You clicked on " + report.getTitle();

                Intent intent = new Intent(mCtx, ItemDetailPage.class);
                // Include extras
                intent.putExtra("title", report.getTitle());
                intent.putExtra("author", report.getAuthor());
                intent.putExtra("address", report.getAddress());
                intent.putExtra("dateAuthored", report.getDateAuthored());
                intent.putExtra("dateOccurred", report.getDateOccurred());
                intent.putExtra("status", report.getStatus());
                intent.putExtra("isFound", report.getType() == ItemReport.ReportType.LOST ?0:1 );
                intent.putExtra("reportID", report.reportId);
                intent.putExtra("description", report.getDescription());
                // Break up the LatLng object fields as doubles. We can't pass a LatLng object
                // into an intent extra :(
                LatLng mLatlng = report.getLatLng();
                intent.putExtra("latitude", mLatlng.latitude);
                intent.putExtra("longitude", mLatlng.longitude);
                intent.putExtra("authorEmailAddress", report.getAuthorEmailAddress());

                // If we need to package anything else go ahead and do so here!
                mCtx.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return reportList.size();
    }


    class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDateOccur, textViewAddress, textViewDescription;
        //ImageView imageView;

        public ReportViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDateOccur = itemView.findViewById(R.id.textViewDateOccur);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            //imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
