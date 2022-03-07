package com.saf.Dan.wadspecs;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saf.Dan.model2;
import com.saf.Dan.tabs.MainActivity;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class descfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String Name4;
    String email4;
    String date4;
    String purl4;
    String phone4;
    String key4;
    private String mParam1;
    private String mParam2;
    DatabaseReference databaseS;
    DatabaseReference databaseS1;
    FirebaseDatabase database;
    Button sendEmail, approve;
    String name,email,phone;
    public descfragment() {

    }

    public descfragment(String name,String email, String phone) {
        this.name=name;
      //  this.date=date;
       // this.mpesa=mpesa;
        this.email=email;
      //  this.purl=purl;
        this.phone=phone;
        //this.key=key;
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
        DatabaseReference df;
        Toast.makeText(getActivity(), name+"", Toast.LENGTH_SHORT).show();
        df=FirebaseDatabase.getInstance().getReference("Temporary").child("data");

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot:snapshot.getChildren()){

                    Name4=snapshot.child("name").getValue(String.class);
                    email4=snapshot.child("email").getValue(String.class);
                    date4=snapshot.child("date").getValue(String.class);
                    purl4=snapshot.child("purl").getValue(String.class);
                    phone4=snapshot.child("phone").getValue(String.class);
                    key4=snapshot.child("key").getValue(String.class);






                    }
                Toast.makeText(getActivity(), Name4+" noo", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        ImageView imageholder=view.findViewById(R.id.imagegholder);
        imageholder.setVisibility(View.GONE);
        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView date1=view.findViewById(R.id.date2);
        LinearLayout ln11=view.findViewById(R.id.ln11);
        ln11.setVisibility(View.GONE);
        LinearLayout ln12=view.findViewById(R.id.ln12);
        ln12.setVisibility(View.GONE);
        TextView emailholder=view.findViewById(R.id.emailholder);
        TextView mpesa1=view.findViewById(R.id.mpesa1);
        TextView phone1=view.findViewById(R.id.phone3);
        sendEmail=view.findViewById(R.id.sendEmail);
        approve=view.findViewById(R.id.approve);
        nameholder.setText(name);
        //date1.setText(date);
        emailholder.setText(email);
       // mpesa1.setText(mpesa);
        phone1.setText(phone);


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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Impala Warden Assignment");
                intent.putExtra(Intent.EXTRA_TEXT, "You have been assigned visitors, log into your app acco" +
                        "unt to see the details");

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
        approve.setText("Assign");
        approve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                DatabaseReference dbNode1 = FirebaseDatabase.getInstance().getReference("WardenAssign").child(phone).push();
                //DatabaseReference dbNode1 = FirebaseDatabase.getInstance().getReference("Pending").child(key4);
                HashMap<String, Object> mHashmap = new HashMap<>();
                mHashmap.put("date", date4);
                mHashmap.put("email", email4);
                mHashmap.put("name", Name4);
                mHashmap.put("phone", purl4);
                mHashmap.put("purl", phone4);
                mHashmap.put("key", key4);
                dbNode1.updateChildren(mHashmap);
                Toast.makeText(getContext(), "data updated and sent to warden", Toast.LENGTH_SHORT).show();
                //send data to warden
                DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Approved Finances").child(key4);
                //dbNode.setValue(null);
                Intent i = new Intent(getActivity(), com.saf.Dan.tabs.MainActivity.class);
                startActivity(i);


                if (isOnline()) {

                    Toast.makeText(getActivity(), "Assigned Successifully", Toast.LENGTH_SHORT).show();
//                    DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Visitors").child(name);
//                    dbNode.setValue(null);
                    Intent MainIntent = new Intent(getActivity(), MainActivity.class);
                   // startActivity(MainIntent);
                } else if (!isOnline()) {
                    Toast.makeText(getActivity(), "not online", Toast.LENGTH_SHORT).show();
                }
            }
        });


    // DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Visitors").child(key);
     //dbNode.setValue(null);
       // Toast.makeText(getActivity(), "After Approval this data node is deleted", Toast.LENGTH_SHORT).show();
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