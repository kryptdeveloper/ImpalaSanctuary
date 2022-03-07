package com.saf.planner;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saf.planner.ListAdapters.InviteUserListAdapter;
import com.saf.planner.ModelClass.User;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InviteUser extends Fragment implements InviteUserListAdapter.InviteUser {
    Context context;
    View view;
    EditText SearchEditView;
    RecyclerView userList;
    TextView noUserText;
    ProgressBar inviteProgress;
    Button NextButton;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;

    ArrayList<User> userArrayList = new ArrayList<>();
    ArrayList<User> userFilterArrayList = new ArrayList<>();

    InviteUserListAdapter inviteUserListAdapter;

    String UID,FirstName,LastName,EmailID,PhoneNo,Password,ProfileURL,TokenID;
    String Eventid, HostID, EventName, EventStartDate,EventEndDate, EventStartTime, EventEndTime, EventPlace;
    String InvitationMessage,HostName,CurrentUserToken;

    HashMap<String,String> UIDMap = new HashMap<>();
    HashMap<String,String> TokenIDMap = new HashMap<>();

    public InviteUser() {
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
        return inflater.inflate(R.layout.events_fragment_invite_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        Eventid = getArguments().getString("event_ID");
        HostID = getArguments().getString("host_ID");
        EventName = getArguments().getString("event_Name");
        EventStartDate = getArguments().getString("event_Start_Date");
        EventEndDate = getArguments().getString("event_End_Date");
        EventStartTime = getArguments().getString("event_StartTime");
        EventEndTime = getArguments().getString("event_EndTime");
        EventPlace = getArguments().getString("event_Place");


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    HostName = dataSnapshot.child("firstName").getValue().toString() + " " +dataSnapshot.child("lastName").getValue().toString();
                    CurrentUserToken = dataSnapshot.child("Tokenid").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        context = getActivity().getApplicationContext();
        SearchEditView = view.findViewById(R.id.search_editText_inviteUser);
        userList = view.findViewById(R.id.user_list_invite);

        noUserText = view.findViewById(R.id.noUser_text_invitePage);
        inviteProgress = view.findViewById(R.id.progress_inviteUser);
        NextButton = view.findViewById(R.id.sendbtn_invitePage);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Invite User");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        userList.setLayoutManager(new LinearLayoutManager(context));
        inviteUserListAdapter = new InviteUserListAdapter(this,userFilterArrayList,context);
        userList.setAdapter(inviteUserListAdapter);

        SearchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userList.setLayoutManager(new LinearLayoutManager(context));
        inviteUserListAdapter = new InviteUserListAdapter(this,userFilterArrayList,context);
        userList.setAdapter(inviteUserListAdapter);
        inviteUserListAdapter.setOnClickListner(onClickListener);
        GetData();
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveInvitationData();
            }
        });

    }

    public void SaveInvitationData()
    {
        String InvitationID = UUID.randomUUID().toString();
        DatabaseReference dbref = firebaseDatabase.getReference();
        InvitationMessage = "Hi,there\n I, "+HostName+" invite you for this "+EventName+" event Dates From"+EventStartDate+" to "+EventEndDate+",Time From "+EventStartTime+" To "+EventEndTime+" . Event is going to place on "+EventPlace +" .\n We look forward to your presence on this special occasion.\n\n Thank you \n Sincerly,\n"+HostName ;
        //dbref.child("Invitations").child(uid).child(EventID).child(InvitationID);

        for (Map.Entry<String, String> entry : UIDMap.entrySet()) {
                //System.out.println("UID: " + entry);
            dbref.child("Invitations").child(Eventid).child(entry.getKey()).child("Invitation_Message").setValue(InvitationMessage);
            dbref.child("Invitations").child(Eventid).child(entry.getKey()).child("Status").setValue("Pending");
            dbref.child("Invitations").child(Eventid).child(entry.getKey()).child("SenderUID").setValue(uid);
            dbref.child("Invitations").child(Eventid).child(entry.getKey()).child("Receiver_UID").setValue(entry.getValue());
        }
        for (Map.Entry<String, String> entry : TokenIDMap.entrySet()) {
            //System.out.println("Token:"+entry);
            dbref.child("Invitations").child(Eventid).child(entry.getKey()).child("Invitation").child("Receiver_TokenID").setValue(entry.getValue());
            dbref.child("Invitations").child(Eventid).child(entry.getKey()).child("Invitation").child("Sender_TokenID").setValue(CurrentUserToken);
            //dbref.child("Invitations").child(InvitationID).child(entry.getKey()).child("EventID").setValue(Eventid);
        }
        Toast.makeText(context,"Invitation Send Successfully!",Toast.LENGTH_LONG).show();
        Navigation.findNavController(view).navigate(R.id.action_inviteUser_to_upcomingEventDetails);
    }

    private void filter(String toString) {
        userFilterArrayList.clear();
        for(User item :userArrayList)
        {
            if(item.getFirstName().toLowerCase().startsWith(toString.toLowerCase()))
            {
                userFilterArrayList.add(item);
            }
        }
        inviteUserListAdapter.notifyDataSetChanged();
    }


    public void GetData()
    {
        userArrayList.clear();
        userFilterArrayList.clear();

        inviteUserListAdapter.notifyDataSetChanged();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inviteProgress.setVisibility(View.VISIBLE);
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(!ds.getKey().contentEquals(uid)) {
                            inviteProgress.setVisibility(View.GONE);
                            UID = ds.getKey();
                            FirstName = ds.child("firstName").getValue().toString();
                            LastName = ds.child("lastName").getValue().toString();
                            EmailID = ds.child("emailID").getValue().toString();
                            PhoneNo = ds.child("phoneNo").getValue().toString();
                            Password = ds.child("password").getValue().toString();
                            ProfileURL = ds.child("profileURL").getValue().toString();
                            TokenID = (ds.child("Tokenid").getValue().toString());


                            userList.setVisibility(View.VISIBLE);
                            noUserText.setVisibility(View.GONE);
                            userArrayList.add(new User(TokenID,UID, FirstName, LastName, EmailID, PhoneNo, Password, ProfileURL));
                            userFilterArrayList.add(new User(TokenID,UID, FirstName, LastName, EmailID, PhoneNo, Password, ProfileURL));
                        }
                    }

                    inviteUserListAdapter.notifyDataSetChanged();
                    if(userFilterArrayList.isEmpty())
                    {
                        inviteProgress.setVisibility(View.GONE);
                        userList.setVisibility(View.VISIBLE);
                        noUserText.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override

        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            //int position = viewHolder.getAdapterPosition();
        }
    };

    @Override
    public void AddUser(User user, boolean status, int position) {

       if(status) {
           UIDMap.put(String.valueOf(position),user.getUID());
           TokenIDMap.put(String.valueOf(position),user.getTokenID());
       }
       else
       {
        UIDMap.remove(String.valueOf(position));
        TokenIDMap.remove(String.valueOf(position));
       }
    }
}
