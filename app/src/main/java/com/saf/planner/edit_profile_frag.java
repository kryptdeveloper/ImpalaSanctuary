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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class edit_profile_frag extends Fragment implements View.OnClickListener{
    Context context;
    ProgressBar progressBar;
    Button updatebtn;
    CircleImageView profileImage;
    EditText firstName,lastName,emailID,phoneNo,password,confirmPassword;
    NavController navController;

    public String Imageextention,uid,EMAILID,PASSWORD,FIRST_NAME,LAST_NAME,PHONE,CONFORM;
    Uri image_uri;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    //FirebaseUser firebaseUser1 = firebaseAuth.getCurrentUser();


    public edit_profile_frag() {
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
        return inflater.inflate(R.layout.events_fragment_edit_profile_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        progressBar = view.findViewById(R.id.progrerss_editProfile);
        profileImage = view.findViewById(R.id.profile_pic_edit);
        firstName = view.findViewById(R.id.user_Fname_edit);
        lastName = view.findViewById(R.id.user_Lname_edit);
        emailID = view.findViewById(R.id.user_email_edit);
        phoneNo = view.findViewById(R.id.user_phone_edit);
        password = view.findViewById(R.id.user_password_edit);
        confirmPassword = view.findViewById(R.id.user_confirm_edit);
        updatebtn = view.findViewById(R.id.update_profile_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        navController = Navigation.findNavController(getActivity(),R.id.nav_home_host_fragment);

        getDatainfields();

        profileImage.setOnClickListener(this);
        updatebtn.setOnClickListener(this);
    }

    private void getDatainfields()
    {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (uid.contentEquals(ds.getKey())) {
                            progressBar.setVisibility(View.GONE);
                            Log.i("Key",ds.getKey());
                            firstName.setText(ds.child("firstName").getValue().toString());
                            lastName.setText(ds.child("lastName").getValue().toString());
                            emailID.setText(ds.child("emailID").getValue().toString());
                            phoneNo.setText(ds.child("phoneNo").getValue().toString());
                            image_uri = Uri.parse(ds.child("profileURL").getValue().toString());
                            Picasso.get().load(image_uri).into(profileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(context,"Database error.Please Try again!",Toast.LENGTH_LONG).show();
            }
        });

    }

  /*  @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.add_event_toolbar);
        item.setVisible(false);
    }*/

    @Override
    public void onClick(View v) {
        if(v == profileImage)
        {
            getProfilePic();
            //Toast.makeText(context,"Profile Image",Toast.LENGTH_LONG).show();
        }
        else if(v == updatebtn)
        {
            SaveDataInDB();
            //Toast.makeText(context,"Update Profile Clicked!",Toast.LENGTH_LONG).show();

        }
    }



    /** METHOD FOR PROFILE PIC SELECT AND DISPLAY START **/
    private void getProfilePic() {
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //new EncodeImage(image_uri).execute();
                    profileImage.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    Log.i("ImageURI",image_uri.toString());
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    //new EncodeImage(image_uri).execute();
                    profileImage.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    Log.i("ImageURI",image_uri.toString());
                    break;
            }

        }
    }
    private void getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Imageextention = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    /** METHOD FOR PROFILE PIC AND DISPLAY END **/

    private void SaveDataInDB ()
    {
        progressBar.setVisibility(View.VISIBLE);
        EMAILID = emailID.getText().toString();
        PHONE=phoneNo.getText().toString();
        PASSWORD=password.getText().toString();
        FIRST_NAME=firstName.getText().toString();
        LAST_NAME = lastName.getText().toString();
        CONFORM = confirmPassword.getText().toString();

        if (!EMAILID.isEmpty()
                && !PASSWORD.isEmpty()
                && !PHONE.isEmpty()
                && !FIRST_NAME.isEmpty()
                && !LAST_NAME.isEmpty()
                && PASSWORD.contentEquals(CONFORM))
        {
            firebaseUser.updateEmail(EMAILID).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        //Log.e("Email Section","\nEmail:"+EMAILID+"\nPhone:"+PHONE+"\nFirstName:"+FIRST_NAME+"\nLast name:"+LAST_NAME+"\nPassword:"+PASSWORD+"\nCONFIRM:"+CONFORM);
                        Log.e("Firebase Current user",firebaseUser.getEmail());
                        firebaseUser.updatePassword(PASSWORD).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                  progressBar.setVisibility(View.GONE);
        //Log.e("Whole Task","\nEmail:"+EMAILID+"\nPhone:"+PHONE+"\nFirstName:"+FIRST_NAME+"\nLast name:"+LAST_NAME+"\nPassword:"+PASSWORD+"\nCONFIRM:"+CONFORM);
        //Toast.makeText(context,"Email & Password Changed Successfully !",Toast.LENGTH_LONG).show();
        databaseReference.child(uid).child("emailID").setValue(EMAILID);
        databaseReference.child(uid).child("password").setValue(PASSWORD);
        databaseReference.child(uid).child("phoneNo").setValue(PHONE);
        databaseReference.child(uid).child("firstName").setValue(FIRST_NAME);
        databaseReference.child(uid).child("lastName").setValue(LAST_NAME);
        databaseReference.child(uid).child("profileURL").setValue(getDownlaodUrl(image_uri));
        Toast.makeText(context,"Profile is Update Successfully !",Toast.LENGTH_LONG).show();
        reAuthUser(EMAILID,PASSWORD,firebaseUser);
        navController.navigate(R.id.action_edit_profile_frag_to_home_frag);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context,"Can't Change Email!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context,"Please fill all fields!",Toast.LENGTH_LONG).show();
        }
    }

    private String getDownlaodUrl(final Uri image_uri) {
        final String[] download_url = {""};
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference().child("ProfilePictures").child(EMAILID);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                storageReference.child(System.currentTimeMillis()+"."+Imageextention);
                storageReference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                download_url[0] =  uri.toString();
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
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        return download_url[0];
    }

    private void reAuthUser(String email,String password,FirebaseUser firebaseUser)
    {
        AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(context,"User is reAuthenticated!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(context, Main2Activity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.edit_profile_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_account_editprofile:
                deleteAccount();
                //Toast.makeText(this,"Delete Account",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAccount() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");
        final String uid = firebaseUser.getUid();
        final String email = firebaseUser.getEmail();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference profilepicRef = storageReference.child("/ProfilePictures/"+email);
        final StorageReference eventPicRef = storageReference.child("/Events/"+uid);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete acccount ? Once You delete your account, you will not be able to access your data again.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        //dialog.dismiss();
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    databaseReference.child(uid).removeValue();
                                    Intent i = new Intent(context,Main2Activity.class);
                                    startActivity(i);
                                    getActivity().finish();

                                }
                                else
                                {
                                    Toast.makeText(context,"Error:"+task.getException(),Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        builder.show();
    }
}
