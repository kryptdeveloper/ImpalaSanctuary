package com.saf.planner;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.saf.planner.ListAdapters.GuestListAdapter;
import com.saf.planner.ModelClass.GuestData;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GuestList extends Fragment {
    Context context;
    RecyclerView guestList;
    TextView textView;
    ArrayList<GuestData> guestDataArrayList = new ArrayList<>();
    GuestListAdapter guestListAdapter;
    SharedPreferences eventDetails;
    String eventID,HostID;

    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference mDBref;
    String E_ID,userID,UserNAME;
    ArrayList<String> Emailarray = new ArrayList<>();
    View view;

    public GuestList() {
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
        return inflater.inflate(R.layout.events_fragment_guest_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        eventDetails = getActivity().getSharedPreferences("EventInfo",Context.MODE_PRIVATE);
        context = getActivity().getApplicationContext();
        guestList = view.findViewById(R.id.guest_list);
        textView = view.findViewById(R.id.noGuest_text_GuestList);
        guestList.setLayoutManager(new LinearLayoutManager(context));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Guest List");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        eventID = eventDetails.getString("event__ID","");
        HostID = eventDetails.getString("host__ID","");

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        getUserName();
    }

    private void getUserName() {
        databaseReference = database.getReference("Events");
        mDBref = database.getReference("User");

        databaseReference.child(eventID).child("GuestList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        E_ID = dataSnapshot.child(ds.getKey()).child("Invited_UID").getValue().toString();
                        Log.e("EID:",E_ID);
                       getNameFromDB(E_ID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getNameFromDB(final String e_id) {
        mDBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        if(dataSnapshot1.getKey().contentEquals(e_id))
                        {
                            guestList.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            String userName = dataSnapshot1.child("firstName").getValue().toString()+" "+dataSnapshot1.child("lastName").getValue().toString();
                         guestDataArrayList.add(new GuestData(userName));
                         Emailarray.add(dataSnapshot1.child("emailID").getValue().toString());

                        }
                    }

                    guestListAdapter = new GuestListAdapter(guestDataArrayList,context);
                    guestList.setAdapter(guestListAdapter);
                    guestListAdapter.notifyDataSetChanged();

                    if(guestDataArrayList.isEmpty())
                    {
                        guestList.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.guest_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sendUpdate_guestList:
                sendUpdateToGuests(view);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUpdateToGuests(View view) {
        if(Emailarray.isEmpty())
        {
            Toast.makeText(context,"There is no Guest available to send update!",Toast.LENGTH_LONG).show();
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("EmailArray",Emailarray);
            Navigation.findNavController(view).navigate(R.id.action_guestList_to_sendUpdateFrag,bundle);
        }
    }
}
