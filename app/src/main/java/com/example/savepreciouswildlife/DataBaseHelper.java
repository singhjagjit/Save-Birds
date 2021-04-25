package com.example.savepreciouswildlife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.savepreciouswildlife.models.Hospital;
import com.example.savepreciouswildlife.models.Patient;
import com.example.savepreciouswildlife.models.Rating;
import com.example.savepreciouswildlife.models.ReportedBird;
import com.example.savepreciouswildlife.models.ReportedBirdPhoto;
import com.example.savepreciouswildlife.models.User;
import com.example.savepreciouswildlife.models.Volunteer;
import com.example.savepreciouswildlife.models.VolunteerPhoto;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context) {
        super(context, "saveBirds.db", null, 1);
    }
     // @author Kateryna. Instantiated database helper class. Created initial tables, columns
    // create  table names  here
    private static final String TABLE_PATIENT = "TABLE_PATIENT";
    private static final String TABLE_VOLUNTEER = "VOLUNTEER";
    private static final String TABLE_USER = "USER";
    private static final String TABLE_RATINGS = "RATINGS";
    private static final String TABLE_VOLUNTEER_PHOTO = "VOLUNTEER_PHOTO";
    private static final String TABLE_BEFAMILIAR_BIRD = "BEFAMILIAR_BIRD";
    private static final String TABLE_REPORTED_BIRD = "REPORTED_BIRD";
    private static final String TABLE_REPORTED_BIRD_PHOTO = "REPORTED_BIRD_PHOTO";
    private static final String TABLE_HOSPITAL = "HOSPITAL";

    //create all necessary columns here
    // --PATIENT COLUMNS
    private static final String COLUMN_PATIENT_ID = "PATIENT_ID";
    private static final String COLUMN_PATIENT_PICTURE = "PATIENT_PICTURE";
    private static final String COLUMN_PATIENT_NAME = "PATIENT_NAME";
    private static final String COLUMN_PATIENT_BREED = "PATIENT_BREED";
    private static final String COLUMN_PATIENT_INJURE = "PATIENT_INJURE";
    private static final String COLUMN_SUM_NEED = "SUM_NEED";
    private static final String COLUMN_SUM_DONATE = "SUM_DONATE";
    private static final String COLUMN_SUM_LEFT = "SUM_LEFT";

    // --VOLUNTEER COLUMNS
    private static final String COLUMN_VOLUNTEER_ID = "VOLUNTEER_ID";
    private static final String COLUMN_VOLUNTEER_FIRST_NAME = "FIRST_NAME";
    private static final String COLUMN_VOLUNTEER_LAST_NAME = "LAST_NAME";
    private static final String COLUMN_VOLUNTEER_EMAIL = "EMAIL";
    private static final String COLUMN_VOLUNTEER_EXPERIENCE = "EXPERIENCE";

    // --USER COLUMNS
    private static final String COLUMN_USER_EMAIL = "EMAIL";

    // --VOLUNTEER_USER COLUMNS
    private static final String COLUMN_RATINGS_USER_ID = "USER_ID";
    private static final String COLUMN_RATINGS_VOLUNTEER_ID = "VOLUNTEER_ID";
    private static final String COLUMN_RATINGS_RATING = "RATING";

    // --VOLUNTEER_PHOTO COLUMNS
    private static final String COLUMN_VOLUNTEER_PHOTO_VOLUNTEER_ID = "VOLUNTEER_ID";
    private static final String COLUMN_VOLUNTEER_PHOTO_PHOTO_ID = "PHOTO_ID";

    // --BEFAMILIAR_BIRDS COLUMNS
    private static final String COLUMN_BEFAMILIAR_BIRD_ID = "ID";
    private static final String COLUMN_BEFAMILIAR_BIRD_PROVINCE = "PROVINCE";
    private static final String COLUMN_BEFAMILIAR_BIRD_CITY = "CITY";
    private static final String COLUMN_BEFAMILIAR_BIRD_NAME = "NAME";
    private static final String COLUMN_BEFAMILIAR_BIRD_ETC = "ETC";
    private static final String COLUMN_BEFAMILIAR_BIRD_URL = "URL";

    // --REPORTED_BIRDS COLUMNS
    private static final String COLUMN_REPORTED_BIRD_ID = "ID";
    private static final String COLUMN_REPORTED_BIRD_REPORTER_EMAIL = "REPORTER_EMAIL";
    private static final String COLUMN_REPORTED_BIRD_PROVINCE = "PROVINCE";
    private static final String COLUMN_REPORTED_BIRD_CITY = "CITY";
    private static final String COLUMN_REPORTED_BIRD_SPECIES = "SPECIES";
    private static final String COLUMN_REPORTED_BIRD_INJURY = "INJURY";

    // --REPORTED_BIRD_PHOTO COLUMNS
    private static final String COLUMN_REPORTED_BIRD_PHOTO_REPORTED_BIRD_ID = "REPORTED_BIRD_ID";
    private static final String COLUMN_REPORTED_BIRD_PHOTO_PHOTO_ID = "PHOTO_ID";

    // --HOSPITAL COLUMNS
    private static final String COLUMN_HOSPITAL_ID = "ID";
    private static final String COLUMN_HOSPITAL_NAME = "NAME";
    private static final String COLUMN_HOSPITAL_CITY = "CITY";
    private static final String COLUMN_HOSPITAL_PROVINCE = "PROVINCE";
    private static final String COLUMN_HOSPITAL_ADDRESS = "ADDRESS";

    // statement for creating tables
    private static final String CREATE_TABLE_HOSPITAL = "CREATE TABLE " + TABLE_HOSPITAL +
            "(" + COLUMN_HOSPITAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_HOSPITAL_NAME + " TEXT, " +
            COLUMN_HOSPITAL_CITY + " TEXT, " +
            COLUMN_HOSPITAL_PROVINCE + " TEXT, " +
            COLUMN_HOSPITAL_ADDRESS + " TEXT " + ")";

    private static final String CREATE_TABLE_PATIENT = "CREATE TABLE "
            + TABLE_PATIENT + "(" + COLUMN_PATIENT_ID + " INT, " +
            COLUMN_PATIENT_PICTURE + " INT," +
            COLUMN_PATIENT_NAME + " TEXT," +
            COLUMN_PATIENT_BREED + " TEXT," +
            COLUMN_PATIENT_INJURE + " TEXT,"
            + COLUMN_SUM_NEED + " INT," +
            COLUMN_SUM_DONATE + " INT," +
            COLUMN_SUM_LEFT + " INT" + ")";

    private static final String CREATE_TABLE_VOLUNTEER = "CREATE TABLE " + TABLE_VOLUNTEER +
    "(" + COLUMN_VOLUNTEER_ID + " TEXT, " +
            COLUMN_VOLUNTEER_FIRST_NAME + " TEXT, " +
            COLUMN_VOLUNTEER_LAST_NAME + " TEXT, " +
            COLUMN_VOLUNTEER_EMAIL + " TEXT, " +
            COLUMN_VOLUNTEER_EXPERIENCE + " TEXT " + ")";

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER +
            "(" + COLUMN_USER_EMAIL + " TEXT PRIMARY KEY" + ")";

    private static final String CREATE_TABLE_RATINGS = "CREATE TABLE " + TABLE_RATINGS +
            "(" + COLUMN_RATINGS_USER_ID + " TEXT, " +
            COLUMN_RATINGS_VOLUNTEER_ID + " TEXT, " +
            COLUMN_RATINGS_RATING + " INTEGER, " +
            "PRIMARY KEY (" + COLUMN_RATINGS_USER_ID + ", " + COLUMN_RATINGS_VOLUNTEER_ID + "))";

    private static final String CREATE_TABLE_VOLUNTEER_PHOTO = "CREATE TABLE " + TABLE_VOLUNTEER_PHOTO +
            "(" + COLUMN_VOLUNTEER_PHOTO_PHOTO_ID + " TEXT PRIMARY KEY, " +
                COLUMN_VOLUNTEER_PHOTO_VOLUNTEER_ID + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_VOLUNTEER_PHOTO_VOLUNTEER_ID + ")" + " REFERENCES " + TABLE_VOLUNTEER + "(" + COLUMN_VOLUNTEER_ID + "))";


    /**
     * @author JunHyungKim
     * Created Befamiliar Table as other group member(Kateryna) did before.
     */
    private static final String CREATE_TABLE_BEFAMILIAR_BIRD = "CREATE TABLE " + TABLE_BEFAMILIAR_BIRD +
            "(" + COLUMN_BEFAMILIAR_BIRD_ID + " TEXT PRIMARY KEY, " +
            COLUMN_BEFAMILIAR_BIRD_PROVINCE + " TEXT, " +
            COLUMN_BEFAMILIAR_BIRD_CITY + " TEXT, " +
            COLUMN_BEFAMILIAR_BIRD_NAME + " TEXT, " +
            COLUMN_BEFAMILIAR_BIRD_ETC + " TEXT, " +
            COLUMN_BEFAMILIAR_BIRD_URL + " TEXT" +")";

    private static final String CREATE_TABLE_REPORTED_BIRD = "CREATE TABLE " + TABLE_REPORTED_BIRD +
            "(" + COLUMN_REPORTED_BIRD_ID + " TEXT PRIMARY KEY, " +
            COLUMN_REPORTED_BIRD_REPORTER_EMAIL + " TEXT, " +
            COLUMN_REPORTED_BIRD_PROVINCE + " TEXT, " +
            COLUMN_REPORTED_BIRD_CITY + " TEXT, " +
            COLUMN_REPORTED_BIRD_SPECIES + " TEXT, " +
            COLUMN_REPORTED_BIRD_INJURY + " TEXT" + ")";

    private static final String CREATE_TABLE_REPORTED_BIRD_PHOTO = "CREATE TABLE " + TABLE_REPORTED_BIRD_PHOTO +
            "(" + COLUMN_VOLUNTEER_PHOTO_PHOTO_ID + " TEXT PRIMARY KEY, " +
            COLUMN_REPORTED_BIRD_PHOTO_REPORTED_BIRD_ID + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_REPORTED_BIRD_PHOTO_REPORTED_BIRD_ID + ")" + " REFERENCES " + TABLE_REPORTED_BIRD + "(" + COLUMN_REPORTED_BIRD_ID + "))";


    // onCreate method creates all tables in the first place
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     sqLiteDatabase.execSQL(CREATE_TABLE_PATIENT);
     sqLiteDatabase.execSQL(CREATE_TABLE_VOLUNTEER);
     sqLiteDatabase.execSQL(CREATE_TABLE_USER);
     sqLiteDatabase.execSQL(CREATE_TABLE_RATINGS);
     sqLiteDatabase.execSQL(CREATE_TABLE_VOLUNTEER_PHOTO);
     sqLiteDatabase.execSQL(CREATE_TABLE_BEFAMILIAR_BIRD);
     sqLiteDatabase.execSQL(CREATE_TABLE_REPORTED_BIRD);
     sqLiteDatabase.execSQL(CREATE_TABLE_REPORTED_BIRD_PHOTO);
    }

    // onUpgrade method will recreate all tables if the version of the Db will be changed later
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VOLUNTEER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VOLUNTEER_PHOTO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BEFAMILIAR_BIRD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTED_BIRD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTED_BIRD_PHOTO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HOSPITAL);

        onCreate(sqLiteDatabase);
    }

    public void dropAndCreateBirdlist(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BEFAMILIAR_BIRD);
        sqLiteDatabase.execSQL(CREATE_TABLE_BEFAMILIAR_BIRD);
    }

    public void dropAndCreateHospital(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HOSPITAL);
        sqLiteDatabase.execSQL(CREATE_TABLE_HOSPITAL);
    }
    public void dropAndCreatePatient(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        sqLiteDatabase.execSQL(CREATE_TABLE_PATIENT);
    }
     // @ author Kateryna. First version supposed to instantiate and use the list of patients here
    //then will changed to write patients from the csv file
   /* public List<Patient> addInitialPatientData() {
        Patient patient1 = new Patient(1,R.drawable.patient1,"Candy","Oriental Owl","Broken wing",
                200,150,50);
        Patient patient2 = new Patient(2,R.drawable.patient2,"Birdy","Bald Eagle","Broken leg",
                120,20,100);
        Patient patient3 = new Patient (3,R.drawable.patient3,"Jinny","Barn Owl","Broken wing",
                200,100,100);
        Patient patient4 = new Patient (4,R.drawable.patient4,"Elice","Snow Owl","Need care",
                200,100,100);
        Patient patient5 = new Patient (5,R.drawable.patient5,"Tim","Forest Eagle","Poisoned",
                200,100,100);
        Patient patient6 = new Patient (6,R.drawable.patient6,"Dolly","Great Grey Owl","Need care",
                300,100,200);
        List<Patient> patientsList = new ArrayList<>(Arrays.asList(patient1,patient2,patient3,patient4,patient5,patient6));
        return  patientsList;
    }*/
    // author Kateryna
    // method to add a patient
    public boolean addPatient(Patient newPatient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //List<Patient> newPatient = addInitialPatientData();
        //for(int i = 0; i < newPatient.size();i++) {
            cv.put(COLUMN_PATIENT_ID,newPatient.getIdPatient());
            cv.put(COLUMN_PATIENT_PICTURE,newPatient.getPicPatient());
            cv.put(COLUMN_PATIENT_NAME,newPatient.getBirdName());
            cv.put(COLUMN_PATIENT_BREED,newPatient.getBirdBreed());
            cv.put(COLUMN_PATIENT_INJURE,newPatient.getBirdInjure());
            cv.put(COLUMN_SUM_NEED,newPatient.getSumNeed());
            cv.put(COLUMN_SUM_DONATE,newPatient.getSumDonated());
            cv.put(COLUMN_SUM_LEFT,newPatient.getSumLeft());
        long insert = db.insert(TABLE_PATIENT,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addVolunteer(Volunteer volunteer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_VOLUNTEER_ID, volunteer.getId());
        cv.put(COLUMN_VOLUNTEER_FIRST_NAME, volunteer.getFirstName());
        cv.put(COLUMN_VOLUNTEER_LAST_NAME, volunteer.getLastName());
        cv.put(COLUMN_VOLUNTEER_EMAIL, volunteer.getEmail());
        cv.put(COLUMN_VOLUNTEER_EXPERIENCE, volunteer.getExperience());

        long insert = db.insert(TABLE_VOLUNTEER,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addHospital(Hospital hospital){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HOSPITAL_NAME, hospital.getName());
        cv.put(COLUMN_HOSPITAL_CITY, hospital.getCity());
        cv.put(COLUMN_HOSPITAL_PROVINCE, hospital.getProvince());
        cv.put(COLUMN_HOSPITAL_ADDRESS, hospital.getAddress());

        long insert = db.insert(TABLE_HOSPITAL,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addReportedBird(ReportedBird reportedBird){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_REPORTED_BIRD_ID, reportedBird.getId());
        cv.put(COLUMN_REPORTED_BIRD_REPORTER_EMAIL, reportedBird.getReporterEmail());
        cv.put(COLUMN_REPORTED_BIRD_PROVINCE, reportedBird.getProvince());
        cv.put(COLUMN_REPORTED_BIRD_CITY, reportedBird.getCity());
        cv.put(COLUMN_REPORTED_BIRD_SPECIES, reportedBird.getSpecies());
        cv.put(COLUMN_REPORTED_BIRD_INJURY, reportedBird.getInjury());

        long insert = db.insert(TABLE_REPORTED_BIRD,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_EMAIL, user.getEmail());

        long insert = db.insert(TABLE_USER,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addRating(Rating rating){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_RATINGS_VOLUNTEER_ID, rating.getVolunteerID());
        cv.put(COLUMN_RATINGS_USER_ID, rating.getUserEmail());
        cv.put(COLUMN_RATINGS_RATING, rating.getRating());

        long insert = db.insert(TABLE_RATINGS,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addVolunteerPhoto(VolunteerPhoto volunteerPhoto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_VOLUNTEER_PHOTO_PHOTO_ID, volunteerPhoto.getPhotoUrl());
        cv.put(COLUMN_VOLUNTEER_PHOTO_VOLUNTEER_ID, volunteerPhoto.getVolunteerId());

        long insert = db.insert(TABLE_VOLUNTEER_PHOTO,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addReportedBirdPhoto(ReportedBirdPhoto reportedBirdPhoto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_REPORTED_BIRD_PHOTO_PHOTO_ID, reportedBirdPhoto.getPhotoUrl());
        cv.put(COLUMN_REPORTED_BIRD_PHOTO_REPORTED_BIRD_ID, reportedBirdPhoto.getReportedBirdId());

        long insert = db.insert(TABLE_REPORTED_BIRD_PHOTO,null, cv);
        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

     // @author Kateryna.
    //method to browse all the patients
    public List<Patient> getAllPatients() {
        List<Patient> returnList = new ArrayList<Patient>();
        String queryString = "SELECT * FROM " + TABLE_PATIENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);
        // looping through all rows of the patient table and adding to list
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(COLUMN_PATIENT_ID));
                int pic = c.getInt(c.getColumnIndex(COLUMN_PATIENT_PICTURE));
                String name = c.getString(c.getColumnIndex(COLUMN_PATIENT_NAME));
                String breed = c.getString(c.getColumnIndex(COLUMN_PATIENT_BREED));
                String injure = c.getString(c.getColumnIndex(COLUMN_PATIENT_INJURE));
                int sumNeed = c.getInt(c.getColumnIndex(COLUMN_SUM_NEED));
                int sumDonated = c.getInt(c.getColumnIndex(COLUMN_SUM_DONATE));
                int sumLeft = c.getInt(c.getColumnIndex(COLUMN_SUM_LEFT));
                Patient newPatient = new Patient(id,pic, name,breed,injure,sumNeed,sumDonated,sumLeft);
                // adding to the returnList of patients
                returnList.add(newPatient);
            } while (c.moveToNext());
        }
        return returnList;
    }
    //@author Kateryna
    // method to update information about the patient
    public int updatePatient (Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUM_NEED,patient.getSumNeed());
        values.put(COLUMN_SUM_DONATE,patient.getSumDonated());
        values.put(COLUMN_SUM_LEFT,patient.getSumLeft());
        return db.update(TABLE_PATIENT,values,COLUMN_PATIENT_ID + " = ?",new String[]{String.valueOf(patient.getIdPatient())});
    }

    public List<Volunteer> getAllVolunteers() {
        List<Volunteer> returnList = new ArrayList<Volunteer>();
        String queryString = "SELECT * FROM " + TABLE_VOLUNTEER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_VOLUNTEER_ID));
                String firstName = c.getString(c.getColumnIndex(COLUMN_VOLUNTEER_FIRST_NAME));
                String lastName = c.getString(c.getColumnIndex(COLUMN_VOLUNTEER_LAST_NAME));
                String email = c.getString(c.getColumnIndex(COLUMN_VOLUNTEER_EMAIL));
                String experience = c.getString(c.getColumnIndex(COLUMN_VOLUNTEER_EXPERIENCE));

                Volunteer volunteer = new Volunteer(id ,firstName, lastName, email, experience);
                returnList.add(volunteer);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return returnList;
    }

    public List<Hospital> getAllHospitals() {
        List<Hospital> returnList = new ArrayList<Hospital>();
        String queryString = "SELECT * FROM " + TABLE_HOSPITAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if (c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndex(COLUMN_HOSPITAL_NAME));
                String city = c.getString(c.getColumnIndex(COLUMN_HOSPITAL_CITY));
                String province = c.getString(c.getColumnIndex(COLUMN_HOSPITAL_PROVINCE));
                String address = c.getString(c.getColumnIndex(COLUMN_HOSPITAL_ADDRESS));

                Hospital hospital = new Hospital(name, city, province, address);

                returnList.add(hospital);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return returnList;
    }

    public List<ReportedBird> getAllReportedBirds() {
        List<ReportedBird> returnList = new ArrayList<ReportedBird>();
        String queryString = "SELECT * FROM " + TABLE_REPORTED_BIRD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_REPORTED_BIRD_ID));
                String reporterEmail = c.getString(c.getColumnIndex(COLUMN_REPORTED_BIRD_REPORTER_EMAIL));
                String province = c.getString(c.getColumnIndex(COLUMN_REPORTED_BIRD_PROVINCE));
                String city = c.getString(c.getColumnIndex(COLUMN_REPORTED_BIRD_CITY));
                String species = c.getString(c.getColumnIndex(COLUMN_REPORTED_BIRD_SPECIES));
                String injury = c.getString(c.getColumnIndex(COLUMN_REPORTED_BIRD_INJURY));

                ReportedBird reportedBird = new ReportedBird(id, reporterEmail, province, city, species, injury);
                returnList.add(reportedBird);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return returnList;
    }

     //@author Kateryna
    // method to close database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    // check if a user has already submitted rating for a volunteer
    // a user can give rating to a volunteer only once
    public boolean checkIfReviewAlreadySubmitted(String volunteerId, String userEmail){
        String queryString = "SELECT * FROM " + TABLE_RATINGS +
                " WHERE " + COLUMN_RATINGS_USER_ID + " = \"" + userEmail + "\" AND " +
                COLUMN_RATINGS_VOLUNTEER_ID + " = \"" + volunteerId + "\";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if(c.getCount() <= 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    // get average of all the rating given to a volunteer
    public double getAverageRatings(String volunteerId){
        double avgRatings = 0;
        String queryString = "SELECT AVG(" + COLUMN_RATINGS_RATING + ") FROM " + TABLE_RATINGS +
                " WHERE " + COLUMN_RATINGS_VOLUNTEER_ID + " = \"" + volunteerId + "\" GROUP BY " + COLUMN_RATINGS_VOLUNTEER_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if (c.moveToFirst()) {
            do {
                avgRatings = c.getDouble(0);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return avgRatings;
    }

    //@author Kateryna
    //method to check if the table already exists
    public boolean checkTablePatients(SQLiteDatabase db) {
        String queryString = "SELECT name  FROM sqlite_master WHERE name = 'TABLE_PATIENT' and type = 'table';";
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if(c.getCount() == 1){
            c.close();
            return true;
        } else {
            c.close();
            return false;
    }
    }

    //author Kateryna
    //method to check if there are data in the table
    public boolean checkTablePatientsData(SQLiteDatabase db) {
        String queryString = "SELECT * FROM " + TABLE_PATIENT;
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if(c.getCount() <= 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean checkTableHospitalData(SQLiteDatabase db) {
        String queryString = "SELECT * FROM " + TABLE_HOSPITAL;
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(queryString, null);

        if(c.getCount() <= 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

}

