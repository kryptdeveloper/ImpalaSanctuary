package com.saf.Dan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.saf.Dan.activities.UploadData;



public class Attendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Fragment fragment;
        fragment = new EventsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.jkl, fragment).commit();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addvent:
              //  startActivity(new Intent(this, About.class));
                Fragment fragment;
                fragment = new check();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.jkl, fragment).addToBackStack(null).commit();
                return true;
            case R.id.activitiesl:
                //  startActivity(new Intent(this, About.class));
                Intent intent2 = new Intent(Attendance.this, UploadData.class);
                startActivity(intent2);
                return true;
            case R.id.log:
                AlertDialog.Builder builder = new AlertDialog.Builder(Attendance.this);
                builder.setTitle("Log out");
                builder.setMessage("Are you sure you want to logout your EventManager account ?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                //DeleteToken();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent_signout = new Intent(Attendance.this, LoginActivity.class);
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


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
