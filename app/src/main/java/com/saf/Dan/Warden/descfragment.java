package com.saf.Dan.Warden;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class descfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Button sendward;
    boolean isoutdated;
    String name, date, email, purl,phone,mpesa, key;
    public descfragment() {

    }

    public descfragment(String name, String date, String email, String purl,String phone, String key) {
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

        View view=inflater.inflate(R.layout.warden_desc, container, false);

        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView date1=view.findViewById(R.id.date2);
        TextView emailholder=view.findViewById(R.id.emailholder);
      //  TextView mpesa1=view.findViewById(R.id.mpesa1);
        TextView phone1=view.findViewById(R.id.phone3);
        sendward=view.findViewById(R.id.sendEmail);
        nameholder.setText(name);
        date1.setText(date);
        emailholder.setText(email);
      //  mpesa1.setText(mpesa);
        phone1.setText(purl);
        Glide.with(getContext()).load(phone).into(imageholder);

        sendward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                try {
                    String my_date = date;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date strDate = sdf.parse(my_date);
                    if (new Date().after(strDate)) {
                        DatabaseReference dbNode1 = FirebaseDatabase.getInstance().getReference("Completed").push();
                        HashMap<String, Object> mHashmap = new HashMap<>();
                        mHashmap.put("date", date);
                        mHashmap.put("email", email);
                        mHashmap.put("name", name);
                        mHashmap.put("phone", purl);
                        mHashmap.put("purl", phone);
                        dbNode1.updateChildren(mHashmap);
                        Toast.makeText(getContext(), "data updated and sent to admin for completed bookings", Toast.LENGTH_SHORT).show();
//send data to warden
                        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Pending").child(key);
                        dbNode.setValue(null);
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(getContext(), "Wait patiently for the booked date", Toast.LENGTH_SHORT).show();


                    }
                }
                catch (ParseException e) {
                    //LOG("Parse Exception " + e);
                    e.printStackTrace();
                }
//
            }
        });







        return  view;
    }

    public void onBackPressed()
    {
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new recfragment()).addToBackStack(null).commit();

    }
}