package com.saf.planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saf.planner.ListAdapters.NotificationListAdapter;
import com.saf.planner.ModelClass.NotificationData;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notificationList;
    TextView textView;
    ProgressBar progressBar;
    ArrayList<NotificationData> notificationDataArrayList;
    NotificationListAdapter notificationListAdapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String RID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity_notification);

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications");

        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        notificationList = findViewById(R.id.notification_list);
        textView = findViewById(R.id.no_notificationText);
        progressBar = findViewById(R.id.progress_notification);

        notificationList.setLayoutManager(new LinearLayoutManager(this));
        ((SimpleItemAnimator) notificationList.getItemAnimator()).setSupportsChangeAnimations(false);
        notificationDataArrayList = new ArrayList<>();
        notificationListAdapter = new NotificationListAdapter(notificationDataArrayList,this);
        notificationList.setHasFixedSize(true);
        notificationList.setAdapter(notificationListAdapter);
        getDataOfNotification();
    }

    private void getDataOfNotification() {
        mAuth = FirebaseAuth.getInstance();
        RID = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Invitations");
        notificationDataArrayList.clear();
        notificationListAdapter.notifyDataSetChanged();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        for(DataSnapshot ds1: ds.getChildren())
                        {
                            if(ds1.child("Receiver_UID").getValue().toString().contentEquals(RID) && ds1.child("Status").getValue().toString().contentEquals("Pending"))
                            {
                                Log.e("ReceiverUID",ds1.child("Receiver_UID").getValue().toString());
                                Log.e("Message",ds1.child("Invitation_Message").getValue().toString());
                                Log.e("Sender UID",ds1.child("SenderUID").getValue().toString());
                                notificationList.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.GONE);
                                notificationDataArrayList.add(new NotificationData(ds1.child("Invitation_Message").getValue().toString()));
                            }
                        }
                    }
                    notificationListAdapter.notifyDataSetChanged();
                    if(notificationDataArrayList.size() == 0)
                    {
                        textView.setVisibility(View.VISIBLE);
                        notificationList.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NotificationActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(NotificationActivity.this,Main3Activity.class);
        startActivity(i);
        finish();
    }
}
