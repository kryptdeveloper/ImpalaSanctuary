package com.saf.planner;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login_frag extends Fragment implements View.OnClickListener{
    EditText email,password;
    Button forgetPasswored,login,registration;
    CheckBox remember_checkbox;
    String Email,Password;
    Context context;
    NavController navController;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public Login_frag() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(firebaseUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_login_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        progressBar = view.findViewById(R.id.progress_login);
        progressBar.setVisibility(View.GONE);

        email = view.findViewById(R.id.email_login);
        password = view.findViewById(R.id.password_login);
        forgetPasswored = view.findViewById(R.id.forget_password_login);
        login = view.findViewById(R.id.login_btn);
        registration = view.findViewById(R.id.registration_btn);
        remember_checkbox = view.findViewById(R.id.remember_checkBox);

        firebaseAuth = FirebaseAuth.getInstance();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Login");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);


        login.setOnClickListener(this);
        forgetPasswored.setOnClickListener(this);
        registration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == login)
        {
            Email = email.getText().toString();
            Password = password.getText().toString();
            if(Email.isEmpty() )
            {
                Toast.makeText(context,"Please Enter Email ID!",Toast.LENGTH_LONG).show();
            }
            else if (Password.isEmpty())
            {
                Toast.makeText(context,"Please Enter Password!",Toast.LENGTH_LONG).show();
            }
            else {
                FirebaseLoginMethod(Email, Password);
            }
        }
        else if(v ==  forgetPasswored)
        {
            //Toast.makeText(context,"Forget Password Clicked!",Toast.LENGTH_LONG).show();
            navController.navigate(R.id.action_login_frag_to_forgetPassword_frag);
        }
        else if(v == registration)
        {
            //Toast.makeText(context,"Registeration button Clicked!",Toast.LENGTH_LONG).show();
            navController.navigate(R.id.action_login_frag_to_registration_frag);
        }

    }
    public void FirebaseLoginMethod(String UserEmail,String Password)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(UserEmail,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    updateUI(firebaseUser);
                    Intent i = new Intent(context,Main3Activity.class);
                    startActivity(i);
                    getActivity().finish();
                    Toast.makeText(getActivity().getApplicationContext(),"Successfully Logged in!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    updateUI(null);
                    Toast.makeText(getActivity().getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        //getActivity().finish();
    }
    public void updateUI(FirebaseUser user)
    {
        user = firebaseAuth.getCurrentUser();
        progressBar.setVisibility(View.GONE);
        if(user != null)
        {
            Intent i = new Intent(context,Main3Activity.class);
            startActivity(i);
            getActivity().finish();
        }
    }
}
