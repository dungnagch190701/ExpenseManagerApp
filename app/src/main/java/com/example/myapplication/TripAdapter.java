package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<TripEntity> tripArrayList = new ArrayList<>();
    ArrayList<TripEntity> tripArrayListOld;
    Activity activity;
    TripAdapter(Activity activity, Context context, ArrayList<TripEntity> tripArrayList){
        this.activity = activity;
        this.context = context;
        this.tripArrayList = tripArrayList;
        this.tripArrayListOld = tripArrayList;

    }

    @NonNull
    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.MyViewHolder holder, int position) {
        final TripEntity tripEntity = tripArrayList.get(position);
        if (tripEntity.getTripName().equals("Vacation")) {
            holder.image.setImageResource(R.drawable.vacation);
        } else if (tripEntity.getTripName().equals("Meeting")){
            holder.image.setImageResource(R.drawable.meeting);
        } else if (tripEntity.getTripName().equals("Events")) {
            holder.image.setImageResource(R.drawable.events);
        } else if (tripEntity.getTripName().equals("Conference")) {
            holder.image.setImageResource(R.drawable.conference);
        }else if (tripEntity.getTripName().equals("Offices")) {
            holder.image.setImageResource(R.drawable.offices);
        }else if (tripEntity.getTripName().equals("Others")) {
            holder.image.setImageResource(R.drawable.other);
        } else{
            holder.image.setImageResource(R.drawable.house);
        }
        holder.image.setDrawingCacheEnabled(true);
        Bitmap b = holder.image.getDrawingCache();

        holder.trip_name_txt.setText(tripEntity.getTripName());
        holder.trip_dest_txt.setText(tripEntity.getTripDest());
        holder.trip_date_txt.setText(tripEntity.getTripDateFrom());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TripDetailActivity.class);
                intent.putExtra("id",tripEntity.getId());
                intent.putExtra("dept",tripEntity.getTripDept());
                intent.putExtra("name",tripEntity.getTripName());
                intent.putExtra("dest",tripEntity.getTripDest());
                intent.putExtra("datefrom",tripEntity.getTripDateFrom());
                intent.putExtra("dateto",tripEntity.getTripDateTo());
                intent.putExtra("risk",tripEntity.getTripRisk());
                intent.putExtra("desc",tripEntity.getTripDesc());
                intent.putExtra("img",b);



                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripArrayList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        TextView trip_name_txt, trip_dest_txt, trip_date_txt,trip_risk_txt,trip_desc_txt;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            trip_name_txt = itemView.findViewById(R.id.trip_name);
            trip_dest_txt = itemView.findViewById(R.id.trip_dest);
            trip_date_txt = itemView.findViewById(R.id.trip_date);
            mainLayout = itemView.findViewById(R.id.mainLayout);


        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    tripArrayList = tripArrayListOld;
                } else {
                    ArrayList<TripEntity> list = new ArrayList<>();
                    for (TripEntity trip : tripArrayListOld){
                        if (trip.getTripName().toLowerCase().contains(strSearch.toLowerCase()) ||
                                trip.getTripDest().toLowerCase().contains(strSearch.toLowerCase()) ||
                        trip.getTripDateFrom().contains(strSearch)
                        ){
                            list.add(trip);
                        }
                    }
                    tripArrayList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = tripArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tripArrayList = (ArrayList<TripEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
