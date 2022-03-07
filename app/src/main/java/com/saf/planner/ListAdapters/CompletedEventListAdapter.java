package com.saf.planner.ListAdapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saf.planner.ModelClass.Event_Info;
import com.example.planner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CompletedEventListAdapter extends RecyclerView.Adapter<CompletedEventListAdapter.ItemViewHolder>
{
    Context context;
    ArrayList<Event_Info> dataArrayList;
    Event_Info event_info;
    Uri eventImage;

    public CompletedEventListAdapter(ArrayList<Event_Info> eventDataArrayList, Context context) {
        dataArrayList = eventDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_item,parent,false);
        return new CompletedEventListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        event_info = dataArrayList.get(position);
        eventImage = Uri.parse(event_info.getEventImage());
        Picasso.get().load(eventImage).into(holder.imageView);
        holder.eventName.setText(event_info.getEventName());
        holder.eventStartdate.setText(event_info.getEventStartDate());
        holder.eventEndDate.setText(event_info.getEventEndDate());
        holder.eventStartTime.setText(event_info.getEventStartTime());
        holder.eventEndTime.setText(event_info.getEventEndTime());

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView eventName,eventStartdate,eventEndDate,eventStartTime,eventEndTime;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image_list_item);
            eventName = itemView.findViewById(R.id.eventName_item);
            eventStartdate = itemView.findViewById(R.id.eventStartDate_item);
            eventEndDate = itemView.findViewById(R.id.eventEndDate_item);
            eventStartTime = itemView.findViewById(R.id.eventStart_item);
            eventEndTime = itemView.findViewById(R.id.eventEnd_item);

        }
    }
}
