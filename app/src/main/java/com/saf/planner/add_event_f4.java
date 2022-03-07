package com.saf.planner;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saf.planner.ModelClass.Event_Info;
import com.example.planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class add_event_f4 extends Fragment implements View.OnClickListener {
    Context context;
    NavController navController;
    ProgressBar progressBar;
    ImageView EventImage;
    TextView Name,StartDate,EndDate,StartTime,EndTime,Place,Type,Instruction,NoOfGuest,Extras;
    Button AddButton;
    Uri EventImageUri;
    String eventImage,eventImageExtention,eventName,eventStartDate,eventEndDate,eventStartTime,eventEndTime,eventPlace,eventType,eventInstruction,noOfGuest,assistance,decoration,food,extras="";
    SharedPreferences sharedPreferences;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    Intent i;


    public add_event_f4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_add_event_f4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
       // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Event");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        sharedPreferences = getActivity().getSharedPreferences("AddEventData",Context.MODE_PRIVATE);
        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.nav_addEvent_host_fragment);
        EventImage = view.findViewById(R.id.event_pic_f4);
        Name = view.findViewById(R.id.event_name_f4);
        StartDate = view.findViewById(R.id.event_start_date_f4);
        EndDate = view.findViewById(R.id.event_end_date_f4);
        StartTime = view.findViewById(R.id.event_start_time_f4);
        EndTime = view.findViewById(R.id.event_end_time_f4);
        Place = view.findViewById(R.id.event_place_f4);
        Type = view.findViewById(R.id.event_type_f4);
        Instruction = view.findViewById(R.id.instruction_f4);
        NoOfGuest = view.findViewById(R.id.no_people_f4);
        Extras = view.findViewById(R.id.extra_f4);
        AddButton = view.findViewById(R.id.AddEvenr_btn_f4);
        progressBar = view.findViewById(R.id.progrerss_add_event_f4);
        getAllInfo();
        setAllInfo();
        i = getActivity().getIntent();
        if(i.getBooleanExtra("flag",false))
        {
          //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Event");
            AddButton.setText("Update Event");
        }
        AddButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == AddButton)
        {
            if(i.getBooleanExtra("flag",false))
            {
                updateEvent();
            }
            else {
                AddEvent();
                AddButton.setEnabled(false);
                AddButton.setTextColor(getResources().getColor(R.color.deeppurple));
                AddButton.setText("Loading...");
            }
        }
    }

    private void getAllInfo()
    {
        eventImage = sharedPreferences.getString("event_image_uri","");
        eventImageExtention = sharedPreferences.getString("event_image_extention","jpg");
        eventName = sharedPreferences.getString("event_name","");
        eventStartDate = sharedPreferences.getString("event_Start_date","");
        eventEndDate = sharedPreferences.getString("event_End_date","");
        eventStartTime = sharedPreferences.getString("event_start_time","");
        eventEndTime = sharedPreferences.getString("event_end_time","");
        eventPlace = sharedPreferences.getString("event_place","");
        eventType = sharedPreferences.getString("event_type","");
        eventInstruction = sharedPreferences.getString("event_description","");
        noOfGuest = sharedPreferences.getString("event_no_of_guests","");
        assistance = sharedPreferences.getString("event_assistance","");
        decoration = sharedPreferences.getString("event_decoration","");
        food=sharedPreferences.getString("event_food","");
        //Log.e("extras ",assistance+"\n"+decoration+"\n"+food);
    }
    private void setAllInfo()
    {
            EventImageUri = Uri.parse(eventImage);
            EventImage.setImageURI(EventImageUri);
            Name.setText(eventName);
            StartDate.setText(eventStartDate);
            EndDate.setText(eventEndDate);
            StartTime.setText(eventStartTime);
            EndTime.setText(eventEndTime);
            Place.setText(eventPlace);
            Type.setText(eventType);
            Instruction.setText(eventInstruction);
            NoOfGuest.setText(noOfGuest);
            if(!assistance.isEmpty() && !decoration.isEmpty() && !food.isEmpty() ) {
                if (assistance.contentEquals("Yes")) {
                    extras = "[Assistance Needed]";
                    //extras = assistance+",";
                }
                if (decoration.contentEquals("Yes")) {
                    //extras.concat(decoration + ",");
                    extras += "[Decoration Needed]";
                }
                if (food.contentEquals("Yes")) {
                    //extras.concat(food);
                    extras += "[Food Arrengement Needed]";
                }
            }else
            {
                extras = "No Extra services needed!";
            }
            Extras.setText(extras);
       // Log.e("Extra services ",extras);
    }
    private void AddEvent()
    {


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Events");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        final String uid = firebaseAuth.getCurrentUser().getUid();
        final String eventID = UUID.randomUUID().toString();

        final StorageReference str_ref = storageReference.child("Events/"+uid+"/"+eventID).child("Event_Image."+eventImageExtention);
        str_ref.putFile(EventImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {

                storageReference.child("Events/"+uid+"/"+eventID).child("Event_Image."+eventImageExtention).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.i("Image URL",url);

                        Event_Info event_info = new Event_Info(uid,url,eventName,eventStartDate,eventEndDate,eventStartTime,eventEndTime,eventPlace,eventType,eventInstruction,noOfGuest,extras);
                        progressBar.setVisibility(View.VISIBLE);
                        FirebaseDatabase.getInstance().getReference("Events").child(eventID).setValue(event_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("EventID", eventID);
                                    editor.commit();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(context, "Event Added Successfully!", Toast.LENGTH_SHORT).show();
                                   // navController.navigate(R.id.action_add_event_f4_to_add_event_f5);
                                    add_event_f5 nextFrag= new add_event_f5();
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.jkl, nextFrag, "findThisFragment")
                                            .addToBackStack(null)
                                            .commit();
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void updateEvent() {
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Events");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        final String uid = firebaseAuth.getCurrentUser().getUid();
        final String eventID = i.getStringExtra("eventID");

        if(i.getBooleanExtra("flag",false) && !i.getStringExtra("eventImage").contentEquals(EventImageUri.toString()))
        {

            final StorageReference str_ref = storageReference.child("Events/"+uid+"/"+eventID).child("Event_Image."+eventImageExtention);
            str_ref.putFile(EventImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {

                    storageReference.child("Events/"+uid+"/"+eventID).child("Event_Image."+eventImageExtention).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.i("Image URL",url);

                            Event_Info event_info = new Event_Info(uid,url,eventName,eventStartDate,eventEndDate,eventStartTime,eventEndTime,eventPlace,eventType,eventInstruction,noOfGuest,extras);

                            FirebaseDatabase.getInstance().getReference("Events").child(eventID).setValue(event_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("EventID", eventID);
                                        editor.commit();
                                        Toast.makeText(context, "Event Updated Successfully!", Toast.LENGTH_SHORT).show();
                                        //navController.navigate(R.id.action_add_event_f4_to_add_event_f5);
                                        add_event_f5 nextFrag= new add_event_f5();
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.jkl, nextFrag, "findThisFragment")
                                                  .commit();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(context,"Please Upload new Image or Same Image Again!",Toast.LENGTH_LONG).show();
        }

    }
}
