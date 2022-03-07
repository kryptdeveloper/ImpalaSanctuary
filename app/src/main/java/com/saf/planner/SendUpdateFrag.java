package com.saf.planner;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planner.R;

import java.util.ArrayList;


public class SendUpdateFrag extends Fragment implements View.OnClickListener {

    EditText subject,message;
    Button sendupdatebtn;
    ArrayList<String> email;
    String Subject,Message;
    Context context;

    public SendUpdateFrag() {
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
        return inflater.inflate(R.layout.events_fragment_send_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        subject = view.findViewById(R.id.subject_sendUpdate);
        message = view.findViewById(R.id.message_sendUpdate);
        sendupdatebtn = view.findViewById(R.id.btn_sendUpdate);
        email = getArguments().getStringArrayList("EmailArray");
        //Log.e("EMAIL ARRAY :",email.toString());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Send Update To Guests");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        sendupdatebtn.setOnClickListener(this);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
            menu.clear();
    }


    @Override
    public void onClick(View v) {
        if(v == sendupdatebtn)
        {
            Subject = subject.getText().toString();
            Message = message.getText().toString();

            if(Subject.isEmpty())
            {
                Toast.makeText(context,"Please Enter Subject!",Toast.LENGTH_LONG).show();
            }
            else if(Message.isEmpty())
            {
                Toast.makeText(context,"Please Enter Message!",Toast.LENGTH_LONG).show();
            }
            else if(email.isEmpty())
            {
                Toast.makeText(context,"Something is Wrong.Please try again!",Toast.LENGTH_LONG).show();
            }
            else if(!Subject.isEmpty() && !Message.isEmpty() && !email.isEmpty())
            {
                String EMAIL = email.get(0);
                for(int i = 1;i<=email.size()-1;i++)
                {
                    EMAIL = EMAIL+","+email.get(i);
                }
                Log.e("EMAIL",EMAIL);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", EMAIL, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, Message);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        }
    }
}
