package com.saf.Dan;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.saf.Dan.MainActivity2;
import com.squareup.picasso.Picasso;


public class UpcomingEventShow extends Fragment {
    ProgressBar progressBar;
    Context context;
    ImageView eventImage;
    TextView eventName, eventStartDate, eventEndDate, eventStartTime, eventEndTime, eventPlace, eventType, eventInstruction, eventExtras, eventNoOfGuest, eventHostID;
    String ID, HostID, EventImage, EventName, EventStartDate, EventEndDate, EventStartTime, EventEndTime, EventPlace, EventType, EventInstruction, NoOfGuest, Extras, TAG;
    Uri imageURI;
    Bundle bundle;
    String Imageextention;
    SharedPreferences eventData;
    View view;



    public UpcomingEventShow() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming_event_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventData = getActivity().getSharedPreferences("EventInfo", Context.MODE_PRIVATE);
        bundle = new Bundle();
        this.view = view;
        context = getActivity().getApplicationContext();
        progressBar = view.findViewById(R.id.progress_Upcomingdetails);
        eventImage = view.findViewById(R.id.eventImage_upcomingDetails);
        eventName = view.findViewById(R.id.eventName_upcomingDetails);
        eventStartDate = view.findViewById(R.id.eventStartDate_upcomingDetails);
        eventEndDate = view.findViewById(R.id.eventEndDate_upcomingDetails);
        eventStartTime = view.findViewById(R.id.eventStart_upcomingDetails);
        eventEndTime = view.findViewById(R.id.eventEnd_upcomingDetails);
        eventPlace = view.findViewById(R.id.eventPlace_upcomingDetails);
        eventType = view.findViewById(R.id.eventType_upcomingDetails);
        eventInstruction = view.findViewById(R.id.eventInstruction_upcomingDetails);
        eventExtras = view.findViewById(R.id.eventExtras_upcomingDetails);
        eventNoOfGuest = view.findViewById(R.id.eventNoGuest_upcomingDetails);
        eventHostID = view.findViewById(R.id.eventHostID_upcomingDetails);

        ID = eventData.getString("event__ID", "");
        HostID = eventData.getString("host__ID", "");
        EventImage = eventData.getString("event__Image", "");
        EventName = eventData.getString("event__Name", "");
        EventStartDate = eventData.getString("event__StartDate", "");
        EventEndDate = eventData.getString("event__EndDate", "");
        EventStartTime = eventData.getString("event__StartTime", "");
        EventEndTime = eventData.getString("event__EndTime", "");
        EventPlace = eventData.getString("event__Place", "");
        EventType = eventData.getString("event__Type", "");
        EventInstruction = eventData.getString("event__Instruction", "");
        NoOfGuest = eventData.getString("noOf__Guest", "");
        Extras = eventData.getString("__extras", "");
        TAG = eventData.getString("TAG", "");


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(EventName);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        imageURI = Uri.parse(EventImage);
        Picasso.get().load(imageURI).into(eventImage);
        eventName.setText(EventName);
        eventStartDate.setText(EventStartDate);
        eventEndDate.setText(EventEndDate);
        eventStartTime.setText(EventStartTime);
        eventEndTime.setText(EventEndTime);
        eventPlace.setText(EventPlace);
        eventType.setText(EventType);
        eventInstruction.setText(EventInstruction);
        eventExtras.setText(Extras);
        eventNoOfGuest.setText(NoOfGuest);
        eventHostID.setText(HostID);

        if (imageURI.toString().contains(".jpg") || imageURI.toString().contains(".JPG")) {
            Imageextention = "jpg";
        } else if (imageURI.toString().contains(".png") || imageURI.toString().contains(".PNG")) {
            Imageextention = "png";
        } else if (imageURI.toString().contains(".jpeg") || imageURI.toString().contains(".JPEG")) {
            Imageextention = "JPEG";
        }


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (TAG.contentEquals("Accepted_Event")) {
            menu.clear();
        } else {
            menu.clear();
            inflater.inflate(R.menu.event_options, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_EventMenu:
                //Toast.makeText(context,"Delete Event Clicked!",Toast.LENGTH_LONG).show();
                DeleteEvent();
                break;
        }
        return super.onOptionsItemSelected(item);

    }



    private void DeleteEvent() {
        Toast.makeText(getActivity(), "Sorry only admin manager can cancel this event!!", Toast.LENGTH_LONG).show();

    }



}
