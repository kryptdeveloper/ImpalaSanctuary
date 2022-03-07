package com.saf.Dan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.saf.Dan.Warden.MainActivity;

public class wardenlog extends AppCompatActivity {
    EditText userName,password;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    Button login;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardlog);

        TextView textView = findViewById(R.id.textView);
// textView.setText(getIntent().getStringExtra("param"));
        userName = (EditText) findViewById(R.id.userNamew);
        password = (EditText) findViewById(R.id.pwdw);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        loadingBar = new ProgressDialog(this);
        login = (Button) findViewById(R.id.loginward6);
        //TextView text=findViewById(R.id.text);
       // text.setText("Warden Selected");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userName.getText().toString().trim();
                String pwd = password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(wardenlog.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(wardenlog.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                loadingBar.setTitle("Signing in");
                loadingBar.setMessage("be patient dear warden");
                loadingBar.show();
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())//If account login successful print message and send user to main Activity
                                {
                                    Intent MainIntent = new Intent(wardenlog.this, MainActivity.class);
                                    startActivity(MainIntent);
                                    // sendToMainActivity();
                                    Toast.makeText(wardenlog.this, "Welcome warden", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                } else//Print the error message incase of failure
                                {
                                    String msg = task.getException().toString();
                                    Toast.makeText(wardenlog.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
            }
        });
    }
}