package com.example.savepreciouswildlife.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.activities.BeFamiliarBirdDetailsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author JunHyungKim
 * this class is for insering values from beFamiliarFragment to the listview of the fragment.
 */
public class BeFamiliarAdapter extends BaseAdapter {

    Context context;
    List<String[]> birdData;
    ImageView imgViewBirdPhoto;


    public BeFamiliarAdapter(Context context, List<String[]> birdData) {
        this.context = context;
        this.birdData = birdData;
    }


    @Override
    public int getCount() {
        return birdData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_befamiliarbirdlist,viewGroup, false);
        }

        TextView txtViewBirdProvince = view.findViewById(R.id.txtViewBirdProvince);
        TextView txtViewBirdCity = view.findViewById(R.id.txtViewBirdCity);
        TextView txtViewBirdName = view.findViewById(R.id.txtViewBirdName);

        imgViewBirdPhoto = view.findViewById(R.id.imgViewBird);

        Button btnViewDetail = view.findViewById(R.id.btnViewDetail);

        txtViewBirdName.setTextColor(Color.WHITE);

        final String birdProvince = birdData.get(i)[1];
        final String birdCity = birdData.get(i)[2];
        final String birdName = birdData.get(i)[3];
        final String birdEtc = birdData.get(i)[4];
        final String birdUrl = birdData.get(i)[5];

        txtViewBirdProvince.setText(birdProvince);
        txtViewBirdCity.setText(birdCity);
        txtViewBirdName.setText(birdName);


        /**
         * @author JunHyung-Kim
         * This part can control FirebaseStorage to identify the address of an outer image and set this image into imageView.
         */
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://savewildlife-bd0f2.appspot.com/");

        StorageReference storageRef = storage.getReference();

        storageRef.child("birds/" + birdData.get(i)[5]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.main_background)
                        .fit()
                        .centerCrop()
                        .into(imgViewBirdPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "image load fail", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * @author JunHyung-Kim
         * This part is moving to secondActivity when user click the view detail button.
         */
        btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent birdResults = new Intent(context, BeFamiliarBirdDetailsActivity.class);

                Bundle birdBundle = new Bundle();

                birdBundle.putString("PROVINCE", birdProvince);
                birdBundle.putString("CITY", birdCity);
                birdBundle.putString("NAME", birdName);
                birdBundle.putString("ETC", birdEtc);
                birdBundle.putString("URL", birdUrl);

                birdResults.putExtras(birdBundle);

                context.startActivity(birdResults);
            }
        });

        return view;
    }
}
