package com.saf.Dan.finance;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.saf.Dan.Status;

import java.io.File;


public class recfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    myadapter adapter;
    public recfragment() {

    }

    public static recfragment newInstance(String param1, String param2) {
        recfragment fragment = new recfragment();
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

        View view=inflater.inflate(R.layout.finance_recfragment, container, false);
      View  content = inflater.inflate(R.layout.finance_recfragment, null);
        recview=(RecyclerView)view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Visitors"), model.class)
                        .build();

        adapter=new myadapter(options);
        recview.setAdapter(adapter);

    // View  content = inflater.inflate(R.layout.content_receipt, null);
       Button audit=view.findViewById(R.id.audit);
       audit.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
            public void onClick(View v) {

               PdfGenerator.getBuilder()
                       .setContext(getActivity())
                       .fromViewSource()
                       .fromView(content)
                       .setFileName("Report")
                       .setFolderName("Impala-folder")
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

       });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}