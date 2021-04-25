package com.example.savepreciouswildlife;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.savepreciouswildlife.activities.RegistrationActivity;
import com.example.savepreciouswildlife.fragments.BeFamiliarFragment;
import com.example.savepreciouswildlife.fragments.BecomeVolunteerFragment;
import com.example.savepreciouswildlife.fragments.DonateFragment;
import com.example.savepreciouswildlife.fragments.HomeFragment;
import com.example.savepreciouswildlife.fragments.OurVolunteersFragment;
import com.example.savepreciouswildlife.fragments.ReportBirdFragment;
import com.example.savepreciouswildlife.fragments.ReportedBirdsFragment;
import com.example.savepreciouswildlife.models.Hospital;
import com.example.savepreciouswildlife.models.Patient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView textViewUserEmail;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public DataBaseHelper dataBaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public boolean checkTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = firebaseUser.getEmail();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.main_toolbar);

        View headerView = navigationView.getHeaderView(0);
        textViewUserEmail = headerView.findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText(userEmail);


        dataBaseHelper = new DataBaseHelper(this);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        dataBaseHelper.dropAndCreateHospital(sqLiteDatabase);

        // @author Kateryna. Here we are checking if the patients data are already populated into database
        //the data will be written in the database only once

        checkTable = dataBaseHelper.checkTablePatientsData(sqLiteDatabase);
        if (!checkTable ){
            ReadCSVPatients();
        }

        checkTable = dataBaseHelper.checkTableHospitalData(sqLiteDatabase);
        if (!checkTable ){
            readCSVHospitals();
        }

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            //super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_be_familiar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BeFamiliarFragment()).commit();
                break;
            case R.id.nav_become_a_volunteer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BecomeVolunteerFragment()).commit();
                break;
            case R.id.nav_make_a_donation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonateFragment()).commit();
                break;
            case R.id.nav_found_a_bird:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportBirdFragment()).commit();
                break;
            case R.id.nav_volunteers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OurVolunteersFragment()).commit();
                break;
            case R.id.nav_reported_birds:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportedBirdsFragment()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void readCSVHospitals(){

        InputStream inputStream = getResources().openRawResource(R.raw.hospitals);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String csvLine;
            while((csvLine = reader.readLine()) != null) {
                String[] eachHospital = csvLine.split(",");
                Hospital hospital = new Hospital(eachHospital[0], eachHospital[1], eachHospital[2], eachHospital[3]);
                dataBaseHelper.addHospital(hospital);
            }
        } catch (IOException ex) {
            dataBaseHelper.closeDB();
            throw new RuntimeException("Error reading CSV file " + ex);
        } finally {
            try {
                if (inputStream != null)
                {
                    inputStream.close();
                    dataBaseHelper.closeDB();
                }
            }catch (IOException e) {
            }
        }
    }

    //@author Kateryna
    //method to write the data from a csv file into database using DB Helper class
      public void ReadCSVPatients() {
        InputStream inputStream = getResources().openRawResource(R.raw.patients1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine1;
            while ((csvLine1 = reader.readLine()) != null) {
                String[] fieldsArray = csvLine1.split(",");

               Patient eachPatient = new Patient(Integer.parseInt(fieldsArray[0]),
                       getResources().getIdentifier(fieldsArray[1],"drawable",getPackageName()),
                       fieldsArray[2],fieldsArray[3],fieldsArray[4],Integer.parseInt(fieldsArray[5]),
                       Integer.parseInt(fieldsArray[6]),Integer.parseInt(fieldsArray[7]));

                dataBaseHelper.addPatient(eachPatient);

            }

        } catch (IOException ex) {
            dataBaseHelper.closeDB();
            throw new RuntimeException("error reading CSV file" + ex);
        }
        finally {
            try{
                if (inputStream != null)
                {
                    inputStream.close();
                    dataBaseHelper.closeDB();
                }
            }
            catch(IOException ex) {
                throw new RuntimeException("Error closing input stream" + ex);
            }
        }
    }
}