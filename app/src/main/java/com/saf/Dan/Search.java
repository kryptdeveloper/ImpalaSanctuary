package com.saf.Dan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.saf.Dan.History.VisitingHistory;
import com.saf.Dan.updateAnimals.ViewData;
import com.saf.planner.home_frag;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
ListView list;
    String names[]={"Help","Animals", "Bookings","Reservations","Activities"
                       ,"History","Logout","FeedBack","Support","About"};
    String desc[]={"go to help module","go to see animals", "go to bookings","Find Reservations"
                    ,"Go through Activities","Find history and print receipt faster","Logout of the app"
                      ,"See feedback and message us!!","Support module and chat with our agent","See and read abot the app and us as general"};
    List<itemsModel> listitem=new ArrayList<>();

    customAdapter cadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        list=findViewById(R.id.gan);

   for(int i=0;i<names.length;i++){
       itemsModel items=new itemsModel(names[i],desc[i]);
       listitem.add(items);
   }
 cadapter=new customAdapter(listitem, this);
   list.setAdapter(cadapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem menuItem =menu.findItem(R.id.searchv) ;
        SearchView sear= (SearchView) menuItem.getActionView();

        sear.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cadapter.getFilter().filter(newText);
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.searchv){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class customAdapter extends BaseAdapter implements Filterable {
private List<itemsModel> list;
private List<itemsModel> filtered;
private Context context;

        public customAdapter(List<itemsModel> list, Context context) {
            this.list = list;
            this.filtered=list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return filtered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           View view =getLayoutInflater().inflate(R.layout.itemsearch, null);
            TextView t1,tdesc;
            t1=view.findViewById(R.id.t1);
            tdesc=view.findViewById(R.id.tdesc);
            t1.setText(filtered.get(position).getName());
            tdesc.setText(filtered.get(position).getDesc());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(filtered.get(position).getName()=="Help"){
                        Toast.makeText(context, "help", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, MyCustomAppIntro.class));
                    }
                    else if(filtered.get(position).getName()=="Animals"){
                        Toast.makeText(context, "animals", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, ViewData.class));
                    }
                    else if(filtered.get(position).getName()=="Activities"){
                        Toast.makeText(context, "Activities", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, com.saf.Dan.activities.ViewData.class));
                    }
                    else if(filtered.get(position).getName()=="Bookings"){
                        Toast.makeText(context, "Bookings", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, book.class));
                    }
                    else if(filtered.get(position).getName()=="Reservations"){
                        Toast.makeText(context, "Reservations", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, com.saf.Dan.accomodation.ViewData.class));
                    }
//                    else if(filtered.get(position).getName()=="Events"){
//                        Toast.makeText(context, "events", Toast.LENGTH_SHORT).show();
//                        Fragment fragment;
//                        fragment = new home_frag();
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.take, fragment).addToBackStack(null).commit();
//
//                    }
//                    else if(filtered.get(position).getName()=="Maps"){
//                        Toast.makeText(context, "maps", Toast.LENGTH_SHORT).show();
//                        Fragment fragment;
//                        fragment = new FourthFragment();
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.take, fragment).addToBackStack(null).commit();
//
//                    }
                    else if(filtered.get(position).getName()=="History"){
                        Toast.makeText(context, "history", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, VisitingHistory.class));
                    }
                    else if(filtered.get(position).getName()=="FeedBack"){
                        Toast.makeText(context, "feedback", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, FeedbackActivity.class));
                    }
                    else if(filtered.get(position).getName()=="Support"){
                        Toast.makeText(context, "support", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Search.this, Whatsapp.class));
                    }
                    else if(filtered.get(position).getName()=="Logout"){
                        Toast.makeText(context, "logout", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                        builder.setTitle("Log out");
                        builder.setMessage("Are you sure you want to logout your account ?")
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //your deleting code
                                        //DeleteToken();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent_signout = new Intent(Search.this, LoginActivity.class);
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
                    }
                    else if(filtered.get(position).getName()=="About"){
                        Toast.makeText(context, "about", Toast.LENGTH_SHORT).show();
                        new HDialog().show(Search.this.getFragmentManager(), UIConsts.Fragments.ABOUT_DIALOG_TAG);

                    }
                }
            });
            return view;
        }

        @Override
        public Filter getFilter() {

            Filter fil=new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults =new FilterResults();

                    if(constraint==null|| constraint.length()==0){
                        filterResults.count=list.size();
                        filterResults.values=list;
                    }
                    else{

                        String searchStr=constraint.toString().toLowerCase();

                        List<itemsModel> resultData= new ArrayList<>();
                        for(itemsModel itemsModel: list){
                            if(itemsModel.getName().contains(searchStr) || itemsModel.getDesc().contains(searchStr)){
                                resultData.add(itemsModel);

                            }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                      filtered= (List<itemsModel>) results.values;
                      notifyDataSetChanged();
                }
            };
            return fil;
        }
    }
}