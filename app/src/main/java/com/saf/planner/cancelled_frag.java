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

import com.saf.planner.ListAdapters.CancelledEventListAdapter;
import com.saf.planner.ModelClass.Event_Info;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cancelled_frag extends Fragment {



    RecyclerView recyclerView;
    TextView textView;
    Context context;
    ProgressBar progressBar;

    ArrayList<Event_Info> eventDataArrayList = new ArrayList<>();
    CancelledEventListAdapter cancelledEventListAdapter ;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,dbref;

    String uid,eventName,eventDate,eventStartTime,eventEndTime,eventImageURL;
    public cancelled_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_cancelled_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        textView = view.findViewById(R.id.no_cancelled_text);
        progressBar = view.findViewById(R.id.progress_cancelled);
        progressBar.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.CancelledEvent_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Events");
        dbref = firebaseDatabase.getReference("Invitations");
//        uid = firebaseAuth.getCurrentUser().getUid();

        cancelledEventListAdapter = new CancelledEventListAdapter(eventDataArrayList,context);
        eventDataArrayList.clear();
        getRejectedEvents();

    }

    private void getRejectedEvents() {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            progressBar.setVisibility(View.VISIBLE);
                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                for(DataSnapshot ds1 : ds.getChildren())
                                {
                                    String RecieverUID = ds1.child("Receiver_UID").getValue().toString();
                                    String Status = ds1.child("Status").getValue().toString();
                                    if(RecieverUID.contentEquals(uid) && Status.contentEquals("Rejected"))
                                    {

                                        String EVENT_ID = ds.getKey();
                                        Log.e("Event ID : ",EVENT_ID);

                                        databaseReference.child(EVENT_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){

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
                                                        progressBar.setVisibility(View.GONE);
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                        textView.setVisibility(View.GONE);
                                                        //eventDataArrayList.add(new EventData(eventDate,eventStartTime,eventEndTime,eventImageURL,eventName));
                                                        eventDataArrayList.add(new Event_Info(id,host_id,image_url,name,start_date,end_date,start,end,place,type,instruction,no_guest,extra,"Accepted_Event"));
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
                            recyclerView.setAdapter(cancelledEventListAdapter);
                            cancelledEventListAdapter.notifyDataSetChanged();
                            if(eventDataArrayList.isEmpty())
                            {
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
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
}
