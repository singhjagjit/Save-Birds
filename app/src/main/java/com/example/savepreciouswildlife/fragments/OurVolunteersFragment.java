package com.example.savepreciouswildlife.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.MainActivity;
import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.RecyclerItemClickListener;
import com.example.savepreciouswildlife.models.Volunteer;
import com.example.savepreciouswildlife.adapters.VolunteerAdapter;
import com.example.savepreciouswildlife.activities.VolunteerDetailsActivity;
import com.example.savepreciouswildlife.models.VolunteerPhoto;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class OurVolunteersFragment extends Fragment {
    private RecyclerView recyclerViewVolunteers;
    private RecyclerView.Adapter volunteersAdapter;
    private RecyclerView.LayoutManager volunteersLayoutManager;

    private List<Volunteer> volunteersList;
    private List<VolunteerPhoto> volunteerPhotos;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_our_volunteers, container, false);

        getActivity().setTitle("Volunteers");

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        volunteersList = dataBaseHelper.getAllVolunteers();
        dataBaseHelper.closeDB();

        /**
         * @author Jagjit Singh
         * if condition to check if there are volunteers in the storage
         * if there are no volunteer, it will show a dialog box that 'there are no volunteers'
         * otherwise it will show all the volunteers
         */
        if(volunteersList.size() > 0){
            /**
             * @author Jagjit Singh
             * using Firebase get images
             * and RecyclerView to show the volunteer as well as their images
             */

            recyclerViewVolunteers = rootView.findViewById(R.id.recyclerViewVolunteers);
            recyclerViewVolunteers.setHasFixedSize(true);
            volunteersLayoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerViewVolunteers.setLayoutManager(volunteersLayoutManager);

            volunteerPhotos = new ArrayList<>();

            firebaseStorage = FirebaseStorage.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("volunteers");

            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    volunteerPhotos.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        VolunteerPhoto volunteerPhoto = dataSnapshot.getValue(VolunteerPhoto.class);
                        volunteerPhotos.add(volunteerPhoto);
                    }
                    volunteersAdapter = new VolunteerAdapter(getActivity(), volunteerPhotos, volunteersList);
                    recyclerViewVolunteers.setAdapter(volunteersAdapter);
                    volunteersAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            /**
             * @author Jagjit Singh
             * clickListener on each item (Volunteers) in RecyclerView
             */
            recyclerViewVolunteers.addOnItemTouchListener(
                    new RecyclerItemClickListener(rootView.getContext(), recyclerViewVolunteers ,new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {

                            try{
                                /**
                                 * @author Jagjit Singh
                                 * Here we are trying to find the photo of the volunteer clicked
                                 * if the photo is found, then its url will be passed in a bundle to the next activity (VolunteerDetailsActivity)
                                 * other volunteer data will be passed too
                                 */

                                boolean foundVolunteerPhoto = false;

                                String imageUrl = "";

                                for(int i = 0; i < volunteerPhotos.size(); i++){
                                    if(volunteersList.get(position).getId().equals(volunteerPhotos.get(i).getVolunteerId())){
                                        imageUrl = volunteerPhotos.get(i).getPhotoUrl();
                                        foundVolunteerPhoto = true;
                                        break;
                                    }
                                }

                                if(foundVolunteerPhoto){
                                    double avgRatings = dataBaseHelper.getAverageRatings(volunteersList.get(position).getId());

                                    Intent myIntent = new Intent(rootView.getContext(), VolunteerDetailsActivity.class);

                                    Bundle myBundle = new Bundle();
                                    myBundle.putString("VOLUNTEER_ID", volunteersList.get(position).getId());
                                    myBundle.putString("FIRST_NAME", volunteersList.get(position).getFirstName());
                                    myBundle.putString("LAST_NAME", volunteersList.get(position).getLastName());
                                    myBundle.putString("EMAIL", volunteersList.get(position).getEmail());
                                    myBundle.putString("EXPERIENCE", volunteersList.get(position).getExperience());
                                    myBundle.putString("IMAGEURL", imageUrl);
                                    myBundle.putDouble("AVG_RATINGS", avgRatings);

                                    myIntent.putExtras(myBundle);

                                    startActivity(myIntent);
                                }

                            } catch (Exception ex){
                            }

                        }

                        @Override public void onLongItemClick(View view, int position) {

                        }
                    })
            );
        } else {
            /**
             * @author Jagjit Singh
             * Dialog Box, if no volunteer are in storage
             */
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setMessage("There are no volunteer yet!");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent myIntent = new Intent(rootView.getContext(), MainActivity.class);
                    startActivity(myIntent);
                }
            });
            builder.show();
        }

        return rootView;
    }
}
