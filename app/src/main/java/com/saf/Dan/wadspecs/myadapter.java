package com.saf.Dan.wadspecs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final model model) {
      holder.nametext.setText(model.getName());
      //holder.datetext.setText(model.getDate());
      holder.emailtext.setText(model.getEmail());
      holder.phone.setText(model.getPhone());
      //holder.mpesa.setText(model.getMpesa());


      //Glide.with(holder.img1.getContext()).load(model.getPurl()).into(holder.img1);

              holder.nametext.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Toast.makeText(view.getContext(), model.getName(), Toast.LENGTH_SHORT).show();
                      AppCompatActivity activity=(AppCompatActivity)view.getContext();
                      activity.getSupportFragmentManager().beginTransaction()
                              .replace(R.id.wrapper,new descfragment(model.getName(),model.getEmail(),model.getPhone()))
                              .addToBackStack(null).commit();
                  }
              });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wadspecs,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView nametext,datetext,emailtext,mpesa,phone;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

          //  img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
           // datetext=itemView.findViewById(R.id.date);
            emailtext=itemView.findViewById(R.id.emailtext);
           // mpesa=itemView.findViewById(R.id.message);
            phone=itemView.findViewById(R.id.phone4);
        }
    }

}
