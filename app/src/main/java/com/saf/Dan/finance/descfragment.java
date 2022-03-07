package com.saf.Dan.finance;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saf.Dan.MainActivity2;
import com.saf.Dan.book;
import com.saf.Dan.model2;

import java.util.Objects;

public class descfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    DatabaseReference databaseS;
    DatabaseReference databaseS1;
    FirebaseDatabase database;
    Button sendEmail, approve;
    String name, date, email, purl,phone,mpesa,key;
    public descfragment() {

    }

    public descfragment(String name, String date, String email, String purl,String phone,String mpesa,String key) {
        this.name=name;
        this.date=date;
        this.mpesa=mpesa;
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

        View view=inflater.inflate(R.layout.finance_descfragment, container, false);

        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView date1=view.findViewById(R.id.date2);
        TextView emailholder=view.findViewById(R.id.emailholder);
        TextView mpesa1=view.findViewById(R.id.mpesa1);
        TextView phone1=view.findViewById(R.id.phone3);
        sendEmail=view.findViewById(R.id.sendEmail);
        approve=view.findViewById(R.id.approve);
        nameholder.setText(name);
        date1.setText(date);
        emailholder.setText(email);
        mpesa1.setText(mpesa);
        phone1.setText(purl);
        Glide.with(getContext()).load(phone).into(imageholder);
       DatabaseReference database1S=FirebaseDatabase.getInstance()
                .getReference("History");
        databaseS=FirebaseDatabase.getInstance()
                .getReference("Approved Finances");
      //  String key = databaseS1.push().getKey();
        databaseS1=FirebaseDatabase.getInstance()
                .getReference("Approved check").child(key);
     //   databaseS= database.getReference("Visitors");
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Impala Visitor Booking");
                intent.putExtra(Intent.EXTRA_TEXT, "Booking confirmed successfully!! come the booked Date!");

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


                model2 obj =new model2(name,date,email,phone,purl,mpesa);
                obj.setdate(date);
                obj.setMpesa(mpesa);
                obj.setName(name);
                obj.setPhone(purl);
                obj.setEmail(email);
                obj.setPurl(phone);
              //  String key = databaseS1.push().getKey();
                //String id = databaseS.push().getKey();
                // Push creates a unique id in database
                // FirebaseDatabase.getInstance().getReference().child("Customer" + value).child("Details").updateChildren(map);
                databaseS.child(key).setValue(obj);
                databaseS.child(key).child("Approved").setValue("Approved");
                databaseS.child(key).child("key").setValue(key);
                databaseS1.child("name").setValue(name);
                databaseS1.child("date").setValue(date);
                databaseS1.child("mpesa").setValue(mpesa);
                databaseS1.child("phone").setValue(purl);
                databaseS1.child("purl").setValue(phone);

                //database1S.child(key).child("approved").setValue("yes");
              // DatabaseReference databaseReference = firebaseDatabase.getReference("FinanceKey").child("Approved checkey")
              //database1S.child(key).child("Approved").setValue(key);


                if (isOnline()) {

                    Toast.makeText(getActivity(), "Approved", Toast.LENGTH_SHORT).show();
                    DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Visitors").child(name);
                    dbNode.setValue(null);
                    Intent MainIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(MainIntent);
                } else if (!isOnline()) {
                    Toast.makeText(getActivity(), "not online", Toast.LENGTH_SHORT).show();
                }
            }
        });


     DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference("Visitors").child(key);
     dbNode.setValue(null);
        Toast.makeText(getActivity(), "After Approval this data node is deleted", Toast.LENGTH_SHORT).show();
        return  view;
    }

    public void onBackPressed()
    {
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new com.saf.Dan.finance.recfragment()).addToBackStack(null).commit();

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