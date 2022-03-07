package com.saf.Dan.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saf.Dan.book;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

public class descfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Button sendward;
    List<String> names;

    Spinner spinner;
    DatabaseReference df;
    String name, date, email, purl,phone,mpesa,key;
    public descfragment() {

    }

    public descfragment(String name, String date, String email, String purl,String phone,String key) {
        this.name=name;
        this.date=date;
        this.email=email;
        this.purl=purl;
        this.phone=phone;
        this.key=key;
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

        View view=inflater.inflate(R.layout.admin_desc, container, false);

        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView date1=view.findViewById(R.id.date2);
        TextView emailholder=view.findViewById(R.id.emailholder);
      //  TextView mpesa1=view.findViewById(R.id.mpesa1);
        TextView phone1=view.findViewById(R.id.phone3);
        sendward=view.findViewById(R.id.sendEmail);
         Button selward=view.findViewById(R.id.selward);
        nameholder.setText(name);
        date1.setText(date);
        emailholder.setText(email);
      //  mpesa1.setText(mpesa);
        phone1.setText(purl);
        Glide.with(getContext()).load(phone).into(imageholder);

        spinner=view.findViewById(R.id.spinner);
        names=new ArrayList<>();
        df=FirebaseDatabase.getInstance().getReference("User");

        df.child("wardens").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot:snapshot.getChildren()){

                String spinnerName=snapshot.child("0759867550").child("name").getValue(String.class);
                 names.add(spinnerName);}
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
           spinner.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });











        selward.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            DatabaseReference dbNode1 = FirebaseDatabase.getInstance().getReference("Temporary").child("data");
                                            HashMap<String, Object> mHashmap = new HashMap<>();
                                            mHashmap.put("date", date);
                                            mHashmap.put("email", email);
                                            mHashmap.put("name", name);
                                            mHashmap.put("phone", purl);
                                            mHashmap.put("purl", phone);
                                            mHashmap.put("key", key);
                                            dbNode1.updateChildren(mHashmap);
                                            Toast.makeText(getContext(), "temp", Toast.LENGTH_SHORT).show();
                                            //send data to warden
                                            DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Approved Finances").child(key);
                                            dbNode.setValue(null);
                                            Intent i = new Intent(getActivity(),MainActivity.class);
                                            startActivity(i);
                                            startActivity(new Intent(getActivity(), com.saf.Dan.wadspecs.MainActivity.class));
                                        }
                                    });
        if(!selward.getText().toString().equals("Select Warden")){
        sendward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {

            }
        });}
        else{
            Toast.makeText(getActivity(), "Invalid warden selection!!", Toast.LENGTH_SHORT).show();
        }







        return  view;
    }

    public void onBackPressed()
    {
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new recfragment()).addToBackStack(null).commit();

    }
}