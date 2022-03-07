package com.saf.planner;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.planner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class add_event_f2 extends Fragment implements View.OnClickListener{
    Context context;
    NavController navController;
    EditText No_of_Guests;
    TextView Start_Time,End_Time,StartDate,EndDate;
    String[] Type_of_Event = {"Select Type of Event","Small","Medium","Large","Grand"};
    Button Next;
    Spinner Type;
    ProgressBar progressBar;
    String start_date,end_date,startTime,endTime,type,noOfGuests,time;
    SimpleDateFormat sdf;
    SharedPreferences sharedPreferences;
    Intent i;


    public add_event_f2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.events_fragment_add_event_f2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar();
    //    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Event");

        sharedPreferences = getActivity().getSharedPreferences("AddEventData",Context.MODE_PRIVATE);
        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.nav_addEvent_host_fragment);
        progressBar = view.findViewById(R.id.progrerss_add_event_f2);
        Type = view.findViewById(R.id.event_Type_f2);
        Start_Time = view.findViewById(R.id.event_Start_time_f2);
        End_Time = view.findViewById(R.id.event_End_time_f2);
        StartDate = view.findViewById(R.id.event_StartDate_f2);
        EndDate = view.findViewById(R.id.event_EndDate_f2);
        No_of_Guests = view.findViewById(R.id.event_no_guest_f2);
        Next = view.findViewById(R.id.next_btn_f2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,Type_of_Event);
        Type.setAdapter(adapter);
        Type.setSelection(0);
        Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        i = getActivity().getIntent();
        if (i.getBooleanExtra("flag",false))
        {
            int position = 0;
            for(int j=0;j<Type_of_Event.length;j++)
            {
                if(Type_of_Event[j].contentEquals(i.getStringExtra("eventType")))
                {
                    position = j;
                    break;
                }
            }
            Type.setSelection(position);
            Start_Time.setText(i.getStringExtra("eventStartTime"));
            End_Time.setText(i.getStringExtra("eventEndTime"));
            StartDate.setText(i.getStringExtra("eventStartDate"));
            EndDate.setText(i.getStringExtra("eventEndDate"));
            No_of_Guests.setText(i.getStringExtra("noOfGuest"));
        //    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Event");
        }


        Start_Time.setOnClickListener(this);
        End_Time.setOnClickListener(this);
        StartDate.setOnClickListener(this);
        EndDate.setOnClickListener(this);
        Next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == Start_Time)
        {
            setStartTime();
        }
        else if(v == End_Time)
        {
            setEndTime();
        }
        else  if(v == StartDate)
        {
            setStartDate();
        }
        else if(v == EndDate)
        {
            setEndDate();
        }
        else if(v == Next)
        {
            Log.e("Selected Dropdown Item",type);
            start_date = StartDate.getText().toString();
            end_date = EndDate.getText().toString();
            startTime = Start_Time.getText().toString();
            endTime = End_Time.getText().toString();
            noOfGuests = No_of_Guests.getText().toString();

            if(start_date.isEmpty())
            {
                Toast.makeText(context,"Please Select Start Date for Event!",Toast.LENGTH_LONG).show();
            }
            else if(end_date.isEmpty())
            {
                Toast.makeText(context,"Please Select End Date for Event!",Toast.LENGTH_LONG).show();
            }
            else if(startTime.isEmpty())
            {
                Toast.makeText(context,"Please Select Start time!",Toast.LENGTH_LONG).show();
            }
            else if(endTime.isEmpty())
            {
                Toast.makeText(context,"Please Select End time!",Toast.LENGTH_LONG).show();
            }
            else if(noOfGuests.isEmpty())
            {
                Toast.makeText(context,"Please Enter No of Guests!",Toast.LENGTH_LONG).show();
            }
            else if(type.contentEquals("Select Type of Event"))
            {
                Toast.makeText(context,"Please Select Type of Event!",Toast.LENGTH_LONG).show();
            }
            else{
                if(compareTime())
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("event_type",type);
                    editor.putString("event_Start_date",start_date);
                    editor.putString("event_End_date",end_date);
                    editor.putString("event_start_time",startTime);
                    editor.putString("event_end_time",endTime);
                    editor.putString("event_no_of_guests",noOfGuests);
                    editor.commit();
                    navController.navigate(R.id.action_add_event_f2_to_add_event_f3);
                }
                else
                {
                    Toast.makeText(context,"End Should not less then Start Time!",Toast.LENGTH_LONG).show();
                }
            }


            /*if(!date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()  && !noOfGuests.isEmpty() && !type.contentEquals("Select Type of Event"))
            {}
            else
            {Toast.makeText(context,"Please fill all fields!",Toast.LENGTH_LONG).show(); }*/
        }
    }

    /** Method for Setting Start DATE In Edit Text using Calender START **/
    private void setStartDate()
    {
        DatePickerDialog datePicker;
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date_ = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                StartDate.setText(date_);
                Log.i("StartTime_booking:", date_);
            }
        }, yy, mm, dd);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
    }
    /** Method for Setting Start DATE In Edit Text using Calender END **/

    /** Method for Setting End DATE In Edit Text using Calender START **/
    private void setEndDate()
    {
        DatePickerDialog datePicker;
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date_ = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                EndDate.setText(date_);
                Log.i("StartTime_booking:", date_);
            }
        }, yy, mm, dd);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
    }
    /** Method for Setting End DATE In Edit Text using Calender END **/

    /** Method for Setting START TIME In Edit Text using CLOCK START **/
    private void setStartTime()
    {
        TimePickerDialog timePicker;
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        final String today_date = dd + "/" + mm + "/" + yy;
        final Calendar calendar = Calendar.getInstance();
        timePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                if (Start_Time.getText().toString().contains(today_date))
                {

                    if (Hour >= calendar.get(Calendar.HOUR_OF_DAY))
                    {
                        String M;
                        //Log.i("Status:", "Booking date is today and Hour>=current");
                        if (Minute < 10)
                        {
                            M = "0" + Minute;
                        }
                        else
                        {
                            M = String.valueOf(Minute);
                        }

                        String H;
                        if (Hour < 10)
                        {
                            H = "0" + Hour;
                        }
                        else
                        {
                            H = String.valueOf(Hour);
                        }
                        time = H + ":" + M;
                        Start_Time.setText(time);
                        Log.i("Start", time);
                        Log.i("Booking Start Time :", Start_Time.getText().toString());
                    }
                    else
                    {
                        Toast.makeText(context, "Invalid time!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    String M;
                    if (Minute < 10) {
                        M = "0" + Minute;
                    } else {
                        M = String.valueOf(Minute);
                    }

                    String H;
                    if (Hour < 10) {
                        H = "0" + Hour;
                    } else {
                        H = String.valueOf(Hour);
                    }

                    time = H + ":" + M;
                    Start_Time.setText(time);
                    Log.i("Start", time);
                    Log.i("Booking Start Time :", Start_Time.getText().toString());
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePicker.show();
        timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
        timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
    }
    /** Method for Setting START TIME In Edit Text using CLOCK END **/
    /** Method for Setting END TIME In Edit Text using CLOCK START **/
    private void setEndTime()
    {
        TimePickerDialog timePicker;
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        final String today_date = dd + "/" + mm + "/" + yy;
        final Calendar calendar = Calendar.getInstance();
        timePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                if (End_Time.getText().toString().contains(today_date))
                {

                    if (Hour >= calendar.get(Calendar.HOUR_OF_DAY))
                    {
                        String M;
                        //Log.i("Status:", "Booking date is today and Hour>=current");
                        if (Minute < 10)
                        {
                            M = "0" + Minute;
                        }
                        else
                        {
                            M = String.valueOf(Minute);
                        }

                        String H;
                        if (Hour < 10)
                        {
                            H = "0" + Hour;
                        }
                        else
                        {
                            H = String.valueOf(Hour);
                        }
                        time = H + ":" + M;
                        End_Time.setText(time);
                        Log.i("Start", time);
                        Log.i("Booking Start Time :", End_Time.getText().toString());
                    }
                    else
                    {
                        Toast.makeText(context, "Invalid time!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    String M;
                    if (Minute < 10) {
                        M = "0" + Minute;
                    } else {
                        M = String.valueOf(Minute);
                    }

                    String H;
                    if (Hour < 10) {
                        H = "0" + Hour;
                    } else {
                        H = String.valueOf(Hour);
                    }

                    time = H + ":" + M;
                    End_Time.setText(time);
                    Log.i("Start", time);
                    Log.i("Booking Start Time :", End_Time.getText().toString());
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePicker.show();
        timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
        timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.AppPrimary));
    }
    /** Method for Setting END TIME In Edit Text using CLOCK END **/
    /** METHOD FOR COMPARE START TIME AND END TIME START **/
    private boolean compareTime() {
        sdf = new SimpleDateFormat("hh:mm");
        if (startTime != null && endTime != null) {
            try {
                java.util.Date start = sdf.parse(startTime);
                Date end = sdf.parse(endTime);
                if (isTimeAfter(start, end)) {
                    //Toast.makeText(context,"End time is greater then Start time",Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    //Toast.makeText(context,"End time is not greater then Start time!!!",Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    boolean isTimeAfter(Date start, Date end) {
        if (start.compareTo(end)>=0 ) {
            return false;
        } else {
            return true;
        }
    }
    /** METHOD FOR COMPARE START TIME AND END TIME END **/


}
