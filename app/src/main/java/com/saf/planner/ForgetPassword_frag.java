package com.saf.planner;


import android.content.Context;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.planner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPassword_frag extends Fragment implements View.OnClickListener{
    Context context;
    EditText email_id;
    Button send_verification_email;
    String Email_ID;
    NavController navController;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    public ForgetPassword_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_forget_password_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        progressBar = view.findViewById(R.id.progress_forget);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Forget Password");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        email_id = view.findViewById(R.id.email_forget);
        send_verification_email = view.findViewById(R.id.send_password_link_btn);

        send_verification_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == send_verification_email)
        {
            Email_ID = email_id.getText().toString();

            if(Email_ID.isEmpty())
            {
                Toast.makeText(context,"Please Enter Registered Email ID!",Toast.LENGTH_LONG).show();
            }
            else
            {
                sendPasswordResetLink(Email_ID);
                navController.navigate(R.id.action_forgetPassword_frag_to_login_frag);
            }
        }
    }

    public void sendPasswordResetLink(String email_ID)
    {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.sendPasswordResetEmail(email_ID).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context,"Password reset link is send successfully!", Toast.LENGTH_LONG).show();
                }
            });

    }
}
