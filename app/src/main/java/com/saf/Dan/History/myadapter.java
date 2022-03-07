package com.saf.Dan.History;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planner.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.saf.Dan.Status;


public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final model model) {
     holder.nametext.setText(model.getName());
        holder.datetext.setText(model.getDate());
        holder.noep.setText(model.getNopeople());
        holder.amount.setText(model.getAmount());
        holder.mpesa.setText(model.getMpesa());
        holder.img1.setVisibility(View.GONE);


         holder.card.setOnClickListener(new View.OnClickListener() {
           @Override
               public void onClick(View view) {

        Toast.makeText(view.getContext(),"unique rname:  "+model.getRname(),Toast.LENGTH_SHORT).show();

               Intent inte=new Intent(view.getContext(),Status.class);
               inte.putExtra("date",model.getDate());
               inte.putExtra("amount",model.getAmount());
               inte.putExtra("people",model.getNopeople());
               inte.putExtra("name",model.getName());
               inte.putExtra("key",model.getunique());

               inte.putExtra("rprice",model.getRprice());
               inte.putExtra("rname",model.getRname());

               view.getContext().startActivity(inte);



                }
               });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.historystatus,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView nametext,datetext,emailtext,mpesa,phone, noep,amount;
        CardView card;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            nametext=itemView.findViewById(R.id.nametext);
            datetext=itemView.findViewById(R.id.date);
           // emailtext=itemView.findViewById(R.id.emailtext);
            mpesa=itemView.findViewById(R.id.message);
           // phone=itemView.findViewById(R.id.phone4);
            noep=itemView.findViewById(R.id.noep1);
            card=itemView.findViewById(R.id.click);
            amount=itemView.findViewById(R.id.amount1);

        }
    }

}
