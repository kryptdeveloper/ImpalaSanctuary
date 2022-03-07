package com.saf.Dan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.saf.Dan.History.VisitingHistory;
import com.saf.Dan.History.model;
import com.saf.Dan.History.myadapter;
import com.saf.Dan.accomodation.MainActivity;
import com.saf.Dan.accomodation.UploadData;
import com.saf.Dan.accomodation.User;


import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class _reservations extends AppCompatActivity {
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
        setContentView(R.layout.activity_reservations);
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
        String stat = getIntent().getStringExtra("status");
        String uid;
        try{
            uid = firebaseUser.getUid();

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
                                profilePhotoUrl = ds.child("profileURL").getValue().toString();
                                Toast.makeText(_reservations.this, namel, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
       catch (NullPointerException e){
           Toast.makeText(this, "you cannot book!!", Toast.LENGTH_SHORT).show();
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
                   Toast.makeText(_reservations.this, "choose yes or no", Toast.LENGTH_SHORT).show();

               }
              else if(answe.getText().toString()=="yes"){
                   DatabaseReference reff=FirebaseDatabase.getInstance().getReference().child("Room").child(name1);
                   reff.child("status").setValue("yes");
               }
              else if(answe.getText().toString()=="no"){
                   DatabaseReference reff=FirebaseDatabase.getInstance().getReference().child("Room").child(name1);
                   reff.child("status").setValue("no");
               }


                  }
                                      });
            //finish();
       }
        TextView date=findViewById(R.id.rdate1);
        TextView date1=findViewById(R.id.rdate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker;
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                datePicker = new DatePickerDialog(_reservations.this, R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date_ = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        date1.setText(date_);
                        Log.i("StartTime_booking:", date_);
                    }
                }, yy, mm, dd);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
                datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
                datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.AppPrimary));

            }
        });

        imgr=findViewById(R.id.imgr);
        name=findViewById(R.id.rname);
        price=findViewById(R.id.rprice);
        desc=findViewById(R.id.descr);


        Glide.with(this).load(image1).into(imgr);
        name.setText(name1);
        desc.setText(desc1);
        price.setText(price1);


        GetToakenID();
        nn.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
              //  Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                DatabaseReference reff=FirebaseDatabase.getInstance().getReference().child("ToResubmit").child(nn.getText().toString());
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Button submit=findViewById(R.id.rroom);
                        submit .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if (stat.equals("yes")){
                                    if (date1.getText().toString()=="") {
                                        date1.requestFocus();
                                        date1.setError("incorrect date format");
                                        Toast.makeText(_reservations.this, "Invalid date!!", Toast.LENGTH_SHORT).show();
                                    }
                                    if (!isValidCode(mpesa.getText().toString())) {
                                        mpesa.requestFocus();
                                        mpesa.setError("incorrect mpesa code");
                                        Toast.makeText(_reservations.this, "Invalid mpesa code", Toast.LENGTH_SHORT).show();
                                    } else if (submit.getText() == "Choose Different date to visit again!") {
                                        Intent MainIntent = new Intent(_reservations.this, book.class);

                                        Toast.makeText(_reservations.this, "create another visit Entry", Toast.LENGTH_LONG).show();
                                        startActivity(MainIntent);
                                        finish();
                                    } else {

                                        submit.setActivated(false);

                                        submit.setText("Choose Different date to visit again!");
                                        submit.setBackgroundColor(getResources().getColor((R.color.color_11)));
                                        {

                                            String current1 = DateFormat.getDateTimeInstance()
                                                    .format(new Date());
                                            String d = date1.getText().toString();
                                            model2 obj = new model2(ln + " " + fn, d, emailID, profilePhotoUrl, pn, mpesa.getText().toString());
                                            obj.setdate(d);
                                            obj.setMpesa(mpesa.getText().toString());
                                            obj.setName(ln + " " + fn);
                                            obj.setPhone(pn);
                                            obj.setEmail(emailID);
                                            obj.setPurl(profilePhotoUrl);
                                            //String id = databaseS.push().getKey();

                                            if (isOnline() == true) {
                                                loadingBar.setTitle("sending details");
                                                loadingBar.setMessage("Please wait ,while we send your data...");
                                                loadingBar.show();
                                                databaseReference = firebaseDatabase.getReference().child("Visitors").child(ln + " " + fn);
                                                String nam = ln + " " + fn;
                                                databaseReference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists())

                                                            Toast.makeText(_reservations.this,
                                                                    HtmlCompat.fromHtml("<font color='yellow'>You have already booked, but you can book again with a different date!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                                                                    Toast.LENGTH_LONG).show();


                                                        loadingBar.setCanceledOnTouchOutside(false);
                                                        databaseS2 = database.getReference("History").child(nam).push();
                                                        databaseS2.child("name").setValue(nam);
                                                        String keyH = database.getReference("History").child(nam).push().getKey();

                                                        database1 = firebaseDatabase.getReference().child("GetRP").child(keyH);

                                                        database1.child("room_image").setValue(image1);
                                                        database1.child("room_desc").setValue(desc1);
                                                        database1.child("room_name").setValue(name1);
                                                        database1.child("room_price").setValue(price1);

                                                        // databaseS = database.getReference("Visitors").push();
                                                        databaseS = database.getReference("Visitors").child(keyH);
                                                        // String key = database.getReference("Visitors").push().getKey();
                                                        databaseS.setValue(obj);
                                                        databaseS.child("amount").setValue("room price");
                                                        databaseS.child("nopeople").setValue(" persons");
                                                        databaseS.child("room_image").setValue(image1);
                                                        databaseS.child("room_desc").setValue(desc1);
                                                        databaseS.child("room_name").setValue(name1);
                                                        databaseS.child("room_price").setValue(price1);

                                                        databaseS.child("unique").setValue(keyH);
                                                        databaseS.child("keyH").setValue(keyH);
                                                        databaseS2.child("unique").setValue(keyH);
                                                        databaseS2.child("date").setValue(d);

                                                        databaseS2.child("mpesa").setValue(mpesa.getText().toString());
                                                        databaseS2.child("nopeople").setValue(" persons");
                                                        databaseS2.child("amount").setValue("room price");
                                                        databaseS2.child("room_image").setValue(image1);
                                                        databaseS2.child("room_desc").setValue(desc1);
                                                        databaseS2.child("room_name").setValue(name1);
                                                        databaseS2.child("room_price").setValue(price1);


                                                        // Toast.makeText(book.this, fn+" "+ln, Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(_reservations.this, "online, data sent. Wait for status", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                        Toast.makeText(_reservations.this, "get receipt from your History", Toast.LENGTH_SHORT).show();
//                                if(isOnline()){


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            } else if (isOnline() == false) {
                                                Toast.makeText(_reservations.this, "no internet connection, retry booking later", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                            }
                                else{
                                    Toast.makeText(_reservations.this, "SITE IS FULL", Toast.LENGTH_SHORT).show();
                                }
                        }

                        });

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

              // Toast.makeText(getApplicationContext(),user.getAmount(),Toast.LENGTH_LONG).show();



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
                    try{
                        String uid = firebaseUser.getUid();
                        TokenID = task.getResult().getToken();
                        databaseReference.child(uid).child("Tokenid").setValue(TokenID);
                    }
                    catch (NullPointerException e){
                        Toast.makeText(_reservations.this, "NullPointerException, you're not a visitor!", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    //Toast.makeText(_reservations.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isValidCode(final String code) {

        boolean number = false;
        boolean character = false;
        boolean symbol = false;
        boolean length = false;
        int letterCounter = 0;
        int numCounter = 0;

        char [] letters = code.toCharArray();

        for (char c: letters){
            if(Character.isLetter(c)) {
                letterCounter++;
            }
            else if(Character.isDigit(c)) {
                numCounter++;
            }
            else {
                symbol = true;
            }
        }

        //Checking booleans
        if (code.length()>=10) {
            length = true;
        }
        if (letterCounter>=8) {
            character = true;
        }
        if (numCounter>=2) {
            number = true;
        }

        if (character && length && number && !symbol){
            Toast.makeText(_reservations.this, "valid mpesa code", Toast.LENGTH_SHORT).show();
            System.out.println("Success");
            return true;
        }
        else {
            System.out.println("Invalid");
            return false;
        }

    }

}