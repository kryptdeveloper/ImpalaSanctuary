package com.saf.Dan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ModelHAdapter extends FirebaseRecyclerAdapter<
        person, ModelHAdapter.personsViewholder> {

    public ModelHAdapter(
            @NonNull FirebaseRecyclerOptions<person> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull personsViewholder holder,
                     int position, @NonNull person model)
    {

        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.date.setText(model.getdate());

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.codeh.setText(model.getMpesa());

        // Add age from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.nope.setText(model.getNopeople());
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public personsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_visiting_history, parent, false);
        return new personsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    static class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView date, codeh, nope;
        public personsViewholder(@NonNull View itemView)
        {
            super(itemView);

            date
                    = itemView.findViewById(R.id.dateh);
            codeh = itemView.findViewById(R.id.codeh);
            nope = itemView.findViewById(R.id.nope);
        }
    }
}