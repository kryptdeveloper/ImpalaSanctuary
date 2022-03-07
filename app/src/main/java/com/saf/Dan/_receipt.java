package com.saf.Dan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.planner.R;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class _receipt extends AppCompatActivity {
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_receipt);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View content = inflater.inflate(R.layout.content_receipt, null);
       // View view=inflater.inflate(R.layout.finance_recfragment, container, false);



        String current = getIntent().getStringExtra("daten");




        String name = getIntent().getStringExtra("name");
        String datel = getIntent().getStringExtra("datey");
        dateTimeDisplay = content.findViewById(R.id.datenow);
        dateTimeDisplay.setText(current);
        TextView t=content.findViewById(R.id.kkk);
        t.setText("Receipt");
        TextView t1=content.findViewById(R.id.datelater);
        t1.setText(datel);
       // TextView namee=content.findViewById(R.id.namenow);
      //  namee.setText(name);

        PdfGenerator.getBuilder()
                .setContext(_receipt.this)
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
        Intent MainIntent = new Intent(_receipt.this, MainActivity2.class);
        startActivity(MainIntent);
        finish();
    }
}