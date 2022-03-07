package com.saf.Dan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.planner.R;
import com.saf.planner.Main3Activity;
import com.saf.planner.Main4Activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class check extends Fragment {



    private OnFragmentInteractionListener listener;
    DatePickerDialog picker;
    EditText date, name, email, phone;
    Button btnGet;
    DatabaseReference rootRef;
    FirebaseDatabase database;
    DatabaseReference databaseS;
    public static check newInstance() {
        return new check();
    }
    public check() {
        // Required empty public constructor
    }

    public static check newInstance(String param1, String param2) {
        check fragment = new check();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check, container, false);

                    //msg = "Add Event";
//
//                            Intent i = new Intent(getActivity(), Main4Activity.class);
//                            startActivity(i);


        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {

        void onClicked();

    }

}