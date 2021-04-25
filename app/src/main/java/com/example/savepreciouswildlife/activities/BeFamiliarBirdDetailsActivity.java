package com.example.savepreciouswildlife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savepreciouswildlife.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * @author JunHyungKim
 * This Class is for representing detailed birds information on the new(Result) activity
 */
public class BeFamiliarBirdDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_befamiliarbird_details);

        final ImageView imgViewBirdD = findViewById(R.id.imgViewBirdD);

        TextView txtViewBirdNameD = findViewById(R.id.txtViewBirdNameD);
        TextView txtViewBirdProvinceD = findViewById(R.id.txtViewBirdProvinceD);
        TextView txtViewBirdCityD = findViewById(R.id.txtViewBirdCityD);
        TextView txtViewBirdEtcD = findViewById(R.id.txtViewBirdEtcD);

        String rcvBirdName = getIntent().getExtras().getString("NAME");
        String rcvBirdPROVINCE = getIntent().getExtras().getString("PROVINCE");
        String rcvBirdCITY = getIntent().getExtras().getString("CITY");
        String rcvBirdETC = getIntent().getExtras().getString("ETC");
        String rcvBirdURL = getIntent().getExtras().getString("URL");

        txtViewBirdNameD.setText(rcvBirdName);
        txtViewBirdProvinceD.setText(rcvBirdPROVINCE);
        txtViewBirdCityD.setText(rcvBirdCITY);
        txtViewBirdEtcD.setText(rcvBirdETC);

        /**
         * @author : JunHyung-Kim
         * This part can control FirebaseStorage to identify the address of an outer image and set this image into imageView.
         */
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://savewildlife-bd0f2.appspot.com/");

        StorageReference storageRef = storage.getReference();

        storageRef.child("birds/" + rcvBirdURL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.main_background)
                        .fit()
                        .centerCrop()
                        .into(imgViewBirdD);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BeFamiliarBirdDetailsActivity.this, "image load fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}