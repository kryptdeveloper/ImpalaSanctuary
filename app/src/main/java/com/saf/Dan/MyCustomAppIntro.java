package com.saf.Dan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.planner.R;
import com.github.appintro.AppIntro;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyCustomAppIntro extends AppIntro {
    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom_app_intro);
       TextView talk=findViewById(R.id.talk);
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(MyCustomAppIntro.this, Whatsapp.class);
                startActivity(intent5);
            }
        });
    }
}