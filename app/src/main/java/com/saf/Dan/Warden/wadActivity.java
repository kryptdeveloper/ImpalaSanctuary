package com.saf.Dan.Warden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.planner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saf.Dan.EventsFragment;
import com.saf.Dan.FirstFragment;
import com.saf.Dan.LoginActivity;
import com.saf.Dan.updateAnimals.MainActivity;
import com.saf.Dan.updateAnimals.UploadData;
import com.saf.Dan.updateAnimals.ViewData;

public class wadActivity extends AppCompatActivity {

    Button uploadButton;
    String phone1;
    Button viewButton;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wad);
        phone1 = getIntent().getStringExtra("phone");
        Toast.makeText(this, phone1, Toast.LENGTH_SHORT).show();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(itemSelectectedListener);
    }
    private void loadFragment(Fragment fragment) {
         FragmentManager fragmentManager = getSupportFragmentManager();
         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
         fragmentTransaction.replace(R.id.wad11, fragment).commit();
         //drawerLayout.closeDrawer(GravityCompat.START);
         fragmentTransaction.addToBackStack(null);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()){

                case R.id.asin:
//                    fragment = new recfragment();
//                    Toast.makeText(wadActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                    loadFragment(fragment);
                    Intent MainIntent = new Intent(wadActivity.this,recActivity.class);
                    MainIntent.putExtra("phone",phone1);
                    startActivity(MainIntent);
                    //startActivity(new Intent(wadActivity.this,recActivity.class));

                    break;
                case R.id.comp:
//                    fragment = new completed();
//                    loadFragment(fragment);
//                    Toast.makeText(wadActivity.this, "clickednnnnnn", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(wadActivity.this,compActivity.class));

                    break;
            }

            return true;
        }
    };
}