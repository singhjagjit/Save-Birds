package com.example.savepreciouswildlife.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.models.Rating;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Jagjit Singh
 * This fragment will open in the VolunteerDetails activity when user clicks Add Rating Button
 */
public class RateVolunteerFragment extends Fragment {
    private Button btnSubmitRatings;
    private Slider sliderRatings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_rate_volunteer, container, false);


        /**
         * @author Jagjit Singh
         * getting the current logged in user
         */
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        btnSubmitRatings = rootView.findViewById(R.id.btnSubmitRatings);
        sliderRatings = rootView.findViewById(R.id.sliderRatings);


        /**
         * @author Jagjit Singh
         * button to submit ratings
         */
        btnSubmitRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ratings = 1;
                try{
                    ratings = (int) sliderRatings.getValue();
                } catch (Exception ex){
                }

                String volunteerId = getActivity().getIntent().getExtras().getString("VOLUNTEER_ID");
                String userEmail = firebaseUser.getEmail();

                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());

                /**
                 * @author Jagjit Singh
                 * using a method in DataBaseHelper class to check if the user has already submitted the review for a volunteer
                 */
                boolean reviewAlreadyExists = dataBaseHelper.checkIfReviewAlreadySubmitted(volunteerId, userEmail);


                /**
                 * @author Jagjit Singh
                 * if review already submitted, a Snackbar will show
                 * otherwise review will be submitted
                 */
                if(reviewAlreadyExists){
                    Snackbar snackbar = Snackbar.make(rootView, "You have already added the review!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                } else{
                    /**
                     * @author Jagjit Singh
                     * storing the user rating in database
                     */
                    Rating rating = new Rating(volunteerId, userEmail, ratings);
                    dataBaseHelper.addRating(rating);
                    dataBaseHelper.closeDB();
                    Snackbar snackbar = Snackbar.make(rootView, "Review added successfully!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("OKAY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }

            }
        });

        return rootView;
    }
}
