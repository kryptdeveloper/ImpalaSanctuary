package com.saf.Dan.wadspecs;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.firebase.database.FirebaseDatabase;


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

        recview=(RecyclerView)view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child("wardens"), model.class)
                        .build();

        adapter=new myadapter(options);
        recview.setAdapter(adapter);


       Button audit=view.findViewById(R.id.audit);
       audit.setVisibility(View.GONE);
       audit.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
            public void onClick(View v) {
               PdfGenerator.getBuilder()
                       .setContext(getContext())
                       .fromViewSource()
                       .fromView(view) /* "targetView" is the view ,you want to convert PDF */
                       /* "fromLayoutXML()" takes array of layout resources.
                        * You can also invoke "fromLayoutXMLList()" method here which takes list of layout resources instead of array. */

                       /* It takes default page size like A4,A5. You can also set custom page size in pixel
                        * by calling ".setCustomPageSize(int widthInPX, int heightInPX)" here. */
                       .setFileName("Test-PDF")
                       /* It is file name */
                       .setFolderName("FolderA/FolderB/FolderC")
                       /* It is folder name. If you set the folder name like this pattern (FolderA/FolderB/FolderC), then
                        * FolderA creates first.Then FolderB inside FolderB and also FolderC inside the FolderB and finally
                        * the pdf file named "Test-PDF.pdf" will be store inside the FolderB. */
                       .openPDFafterGeneration(true)
                       /* It true then the generated pdf will be shown after generated. */
                       .build(new PdfGeneratorListener() {
                           @Override
                           public void onFailure(FailureResponse failureResponse) {
                               super.onFailure(failureResponse);
                               /* If pdf is not generated by an error then you will findout the reason behind it
                                * from this FailureResponse. */
                           }

                           @Override
                           public void showLog(String log) {
                               super.showLog(log);
                               /*It shows logs of events inside the pdf generation process*/
                           }

                           @Override
                           public void onStartPDFGeneration() {

                           }

                           @Override
                           public void onFinishPDFGeneration() {

                           }

                           @Override
                           public void onSuccess(SuccessResponse response) {
                               super.onSuccess(response);
                               /* If PDF is generated successfully then you will find SuccessResponse
                                * which holds the PdfDocument,File and path (where generated pdf is stored)*/

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