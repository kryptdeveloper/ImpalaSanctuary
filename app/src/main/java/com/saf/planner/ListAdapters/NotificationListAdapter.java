package com.saf.planner.ListAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.saf.planner.Main3Activity;
import com.saf.planner.ModelClass.NotificationData;
import com.saf.planner.NotificationActivity;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ItemViewHolder> {

    ArrayList<NotificationData> notificationDataArrayList;
    Context context;
    NotificationData data;
    String uid, RUID,SUID,EID,ID;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dbref;


    public NotificationListAdapter(ArrayList<NotificationData> notificationDataArrayList, NotificationActivity notificationActivity) {
        this.notificationDataArrayList = notificationDataArrayList;
        context = notificationActivity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_notification_list_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        data = notificationDataArrayList.get(position);
        holder.notificationTitle.setText("Invitation");
        holder.notificationMessage.setText(notificationDataArrayList.get(position).getMessage());
        holder.bind(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = data.isExpanded();
                data.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Accepted!",Toast.LENGTH_LONG).show();
                notificationDataArrayList.remove(position);
                setStatus_in_Notification("Accepted");
                notifyDataSetChanged();
            }
        });
        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Rejected!",Toast.LENGTH_LONG).show();
                notificationDataArrayList.remove(position);
                setStatus_in_Notification("Rejected");
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationDataArrayList.size();
    }

    public void setStatus_in_Notification(final String status)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Invitations");
        dbref = firebaseDatabase.getReference("Events");

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
        if(status.contentEquals("Accepted")) {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                           EID = ds.getKey();
                            for (DataSnapshot ds1 : ds.getChildren()) {
                                ID = ds1.getKey();
                                if (ds1.child("Receiver_UID").getValue().toString().contentEquals(uid)) {
                                    if (ds1.child("Status").getValue().toString().contentEquals("Pending"))
                                    {
                                        ds1.child("Status").getRef().setValue(status);
                                        RUID = ds1.child("Receiver_UID").getValue().toString();
                                        SUID = ds1.child("SenderUID").getValue().toString();
                                        dbref.child(EID).child("GuestList").child(ID).child("Invited_UID").setValue(RUID);
                                        GetGiftDetails(EID,ID,context);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(status.contentEquals("Rejected")){

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (DataSnapshot ds1 : ds.getChildren()) {
                                if (ds1.child("Receiver_UID").getValue().toString().contentEquals(uid)) {
                                    if (ds1.child("Status").getValue().toString().contentEquals("Pending")) {
                                        ds1.child("Status").getRef().setValue(status);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

            }
        });
    }

    private void GetGiftDetails(final String eid, final String id, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View customLayout = LayoutInflater.from(context).inflate(R.layout.events_gift_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Add Gift", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText giftName = customLayout.findViewById(R.id.giftName_dialog);
                EditText giftDesc = customLayout.findViewById(R.id.giftDescription_dialog);

                dbref.child(eid).child("GuestList").child(id).child("GiftName").setValue(giftName.getText().toString());
                dbref.child(eid).child("GuestList").child(id).child("Gift_Description").setValue(giftDesc.getText().toString());

                Intent i = new Intent(context, Main3Activity.class);
                context.startActivity(i);

                //Toast.makeText(context,"GiftName: "+giftName.getText().toString()+"\n Gift Desc: "+giftDesc.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView notificationTitle,notificationMessage;
        Button Accept,Reject;
        LinearLayout buttonLayout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationMessage = itemView.findViewById(R.id.notification_message);
            Accept = itemView.findViewById(R.id.accept_button_notification);
            Reject = itemView.findViewById(R.id.reject_button_notification);
            buttonLayout = itemView.findViewById(R.id.linear_button_notification);
        }
        private void bind(NotificationData notificationData) {
            boolean expanded = notificationData.isExpanded();

            buttonLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }
    }
}
