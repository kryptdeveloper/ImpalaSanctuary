package com.saf.Dan;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.saf.planner.ModelClass.Event_Info;
import com.example.planner.R;
import com.saf.planner.UpcomingEventDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsListener extends RecyclerView.Adapter<EventsListener.ItemViewHolder>
{
    Context context;
    ArrayList<Event_Info> dataArrayList;
    Event_Info event_info;
    Uri eventImage;
    private View.OnClickListener itemClickListner;
    SharedPreferences data ;
    NavController navController;

    public EventsListener(ArrayList<Event_Info> eventDataArrayList, Context context) {
        dataArrayList = eventDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsListener.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_show_user,parent,false);
        return new EventsListener.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventsListener.ItemViewHolder holder, final int position) {
        data = context.getSharedPreferences("EventInfo",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = data.edit();
        event_info = dataArrayList.get(position);
        eventImage = Uri.parse(event_info.getEventImage());
        Picasso.get().load(eventImage).into(holder.imageView);
        holder.eventName.setText(event_info.getEventName());
        holder.event_Start_date.setText(event_info.getEventStartDate());
        holder.event_End_date.setText(event_info.getEventEndDate());
        holder.eventStartTime.setText(event_info.getEventStartTime());
        holder.eventEndTime.setText(event_info.getEventEndTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("event__ID",dataArrayList.get(position).getEventID());
                editor.putString("host__ID",dataArrayList.get(position).getHostID());
                editor.putString("event__Image",dataArrayList.get(position).getEventImage());
                editor.putString("event__Name",dataArrayList.get(position).getEventName());
                editor.putString("event__StartDate",dataArrayList.get(position).getEventStartDate());
                editor.putString("event__EndDate",dataArrayList.get(position).getEventEndDate());
                editor.putString("event__StartTime",dataArrayList.get(position).getEventStartTime());
                editor.putString("event__EndTime",dataArrayList.get(position).getEventEndTime());
                editor.putString("event__Place",dataArrayList.get(position).getEventPlace());
                editor.putString("event__Type",dataArrayList.get(position).getEventType());
                editor.putString("event__Instruction",dataArrayList.get(position).getEventInstruction());
                editor.putString("noOf__Guest",dataArrayList.get(position).getNoOfGuest());
                editor.putString("__extras",dataArrayList.get(position).getExtras());
                editor.putString("TAG",dataArrayList.get(position).getTAG());
                editor.commit();
                //Log.e("EventID",dataArrayList.get(position).getEventID());
                //Navigation.createNavigateOnClickListener(R.id.upcomingEventDetails,bundle);
                //   Navigation.findNavController(v).navigate(R.id.upcomingEventDetails);
                // UpcomingEventDetails nextFrag= new UpcomingEventDetails();

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new UpcomingEventShow();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.inflate2, myFragment)
                        .addToBackStack(null).commit();

                // navController = Navigation.findNavController(activity, R.id.nav_host_fragment)
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void setOnClickListner(View.OnClickListener clickListner)
    {
        itemClickListner = clickListner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView eventName,event_Start_date,event_End_date,eventStartTime,eventEndTime;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image_list_item);
            eventName = itemView.findViewById(R.id.eventName_item);
            event_Start_date = itemView.findViewById(R.id.eventStartDate_item);
            event_End_date = itemView.findViewById(R.id.eventEndDate_item);
            eventStartTime = itemView.findViewById(R.id.eventStart_item);
            eventEndTime = itemView.findViewById(R.id.eventEnd_item);
            itemView.setOnClickListener(itemClickListner);

        }
    }
}
