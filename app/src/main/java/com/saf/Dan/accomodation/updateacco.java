package com.saf.Dan.accomodation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saf.Dan._reservations;

public class updateacco extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    private String userId;
    EditText mpesa;
    TextView name, price, desc;
    ImageView imgr;
    private String fn,ln,emailID,pn,profilePhotoUrl;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference,database1;
    Uri imageuri;
    RecyclerView recview;
    com.saf.Dan.History.myadapter adapter;
    String TokenID = "",namel;
    ProgressDialog loadingBar;

    ImageView image;

    DatabaseReference mbase;

    FirebaseDatabase database;
    DatabaseReference databaseS,databaseS2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateacco);
        mDatabase = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        TextView nn=findViewById(R.id.rname1);
        loadingBar = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mpesa=findViewById(R.id.rcode);

        String price1 = getIntent().getStringExtra("price");
        String desc1 = getIntent().getStringExtra("desc");
        String name1 = getIntent().getStringExtra("name");
        String image1 = getIntent().getStringExtra("image");
        imgr=findViewById(R.id.imgr);
        name=findViewById(R.id.rname);
        price=findViewById(R.id.rprice);
        desc=findViewById(R.id.descr);


        Glide.with(this).load(image1).into(imgr);
        name.setText(name1);
        desc.setText(desc1);
        price.setText(price1);

        TextView yes,no,answe;
        yes=findViewById(R.id.yes);
        no=findViewById(R.id.yesno);
        answe=findViewById(R.id.answe);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answe.setText("yes");
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answe.setText("no");
            }
        });
        Button submit=findViewById(R.id.rroom);
        submit.setText("Update Status");
        submit .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(answe.getText().toString()=="result"){
                    Toast.makeText(updateacco.this, "choose yes or no", Toast.LENGTH_SHORT).show();

                }
                else if(answe.getText().toString()=="yes"){
                    DatabaseReference reff=FirebaseDatabase.getInstance().getReference().child("Room").child(name1);
                    reff.child("status").setValue("yes");
                    Toast.makeText(updateacco.this, "Updated to Available!", Toast.LENGTH_SHORT).show();

                }
                else if(answe.getText().toString()=="no"){
                    DatabaseReference reff=FirebaseDatabase.getInstance().getReference().child("Room").child(name1);
                    reff.child("status").setValue("no");
                    Toast.makeText(updateacco.this, "Updated to Unavailable", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }
}