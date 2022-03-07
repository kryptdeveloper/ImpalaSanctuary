package com.saf.planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main3Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    public DrawerLayout HomeDrawerLayout;
    public NavigationView HomeNavigationView;
    public NavController navController;
    public TextView UserName,EmailID,PhoneNo;
    public CircleImageView profileImage;
    private String fn,ln,emailID,pn,profilePhotoUrl;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    Uri imageuri;
    private static int SplashTimeout = 10;
    String TokenID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity_main3);
        toolbar = findViewById(R.id.home_toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        setUpNavDrawer();
        GetToakenID();
    }


    public void setUpNavDrawer()
    {
        setDataInNavDrawer();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HomeDrawerLayout = findViewById(R.id.nav_drawer);
        HomeNavigationView = findViewById(R.id.home_navigationview);

        UserName = HomeNavigationView.getHeaderView(0).findViewById(R.id.user_name_nav_drawer);
        EmailID = HomeNavigationView.getHeaderView(0).findViewById(R.id.user_email_nav_drawer);
        PhoneNo = HomeNavigationView.getHeaderView(0).findViewById(R.id.user_phone_nav_drawer);
        profileImage = HomeNavigationView.getHeaderView(0).findViewById(R.id.C_user_profile_photo);

        navController = Navigation.findNavController(this,R.id.nav_home_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,HomeDrawerLayout);
        NavigationUI.setupWithNavController(HomeNavigationView,navController);

        HomeNavigationView.setNavigationItemSelectedListener(this);
    }


    public void setDataInNavDrawer() {
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
                            UserName.setText(fn + " " + ln);
                            EmailID.setText(emailID);
                            PhoneNo.setText(pn);

                            profilePhotoUrl = ds.child("profileURL").getValue().toString();
                            Picasso.get().load(profilePhotoUrl).into(profileImage);

                           /* Log.e("FirstName:",ds.child("firstName").getValue().toString());
                            Log.e("LastName:",ds.child("lastName").getValue().toString());
                            Log.e("EmailID:",ds.child("emailID").getValue().toString());
                            Log.e("Phone:",ds.child("phoneNo").getValue().toString());
                            Log.e("profileURL:",ds.child("profileURL").getValue().toString());*/


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
                    Toast.makeText(Main3Activity.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_home_host_fragment),HomeDrawerLayout) || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        if (HomeDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            HomeDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        HomeDrawerLayout.closeDrawers();
        int SelectedItem = item.getItemId();
        switch (SelectedItem)
        {
            case R.id.home_nav_drawer:
                Toast.makeText(this,"home",Toast.LENGTH_LONG).show();
                break;
            case R.id.edit_profile_nav_drawer:
                //Toast.makeText(this,"edit Profile",Toast.LENGTH_LONG).show();
                navController.navigate(R.id.action_home_frag_to_edit_profile_frag);
                break;
            case R.id.notification_nav_draw:
                Intent i = new Intent(this,NotificationActivity.class);
                startActivity(i);
                finish();
                //Toast.makeText(this,"Notification",Toast.LENGTH_LONG).show();
                break;
            case R.id.about_us_nav_draw:
                navController.navigate(R.id.action_home_frag_to_about_us_frag);
                //Toast.makeText(this,"About us",Toast.LENGTH_LONG).show();
                break;
           /* case R.id.delete_account_nav_draw:
                deleteAccount();
                //Toast.makeText(this,"Delete Account",Toast.LENGTH_LONG).show();
                break;*/
            case R.id.log_out_nav_draw:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Log out");
                builder.setMessage("Are you sure you want to logout your acccount ?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                //DeleteToken();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent_signout = new Intent(Main3Activity.this, Main2Activity.class);
                                startActivity(intent_signout);
                                finish();
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();


                break;
        }
        return false;
    }

    private void deleteAccount() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");
        final String uid = firebaseUser.getUid();
        final String email = firebaseUser.getEmail();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference profilepicRef = storageReference.child("/ProfilePictures/"+email);
        final StorageReference eventPicRef = storageReference.child("/Events/"+uid);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete acccount ? Once You delete your account, you will not be able to access your data again.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        //dialog.dismiss();
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    databaseReference.child(uid).removeValue();
                                    Intent i = new Intent(Main3Activity.this,Main2Activity.class);
                                    startActivity(i);
                                    finish();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Error:"+task.getException(),Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

}
