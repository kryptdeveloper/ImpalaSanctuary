package com.saf.planner;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.planner.R;
import com.squareup.picasso.Picasso;


public class add_event_f1 extends Fragment implements View.OnClickListener{
    EditText eventName,eventPlace;
    ImageView eventImage;
    Button nextButton;
    String EventName,EventPlace;
    Context context;
    public Uri image_uri;
    private String Imageextention;
    ProgressBar progressBar;
    NavController navController;
    SharedPreferences sharedPreferences ;
    Intent i ;


    public add_event_f1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_add_event_f1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();

        ((AppCompatActivity)getActivity()).getSupportActionBar();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Event");

        eventName = view.findViewById(R.id.event_name_f1);
        eventPlace = view.findViewById(R.id.event_place_f1);
        eventImage = view.findViewById(R.id.event_pic_f1);
        nextButton = view.findViewById(R.id.next_btn_f1);
        progressBar = view.findViewById(R.id.progrerss_add_event_f1);
        progressBar.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(),R.id.nav_addEvent_host_fragment);
        i = getActivity().getIntent();
            if (i.getBooleanExtra("flag",false))
            {
                eventName.setText(i.getStringExtra("eventName"));
                image_uri = Uri.parse(i.getStringExtra("eventImage"));

                Picasso.get().load(image_uri).into(eventImage);
                eventPlace.setText(i.getStringExtra("eventPlace"));
               // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Event");
            }



        sharedPreferences = getActivity().getSharedPreferences("AddEventData",Context.MODE_PRIVATE);

        eventImage.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == eventImage)
        {
            SelectEventPic();
        }
        else if(v == nextButton)
        {
            EventName = eventName.getText().toString();
            EventPlace = eventPlace.getText().toString();

            if(image_uri == null)
            {
                Toast.makeText(context,"Please Upload Event Image!",Toast.LENGTH_LONG).show();
            }
            else if(EventName.isEmpty())
            {
                Toast.makeText(context,"Please Enter Event Name!",Toast.LENGTH_LONG).show();
            }
            else if(EventPlace.isEmpty())
            {
                Toast.makeText(context,"Please Enter Place for Event!",Toast.LENGTH_LONG).show();
            }
            else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("event_name",EventName);
                editor.putString("event_place",EventPlace);
                editor.putString("event_image_uri",image_uri.toString());
                editor.putString("event_image_extention",Imageextention);
                editor.commit();
                navController.navigate(R.id.action_add_event_f1_to_add_event_f2);
            }

            /*if(!image_uri.toString().isEmpty() && !EventName.isEmpty() && !EventPlace.isEmpty()) {
            }
            else {
                if(image_uri.toString().isEmpty())
                {
                    Toast.makeText(context,"Please upload Event Image!",Toast.LENGTH_LONG).show();
                }
                else if(EventName.isEmpty())
                {
                    Toast.makeText(context,"Please Enter Event Name!",Toast.LENGTH_LONG).show();
                }
                else if(EventPlace.isEmpty())
                {
                    Toast.makeText(context,"Please Enter Event Place!",Toast.LENGTH_LONG).show();
                }
            }*/
           // Toast.makeText(context,EventName+"\n"+EventDate+"\n"+EventTime+"\n"+EventPlace+"\n"+EventType+"\n"+EventInfo,Toast.LENGTH_LONG).show();
        }
    }




    /** Method For Profile Pic Start **/

    private void getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Imageextention = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void SelectEventPic()
    {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ADD Profile Picture!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, 1000);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(Intent.createChooser(intent,"Select Image"), 2);

                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(takePictureIntent, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    //permisiion from pop up was denied.
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Denied...", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case 1:
                    eventImage.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    Log.i("ImageURI",image_uri.toString());
                    break;
                case 2:
                    image_uri = data.getData();
                    eventImage.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    Log.i("ImageURI",image_uri.toString());
                    break;
            }
        }
    }

    /** Method For Profile Pic End **/

}
