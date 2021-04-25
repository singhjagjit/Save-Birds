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
import com.example.savepreciouswildlife.models.ReportedBird;
import com.example.savepreciouswildlife.models.ReportedBirdPhoto;
import com.example.savepreciouswildlife.adapters.ReportedBirdsAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jagjit Singh
 * Similar to OurVolunteersFragment
 */

public class ReportedBirdsFragment extends Fragment {
    private RecyclerView recyclerViewReportedBirds;
    private RecyclerView.Adapter reportedBirdsAdapter;
    private RecyclerView.LayoutManager reportedBirdsLayoutManager;

    private List<ReportedBird> reportedBirdsList;
    private List<ReportedBirdPhoto> reportedBirdPhotos;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_reported_birds, container, false);

        getActivity().setTitle("Reported Birds");

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        reportedBirdsList = dataBaseHelper.getAllReportedBirds();
        dataBaseHelper.closeDB();

        if(reportedBirdsList.size() > 0){
            recyclerViewReportedBirds = rootView.findViewById(R.id.recyclerViewReportedBirds);
            recyclerViewReportedBirds.setHasFixedSize(true);
            reportedBirdsLayoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerViewReportedBirds.setLayoutManager(reportedBirdsLayoutManager);

            reportedBirdPhotos = new ArrayList<>();

            firebaseStorage = FirebaseStorage.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("reported birds");

            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reportedBirdPhotos.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ReportedBirdPhoto reportedBirdPhoto = dataSnapshot.getValue(ReportedBirdPhoto.class);
                        reportedBirdPhotos.add(reportedBirdPhoto);
                    }
                    reportedBirdsAdapter = new ReportedBirdsAdapter(getActivity(), reportedBirdPhotos, reportedBirdsList);
                    recyclerViewReportedBirds.setAdapter(reportedBirdsAdapter);
                    reportedBirdsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else{
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setMessage("There are no birds reported yet!");
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
