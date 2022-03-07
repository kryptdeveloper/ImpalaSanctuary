package com.saf.Dan;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.planner.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class homeActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    public static int count=0;
    int[] drawablearray=new int[]{R.drawable.butterfly,R.drawable.cheeter,R.drawable.girrafe,R.drawable.hippo,R.drawable.sjackal};
    Timer _t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout lnMain = (LinearLayout) findViewById(R.id.ln);
        _t = new Timer();
        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() // run on ui thread
                {
                    public void run() {
                        if (count < drawablearray.length) {

                            lnMain.setBackgroundResource((drawablearray[count]));
                            count = (count + 1) % drawablearray.length;
                        }
                    }
                });
            }
        }, 2000, 5000);

       // models = new ArrayList<>();
       // models.add(new Model(R.drawable.lions, "lions", "lions"));
       // models.add(new Model(R.drawable.dfly, "flies", "flies"));
       // models.add(new Model(R.drawable.gparrot, "parrots", "parrots."));
       // models.add(new Model(R.drawable.olivebabboon, "babboon", "babboons"));
     //   adapter = new Adapter(models, this);
       // viewPager = findViewById(R.id.viewPager);
       // viewPager.setAdapter(adapter);



    }
}