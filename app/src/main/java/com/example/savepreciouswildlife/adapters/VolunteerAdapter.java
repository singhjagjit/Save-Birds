package com.example.savepreciouswildlife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.models.Volunteer;
import com.example.savepreciouswildlife.models.VolunteerPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder> {
    private Context mContext;
    private List<VolunteerPhoto> volunteerPhotos;
    private List<Volunteer> volunteers;

    public VolunteerAdapter(Context context, List<VolunteerPhoto> volunteerPhotos, List<Volunteer> volunteers) {
        mContext = context;
        this.volunteerPhotos = volunteerPhotos;
        this.volunteers = volunteers;
    }

    @Override
    public VolunteerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.volunteers_list, parent, false);
        return new VolunteerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VolunteerViewHolder holder, int position) {
        try{
            boolean foundVolunteerPhoto = false;

            VolunteerPhoto currentPhoto = volunteerPhotos.get(position);
            Volunteer currentVolunteer = volunteers.get(position);

            for(int i = 0; i < volunteerPhotos.size(); i++){
                if(currentVolunteer.getId().equals(volunteerPhotos.get(i).getVolunteerId())){
                    currentPhoto = volunteerPhotos.get(i);
                    foundVolunteerPhoto = true;
                    break;
                }
            }

            if(foundVolunteerPhoto){
                DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
                double avgRatings = dataBaseHelper.getAverageRatings(currentVolunteer.getId());
                dataBaseHelper.closeDB();

                holder.textViewVolunteerName.setText(currentVolunteer.getFirstName());

                if(avgRatings > 0){
                    holder.textViewVolunteerStars.setText(String.valueOf(avgRatings) + " Stars");
                } else{
                    holder.textViewVolunteerStars.setText("No Ratings");
                }

                Picasso.get()
                        .load(currentPhoto.getPhotoUrl())
                        .placeholder(R.drawable.placeholder)
                        .fit()
                        .centerCrop()
                        .into(holder.imageViewVolunteer);
            }

        } catch (Exception ex){
        }

    }

    @Override
    public int getItemCount() {
        return volunteers.size();
    }

    public static class VolunteerViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewVolunteer;
        public TextView textViewVolunteerName, textViewVolunteerStars;

        public VolunteerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewVolunteer = itemView.findViewById(R.id.imageViewVolunteer);
            textViewVolunteerName = itemView.findViewById(R.id.textViewVolunteerName);
            textViewVolunteerStars = itemView.findViewById(R.id.textViewVolunteerStars);
        }
    }

}
