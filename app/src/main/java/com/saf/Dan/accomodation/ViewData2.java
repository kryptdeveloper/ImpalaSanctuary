package com.saf.Dan.accomodation;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewData2 extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private RecyclerView.Adapter recyclerAdapter;
    String Database_Path = "Room";
    private ArrayList<ImageUploadInfo> uploads = new ArrayList<>();
    Spinner spinner;
    public String spinnerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data2);
        spinner = findViewById(R.id.filterType);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewData2.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        spinner.setVisibility(View.INVISIBLE);
        spinnerValue = spinner.getSelectedItem().toString();
        //Get Data From FireBase
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerValue = spinner.getSelectedItem().toString();
                getDataFromFireBase(spinnerValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getDataFromFireBase(spinnerValue);
    }

    private void getDataFromFireBase(final String spinVal) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = snapshot.getValue(ImageUploadInfo.class);
                    // if (spinVal.equals("All Rooms")) {
                    uploads.add(imageUploadInfo);
                    //}
                    if (spinVal.equals(imageUploadInfo.gethouseType())) {
                        uploads.add(imageUploadInfo);
                    }
                    System.out.println(spinVal + "    checking    " + imageUploadInfo.gethouseType().toString());
                }
                recyclerAdapter = new recylerupdate(getApplicationContext(), uploads);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clearAll() {
        if (uploads != null) {
            uploads.clear();
        }
        uploads = new ArrayList<>();
    }
}