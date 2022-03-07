package com.saf.planner;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saf.planner.ModelClass.User;
import com.example.planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Registration_frag extends Fragment implements View.OnClickListener {
    EditText fn,ln,email,phone,password,confirm_pass;
    Button signup;
    ImageView profilepic;
    String FirstName,LastName,EmailID,PhoneNo,Password,Confirm_Password,ImagePath = "";
    public Uri image_uri;
    private String Image_extention = "";
    Context context;
    NavController navController;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    public Registration_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_registration_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        fn = view.findViewById(R.id.fn_signup);
        ln = view.findViewById(R.id.ln_signup);
        email = view.findViewById(R.id.email_signup);
        phone = view.findViewById(R.id.phone_signup);
        password = view.findViewById(R.id.password_signup);
        confirm_pass = view.findViewById(R.id.confirm_signup);
        signup = view.findViewById(R.id.sign_up_btn);
        profilepic = view.findViewById(R.id.ProfilePic);
        //Progress Bar
        progressBar = view.findViewById(R.id.progress_signup);
        progressBar.setVisibility(View.GONE);
        //Toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Registration");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //Set on click Listner
        profilepic.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == profilepic)
        {
            SelectProfileImage();
        }
        else if(v == signup)
        {
            FirstName = fn.getText().toString();
            LastName = ln.getText().toString();
            EmailID = email.getText().toString();
            PhoneNo = phone.getText().toString();
            Password = password.getText().toString();
            Confirm_Password = confirm_pass.getText().toString();

            if(image_uri == null)
            {
                Toast.makeText(context,"Please Select Profile Pic!",Toast.LENGTH_LONG).show();
            }
            else if(FirstName.isEmpty())
            {
                Toast.makeText(context,"Please Enter FirstName!",Toast.LENGTH_LONG).show();
            }
            else if(LastName.isEmpty())
            {
                Toast.makeText(context,"Please Enter LastName!",Toast.LENGTH_LONG).show();
            }
            else if(EmailID.isEmpty())
            {
                Toast.makeText(context,"Please Enter !",Toast.LENGTH_LONG).show();
            }
            else if(Password.isEmpty())
            {
                Toast.makeText(context,"Please Enter Password!",Toast.LENGTH_LONG).show();
            }
            else if(Confirm_Password.isEmpty())
            {
                Toast.makeText(context,"Please Enter Confirm Password!",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(Password.contentEquals(Confirm_Password))
                {
                    registration();
                    Toast.makeText(context,"Everything is Perfect!",Toast.LENGTH_LONG).show();
                    navController.navigate(R.id.action_registration_frag_to_login_frag);
                }
                else
                {
                    Toast.makeText(context,"Password are not same!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /** METHODS FOR PROFILE PIC START **/
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //new EncodeImage(image_uri).execute();
                    profilepic.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    ImagePath = image_uri.getPath();
                    Log.i("ImageURI",image_uri.toString());
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    //new EncodeImage(image_uri).execute();
                    profilepic.setImageURI(image_uri);
                    ImagePath = image_uri.getPath();
                    getFileExtention(image_uri);
                    Log.i("ImageURI",image_uri.toString());
                    break;
            }

        }
    }



    private void SelectProfileImage() 
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
    private void getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Image_extention = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /** METHODS FOR PROFILE PIC END **/


    /** Method For Sign up Process start*/
    public void registration()
    {
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//Block UI
        //Log.i("Values:",user_name+"\n"+user_email+"\n"+user_phone+"\n"+user_pass);

        firebaseAuth.createUserWithEmailAndPassword(EmailID,Password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//UnBlock UI
                        if(task.isSuccessful())
                        {
                            //Upload Image to ref
                            storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures").child(EmailID).child(System.currentTimeMillis()+"."+Image_extention);
                            storageReference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();
                                            Log.i("Image URL",url);
                                            //Other Data to DB
                                                User user = new User(FirstName, LastName, EmailID, PhoneNo, Password, url);
                                                FirebaseDatabase.getInstance().getReference().child("User")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isComplete()) {
                                                            Toast.makeText(getContext().getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(context, MainActivity.class));
                                                        }
                                                    }
                                                });
                                        }
                                    });
                                }
                            });
                        }
                        if (!task.isSuccessful()) {
                            //Log.e("ERROR NOT Successful", "onComplete: Failed=" + task.getException().getMessage());
                            Toast.makeText(getContext().getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /** Method For Sign up Process End*/

    
}
