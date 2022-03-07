package com.saf.Dan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.example.planner.R;
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
import com.saf.Dan.updateAnimals.ViewData;
import com.saf.planner.Main2Activity;
import com.saf.planner.Main3Activity;
import com.saf.planner.Main4Activity;
import com.saf.planner.home_frag;
import com.squareup.picasso.Picasso;

public class MainActivity2 extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    FirebaseDatabase database;
    String fn,ln;
    DatabaseReference databaseS;
    DrawerLayout drawerLayout;
    String TokenID = "";
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private static int SplashTimeout = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        drawerLayout = findViewById(R.id.drawer1);
        toolbar = findViewById(R.id.toolBar);
        //toolbar.getNavigationIcon().setAlpha(R.color.babypink);
        firebaseAuth = FirebaseAuth.getInstance();
        //ActionBar actionBar;
        //actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
       // ColorDrawable colorDrawable
         //       = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        //actionBar.setBackgroundDrawable(colorDrawable);
        ///setDataIn();
        ///GetToakenID();
       /// Toast.makeText(MainActivity2.this, setDataIn(), Toast.LENGTH_LONG).show();
       Fragment fragment = new FirstFragment();
        loadFragment(fragment);
        Bundle bundle = new Bundle();
        bundle.putString("key", fn+ln);

        fragment.setArguments(bundle);
     // loadFragment(new FirstFragment());
        setSupportActionBar(toolbar);
       // getSupportActionBar();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment fragment = null;
                switch (id) {
                    case R.id.home1:
                        fragment = new FirstFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.book:
                       // fragment = new check();
                       // loadFragment(fragment);
                        Intent intent = new Intent(MainActivity2.this, book.class);
                        startActivity(intent);
                        break;
                  
                    case R.id.events:
                        fragment = new home_frag();
                        loadFragment(fragment);
                        break;
                    case R.id.activities:
                        Intent intent21 = new Intent(MainActivity2.this, com.saf.Dan.activities.ViewData.class);
                        startActivity(intent21);
                        break;
                    case R.id.anim:
                        Intent intent2 = new Intent(MainActivity2.this, ViewData.class);
                        startActivity(intent2);
                        break;
                    case R.id.abt:
                        fragment = new FourthFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.feedb:
                        Intent intent3 = new Intent(MainActivity2.this, FeedbackActivity.class);
                        startActivity(intent3);
                        break;
                     case R.id.reserv3:
                         Intent intent4 = new Intent(MainActivity2.this, com.saf.Dan.accomodation.ViewData.class);
                         startActivity(intent4);
                      break;
                    case R.id.sert:
                        Intent intent74 = new Intent(MainActivity2.this, Search.class);
                        startActivity(intent74);
                        break;
                    case R.id.hist:
                        Intent intent7 = new Intent(MainActivity2.this, VisitingHistory.class);
                        startActivity(intent7);
                        break;
                   case R.id.support:
                       Intent intent5 = new Intent(MainActivity2.this, Whatsapp.class);
                       startActivity(intent5);
                        break;
                    case R.id.help1:
                        Intent intent51 = new Intent(MainActivity2.this, MyCustomAppIntro.class);
                        startActivity(intent51);
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                        builder.setTitle("Log out");
                        builder.setMessage("Are you sure you want to logout your acccount ?")
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //your deleting code
                                        //DeleteToken();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent_signout = new Intent(MainActivity2.this, LoginActivity.class);
                                        startActivity(intent_signout);
                                        //finish();
                                        dialog.dismiss();
                                    }

                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();


                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String msg = "";
        switch(item.getItemId())
        {
            case R.id.add_event_toolbar:
                //msg = "Add Event";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity2.this, Main4Activity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
                    }
                },SplashTimeout);
                break;
        }
        //Toast.makeText(this,msg+" is checked!",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    private void help() {
        new HDialog().show(getFragmentManager(), UIConsts.Fragments.ABOUT_DIALOG_TAG);

    }
    public String setDataIn() {

        do

            {
            firebaseUser = firebaseAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().contentEquals(uid)) {
                            fn = ds.child("firstName").getValue().toString();
                            ln = ds.child("lastName").getValue().toString();


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       // return fn + ln;
    }
        while (fn == null);
        return fn+ln;
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
                    Toast.makeText(MainActivity2.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}