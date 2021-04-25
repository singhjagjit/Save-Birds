package com.example.savepreciouswildlife.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.interfaces.OnItemClickListener;
import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.activities.DonateSumActivity;
import com.example.savepreciouswildlife.adapters.PatientAdapter;
import com.example.savepreciouswildlife.models.Patient;

import java.util.List;

// @author Kateryna.
// The fragment for DONATE page. Used recycler view, customer adapter and onClick listener to go further to donate option
// bundle will go to the next DonateSum Activity to pass information about the donation so we can use database helper class later
// and write new values into database depending on donation sum
// DB Helper class is using here to browse all patients from the database
public class DonateFragment extends Fragment {

    private RecyclerView recyclerViewPatients;
    private PatientAdapter patientsAdapter;
    private RecyclerView.LayoutManager patientsLayoutManager;
    DataBaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_donate, container, false);
        getActivity().setTitle("Donate");
        recyclerViewPatients = rootView.findViewById(R.id.recyclerViewPatients);
        patientsLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerViewPatients.setLayoutManager(patientsLayoutManager);
        db = new DataBaseHelper(getContext());
        final List<Patient> patientsList = db.getAllPatients();
        db.closeDB();
        patientsAdapter = new PatientAdapter(patientsList);
        recyclerViewPatients.setAdapter(patientsAdapter);
        patientsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                patientsList.get(position);
                patientsAdapter.notifyItemChanged(position);
                //Toast.makeText(getActivity(),"Clicked on item" + position,Toast.LENGTH_LONG).show();
                int patientID = position + 1;
                int sumNeedOld = patientsList.get(position).getSumNeed();
                int sumDonatedOld = patientsList.get(position).getSumDonated();
                int sumLeftOld = patientsList.get(position).getSumLeft();
                if(sumLeftOld == 0) {
                    Toast.makeText(getActivity(),"Please, consider another bird for donation!",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), DonateSumActivity.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putInt("ID_PATIENT", patientID);
                    myBundle.putInt("SUMNEED_PATIENT", sumNeedOld);
                    myBundle.putInt("SUMDONATED_PATIENT", sumDonatedOld);
                    myBundle.putInt("SUMLEFT_PATIENT", sumLeftOld);
                    intent.putExtras(myBundle);
                    startActivity(intent);
                }

            }
        });

        return rootView;
    }
}
