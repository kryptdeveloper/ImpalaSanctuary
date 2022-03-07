package com.saf.Dan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.planner.R;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saf.planner.MainActivity;
import com.saf.planner.ModelClass.User;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener{
    EditText fn,ln,email,phone,password,confirm_pass, editotp;
    Button signup,getotp;
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
    ProgressDialog loadingBar;
    String verificationCodeBySystem;
    EditText otp;
    Button verify_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
     //   navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        fn = findViewById(R.id.fn_signup);
        ln = findViewById(R.id.ln_signup);
        email = findViewById(R.id.email_signup);
        phone = findViewById(R.id.phone_signup);
        password =findViewById(R.id.password_signup);
        confirm_pass = findViewById(R.id.confirm_signup);
        signup = findViewById(R.id.sign_up_btn);
       // getotp = findViewById(R.id.otp);
        profilepic = findViewById(R.id.ProfilePic);
        //Progress Bar
        progressBar = findViewById(R.id.progress_signup);
        progressBar.setVisibility(View.GONE);
        //Toolbar
        loadingBar = new ProgressDialog(this);
        //Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //Set on click Listner
        profilepic.setOnClickListener(this);

//        getotp.setOnClickListener( new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                sendVerificationCodeToUser(phone.getText().toString());
//            }
//        });

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
                Toast.makeText(this,"Please Select Profile Pic!",Toast.LENGTH_SHORT).show();

            }
            else if(FirstName.isEmpty())
            {
                fn.setError("Enter first name!");
                Toast.makeText(this,"Please Enter FirstName!",Toast.LENGTH_SHORT).show();
            }
            else if(LastName.isEmpty())
            {
                ln.setError("Enter last name!");
                Toast.makeText(this,"Please Enter LastName!",Toast.LENGTH_SHORT).show();
            }
            else if(EmailID.isEmpty())
            {
                Toast.makeText(this,"Please Enter email !",Toast.LENGTH_SHORT).show();
            }
            else if(!isValidEmailId(EmailID)){
                email.setError("Invalid email!");
                Toast.makeText(getApplicationContext(), "invalid Email Address.", Toast.LENGTH_SHORT).show();
            }
            else if((phone.getText().toString().length() < 10)||(phone.getText().toString().length() > 11))
            {
                phone.setError("invalid!");
                Toast.makeText(this,"invalid phone number!  must be over a length 10",Toast.LENGTH_SHORT).show();
            }
            else if((password.getText().toString().length() < 8)||Password.isEmpty())
            {
                Toast.makeText(this,"invalid Password! must be over a length 8",Toast.LENGTH_SHORT).show();
            }
            else if(Confirm_Password.isEmpty())
            {
                Toast.makeText(this,"Please Enter Confirm Password!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(Password.contentEquals(Confirm_Password))
                {
                    Toast.makeText(this,"Everything is Perfect!",Toast.LENGTH_SHORT).show();
                    registration();

                   // navController.navigate(R.id.action_registration_frag_to_login_frag);
                }
                else
                {
                    Toast.makeText(this,"Password are not same!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /** METHODS FOR PROFILE PIC START **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    //permission from pop up was denied.
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ADD Profile Picture!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (RegisterActivity.this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                RegisterActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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
        image_uri = RegisterActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(takePictureIntent, 1);

    }
    private void getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = RegisterActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Image_extention = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /** METHODS FOR PROFILE PIC END **/


    /** Method For Sign up Process start*/
    public void registration()
    {
        progressBar.setVisibility(View.VISIBLE);
        RegisterActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//Block UI
        //Log.i("Values:",user_name+"\n"+user_email+"\n"+user_phone+"\n"+user_pass);
       // otp=findViewById(R.id.otpdet);
       // String code = otp.getText().toString();

//        if(code.isEmpty() || code.length() < 6){
//            otp.setError("Wrong OTP...");
//            otp.requestFocus();
//            return;
//        }
        progressBar.setVisibility(View.VISIBLE);
       // verifyCode(code);
        loadingBar.setTitle("Registering...");
        loadingBar.setMessage("Please wait ,Registering you in the system...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(EmailID,Password)
                .addOnCompleteListener((Activity) RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        RegisterActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//UnBlock UI
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
                                                        loadingBar.dismiss();
                                                        FirebaseDatabase database;
                                                        database = FirebaseDatabase.getInstance();
                                                        DatabaseReference databaseS,databaseS2;
                                                        databaseS2 = database.getReference("UserApproval").child(PhoneNo);
                                                       databaseS2.child("name").setValue(FirstName + " " + LastName);
                                                          databaseS2.child("phone").setValue(PhoneNo);
                                                       databaseS2.child("password").setValue(Password);
                                                        databaseS2.child("purl").setValue(url);
                                                        databaseS2.child("email").setValue(EmailID);
                                                        databaseS2.child("status").setValue("no");




                                                        Toast.makeText(RegisterActivity.this, "Registration Successful...Wait for Approval to be able to login", Toast.LENGTH_SHORT).show();

                                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /** Method For Sign up Process End*/
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    }


