package com.example.savepreciouswildlife.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.models.Hospital;
import com.example.savepreciouswildlife.MainActivity;
import com.example.savepreciouswildlife.R;
import com.example.savepreciouswildlife.models.ReportedBird;
import com.example.savepreciouswildlife.models.ReportedBirdPhoto;
import com.example.savepreciouswildlife.RouteRequest;
import com.example.savepreciouswildlife.interfaces.ServerResponseCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ReportBirdFragment extends Fragment {

    private Spinner spinnerCity, spinnerProvince;
    private EditText editTextInjury, editTextSpecies;
    private ImageView imgViewReportedBird;
    private Button btnChooseReportedBirdPhoto, btnReport;
    private ProgressBar progressBarBirdImageUpload;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_report_bird, container, false);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getActivity().setTitle("Report Bird");


        spinnerProvince = rootView.findViewById(R.id.spnSelectProvince);
        setSpinner();

        spinnerCity = rootView.findViewById(R.id.spnSelectCity);

        editTextInjury = rootView.findViewById(R.id.editTextInjury);
        editTextSpecies = rootView.findViewById(R.id.editTextSpecies);
        imgViewReportedBird = rootView.findViewById(R.id.imgViewReportedBird);
        btnChooseReportedBirdPhoto = rootView.findViewById(R.id.btnChooseReportedBirdPhoto);
        btnReport = rootView.findViewById(R.id.btnReport);
        progressBarBirdImageUpload = rootView.findViewById(R.id.progressBarBirdImageUpload);

        storageReference = FirebaseStorage.getInstance().getReference("reported birds");
        databaseReference = FirebaseDatabase.getInstance().getReference("reported birds");

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 0:
                        setSpinner(R.array.city_rb_EveryProvinces);
                        break;
                    case 1:
                        setSpinner(R.array.city_rb_Alberta);
                        break;
                    case 2:
                        setSpinner(R.array.city_rb_British_Columbia);
                        break;
                    case 3:
                        setSpinner(R.array.city_rb_Manitoba);
                        break;
                    case 4:
                        setSpinner(R.array.city_rb_New_Brunswick);
                        break;
                    case 5:
                        setSpinner(R.array.city_rb_Newfoundland_and_Labrador);
                        break;
                    case 6:
                        setSpinner(R.array.city_rb_Northwest_Territories);
                        break;
                    case 7:
                        setSpinner(R.array.city_rb_Nova_Scotia);
                        break;
                    case 8:
                        setSpinner(R.array.city_rb_Ontario);
                        break;
                    case 9:
                        setSpinner(R.array.city_rb_Prince_Edward_Island);
                        break;
                    case 10:
                        setSpinner(R.array.city_rb_Quebec);
                        break;
                    case 11:
                        setSpinner(R.array.city_rb_Saskatchewan);
                        break;
                    case 12:
                        setSpinner(R.array.city_rb_Yukon);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnChooseReportedBirdPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(firebaseUser, view);

                }
            }
        });

        return rootView;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imgViewReportedBird);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(final FirebaseUser firebaseUser, View view) {

        if (!(imageUri == null && editTextInjury.getText().toString().isEmpty() || editTextSpecies.getText().toString().isEmpty() || spinnerProvince.getSelectedItemPosition() == 0)) {
            String imageName = System.currentTimeMillis() + "." + getFileExtension(imageUri);

            final String province = spinnerProvince.getSelectedItem().toString();
            final String city = spinnerCity.getSelectedItem().toString();
            final String species = editTextSpecies.getText().toString();
            final String injury = editTextInjury.getText().toString();

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
                                    progressBarBirdImageUpload.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getContext(), "Reported! Searching for nearest hospital..", Toast.LENGTH_LONG).show();

                            Task<Uri> taskPhotoUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                            taskPhotoUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String photoUrl = uri.toString();

                                    ReportedBird reportedBird = new ReportedBird(
                                            prefix,
                                            firebaseUser.getEmail(),
                                            province,
                                            city,
                                            species,
                                            injury
                                    );

                                    ReportedBirdPhoto reportedBirdPhoto = new ReportedBirdPhoto(
                                            photoUrl,
                                            prefix
                                    );

                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(uploadId).setValue(reportedBirdPhoto);

                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());

                                    final List<Hospital> hospitalList = dataBaseHelper.getAllHospitals();

                                    boolean successReportedBird = dataBaseHelper.addReportedBird(reportedBird);
                                    boolean successPhoto = dataBaseHelper.addReportedBirdPhoto(reportedBirdPhoto);

                                    dataBaseHelper.closeDB();



                                    /**
                                     * @author Jagjit Singh
                                     * MapsQuest API
                                     */

                                    // jsonObject will go in the body of post request made to the API
                                    // jsonArray will be inside the jsonObject
                                    // jsonObject structure is given below:
                                    /*
                                        {
                                          "locations": [
                                            "Denver, CO",
                                            "Westminster, CO",
                                            "Boulder, CO"
                                          ]
                                         }
                                     */
                                    JSONObject jsonObject = new JSONObject();
                                    final JSONArray jsonArray = new JSONArray();


                                    String provinceAbbreviation = province.substring(province.indexOf("(") + 1, province.indexOf(")"));

                                    try {
                                        // first value in jsonArray is the location where the Bird is located
                                        jsonArray.put(city + ", " + provinceAbbreviation);

                                        // now adding location of all the hospitals we have in the jsonArray
                                        for (int i = 0; i < hospitalList.size(); i++) {
                                            jsonArray.put(hospitalList.get(i).getAddress() + ", " + hospitalList.get(i).getCity() + ", " + hospitalList.get(i).getProvince());
                                        }

                                        // putting jsonArray in jsonObject
                                        jsonObject.put("locations", jsonArray);
                                    } catch (JSONException je) {
                                        Log.e("jRBF", je.getMessage());
                                    }


                                    // making a post request
                                    new RouteRequest().sendPOSTRequestToServer(
                                            getActivity(),
                                            jsonObject,
                                            "https://www.mapquestapi.com/directions/v2/routematrix?key=7uJtHk2CgSAQGfIG6WuA1BSrT4YBscUH",
                                            new ServerResponseCallback() {
                                                @Override
                                                public void onJSONResponse(JSONObject jsonObject) {
                                                    try {
                                                        // response will contain the distance of first value in jsonArray to all the other values


                                                        String responseDistance = jsonObject.get("distance").toString();
                                                        String responseDistanceTrimmed = responseDistance.substring(1, responseDistance.length() - 1);

                                                        // now we are storing all the distances in a list
                                                        List<String> distanceList = new ArrayList<String>(Arrays.asList(responseDistanceTrimmed.split(",")));

                                                        double[] distanceArray = new double[distanceList.size()];

                                                        for (int i = 0; i < distanceList.size(); ++i) {
                                                            distanceArray[i] = Double.parseDouble(distanceList.get(i));
                                                        }

                                                        double min = distanceArray[1];


                                                        // finding the index of shortest distance value in the list
                                                        for (int i = 1; i < distanceArray.length; i++) {
                                                            if (distanceArray[i] < min)
                                                                min = distanceArray[i];
                                                        }


                                                        // based on index of shortest distance, finding the hospital data
                                                        int indexOfNearestHospital = distanceList.indexOf(String.valueOf(min));


                                                        // Dialog Box to show the nearest hospital data such as Hospital Name, Place, and Distance.
                                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                                                        builder.setTitle("Information");
                                                        builder.setIcon(R.drawable.ic_baseline_local_hospital_24);
                                                        builder.setMessage("Nearest Hospital is " + hospitalList.get(indexOfNearestHospital - 1).getName() + " at " + jsonArray.get(indexOfNearestHospital).toString() + ". It is " + distanceList.get(indexOfNearestHospital) + " miles away.");
                                                        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });
                                                        builder.show();

                                                    } catch (JSONException je) {
                                                        Log.d("BRUH", je.getMessage());
                                                        Toast.makeText(getActivity(), "Nearest Hospital could not be located!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    Log.e("JRBF", "onError: ", e);
                                                }
                                            }
                                    );
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
                            progressBarBirdImageUpload.setProgress((int) progress);
                        }
                    });

        } else if (editTextInjury.getText().toString().isEmpty() || editTextSpecies.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar.make(view, "Please enter all the text fields!", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else if (spinnerProvince.getSelectedItemPosition() == 0) {
            Snackbar snackbar = Snackbar.make(view, "Please select a province", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else if (imageUri == null) {
            Snackbar snackbar = Snackbar.make(view, "No File Selected!", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else {
            Snackbar snackbar = Snackbar.make(view, "Something went wrong!", Snackbar.LENGTH_LONG);
            snackbar.setAction("OKAY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(activity, R.array.province_rb, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(fAdapter);
    }

    private void setSpinner(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(activity, itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(fAdapter);
    }


}