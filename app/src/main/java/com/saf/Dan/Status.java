package com.saf.Dan;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.R;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Status extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    FirebaseDatabase database;
    public DatabaseReference databaseReference;
    TextView name, name2,mpesa, email, status, url, date3;

    ImageView image;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    EditText paymentFor;
    String TokenID = "";
    DatabaseReference databaseS;
    String amount, nopeop;
    View content;
    String rprice2;
    private String fn, ln, emailID, date2, mpesa1, status2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        status = findViewById(R.id.status3);


        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        content = inflater.inflate(R.layout.content_receipt, null);
        String amount = getIntent().getStringExtra("amount");
        String people = getIntent().getStringExtra("people");
        String date = getIntent().getStringExtra("date");
        String name = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");

        DatabaseReference reff=FirebaseDatabase.getInstance().getReference("GetRP").child(key);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String rname2=snapshot.child("room_name").getValue().toString();
               rprice2=snapshot.child("room_price").getValue().toString();


                TextView rpp=content.findViewById(R.id.rrprice);
                TextView rrname=content.findViewById(R.id.rrrom);
                TextView resr=content.findViewById(R.id.resr);
                rpp.setText("     "+rprice2+"   Shillings");
                rrname.setText("     "+rname2+"  "+rprice2+" ksh Reserved Site");

                Toast.makeText(Status.this,"wow"+ rname2, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });






        firebaseUser = firebaseAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Approved check").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                        status.setText("Approved!! you can get receipt now");
                        status.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        status.setTextColor(getResources().getColor(R.color.TextColor_Primary));
                }
                else{
                    status.setText("Not Approved, be patient");
                    status.setBackgroundColor(getResources().getColor(R.color.darkRed));
                    status.setTextColor(getResources().getColor(R.color.TextColor_Primary));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String  current1 = DateFormat.getDateTimeInstance()
                .format(new Date());
        dateTimeDisplay = content.findViewById(R.id.datenow);
        dateTimeDisplay.setText(current1);
        EditText name1 = content.findViewById(R.id.nameq);
        name1.setText(name);
        TextView t=content.findViewById(R.id.kkk);
        t.setText("Receipt");
        TextView amou=content.findViewById(R.id.sum);

        amou.setText("     "+amount+"   Kenyan Shillings");

        TextView peo=content.findViewById(R.id.paymentFor);


        peo.setText("     "+people+" persons visiting the park");
        if(people==null){
            peo.setText("     Reservations for a site");
            amou.setVisibility(View.GONE);
            TextView text=content.findViewById(R.id.text2);
            text.setVisibility(View.GONE);
        }
        TextView t1=content.findViewById(R.id.datelater);
        t1.setText(date);


        Button print=findViewById(R.id.sprint);
        print.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(status.getText().toString().equals("Not Approved, be patient") || status.getText().toString().equals("Loading Status")){
                    Toast.makeText(Status.this, "Not approved yet OR check your network connection", Toast.LENGTH_LONG).show();
                }
                else{
                    PdfGenerator.getBuilder()
                            .setContext(Status.this)
                            .fromViewSource()
                            .fromView(content)
                            .setFileName("Impala Booking Receipt")
                            .setFolderName("PDF-folder")
                            .openPDFafterGeneration(true)
                            .build(new PdfGeneratorListener() {
                                @Override
                                public void onFailure(FailureResponse failureResponse) {
                                    super.onFailure(failureResponse);
                                }
                                @Override
                                public void onStartPDFGeneration() {

                                }
                                @Override
                                public void onFinishPDFGeneration() {

                                }
                                @Override
                                public void showLog(String log) {
                                    super.showLog(log);
                                }

                                @Override
                                public void onSuccess(SuccessResponse response) {
                                    super.onSuccess(response);
                                }
                            });
                }


            }
        });



    }



}