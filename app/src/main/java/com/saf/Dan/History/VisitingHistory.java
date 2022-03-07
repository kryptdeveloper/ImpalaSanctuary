package com.saf.Dan.History;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class VisitingHistory extends AppCompatActivity {
    private String fn,ln,emailID,pn,profilePhotoUrl;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    Uri imageuri;
    RecyclerView recview;
    com.saf.Dan.History.myadapter adapter;
    String TokenID = "",namel;
    ProgressDialog loadingBar;
SearchView search;
    ImageView image;

    DatabaseReference mbase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitinghistory);
        firebaseAuth = FirebaseAuth.getInstance();
             TextView nn=findViewById(R.id.nn);
        loadingBar = new ProgressDialog(this);
        firebaseUser = firebaseAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();
        search=findViewById(R.id.sear);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.getKey().contentEquals(uid)) {
                            fn = ds.child("firstName").getValue().toString();
                            ln = ds.child("lastName").getValue().toString();
                            emailID = ds.child("emailID").getValue().toString();
                            pn = ds.child("phoneNo").getValue().toString();
                            namel=ln+" "+fn;
                            nn.setText(namel);
                          //  Toast.makeText(VisitingHistory.this, namel, Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        GetToakenID();
        loadingBar.setTitle("Loading your History");
        loadingBar.setMessage("Please be patient...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        nn.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //Toast.makeText(VisitingHistory.this, namel+"text changed", Toast.LENGTH_SHORT).show();
                mbase = FirebaseDatabase.getInstance().getReference().child("History").child(nn.getText().toString());
                FirebaseRecyclerOptions<model> options =
                        new FirebaseRecyclerOptions.Builder<model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("History").child(namel), model.class)
                                .build();
                recview=(RecyclerView)findViewById(R.id.recycler1);
                recview.setLayoutManager(new LinearLayoutManager(VisitingHistory.this));



                adapter=new myadapter(options);
                recview.setAdapter(adapter);
                //Toast.makeText(VisitingHistory.this, "no available data!", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                if(search!=null){
                    search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            return true;
                        }
                    });
                }

                adapter.startListening();

            }
        });
    }

    private void GetToakenID() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("User");

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    String uid = firebaseUser.getUid();
                    TokenID = task.getResult().getToken();
                    databaseReference.child(uid).child("Tokenid").setValue(TokenID);
                } else {
                    Toast.makeText(VisitingHistory.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}