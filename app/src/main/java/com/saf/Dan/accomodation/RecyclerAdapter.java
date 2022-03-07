package com.saf.Dan.accomodation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.saf.Dan.Status;
import com.saf.Dan._reservations;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String Tag="RecyclerView";
    private Context mContext;
    private ArrayList<ImageUploadInfo> uploads;

    public RecyclerAdapter(Context mContext, ArrayList<ImageUploadInfo> uploads) {
        this.mContext = mContext;
        this.uploads = uploads;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_data_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView1.setText(uploads.get(position).getImageName());
        holder.textView2.setText(uploads.get(position).gethouseType());
        holder.textView3.setText(uploads.get(position).getDesc());
        holder.status.setText(uploads.get(position).getStatus());
        Glide.with(mContext).load(uploads.get(position).getImageURL()).into(holder.imageView);

//        if(uploads.get(position).getStatus()=="yes"){
//
//            holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
//            holder.status.setTextColor(mContext.getResources().getColor(R.color.TextColor_Primary));
//        }
//        if(uploads.get(position).getStatus()=="no"){
//
//            holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.darkRed));
//            holder.status.setTextColor(mContext.getResources().getColor(R.color.TextColor_Primary));
//        }
//        if(uploads.get(position).getStatus()=="yes"){


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                Intent inte=new Intent(mContext, _reservations.class);
                inte.putExtra("name",uploads.get(position).getImageName());
                inte.putExtra("price",uploads.get(position).getPrice());
                inte.putExtra("desc",uploads.get(position).getDesc());
                inte.putExtra("image",uploads.get(position).getImageURL());
                inte.putExtra("status",uploads.get(position).getStatus());
                v.getContext().startActivity(inte);
            }
        });
//        else{
//            Toast.makeText(mContext, "Room Not Available, Try another one", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView1,textView2,textView3,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            textView1=itemView.findViewById(R.id.name);
            textView2=itemView.findViewById(R.id.type);
            textView3=itemView.findViewById(R.id.descin);
            status=itemView.findViewById(R.id.statu);
        }
    }

}
