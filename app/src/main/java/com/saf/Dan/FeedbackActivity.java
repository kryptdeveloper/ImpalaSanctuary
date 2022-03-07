package com.saf.Dan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.planner.R;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import com.vimalcvs.materialrating.util.RateDialogManager;

import java.util.Objects;

public class FeedbackActivity extends AppCompatActivity {
    /*
        This acitivty handles providing the feedback about the application to the developers.
    */
    EditText name, feedback;
    Button submitBtn;
    DatabaseReference reference;
    int rating=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);//  Objects.requireNonNull(getSupportActionBar()).setTitle("Give us feedback");
        name = (EditText) findViewById(R.id.Name);
        feedback = (EditText) findViewById(R.id.feedback);
        submitBtn = (Button) findViewById((R.id.submitBtn));

//        LinearLayout linearLayout = findViewById(R.id.rate_ok);
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ///Rate Dialog
//
//
//            }
//        });
      SmileRating smileRating = findViewById(R.id.smile_rating);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // Retrieve the value of the bar dinamically
                // level is from 1 to 5
                // Will return 0 if NONE selected
                rating = smileRating.getRating();

                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Toast.makeText(FeedbackActivity.this, "Bad", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GOOD:
                        Toast.makeText(FeedbackActivity.this, "Good, thanks ", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GREAT:
                        Toast.makeText(FeedbackActivity.this, "Impalla sanctuary Appreciates!! ", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(FeedbackActivity.this, "Okay ", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(FeedbackActivity.this, "Terrible tell us why below", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(FeedbackActivity.this, String.valueOf(rating), Toast.LENGTH_SHORT).show();


                final String Name=name.getText().toString();
                final String word=feedback.getText().toString();
                if(Name.length()==0)
                {
                    name.requestFocus();
                    name.setError("FIELD CANNOT BE EMPTY");
                    return;
                }
                else if(!Name.matches("[a-zA-Z ]+"))
                {
                    name.requestFocus();
                    name.setError("ENTER ONLY ALPHABETICAL CHARACTER");
                    return;
                }
                else if(word.length()==0)
                {
                    feedback.requestFocus();
                    feedback.setError("FIELD CANNOT BE EMPTY");
                }
                else
                {
                    Toast.makeText(FeedbackActivity.this,"Loading",Toast.LENGTH_LONG).show();
                    String userName = name.getText().toString();
                    String feedBackTxt = feedback.getText().toString();
                    //Get reference of the Real time database.
                    reference = FirebaseDatabase.getInstance().getReference().child("Feedback:" + userName);
                    //Push the values to the database.
                    reference.push().setValue(feedBackTxt);

                    name.setText("");
                    feedback.setText("");

                    // define Intent object
                    // with action attribute as ACTION_SEND
                    Intent intent = new Intent(Intent.ACTION_SEND);

                    // add three fiels to intent using putExtra function
                    String email="blistechnologies20@gmail.com";
                    intent.putExtra(Intent.EXTRA_EMAIL,
                            new String[] { email });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Impala Visitor FeedBack");
                    intent.putExtra(Intent.EXTRA_TEXT, "Rating given is:  " +String.valueOf(rating)+"\n\nTell us your feedback");

                    // set type of intent
                    intent.setType("message/rfc822");

                    // startActivity with intent with chooser
                    // as Email client using createChooser function
                    startActivity(
                            Intent
                                    .createChooser(intent,
                                            "Choose an Email client :"));

                    Toast.makeText(FeedbackActivity.this, "Thank you for providing the feedback.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
