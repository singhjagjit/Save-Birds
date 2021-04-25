package com.example.savepreciouswildlife.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.fragments.RateVolunteerFragment;
import com.squareup.picasso.Picasso;


/**
 * @author Jagjit Singh
 * This activity will open when user clicks on a Volunteer in OurVolunteersFragments.
 * It will show details of that volunteer and user can Rate the volunteer.
 */
public class VolunteerDetailsActivity extends AppCompatActivity {
    TextView textViewVolunteerName, textViewVolunteerStars, textViewVolunteerEmail, textViewVolunteerExperience;
    Button btnVolunteerAddRating;
    ImageView imageViewVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_details);

        textViewVolunteerName = findViewById(R.id.textViewVolunteerName);
        textViewVolunteerStars = findViewById(R.id.textViewVolunteerStars);
        textViewVolunteerEmail = findViewById(R.id.textViewVolunteerEmail);
        textViewVolunteerExperience = findViewById(R.id.textViewVolunteerExperience);
        btnVolunteerAddRating = findViewById(R.id.btnVolunteerAddRating);
        imageViewVolunteer = findViewById(R.id.imageViewVolunteer);

        String firstName = "";
        String lastName = "";
        String email = "";
        String experience = "";
        String imageUrl = "";
        double avgRatings = 0;


        try{
            firstName = getIntent().getExtras().getString("FIRST_NAME");
            lastName = getIntent().getExtras().getString("LAST_NAME");
            email = getIntent().getExtras().getString("EMAIL");
            experience = getIntent().getExtras().getString("EXPERIENCE");
            imageUrl = getIntent().getExtras().getString("IMAGEURL");
            avgRatings = getIntent().getExtras().getDouble("AVG_RATINGS");

            textViewVolunteerName.setText(firstName + " " + lastName);

            if(avgRatings > 0){
                textViewVolunteerStars.setText(String.valueOf("Ratings: " + avgRatings) + " Stars");
            } else {
                textViewVolunteerStars.setText("No Ratings");
            }

            textViewVolunteerEmail.setText("Email: " + email);

            if(experience.isEmpty()){
                textViewVolunteerExperience.setText("Volunteer does not have any experience");
            } else{
                textViewVolunteerExperience.setText("\"" + experience + "\" - " + firstName);
            }

            /**
             * @author Jagjit Singh
             * Using Picasso library to get the image and show it
             */
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .centerCrop()
                    .into(imageViewVolunteer);
        } catch (Exception ex){
        }

        btnVolunteerAddRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVolunteerAddRating.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rating, new RateVolunteerFragment()).commit();
            }
        });

    }
}