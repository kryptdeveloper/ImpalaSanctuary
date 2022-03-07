package com.saf.Dan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.example.planner.R;
import com.saf.planner.Main3Activity;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pdf.app.library.Callback;
import pdf.app.library.DesignPdf;

public class book extends AppCompatActivity {

    private String fn,ln,emailID,pn,profilePhotoUrl;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference databaseS;
    Uri imageuri;

    String TokenID = "";
    EditText mpesa;
    private EditText amountEdt;
    private TextView paymentTV;
    DatePickerDialog picker;
    TextView name,name2, email, phone,url;

    Button btnGet;
    EditText date;
    DatabaseReference rootRef;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        firebaseAuth = FirebaseAuth.getInstance();
        name=findViewById(R.id.namex);
        name2=findViewById(R.id.namex1);
        email=findViewById(R.id.emailx);
        phone=findViewById(R.id.numberx);
        url=findViewById(R.id.urlstring);
        btnGet= (findViewById(R.id.submitbooking));
        database = FirebaseDatabase.getInstance();
        date=(EditText) findViewById(R.id.dateEditText);

        date.setInputType(InputType.TYPE_NULL);
        image=findViewById(R.id.picx);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  DatePickerDialog datePicker;
                    final Calendar calendar = Calendar.getInstance();
                    int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);

                    datePicker = new DatePickerDialog(book.this, R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String date_ = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            date.setText(date_);
                            Log.i("StartTime_booking:", date_);
                        }
                    }, yy, mm, dd);
                    datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker.show();
                    datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
                    datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.AppPrimary));

            }
        });
        setDataIn();
        GetToakenID();
        databaseS= database.getReference("Visitors");
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = date.getText().toString();
//                String value1 = mpesa.getText().toString();
                String value2 =String.valueOf(name2.getText());
                String value3 =String.valueOf(email.getText());
                String value4 =String.valueOf(phone.getText());
                String value6 =String.valueOf(url.getText());
                String value7 =String.valueOf(name.getText());
                String  current1 = DateFormat.getDateTimeInstance()
                        .format(new Date());

            if (value.equals("")) {
                    date.setError("empty date");
                }
                else {

                    if (isOnline()) {
                                   Intent MainIntent = new Intent(book.this, book_Payment.class);
                                    String n= name2.getText().toString()+" "+name.getText().toString();
                                    String val = date.getText().toString();
                                    MainIntent.putExtra("value",value);
                                    //MainIntent.putExtra("value1",value1);
                                    MainIntent.putExtra("value2",value2);
                                    MainIntent.putExtra("value4",value4);
                                    MainIntent.putExtra("value3",value3);
                                    MainIntent.putExtra("value7",value7);
                                    MainIntent.putExtra("value6",value6);
                                    MainIntent.putExtra("fn",fn);
                                    MainIntent.putExtra("ln",ln);
                                    MainIntent.putExtra("name",n);
                                    MainIntent.putExtra("datey",val);
                                    MainIntent.putExtra("daten",current1);
                                 startActivity(MainIntent);

                    } else if (!isOnline()) {
                        Toast.makeText(book.this, "no internet connection, retry later", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void setDataIn() {
        firebaseUser = firebaseAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();
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
                            name.setText(fn);
                            name2.setText(ln);
                            email.setText(emailID);
                            phone.setText(pn);

                            profilePhotoUrl = ds.child("profileURL").getValue().toString();
                            url.setText(profilePhotoUrl);
                            Picasso.get().load(profilePhotoUrl).into(image);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    Toast.makeText(book.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
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
}