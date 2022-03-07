package com.saf.Dan;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.planner.R;
import com.saf.planner.home_frag;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class FirstFragment extends Fragment {


    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    public static int count=0;
    int[] drawablearray=new int[]{R.drawable.test1,R.drawable.test2,R.drawable.test3,R.drawable.test4,R.drawable.test5};
    Timer _t;


    private OnFragmentInteractionListener listener;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout. fragment_first, container, false);
        LinearLayout lnMain = view.findViewById(R.id.ln);
        _t = new Timer();
        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                requireActivity().runOnUiThread(new Runnable() // run on ui thread
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




        ImageView map = (ImageView) view.findViewById(R.id.map);
        map.setClickable(true);
        map.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        LinearLayout lnMain=view.findViewById(R.id.ln2);
//                        lnMain.setVisibility(View.INVISIBLE);
//                        TextView tt = view.findViewById(R.id.tt);
//                        tt.setVisibility(View.INVISIBLE);
                        FourthFragment nextFrag = new FourthFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.ln3, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                });
        ImageView event = (ImageView) view.findViewById(R.id.events);
        event.setClickable(true);
        event.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        LinearLayout lnMain=view.findViewById(R.id.ln2);
//                        lnMain.setVisibility(View.INVISIBLE);
//                        TextView tt = view.findViewById(R.id.tt);
//                        tt.setVisibility(View.INVISIBLE);
                        home_frag nextFrag = new home_frag();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.ln3, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                });
        ImageView about = (ImageView) view.findViewById(R.id.about);
        about.setClickable(true);
        about.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HDialog().show(getActivity().getFragmentManager(), UIConsts.Fragments.ABOUT_DIALOG_TAG);
                    }
                });

        return view;

    }



    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


}
