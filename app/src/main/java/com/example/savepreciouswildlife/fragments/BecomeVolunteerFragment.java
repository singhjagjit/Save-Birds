package com.example.savepreciouswildlife.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.MainActivity;
import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.models.Volunteer;
import com.example.savepreciouswildlife.models.VolunteerPhoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class BecomeVolunteerFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btnChoosePhoto, btnSubmit;
    private ImageView imgViewVolunteer;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextDescription;
    private RadioGroup radioGroupExperience;
    private ProgressBar progressBarUploadImage;

    private Uri imageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_become_volunteer, container, false);

        getActivity().setTitle("Become a Volunteer");

        btnChoosePhoto = rootView.findViewById(R.id.btnChoosePhoto);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        imgViewVolunteer = rootView.findViewById(R.id.imgViewVolunteer);
        editTextFirstName = rootView.findViewById(R.id.editTextFirstName);
        editTextLastName = rootView.findViewById(R.id.editTextLastName);
        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        editTextDescription = rootView.findViewById(R.id.editTextDescription);
        progressBarUploadImage = rootView.findViewById(R.id.progressBarImageUpload);
        radioGroupExperience = rootView.findViewById(R.id.radioGroupExperience);

        storageReference = FirebaseStorage.getInstance().getReference("volunteers");
        databaseReference = FirebaseDatabase.getInstance().getReference("volunteers");


        /**
         * @author Jagjit Singh
         * button for choosing picture from phone storage
         */
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        /**
         * @author Jagjit Singh
         * button for uploading the image to firebase and storing volunteer in local storage (SQLite)
         */
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else{
                    uploadFile(view);
                }
            }
        });

        return rootView;
    }

    /**
     * @author Jagjit Singh
     * method to choose photo from local storage
     */
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    /**
     * @author Jagjit Singh
     * we will get image selected from gallery in this method
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imgViewVolunteer);
        }
    }

    /**
     * @author Jagjit Singh
     * method to get the image extension (.jpg, .png, etc.)
     */
    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    /**
     * @author Jagjit Singh
     * method to upload image to firebase and store volunteer data in local storage
     */
    private void uploadFile(View view){
        if(!(imageUri == null || editTextFirstName.getText().toString().isEmpty() || editTextLastName.getText().toString().isEmpty() || editTextEmail.getText().toString().isEmpty() || radioGroupExperience.getCheckedRadioButtonId() == -1)){
            if((radioGroupExperience.getCheckedRadioButtonId() == R.id.radio_button_Yes_Experience && !(editTextDescription.getText().toString().isEmpty())) || (radioGroupExperience.getCheckedRadioButtonId() == R.id.radio_button_No_Experience && editTextDescription.getText().toString().isEmpty())){

                /**
                 * @author Jagjit Singh
                 * imageName will be store as current time in miliseconds plus image extension
                 * thus, image name will be unique alwaays
                 */
                String imageName = System.currentTimeMillis() + "." + getFileExtension(imageUri);

                /**
                 * @author Jagjit Singh
                 * prefix_suggix will store array of strings which will contain two values
                 * first value: image name (this will used as volunteer id whose image it is)
                 * second value: image extension
                 */
                String[] prefix_suffix = imageName.split("\\.");
                final String prefix = prefix_suffix[0];

                final StorageReference fileReference = storageReference.child(imageName);
                uploadTask = fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBarUploadImage.setProgress(0);
                                    }
                                }, 500);

                                Toast.makeText(getContext(), "Submission Successful!", Toast.LENGTH_SHORT).show();

                                Task<Uri> taskPhotoUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                                taskPhotoUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        /**
                                         * @author Jagjit Singh
                                         * when image is uploaded successfully, we create Volunteer and VolunteerPhoto models to store in local database
                                         */

                                        String photoUrl = uri.toString();

                                        Volunteer volunteer = new Volunteer(
                                                prefix,
                                                editTextFirstName.getText().toString(),
                                                editTextLastName.getText().toString(),
                                                editTextEmail.getText().toString(),
                                                editTextDescription.getText().toString()
                                        );

                                        VolunteerPhoto volunteerPhoto = new VolunteerPhoto(
                                                photoUrl,
                                                prefix
                                        );


                                        /**
                                         * @author Jagjit Singh
                                         * realtime database in Firebase is created for Volunteer Photo
                                         */
                                        String uploadId = databaseReference.push().getKey();
                                        databaseReference.child(uploadId).setValue(volunteerPhoto);


                                        /**
                                         * @author Jagjit Singh
                                         * volunteer and volunteer photo data is stored in local database
                                         */
                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                                        boolean successVolunteer = dataBaseHelper.addVolunteer(volunteer);
                                        boolean successPhoto = dataBaseHelper.addVolunteerPhoto(volunteerPhoto);
                                        dataBaseHelper.closeDB();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                progressBarUploadImage.setProgress((int) progress);                        }
                        });
            } else{
                Snackbar snackbar = Snackbar.make(view, "Please describe your experience!", Snackbar.LENGTH_LONG);
                snackbar.setAction("OKAY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        } else if(editTextFirstName.getText().toString().isEmpty() || editTextLastName.getText().toString().isEmpty() || editTextEmail.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(view, "Please enter all the text fields!", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else if(radioGroupExperience.getCheckedRadioButtonId() == -1){
            Snackbar snackbar = Snackbar.make(view, "Please select yes if you have experience, otherwise no!", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else if(imageUri == null){
            Snackbar snackbar = Snackbar.make(view, "No File Selected", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else{
            Snackbar snackbar = Snackbar.make(view, "Something went wrong!", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }
    }
}
