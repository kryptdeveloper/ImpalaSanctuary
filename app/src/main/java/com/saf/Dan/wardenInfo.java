package com.saf.Dan;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;

public class wardenInfo extends AppCompatActivity {

    TextView Name, email, date, phone;

    String name, email1, date1, phone1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden_info);

        Name =  findViewById(R.id.name1);
        email = findViewById(R.id.email1);
        phone = findViewById(R.id.phone1);
        date = findViewById(R.id.date1);


        name = getIntent().getStringExtra("Name");
        email1 = getIntent().getStringExtra("email");
        phone1 = getIntent().getStringExtra("phone");
     //   date1 = getIntent().getStringExtra("date");


        Name.setText(name);
        email.setText(email1);
        phone.setText(phone1);
        //jobDate.setText(jobdate);
        // Picasso.get().load(profpic).into(profPic);


    }
}