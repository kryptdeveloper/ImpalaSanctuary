package com.saf.planner.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.saf.planner.ModelClass.User;
import com.example.planner.R;
import java.util.ArrayList;

public class InviteUserListAdapter extends RecyclerView.Adapter<InviteUserListAdapter.ItemViewHolder>
{
    Context context;
    ArrayList<User> userArrayList;
    User user;
    private View.OnClickListener itemClickListner;
    InviteUser inviteUser;

    public InviteUserListAdapter(InviteUser inviteUser, ArrayList<User> userArrayList, Context context) {
        this.inviteUser = inviteUser;
        this.userArrayList = userArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_user_list_item,parent,false);
        return new InviteUserListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position)
    {
        user = userArrayList.get(position);
        holder.userNameText.setText(user.getFirstName()+" "+user.getLastName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,userArrayList.get(position).getFirstName()+" "+userArrayList.get(position).getLastName()+" Clicked!",Toast.LENGTH_LONG).show();
                holder.CheckImage.setVisibility(View.VISIBLE);
                holder.ClearButton.setVisibility(View.VISIBLE);
                inviteUser.AddUser(userArrayList.get(position),true,position);
            }
        });
        holder.ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.CheckImage.setVisibility(View.GONE);
                holder.ClearButton.setVisibility(View.GONE);
                inviteUser.AddUser(userArrayList.get(position),false,position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return userArrayList.size();
    }

    public void setOnClickListner(View.OnClickListener clickListner)
    {
        itemClickListner = clickListner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameText;
        Button ClearButton;
        ImageView CheckImage;

        public ItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userNameText = itemView.findViewById(R.id.name_userListItem);
            ClearButton = itemView.findViewById(R.id.clear_btn_userListItem);
            CheckImage = itemView.findViewById(R.id.checkImage_userListItem);
            itemView.setOnClickListener(itemClickListner);
        }
    }

    public interface InviteUser
    {
        void AddUser(User user,boolean status,int position);
    }

}
