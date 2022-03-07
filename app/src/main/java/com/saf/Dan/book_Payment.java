package com.saf.Dan;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.R;
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
import com.saf.Dan.accomodation.User;
import com.saf.Dan.accomodation.ViewData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import pdf.app.library.Callback;
import pdf.app.library.DesignPdf;

import static java.lang.Integer.parseInt;

public class book_Payment extends AppCompatActivity {
    EditText cit,res,fore,mpesa,kidno,kidnof;
    TextView citt,ress,foree, note, notee,tt,kid,kidf;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference,database1;
    DatabaseReference rootRef;
    ProgressDialog loadingBar;
    FirebaseDatabase database;
    DatabaseReference databaseS,databaseS2;
    int nopeopleg,amountg;
    String TokenID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_payment);
        setTitle("PAYMENT");
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

       // databaseS2= database.getReference("Histrory");
        cit=findViewById(R.id.citno);
        kid=findViewById(R.id.kid);
        kidno=findViewById(R.id.kidno);
        kidf=findViewById(R.id.kidf);
        kidnof=findViewById(R.id.kidnof);

        kid.setVisibility(View.GONE);
        kidno.setVisibility(View.GONE);
        kidf.setVisibility(View.GONE);
        kidnof.setVisibility(View.GONE);


        res=findViewById(R.id.resno);
        fore=findViewById(R.id.foreno);
        note=findViewById(R.id.note);
        notee=findViewById(R.id.notee);
        citt=findViewById(R.id.citt);
        tt=findViewById(R.id.total);
        ress=findViewById(R.id.ress);
        foree=findViewById(R.id.forr);
        res.setVisibility(View.GONE);
        ress.setVisibility(View.GONE);
        citt.setVisibility(View.GONE);
        cit.setVisibility(View.GONE);
        fore.setVisibility(View.GONE);
        foree.setVisibility(View.GONE);
        mpesa=(EditText) findViewById(R.id.mpesacd);
        note.setSelected(true);
        note.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        note.setSingleLine(true);
        notee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        notee.setSingleLine(true);
        notee.setSelected(true);
        firebaseUser = firebaseAuth.getCurrentUser();
        loadingBar = new ProgressDialog(this);
        String uid1 = firebaseUser.getUid();
        setDataIn();
        GetToakenID();
        String value = getIntent().getStringExtra("value");
        String value1 = getIntent().getStringExtra("value1");
        String value2 = getIntent().getStringExtra("value2");
        String value3 = getIntent().getStringExtra("value3");
        String value4 = getIntent().getStringExtra("value4");
        String value6 = getIntent().getStringExtra("value6");
        String value7 = getIntent().getStringExtra("value7");
        String fn = getIntent().getStringExtra("fn");
        String ln = getIntent().getStringExtra("ln");
        String name = getIntent().getStringExtra("name");
        String datel = getIntent().getStringExtra("datey");

        CheckBox checkBoxkidf= (CheckBox)findViewById(R.id.kidsf);
        //checkBox.setChecked(checkPasswordExist());
        checkBoxkidf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxkidf.isChecked()) {
                    kidf.setVisibility(View.VISIBLE);
                    kidnof.setVisibility(View.VISIBLE);
                    Toast.makeText(book_Payment.this, "non-resident kid", Toast.LENGTH_SHORT).show();
                }
                else {
                    kidf.setVisibility(View.GONE);
                    kidnof.setVisibility(View.GONE);
                    Toast.makeText(book_Payment.this, "unchecked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        CheckBox checkBoxkid= (CheckBox)findViewById(R.id.kids);
        //checkBox.setChecked(checkPasswordExist());
        checkBoxkid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxkid.isChecked()) {
                    kid.setVisibility(View.VISIBLE);
                    kidno.setVisibility(View.VISIBLE);
                    Toast.makeText(book_Payment.this, "Resident kid", Toast.LENGTH_SHORT).show();
                }
                else {
                    kid.setVisibility(View.GONE);
                    kidno.setVisibility(View.GONE);
                    Toast.makeText(book_Payment.this, "unchecked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        //checkBox.setChecked(checkPasswordExist());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    res.setVisibility(View.VISIBLE);
                    ress.setVisibility(View.VISIBLE);
                    Toast.makeText(book_Payment.this, "Resident", Toast.LENGTH_SHORT).show();
                }
                else {
                    res.setVisibility(View.GONE);
                    ress.setVisibility(View.GONE);
                    Toast.makeText(book_Payment.this, "unchecked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        //checkBox.setChecked(checkPasswordExist());
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox2.isChecked()) {
                    citt.setVisibility(View.VISIBLE);
                    cit.setVisibility(View.VISIBLE);
                    Toast.makeText(book_Payment.this, "citizen", Toast.LENGTH_SHORT).show();
                }
                else {
                    citt.setVisibility(View.GONE);
                    cit.setVisibility(View.GONE);
                    Toast.makeText(book_Payment.this, "unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        //checkBox.setChecked(checkPasswordExist());
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox3.isChecked()) {
                    fore.setVisibility(View.VISIBLE);
                    foree.setVisibility(View.VISIBLE);
                    Toast.makeText(book_Payment.this, "Foreigner", Toast.LENGTH_SHORT).show();
                }
                else {
                    fore.setVisibility(View.GONE);
                    foree.setVisibility(View.GONE);
                    Toast.makeText(book_Payment.this, "unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button total=findViewById(R.id.tat);
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tot = 0, nopeople = 0;
                int n1 = 0, n2 = 0, n3 = 0,n4=0,n5=0;

                if (!checkBoxkidf.isChecked()&&!checkBoxkid.isChecked()&&!checkBox.isChecked()&&!checkBox2.isChecked()&&!checkBox3.isChecked()) {

                    Toast.makeText(book_Payment.this, "Select atleast one Category", Toast.LENGTH_SHORT).show();

                } else {
                    int tres;
                    int tcit;
                    int tfor;
                    int tkid;
                    int tkidf;
                    if (checkBoxkidf.isChecked()) {
                        //res.getText().toString() != null;
                        // if(res.getText().toString() != ""){
                        try{
                            tkidf= Integer.parseInt(kidnof.getText().toString());
                            n5 = tkidf * 1000;
                            nopeople+=tkidf;
                        }

                        catch (Exception ex){
                            kidnof.setError("Enter number!");
                            kidnof.requestFocus();
                        }

                        //  }
                        //    else if(res.getText().toString() == ""){

                        //        }
                    }
                    if (checkBoxkid.isChecked()) {
                        //res.getText().toString() != null;
                        // if(res.getText().toString() != ""){
                        try{
                            tkid= Integer.parseInt(kidno.getText().toString());
                            n4 = tkid * 100;
                            nopeople+=tkid;
                        }

                        catch (Exception ex){
                            kidno.setError("Enter number!");
                            kidno.requestFocus();
                        }

                        //  }
                        //    else if(res.getText().toString() == ""){

                        //        }
                    }
                    if (checkBox.isChecked()) {
                        //res.getText().toString() != null;
                        // if(res.getText().toString() != ""){
                        try{
                            tres= Integer.parseInt(res.getText().toString());
                            n1 = tres * 200;
                            nopeople+=tres;
                        }

                        catch (Exception ex){
                            res.setError("Enter number!");
                            res.requestFocus();
                        }

                        //  }
                        //    else if(res.getText().toString() == ""){

                        //        }
                    }
                    if (checkBox2.isChecked()) {
                        try{
                            tcit= parseInt(cit.getText().toString());
                            n2 = tcit * 200;
                            nopeople+=tcit;}
                        catch (Exception ex){
                            cit.setError("Enter number!");
                            cit.requestFocus();
                        }


                    }
                    if (checkBox3.isChecked()) {

                        try {
                            tfor = parseInt(fore.getText().toString());
                            n3 = tfor * 2000;
                            nopeople+=tfor;
                        }

                        catch (Exception ex){
                            fore.setError("Enter number!");
                            fore.requestFocus();}}


                    tot = n1 + n2 + n3+n4+n5;
                    nopeopleg=nopeople;
                    amountg=tot;
                    tt.setText(String.valueOf(tot));
                    tt.setError(null);//removes error
                    tt.clearFocus();




                }
            }
        });
        Button submit=findViewById(R.id.subm);
        Button submitr=findViewById(R.id.submr);
         submitr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked()) {
                    tt.setText("");
                    Toast.makeText(book_Payment.this, "Select atleast one Category", Toast.LENGTH_SHORT).show();


                } else if (!isValidCode(mpesa.getText().toString())) {
                    mpesa.requestFocus();
                    mpesa.setError("incorrect mpesa code");
                    Toast.makeText(book_Payment.this, "Invalid mpesa code", Toast.LENGTH_SHORT).show();
                } else if (tt.getText().toString() == " " || tt.getText().toString() == "0") {
                    tt.requestFocus();
                    tt.setError("No Amount!");
                    Toast.makeText(book_Payment.this, "Invalid Amount!!", Toast.LENGTH_SHORT).show();

                } else {
                    Intent inte=new Intent(book_Payment.this, ViewData.class);
                    DatabaseReference databaseSr = database.getReference("ToResubmit").child(ln+" "+fn);
                    databaseSr.child("date").setValue(value);
                    databaseSr.child("mpesa").setValue(mpesa.getText().toString());
                    databaseSr.child("purl").setValue(value6);
                    databaseSr.child("phone").setValue(value4);
                    databaseSr.child("email").setValue(value3);
                    databaseSr.child("date").setValue(value);
                    databaseSr.child("name").setValue(value2+" "+value7);
                    databaseSr.child("amount").setValue(tt.getText().toString());
                    databaseSr.child("nopeople").setValue(String.valueOf(nopeopleg));


                   startActivity(inte);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (submit.getText() == "Choose Different date to visit again!") {
                    Intent MainIntent = new Intent(book_Payment.this, book.class);

                    Toast.makeText(book_Payment.this, "create another visit Entry", Toast.LENGTH_LONG).show();
                    startActivity(MainIntent);
                    finish();
                } else

              {

                    //submit.setActivated(false);

                submit.setText("Choose Different date to visit again!");
                submit.setBackgroundColor(getResources().getColor((R.color.color_11)));

                if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked()) {
                    tt.setText("");
                    Toast.makeText(book_Payment.this, "Select atleast one Category", Toast.LENGTH_SHORT).show();


                } else if (!isValidCode(mpesa.getText().toString())) {
                    mpesa.requestFocus();
                    mpesa.setError("incorrect mpesa code");
                    Toast.makeText(book_Payment.this, "Invalid mpesa code", Toast.LENGTH_SHORT).show();
                } else if (tt.getText().toString() == " " || tt.getText().toString() == "0") {
                    tt.requestFocus();
                    tt.setError("No Amount!");
                    Toast.makeText(book_Payment.this, "Invalid Amount!!", Toast.LENGTH_SHORT).show();

                } else {

                    String current1 = DateFormat.getDateTimeInstance()
                            .format(new Date());
                    model2 obj = new model2(value2, value, value3, value6, value4, mpesa.getText().toString());
                    obj.setdate(value);
                    obj.setMpesa(mpesa.getText().toString());
                    obj.setName(value2 + " " + value7);
                    obj.setPhone(value4);
                    obj.setEmail(value3);
                    obj.setPurl(value6);
                    //String id = databaseS.push().getKey();

                    if (isOnline() == true) {
                        loadingBar.setTitle("sending details");
                        loadingBar.setMessage("Please wait ,while we send your data...");
                        loadingBar.show();
                        databaseReference = firebaseDatabase.getReference().child("Visitors").child(ln + " " + fn);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())

                                    Toast.makeText(book_Payment.this,
                                            HtmlCompat.fromHtml("<font color='yellow'>You have already booked, but you can book again with a different date!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                                            Toast.LENGTH_LONG).show();


                                loadingBar.setCanceledOnTouchOutside(false);

                                databaseS2 = database.getReference("History").child(value2 + " " + value7).push();

                                String keyH = database.getReference("History").child(value2 + " " + value7).push().getKey();
                                databaseS2.child("name").setValue(value2 + " " + value7);
                                databaseS = database.getReference("Visitors").child(keyH);
                              //  String key = database.getReference("Visitors").push().getKey();
                                databaseS.setValue(obj);
                                databaseS.child("amount").setValue(tt.getText().toString());
                                databaseS.child("nopeople").setValue(String.valueOf(nopeopleg));


                                databaseS.child("unique").setValue(keyH);
                                databaseS.child("keyH").setValue(keyH);
                                databaseS2.child("date").setValue(value);
                                databaseS2.child("unique").setValue(keyH);
                                databaseS2.child("mpesa").setValue(mpesa.getText().toString());
                                databaseS2.child("nopeople").setValue(String.valueOf(nopeopleg));
                                databaseS2.child("amount").setValue(tt.getText().toString());
                                database1 = firebaseDatabase.getReference().child("GetRP").child(keyH);
                                database1.child("room_image").setValue("null");
                                database1.child("room_desc").setValue("null");
                                database1.child("room_name").setValue("no ");
                                database1.child("room_price").setValue("null");


                                // Toast.makeText(book.this, fn+" "+ln, Toast.LENGTH_SHORT).show();
                                Toast.makeText(book_Payment.this, "online, data sent. Wait for status", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Toast.makeText(book_Payment.this, "get receipt from your History", Toast.LENGTH_SHORT).show();
//                                if(isOnline()){


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else if (isOnline() == false) {
                        Toast.makeText(book_Payment.this, "no internet connection, retry booking later", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            }
        });



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
            Toast.makeText(book_Payment.this, "valid mpesa code", Toast.LENGTH_SHORT).show();
            System.out.println("Success");
            return true;
        }
        else {
            System.out.println("Invalid");
            return false;
        }

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
                    Toast.makeText(book_Payment.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}