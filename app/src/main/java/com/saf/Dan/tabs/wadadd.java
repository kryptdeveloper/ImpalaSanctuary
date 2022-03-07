package com.saf.Dan.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.planner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saf.Dan.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Done#newInstance} factory method to
 * create an instance of this fragment.
 */
public class wadadd extends Fragment  implements AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText userName,password,phone,fname;
    String[] users = { "Add Warden", "Add Finance"};

    Button register,register2;
    private FirebaseAuth mAuth;//Used for firebase authentication
    private ProgressDialog loadingBar;//Used to show the progress of the registration process

    public wadadd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TravelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static wadadd newInstance(String param1, String param2) {
        wadadd fragment = new wadadd();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view=inflater.inflate(R.layout.activity_wadenadd, container, false);
        mAuth = FirebaseAuth.getInstance();
        userName = (EditText) view.findViewById(R.id.newarden);
        password = (EditText) view.findViewById(R.id.pass);
        fname = (EditText) view.findViewById(R.id.fname);
        phone = (EditText) view.findViewById(R.id.pho);
        register = (Button) view.findViewById(R.id.add);
       // register2 = (Button) view.findViewById(R.id.add2);
        loadingBar = new ProgressDialog(getActivity());


        // Inflate the layout for this fragment

        Spinner spin = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        return view;
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        switch (users[position]){
            case "Add Warden":

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String email = userName.getText().toString().trim();
                        String pwd = password.getText().toString();
                        String fname1 = fname.getText().toString();
                        String phone1 = phone.getText().toString();

                        if(TextUtils.isEmpty(email))
                        {
                            Toast.makeText(getActivity(),"Please enter email id", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(fname1))
                        {
                            Toast.makeText(getActivity(),"Please enter full names", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(phone1))
                        {
                            Toast.makeText(getActivity(),"Please enter phone", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(pwd))
                        {
                            Toast.makeText(getActivity(),"Please enter password", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            //When both email and password are available create a new accountToast.makeText(RegisterActivity.this,"Please enter password",Toast.LENGTH_SHORT).show();
                            //Show the progress on Progress Dialog
                            loadingBar.setTitle("Creating New Account");
                            loadingBar.setMessage("Please wait, we are creating new warden account");
                            loadingBar.setCanceledOnTouchOutside(true);
                            loadingBar.show();
                            mAuth.createUserWithEmailAndPassword(email,pwd)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful())//If account creation successful print message and send user to Login Activity
                                            {
                                                // sendUserToLoginActivity();
                                                FirebaseDatabase database;
                                                database = FirebaseDatabase.getInstance();
                                                DatabaseReference databaseS,databaseS2;
                                                databaseS2 = database.getReference("User").child("wardens").child(phone1);
                                                databaseS2.child("name").setValue(fname1);
                                                databaseS2.child("phone").setValue(phone1);
                                                databaseS2.child("password").setValue(pwd);

                                                databaseS2.child("email").setValue(email);
                                                Toast.makeText(getActivity(),"Account created successfully", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else//Print the error message incase of failure
                                            {
                                                String msg = task.getException().toString();
                                                Toast.makeText(getActivity(),"Error: "+msg, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }



                        Toast.makeText(getActivity(),"Warden account created successfully", Toast.LENGTH_SHORT).show();

                    }
                });
                Toast.makeText(getContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;







            case "Add Finance":
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String email = userName.getText().toString().trim();
                        String pwd = password.getText().toString();
                        String fname1 = fname.getText().toString();
                        String phone1 = phone.getText().toString();
                        if(TextUtils.isEmpty(email))
                        {
                            Toast.makeText(getActivity(),"Please enter email id", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(pwd))
                        {
                            Toast.makeText(getActivity(),"Please enter password", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(fname1))
                        {
                            Toast.makeText(getActivity(),"Please enter full names", Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(phone1))
                        {
                            Toast.makeText(getActivity(),"Please enter phone", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            //When both email and password are available create a new accountToast.makeText(RegisterActivity.this,"Please enter password",Toast.LENGTH_SHORT).show();
                            //Show the progress on Progress Dialog
                            loadingBar.setTitle("Creating New Account");
                            loadingBar.setMessage("Please wait, we are creating new finance account");
                            loadingBar.setCanceledOnTouchOutside(true);
                            loadingBar.show();
                            mAuth.createUserWithEmailAndPassword(email,pwd)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful())//If account creation successful print message and send user to Login Activity
                                            {
                                                // sendUserToLoginActivity();
                                                FirebaseDatabase database;
                                                database = FirebaseDatabase.getInstance();
                                                DatabaseReference databaseS,databaseS2;
                                                databaseS2 = database.getReference("User").child("financers").child(phone1);
                                                databaseS2.child("name").setValue(fname1);
                                                databaseS2.child("phone").setValue(phone1);
                                                databaseS2.child("password").setValue(pwd);

                                                databaseS2.child("email").setValue(email);
                                                Toast.makeText(getActivity(),"Account created successfully", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else//Print the error message incase of failure
                                            {
                                                String msg = task.getException().toString();
                                                Toast.makeText(getActivity(),"Error: "+msg, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }



                        Toast.makeText(getActivity(),"Finance account created successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(getContext(), "Selected User: "+users[position] ,Toast.LENGTH_SHORT).show();
                break;

        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
    private void createNewAccount() {

    }
}