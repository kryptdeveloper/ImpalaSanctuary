package com.saf.Dan;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saf.Dan.Warden.MainActivity;
import com.example.planner.R;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    /*
        This activity handles the login process to help user enter the application.
    */
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;

    public DatabaseReference databaseReference;

    EditText userName,password,phone;
    Button login, adm1;
    TextView register,forgotPassword;
    FirebaseUser currentUser;//used to store current user of account
    FirebaseAuth mAuth;//Used for firebase authentication
    ProgressDialog loadingBar;
    TextView text;
    TextView admin;
    TextView visitor;
    TextView warden;
    String[] users = { "Head Manager", "Accommodation","Finance", "Warden", "Animal Manager", "Visitor", "Event Manager"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.pwd1);
        login = (Button) findViewById(R.id.login_btn);

        text=findViewById(R.id.text);
        register = (TextView) findViewById(R.id.registerLink);
        forgotPassword = (TextView) findViewById(R.id.ForgetPassword);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        currentUser = mAuth.getCurrentUser();

        login.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String clicked = text.getText().toString();
           if (clicked.equals("Admin")||clicked.equals("Warden")||clicked.equals("User")) {
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please specify Admin? User? or Warden?", Toast.LENGTH_LONG).show();

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegister();
            }
        });
        //if user forgets the password then to reset it
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordUser();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }



        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);



    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

        switch (users[position]){
            case "Head Manager":
                login = (Button) findViewById(R.id.login_btn);
                phone=findViewById(R.id.pphone);
                phone.setVisibility(View.GONE);
                text=findViewById(R.id.text);
                        text.setText("Head Manager");
                        login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String email = userName.getText().toString().trim();
                                String pwd = password.getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                                }
                                if (TextUtils.isEmpty(pwd)) {
                                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                }

                                if(email.equals("manager@impala.com") && pwd.equals("123456")){
                                    Intent MainIntent = new Intent(LoginActivity.this, com.saf.Dan.tabs.MainActivity.class);
                                    startActivity(MainIntent);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                Toast.makeText(getApplicationContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;

            case "Event Manager":
                login = (Button) findViewById(R.id.login_btn);
                text=findViewById(R.id.text);
                text.setText("Event");
                phone.setVisibility(View.VISIBLE);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = userName.getText().toString().trim();
                        String pwd = password.getText().toString();
                        phone=findViewById(R.id.pphone);
                        String phon = phone.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                        }
                        if (TextUtils.isEmpty(phon)) {
                            Toast.makeText(LoginActivity.this, "Please enter number", Toast.LENGTH_SHORT).show();
                        }
                        if (TextUtils.isEmpty(pwd)) {
                            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            loadingBar.setTitle("Sign In");
                            loadingBar.setMessage("Please wait ,Loging you in...");
                            loadingBar.show();
                            loadingBar.setCanceledOnTouchOutside(false);
                            mAuth.signInWithEmailAndPassword(email, pwd)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful())//If account login successful print message and send user to main Activity
                                            {
                                                Intent MainIntent = new Intent(LoginActivity.this,com.saf.Dan.Attendance.class);
                                                startActivity(MainIntent);
                                                //sendToMainActivity();
                                                Toast.makeText(LoginActivity.this, "Welcome event manager", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            } else//Print the error message incase of failure
                                            {
                                                String msg = task.getException().toString();
                                                Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });

//                            Intent MainIntent = new Intent(LoginActivity.this, com.saf.Dan.Attendance.class);
//                            startActivity(MainIntent);
                        }


                    }
                });

                Toast.makeText(getApplicationContext(), "User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;

            case "Animal Manager":
                login = (Button) findViewById(R.id.login_btn);
                phone=findViewById(R.id.pphone);
                phone.setVisibility(View.GONE);
                text=findViewById(R.id.text);
                        text.setText("Animal Manager");
                        login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String email = userName.getText().toString().trim();
                                String pwd = password.getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                                }
                                if (TextUtils.isEmpty(pwd)) {
                                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                }

                                if(email.equals("animalmanager@impala.com") && pwd.equals("123456")){
                                    Intent MainIntent = new Intent(LoginActivity.this, com.saf.Dan.updateAnimals.MainActivity.class);
                                    startActivity(MainIntent);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                Toast.makeText(getApplicationContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;
            case "Accommodation":
                login = (Button) findViewById(R.id.login_btn);
                text=findViewById(R.id.text);
                text.setText("Accommodation");
                phone=findViewById(R.id.pphone);
                phone.setVisibility(View.GONE);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = userName.getText().toString().trim();
                        String pwd = password.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                        }
                        if (TextUtils.isEmpty(pwd)) {
                            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        }

                        if(email.equals("accomanager@impala.com") && pwd.equals("123456")){
                            Intent MainIntent = new Intent(LoginActivity.this, com.saf.Dan.accomodation.MainActivity.class);
                            startActivity(MainIntent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                Toast.makeText(getApplicationContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;
            case "Visitor":
                phone.setVisibility(View.VISIBLE);
                login = (Button) findViewById(R.id.login_btn);
                text=findViewById(R.id.text);
                text.setText("Visitor Selected");
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AllowUserToLogin();
                    }
                });

                Toast.makeText(getApplicationContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;
            case "Warden":
                login = (Button) findViewById(R.id.login_btn);
                phone.setVisibility(View.VISIBLE);
                text=findViewById(R.id.text);
                text.setText("Warden Selected");
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = userName.getText().toString().trim();
                        String pwd = password.getText().toString();
                        phone=findViewById(R.id.pphone);
                        String phonen = phone.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(pwd)) {
                            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(phonen)) {
                            Toast.makeText(LoginActivity.this, "Please enter phone", Toast.LENGTH_SHORT).show();
                        }
                        else{
                        mAuth.signInWithEmailAndPassword(email, pwd)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful())//If account login successful print message and send user to main Activity
                                        {

                                            firebaseDatabase = FirebaseDatabase.getInstance();
                                            databaseReference = firebaseDatabase.getReference("User").child("wardens").child(phonen);
                                            databaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists())
                                                    {
                                                        Intent MainIntent = new Intent(LoginActivity.this,com.saf.Dan.Warden.wadActivity.class);
                                                        MainIntent.putExtra("phone",phonen);
                                                        startActivity(MainIntent);
                                                        //sendToMainActivity();
                                                        Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                    else{
                                                        Toast.makeText(LoginActivity.this, "the number is unknown", Toast.LENGTH_LONG).show();
                                                        loadingBar.dismiss();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        } else//Print the error message incase of failure
                                        {
                                            String msg = task.getException().toString();
                                            Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                    }
                 }
                });

                Toast.makeText(getApplicationContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;
            case "Finance":
                phone.setVisibility(View.VISIBLE);
                login = (Button) findViewById(R.id.login_btn);
                text=findViewById(R.id.text);
                text.setText("Finance Selected");
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = userName.getText().toString().trim();
                        String pwd = password.getText().toString();
                        phone=findViewById(R.id.pphone);
                        String phonen = phone.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(pwd)) {
                            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(phonen)) {
                            Toast.makeText(LoginActivity.this, "Please enter phone", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.setTitle("Sign In");
                            loadingBar.setMessage("Please wait ,Loging you in...");
                            loadingBar.show();
                            loadingBar.setCanceledOnTouchOutside(false);
                            mAuth.signInWithEmailAndPassword(email, pwd)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful())//If account login successful print message and send user to main Activity
                                            {

                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                databaseReference = firebaseDatabase.getReference("User").child("financers").child(phonen);
                                                databaseReference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists())
                                                        {
                                                            Intent MainIntent = new Intent(LoginActivity.this, com.saf.Dan.finance.MainActivity.class);
                                                            startActivity(MainIntent);
                                                            // sendToMainActivity();
                                                            Toast.makeText(LoginActivity.this, "Welcome finance manager", Toast.LENGTH_SHORT).show();
                                                            loadingBar.dismiss();
                                                        }
                                                        else{
                                                            Toast.makeText(LoginActivity.this, "the number is unknown", Toast.LENGTH_LONG).show();
                                                            loadingBar.dismiss();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            } else//Print the error message incase of failure
                                            {
                                                String msg = task.getException().toString();
                                                Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });

                Toast.makeText(getApplicationContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;

        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
    /*
        Handles the password reset operation.
     */
    private void resetPasswordUser() {
        String email = userName.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this,"Please enter your email id", Toast.LENGTH_SHORT).show();
        }
        else
        {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Reset Email sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    /*
        To send user to the registration page.
     */
    private void sendUserToRegister() {
        //When user wants to create a new account send user to Register Activity
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }

    /*
        Handle the login process.
     */
    private void AllowUserToLogin() {
        phone=findViewById(R.id.pphone);
        String phonen = phone.getText().toString();
        String email = userName.getText().toString().trim();
        String pwd = password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userName.setError("cannot be blank!");
            Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phonen)) {
            phone.setError("cannot be blank!");
            Toast.makeText(LoginActivity.this, "Please enter phone", Toast.LENGTH_SHORT).show();
        }
       else if (TextUtils.isEmpty(pwd)) {
            password.setError("cannot be blank!");
            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            //When both email and password are available log in to the account
            //Show the progress on Progress Dialog
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait ,Logging you in...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())//If account login successful print message and send user to main Activity
                            {

                               // firebaseUser = firebaseAuth.getCurrentUser();
                            //    final String uid = firebaseUser.getUid();
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("Approved Users").child(phonen);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            sendToMainActivity();
                                            Toast.makeText(LoginActivity.this, "Approves:: Welcome!", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "You haven't been Approved, be patient please", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else//Print the error message incase of failure
                            {
                                String msg = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }
   // protected void onStart() {
        //Check if user has already signed in if yes send to mainActivity
        //This to avoid signing in everytime you ope n the app.
     //   super.onStart();
      //  if(currentUser!=null)
     //   {
            //sendToMainActivity();
    //    }
   // }
    /**
        After successfull validation of username and password send user to the main activity.
     */
    private void sendToMainActivity() {
        //This is to send user to MainActivity
        Intent MainIntent = new Intent(LoginActivity.this, MainActivity2.class);
        startActivity(MainIntent);
    }

}
