package com.example.savepreciouswildlife.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.savepreciouswildlife.adapters.BeFamiliarAdapter;
import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.MainActivity;
import com.example.savepreciouswildlife.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BeFamiliarFragment extends Fragment {

    MainActivity activity;
    SQLiteDatabase db;
    Spinner spinnerProvince;
    Spinner spinnerCity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.fragment_be_familiar, container, false);
        getActivity().setTitle("Be Familiar With Wildlife");

        /**
         * @author JunHyung-Kim
         * Database initializing
         */
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        db = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.dropAndCreateBirdlist(db);


        /**
         * @author JunHyung-Kim
         * Elements initializing
         */
        Button btnShowBirds = mainView.findViewById(R.id.btnShowBirds);
        final ListView listViewBirds = mainView.findViewById(R.id.listViewShowBirds);

        /**
         * @author JunHyung-Kim
         * Spinner Control
         */
        spinnerProvince = mainView.findViewById(R.id.spnSelectProvince);
        spinnerProvince.setPrompt("Select Province");
        setSpinner();
        spinnerCity = mainView.findViewById(R.id.spnSelectCity);
        spinnerCity.setPrompt("Select City");

        /**
         * @author JunHyung-Kim
         * When user select province, the city will be changed connectly,
         */
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                switch(i) {
                    case 0:
                        setSpinner(R.array.city_do_EveryProvinces);
                        break;
                    case 1:
                        setSpinner(R.array.city_do_Alberta);
                        break;
                    case 2:
                        setSpinner(R.array.city_do_British_Columbia);
                        break;
                    case 3:
                        setSpinner(R.array.city_do_Manitoba);
                        break;
                    case 4:
                        setSpinner(R.array.city_do_New_Brunswick);
                        break;
                    case 5:
                        setSpinner(R.array.city_do_Newfoundland_and_Labrador);
                        break;
                    case 6:
                        setSpinner(R.array.city_do_Northwest_Territories);
                        break;
                    case 7:
                        setSpinner(R.array.city_do_Nova_Scotia);
                        break;
                    case 8:
                        setSpinner(R.array.city_do_Ontario);
                        break;
                    case 9:
                        setSpinner(R.array.city_do_Prince_Edward_Island);
                        break;
                    case 10:
                        setSpinner(R.array.city_do_Quebec);
                        break;
                    case 11:
                        setSpinner(R.array.city_do_Saskatchewan);
                        break;
                    case 12:
                        setSpinner(R.array.city_do_Yukon);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        List<String[]> Birds = ReadCSV();

        for (int i = 0; i < Birds.size(); i++) {
            String id = Birds.get(i)[0];
            String province = Birds.get(i)[1];
            String city = Birds.get(i)[2];
            String name = Birds.get(i)[3];
            String etc = Birds.get(i)[4];
            String url = Birds.get(i)[5];

            addBirdsProfile(id, province, city, name, etc, url);
        }


        /**
         * @author JunHyung-Kim
         * When user click the 'Show Birds' button,
         */
        btnShowBirds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String spnProvinceName = spinnerProvince.getSelectedItem().toString();
                String spnCityName = spinnerCity.getSelectedItem().toString();

                List<String[]> birdsRecsDB = browseBirdsRecs(spnProvinceName, spnCityName);

                listViewBirds.setAdapter(new BeFamiliarAdapter(activity, birdsRecsDB));

            }
        });

        return mainView;
    }

    /**
     * @author JunHyungKim
     * @return ArrayList which contains each value from befamiliar.csv file.
     */
    public List<String[]> ReadCSV(){

        List<String[]> beFamiliarBirds = new ArrayList<>();
        // populate the list

        InputStream inputStream = getResources().openRawResource(R.raw.befamiliarbirds);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String csvLine;
            while((csvLine = reader.readLine()) != null) {
                String[] eachBird = csvLine.split(",");
                beFamiliarBirds.add(eachBird);
            }


        } catch (IOException ex) {
            throw new RuntimeException("Error reading CSV file " + ex);
        } finally {
            try {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }catch (IOException e) {
            }
        }
        return beFamiliarBirds;
    }


    /**
     * @author JunHyungKim
     * @param id field data from csv file
     * @param province field data from csv file
     * @param city field data from csv file
     * @param name field data from csv file
     * @param etc field data from csv file
     * @param url field data from csv file
     */
    private void addBirdsProfile(String id, String province, String city, String name, String etc, String url) {

        long result= 0;
        ContentValues val = new ContentValues();

        val.put("ID", id);
        val.put("PROVINCE", province);
        val.put("CITY", city);
        val.put("NAME", name);
        val.put("ETC", etc);
        val.put("URL", url);

        db.insert("BEFAMILIAR_BIRD", null, val);

        if (result != -1) {
            Log.d("BE_FAMILIAR_DB", "rowid == " + result + " inserted birds id");
        }
        else {
            Log.d("BE_FAMILIAR_DB", "Error inserting birds rec with id" + id);
        }


    }


    /**
     * @author JunHyungKim
     * @param provinceName input Province name by user
     * @param cityName input city name by user
     * @return birdList
     */
    private List<String[]> browseBirdsRecs(String provinceName, String cityName) {

        List<String[]> BirdList = new ArrayList<>();

        String queryAllProvince = "SELECT * FROM BEFAMILIAR_BIRD";
        String queryAllCity = "SELECT * FROM BEFAMILIAR_BIRD WHERE PROVINCE='" + provinceName + "';";
        String queryStr = "SELECT * FROM BEFAMILIAR_BIRD WHERE PROVINCE='" + provinceName + "' AND CITY='" + cityName + "';";

        try{
            Cursor cursor;

            if(provinceName.equals("*Every Provinces*") && cityName.equals("*Every Cities*"))
            {
                cursor = db.rawQuery(queryAllProvince, null);
            } else if (cityName.equals("*Every Cities*"))
            {
                cursor = db.rawQuery(queryAllCity, null);
            } else
            {
                cursor = db.rawQuery(queryStr, null);
            }

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String[] eachRecArray = new String[6];
                    eachRecArray[0] = cursor.getString(0); // this corresponds to the birdid
                    eachRecArray[1] = cursor.getString(1); // this corresponds to the birdprovince
                    eachRecArray[2] = cursor.getString(2); // this corresponds to the birdcity
                    eachRecArray[3] = cursor.getString(3); // this corresponds to the birdname
                    eachRecArray[4] = cursor.getString(4); // this corresponds to the birdetc
                    eachRecArray[5] = cursor.getString(5); // this corresponds to the birdurl

                    BirdList.add(eachRecArray); // adding record to the list of String array

                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            Log.d("DB DEMO", "Querying student recs error " + ex.getMessage());
        }
        return BirdList;
    }

    /**
     * @author JunHyung Kim
     * methods for setting elements in each spinner
     */
    private void setSpinner() {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(activity, R.array.province_do, android.R.layout.simple_spinner_item);
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
