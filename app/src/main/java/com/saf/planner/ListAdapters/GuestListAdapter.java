package com.saf.planner.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saf.planner.ModelClass.GuestData;
import com.example.planner.R;

import java.util.ArrayList;

public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.ItemViewHolder> {

    ArrayList<GuestData> dataArrayList;
    Context context;

    public GuestListAdapter(ArrayList<GuestData> guestDataArrayList, Context context) {
        dataArrayList = guestDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_guest_list_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.userName.setText(dataArrayList.get(position).getGuestName());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.guestName_listItem);
        }
    }
}
