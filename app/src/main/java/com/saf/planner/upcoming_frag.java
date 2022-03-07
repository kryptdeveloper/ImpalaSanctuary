package com.saf.planner;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saf.planner.ListAdapters.UpcomingEventListAdapter;
import com.saf.planner.ModelClass.Event_Info;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class upcoming_frag extends Fragment {

    RecyclerView recyclerView;
    TextView textView;
    Context context;
    ProgressBar progressBar;

    ArrayList<Event_Info> eventDataArrayList = new ArrayList<>();
    UpcomingEventListAdapter upcomingEventListAdapter ;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dbref;

    String uid,eventID,eventName,eventStartDate,eventEndDate,eventStartTime,eventEndTime,eventImageURL,eventInstruction,eventPlace,eventType,extras,hostID,noOFGuests;
    //String id;


    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat time_format = new SimpleDateFormat("hh:mm");
    int dd = calendar.get(Calendar.DAY_OF_MONTH);
    final int mm = calendar.get(Calendar.MONTH) + 1;
    int yy = calendar.get(Calendar.YEAR);
    int hh = calendar.get(Calendar.HOUR_OF_DAY);
    int MM = calendar.get(Calendar.MINUTE);
    final String current_time_s = hh + ":" + MM;
    final String current_date_s = dd + "/" + (mm) + "/" + yy;
    Date current_date_d = null;
    Date current_time_d = null;
    //Current Date and Time Get En


    public upcoming_frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_upcoming_frag, container, false);
    }

    public void getcurrentDateAndTimeObj()
    {
        try {
            current_date_d = date_format.parse(current_date_s);
            //Log.i("D1", current_date_d.toString());
            current_time_d = time_format.parse(current_time_s);
            //Log.i("T1", current_time_d.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        progressBar = view.findViewById(R.id.progress_upcoming);
        recyclerView = view.findViewById(R.id.upcomingEvent_list);
        textView = view.findViewById(R.id.no_upcoming_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Events");
        dbref = firebaseDatabase.getReference("Invitations");
        //uid = firebaseAuth.getCurrentUser().getUid();
      //  Log.e("user id",uid);
        eventDataArrayList.clear();
        getcurrentDateAndTimeObj();
        upcomingEventListAdapter = new UpcomingEventListAdapter(eventDataArrayList,context);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                //getAcceptedEvents();
                getEventsFromDB();
                progressBar.setVisibility(View.GONE);
            }
        });
        recyclerView.setAdapter(upcomingEventListAdapter);
        upcomingEventListAdapter.notifyDataSetChanged();
        upcomingEventListAdapter.setOnClickListner(onClickListener);
        if(eventDataArrayList.isEmpty())
        {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void getAcceptedEvents() {

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        for(DataSnapshot ds1 : ds.getChildren())
                        {
                            String RecieverUID = ds1.child("Receiver_UID").getValue().toString();
                            String Status = ds1.child("Status").getValue().toString();
                            if(RecieverUID.contentEquals(uid) && Status.contentEquals("Accepted"))
                            {

                                String EVENT_ID = ds.getKey();
                                Log.e("Event ID : ",EVENT_ID);

                                databaseReference.child(EVENT_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                          /*  Log.e("EventName",dataSnapshot.child("eventName").getValue().toString());
                                            Log.e("EventDate",dataSnapshot.child("eventDate").getValue().toString());
                                            Log.e("EventStartTime",dataSnapshot.child("eventStartTime").getValue().toString());
                                            Log.e("EventEndTime",dataSnapshot.child("eventEndTime").getValue().toString());*/

                                            String id = dataSnapshot.getKey();
                                            String name = dataSnapshot.child("eventName").getValue().toString();
                                            String start_date = dataSnapshot.child("eventStartDate").getValue().toString();
                                            String end_date = dataSnapshot.child("eventEndDate").getValue().toString();
                                            String start = dataSnapshot.child("eventStartTime").getValue().toString();
                                            String end = dataSnapshot.child("eventEndTime").getValue().toString();
                                            String image_url = dataSnapshot.child("eventImage").getValue().toString();
                                            String instruction = dataSnapshot.child("eventInstruction").getValue().toString();
                                            String place = dataSnapshot.child("eventPlace").getValue().toString();
                                            String type = dataSnapshot.child("eventType").getValue().toString();
                                            String extra = dataSnapshot.child("extras").getValue().toString();
                                            String host_id = dataSnapshot.child("hostID").getValue().toString();
                                            String no_guest = dataSnapshot.child("noOfGuest").getValue().toString();

                                            Date E_date= null;
                                            Date E_startTime = null;
                                            Date E_endTime = null;
                                            try {
                                                E_date = date_format.parse(start_date);
                                                E_startTime = time_format.parse(start);
                                                E_endTime = time_format.parse(end);
                                            }
                                            catch (ParseException e)
                                            {
                                                e.printStackTrace();
                                            }

                                            if(E_date.compareTo(current_date_d) > 0 )
                                            {
                                                //progressBar.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.VISIBLE);
                                                textView.setVisibility(View.GONE);
                                                //eventDataArrayList.add(new EventData(eventDate,eventStartTime,eventEndTime,eventImageURL,eventName));
                                                eventDataArrayList.add(new Event_Info(id,host_id,image_url,name,start_date,end_date,start,end,place,type,instruction,no_guest,extra,"Accepted_Event"));
                                            }
                                            else if(E_date.compareTo(current_date_d) == 0)
                                            {
                                                if(E_startTime.compareTo(current_time_d) > 0)
                                                {
                                                    if(E_endTime.compareTo(current_time_d) < 0) {
                                                        //progressBar.setVisibility(View.GONE);
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        textView.setVisibility(View.GONE);
                                                        //eventDataArrayList.add(new EventData(eventDate, eventStartTime, eventEndTime, eventImageURL, eventName));
                                                        eventDataArrayList.add(new Event_Info(id,host_id,image_url,name,start_date,end_date,start,end,place,type,instruction,no_guest,extra,"Accepted_Event"));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(context,"Event DBRef Error:"+databaseError,Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Invitation DBref Error:"+databaseError,Toast.LENGTH_LONG).show();
            }
        });
            }
        });
    }

    private void getEventsFromDB() {


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
//                        if(uid.contentEquals(ds.child("hostID").getValue().toString()))
//                        {

                            //Log.i("Date",ds.child("eventDate").getValue().toString());
                            eventID = ds.getKey();
                            eventName = ds.child("eventName").getValue().toString();
                            eventStartDate = ds.child("eventStartDate").getValue().toString();
                            eventEndDate = ds.child("eventEndDate").getValue().toString();
                            eventStartTime = ds.child("eventStartTime").getValue().toString();
                            eventEndTime = ds.child("eventEndTime").getValue().toString();
                            eventImageURL = ds.child("eventImage").getValue().toString();
                            eventInstruction = ds.child("eventInstruction").getValue().toString();
                            eventPlace = ds.child("eventPlace").getValue().toString();
                            eventType = ds.child("eventType").getValue().toString();
                            extras = ds.child("extras").getValue().toString();
                            hostID = ds.child("hostID").getValue().toString();
                            noOFGuests = ds.child("noOfGuest").getValue().toString();

                            Date E_date= null;
                            Date E_startTime = null;
                            Date E_endTime = null;
                            try {
                                E_date = date_format.parse(eventStartDate);
                                E_startTime = time_format.parse(eventStartTime);
                                E_endTime = time_format.parse(eventEndTime);
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                            }

                            if(E_date.compareTo(current_date_d) > 0 )
                            {
                                //progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.GONE);
                                //eventDataArrayList.add(new EventData(eventDate,eventStartTime,eventEndTime,eventImageURL,eventName));
                                eventDataArrayList.add(new Event_Info(eventID,hostID,eventImageURL,eventName,eventStartDate,eventEndDate,eventStartTime,eventEndTime,eventPlace,eventType,eventInstruction,noOFGuests,extras,"AddedEvent"));
                            }
                            else if(E_date.compareTo(current_date_d) == 0)
                            {
                                if(E_startTime.compareTo(current_time_d) > 0)
                                {
                                    if(E_endTime.compareTo(current_time_d) < 0) {
                                        //progressBar.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        textView.setVisibility(View.GONE);
                                        //eventDataArrayList.add(new EventData(eventDate, eventStartTime, eventEndTime, eventImageURL, eventName));
                                        eventDataArrayList.add(new Event_Info(eventID,hostID,eventImageURL,eventName,eventStartDate,eventEndDate,eventStartTime,eventEndTime,eventPlace,eventType,eventInstruction,noOFGuests,extras,"AddedEvent"));
                                    }
                                }
                            }
                        //}
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Error:"+databaseError,Toast.LENGTH_LONG).show();
            }
        });
            }
        });

    }


    public View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override

        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            //int position = viewHolder.getAdapterPosition();
        }
    };
}
