package com.example.savepreciouswildlife.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.models.ReportedBird;
import com.example.savepreciouswildlife.models.ReportedBirdPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReportedBirdsAdapter extends RecyclerView.Adapter<ReportedBirdsAdapter.ReportedBirdsViewHolder> {
    private Context mContext;
    private List<ReportedBirdPhoto> reportedBirdPhotos;
    private List<ReportedBird> reportedBirds;

    public ReportedBirdsAdapter(Context context, List<ReportedBirdPhoto> reportedBirdPhotos, List<ReportedBird> reportedBirds) {
        mContext = context;
        this.reportedBirdPhotos = reportedBirdPhotos;
        this.reportedBirds = reportedBirds;
    }

    @Override
    public ReportedBirdsAdapter.ReportedBirdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.reported_birds_list, parent, false);
        return new ReportedBirdsAdapter.ReportedBirdsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReportedBirdsAdapter.ReportedBirdsViewHolder holder, int position) {
        try{
            boolean foundReportedBirdPhoto = false;

            ReportedBirdPhoto currentReportedBirdPhoto = reportedBirdPhotos.get(position);
            ReportedBird currentReportedBird = reportedBirds.get(position);

            for(int i = 0; i < reportedBirdPhotos.size(); i++){
                if(currentReportedBird.getId().equals(reportedBirdPhotos.get(i).getReportedBirdId())){
                    currentReportedBirdPhoto = reportedBirdPhotos.get(i);
                    foundReportedBirdPhoto = true;
                    break;
                }
            }

            if(foundReportedBirdPhoto){
                holder.textViewSpecies.setText("Species: " + currentReportedBird.getSpecies());
                holder.textViewInjury.setText("Injury: " + currentReportedBird.getInjury());
                holder.textViewPlace.setText("Reported at: " + currentReportedBird.getCity() + ", " + currentReportedBird.getProvince() );
                holder.textViewReportersEmail.setText("Reporter's Email: " + currentReportedBird.getReporterEmail());

                Picasso.get()
                        .load(currentReportedBirdPhoto.getPhotoUrl())
                        .placeholder(R.drawable.placeholder)
                        .fit()
                        .centerCrop()
                        .into(holder.imgViewReportedBird);
            }

        } catch (Exception ex){
        }

    }

    @Override
    public int getItemCount() {
        return reportedBirds.size();
    }

    public static class ReportedBirdsViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgViewReportedBird;
        public TextView textViewSpecies, textViewInjury, textViewPlace, textViewReportersEmail;

        public ReportedBirdsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewReportedBird = itemView.findViewById(R.id.imgViewReportedBird);
            textViewSpecies = itemView.findViewById(R.id.textViewSpecies);
            textViewInjury = itemView.findViewById(R.id.textViewInjury);
            textViewPlace = itemView.findViewById(R.id.textViewPlace);
            textViewReportersEmail = itemView.findViewById(R.id.textViewReportersEmail);
        }
    }

}
