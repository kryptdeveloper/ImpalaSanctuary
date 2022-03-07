package com.saf.Dan.tabs.UserApprovals;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

public class descfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    private String mParam2;
    public DatabaseReference databaseReference;
    DatabaseReference databaseS,databaseSd;
    DatabaseReference databaseS1;
    FirebaseDatabase database;
    Button sendEmail, approve, decline;
    String name, date, email, purl,phone,mpesa,key;
    public descfragment() {

    }

    public descfragment(String name, String email, String purl,String phone) {
        this.name=name;


        this.email=email;
        this.purl=purl;
        this.phone=phone;

    }

    public static descfragment newInstance(String param1, String param2) {
        descfragment fragment = new descfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.finance_descfragment, container, false);

        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView nameholder=view.findViewById(R.id.nameholder);
      //  TextView date1=view.findViewById(R.id.date2);
        TextView emailholder=view.findViewById(R.id.emailholder);
    //    TextView mpesa1=view.findViewById(R.id.mpesa1);
        TextView phone1=view.findViewById(R.id.phone3);
        sendEmail=view.findViewById(R.id.sendEmail);
        approve=view.findViewById(R.id.approve);
        decline=view.findViewById(R.id.decline);
        nameholder.setText(name);


        LinearLayout l1=view.findViewById(R.id.ln11);
        l1.setVisibility(View.GONE);
        LinearLayout l2=view.findViewById(R.id.ln12);
        l2.setVisibility(View.GONE);
       // date1.setText(date);
        emailholder.setText(email);
       // mpesa1.setText(mpesa);
        phone1.setText(purl);
        Glide.with(getContext()).load(phone).into(imageholder);

        databaseS=FirebaseDatabase.getInstance()
                .getReference("Approved Users").child(purl);

        sendEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {


                // define Intent object
                // with action attribute as ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);

                // add three fiels to intent using putExtra function
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { email });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Impala Visitor Approvals");
                intent.putExtra(Intent.EXTRA_TEXT, "Write message here");

                // set type of intent
                intent.setType("message/rfc822");

                // startActivity with intent with chooser
                // as Email client using createChooser function
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Choose an Email client :"));
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {


                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Approved Users").child(purl);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            Toast.makeText(getContext(), "User already approved!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), purl, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            databaseS.child("name").setValue(name);
                            databaseS.child("email").setValue(email);
                            databaseS.child("phone").setValue(purl);
                            databaseS.child("purl").setValue(phone);
                            databaseS.child("approved").setValue("yes");

                            Toast.makeText(getActivity(), "data moved to accepted user approvals", Toast.LENGTH_SHORT).show();
                            DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("UserApproval").child(purl);
                            dbNode.setValue(null);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if (isOnline()) {

                   // Toast.makeText(getActivity(), "Approved", Toast.LENGTH_SHORT).show();

                  //  Intent MainIntent = new Intent(getActivity(), MainActivity.class);
                  //  startActivity(MainIntent);
                } else if (!isOnline()) {
                    Toast.makeText(getActivity(), "not online", Toast.LENGTH_SHORT).show();
                }
            }
        });


        decline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {

                databaseSd=FirebaseDatabase.getInstance()
                        .getReference("Declined Users").child(purl);
                databaseSd.child("name").setValue(name);
                databaseSd.child("email").setValue(email);
                databaseSd.child("phone").setValue(purl);
                databaseSd.child("purl").setValue(phone);
                databaseSd.child("approved").setValue("no");




                if (isOnline()) {

                    Toast.makeText(getActivity(), "data moved to declined database table", Toast.LENGTH_SHORT).show();
                    DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("UserApproval").child(purl);
                    dbNode.setValue(null);
                    Intent MainIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(MainIntent);
                } else if (!isOnline()) {
                    Toast.makeText(getActivity(), "not online", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Toast.makeText(getActivity(), "After Approval this data node is deleted", Toast.LENGTH_SHORT).show();
        return  view;
    }

    public void onBackPressed()
    {
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new recfragment()).addToBackStack(null).commit();

    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}