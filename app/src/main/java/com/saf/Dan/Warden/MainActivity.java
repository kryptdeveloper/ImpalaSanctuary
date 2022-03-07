package com.saf.Dan.Warden;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.saf.Dan.LoginActivity;
import com.saf.Dan.MainActivity2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

  //  private me.Dan.warden.incoming incoming;
    private recfragment recfragment;
    private completed completed;
  //  private wadadd add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainwad);

        setSupportActionBar(toolbar);
        String value = getIntent().getStringExtra("phone");

        Bundle bundle = new Bundle();
        //String myMessage = "Stackoverflow is cool!";
        bundle.putString("phone", value );
        //FragmentClass fragInfo = new FragmentClass();
      //  fragInfo.setArguments(bundle);
       // transaction.replace(R.id.fragment_single, fragInfo);
        //transaction.commit();
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

    //    incoming = new incoming();
        recfragment = new recfragment();
        recfragment.setArguments(bundle);
        completed = new completed();
      //  add = new wadadd();
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
    ///    viewPagerAdapter.addFragment(incoming, "Incoming");
        viewPagerAdapter.addFragment(recfragment, "Assigned");
        viewPagerAdapter.addFragment(completed, "Completed");
   //     viewPagerAdapter.addFragment(add, "Add");
        viewPager.setAdapter(viewPagerAdapter);

        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_explore_24);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_pending);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_done);
      ///  tabLayout.getTabAt(3).setIcon(R.drawable.add_2);
        BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(17);

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log1:
               // startActivity(new Intent(MainActivity.this, wadadd.class));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Log out");
                builder.setMessage("Are you sure you want to logout your account ?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                //DeleteToken();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent_signout = new Intent(MainActivity.this, LoginActivity.class);
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
