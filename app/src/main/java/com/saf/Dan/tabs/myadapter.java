package com.saf.Dan.tabs;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.saf.Dan.MainActivity2;
import com.saf.Dan.updateAnimals.ViewData;

public class myadapter extends FirebaseRecyclerAdapter<Model,myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final Model model) {
      holder.nametext.setText(model.getName());
      holder.datetext.setText(model.getDate());
      holder.emailtext.setText(model.getEmail());
      holder.phone.setText(model.getPhone());
      Glide.with(holder.img1.getContext()).load(model.getPurl()).into(holder.img1);

              holder.img1.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      AppCompatActivity activity=(AppCompatActivity)view.getContext();
                      activity.getSupportFragmentManager().beginTransaction()
                              .replace(R.id.wrapper1,new descfragment(model.getName(),model.getDate(),model
                              .getEmail(),model.getPhone(),model.getPurl(),model.getKey()))
                              .addToBackStack(null).commit();
//                      Intent intent2 = new Intent(view.getContext(), new descfragment(model.getName(),model.getDate(),model.getEmail(),model.getPurl(),model.getPhone()));
//                      activity.startActivity(intent2);
                  }
              });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.finance_singlerowdesign,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView nametext,datetext,emailtext,mpesa,phone;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
            datetext=itemView.findViewById(R.id.date);
            emailtext=itemView.findViewById(R.id.emailtext);
            mpesa=itemView.findViewById(R.id.message);
            phone=itemView.findViewById(R.id.phone4);
        }
    }

}
