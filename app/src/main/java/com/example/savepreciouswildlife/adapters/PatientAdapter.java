package com.example.savepreciouswildlife.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savepreciouswildlife.interfaces.OnItemClickListener;
import com.example.savepreciouswildlife.models.Patient;
import com.example.savepreciouswildlife.R;

import java.util.List;

// @author Kateryna
//created custom adapter
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<Patient> patientList;
    private OnItemClickListener mListener;

     public void setOnItemClickListener(OnItemClickListener listener) {
         mListener = listener;
     }

    public PatientAdapter(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewPatient;
        public TextView txtName;
        public TextView txtBreed;
        public TextView txtInjure;
        public TextView txtSumNeed;
        public TextView txtSumDonated;
        public TextView txtSumLeft;
        public Button buttonDonate;

        public PatientViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageViewPatient = itemView.findViewById(R.id.imageViewPatient);
            txtName = itemView.findViewById(R.id.txtViewName);
            txtBreed = itemView.findViewById(R.id.txtViewBreed);
            txtInjure = itemView.findViewById(R.id.txtViewInjureType);
            txtSumNeed = itemView.findViewById(R.id.txtViewSum1);
            txtSumDonated = itemView.findViewById(R.id.txtViewSum2);
            txtSumLeft = itemView.findViewById(R.id.txtViewSum3);
            buttonDonate = itemView.findViewById((R.id.buttonDonate));

             // this part of code works if we want to set on click listener not on the button but on the entire item (Kateryna)
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if(listener != null) {
                      int position = getAdapterPosition();
                      if(position != RecyclerView.NO_POSITION) {
                          listener.onItemClick(position);
                      }
                  }
                }
            });*/

            buttonDonate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }

                }
            });

        }

    }

    @NonNull
    @Override
    public PatientAdapter.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_list, parent, false);
        PatientViewHolder patientViewHolder = new PatientViewHolder(view,mListener);
        return patientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.PatientViewHolder holder, int position) {
        Patient currentPatient = patientList.get(position);
        holder.imageViewPatient.setImageResource(currentPatient.getPicPatient());
        holder.txtName.setText(currentPatient.getBirdName());
        holder.txtBreed.setText(currentPatient.getBirdBreed());
        holder.txtInjure.setText(currentPatient.getBirdInjure());
        holder.txtSumNeed.setText(String.valueOf(currentPatient.getSumNeed()));
        holder.txtSumDonated.setText(String.valueOf(currentPatient.getSumDonated()));
        holder.txtSumLeft.setText(String.valueOf(currentPatient.getSumLeft()));

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
}
