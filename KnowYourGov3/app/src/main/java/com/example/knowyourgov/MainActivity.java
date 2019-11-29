package com.example.knowyourgov;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Official> Officials = new ArrayList<>();
    private official_adapter OfficialAdapter;
    private MainActivity mainActivity;

    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private LocationManager locationManager;
    private Criteria criteria;
    private double lat;
    private double longitude;
    private String zip;
    private String city;
    private String state;
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        if (!doNetCheck()) setContentView(R.layout.noconnection);
        else {
            setTitle("Know Your Government");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            recyclerView = findViewById(R.id.recyclerView);
            mainActivity = this;
            OfficialAdapter = new official_adapter(Officials, this);
            recyclerView.setAdapter(OfficialAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_LOCATION_REQUEST_CODE_ID);
            }
            setLocation();
            getZip(this);

            header = findViewById(R.id.header);
            header.setText(city + ", " + state + " " + zip);
            AsyncTask<String, Void, String> a = new getOfficialInfo(this).execute(zip);
        }
    }

    public void setHeader(){
        TextView header = findViewById(R.id.header);
        header.setText(city +", " + state + " " + zip);
    }

    private boolean doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    protected void onPause(){
        super.onPause();
        doWrite(recyclerView);

    }

    public void doWrite(View v) {

        JSONArray jsonArray = new JSONArray();
        for (Official n : Officials) {
            try {
                JSONObject OfficialJSON = new JSONObject();
                OfficialJSON.put("name", n.name);
                OfficialJSON.put("party", n.party);
                OfficialJSON.put("title", n.title);

                jsonArray.put(OfficialJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String jsonText = jsonArray.toString();

        Log.d(TAG, "doWrite: " + jsonText);

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(
                            openFileOutput("Officials.txt", Context.MODE_PRIVATE)
                    );

            outputStreamWriter.write(jsonText);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.d(TAG, "doWrite: File write failed: " + e.toString());
        }
    }

    public void doRead(View v) {
        Officials.clear();
        try {
            InputStream inputStream = openFileInput("Officials.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                String jsonText = stringBuilder.toString();

                try {
                    JSONArray jsonArray = new JSONArray(jsonText);
                    Log.d(TAG, "doRead: " + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String title = jsonObject.getString("title");
                        String party = jsonObject.getString("party");
                        Official n = new Official(name, title, party);
                        Officials.add(n);
                    }

                    Log.d(TAG, "doRead: " + Officials);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "doRead: File not found: \" + e.toString()");
        } catch (IOException e) {
            Log.d(TAG, "doRead: Can not read file: " + e.toString());
        }
    }
    protected void onSaveInstanceState(Bundle outState){
        outState.putSerializable("Officials", Officials);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstance){
          super.onRestoreInstanceState(savedInstance);
          Officials = (ArrayList)savedInstance.getSerializable("Officials");
          OfficialAdapter = new official_adapter(Officials, this);
          recyclerView.setAdapter(OfficialAdapter);
          recyclerView.setLayoutManager(new LinearLayoutManager((this)));
    }



    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.about:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                return true;
            case R.id.search:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (doNetCheck()) {

                    builder.setMessage("Enter a City, State, or Zip Code:");

                    final EditText et = new EditText(this);
                    et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    et.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(et);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Officials.clear();
                            AsyncTask<String, Void, String> a = new getOfficialInfo(mainActivity).execute(et.getText().toString());
                        }
                    });

                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }
                else{
                    builder.setTitle("No network connection");
                    builder.setMessage("Officials cannot be added without a network connection");
                }

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClick(View view) {
        int index = recyclerView.getChildLayoutPosition(view);
        Official passedOfficial = Officials.get(index);
        int currentIndex = index;
        Intent intent = new Intent(this, OfficialActivity.class);

        intent.putExtra("Official", passedOfficial);
        ArrayList<String> location = new ArrayList();
        location.add(city);
        location.add(state);
        location.add(zip);
        intent.putExtra("location", location);
        startActivity(intent);

    }


    public void updateData(ArrayList<Official> OfficialList) {
        Officials.addAll(OfficialList);
        OfficialAdapter.notifyDataSetChanged();
    }


    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
                return;
            }
        }

    }
    @SuppressLint("MissingPermission")
    private void setLocation() {

        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location currentLocation = locationManager.getLastKnownLocation(bestProvider);

        if (currentLocation != null) {
                 lat = currentLocation.getLatitude();
                 longitude = currentLocation.getLongitude();

        }
    }



    public void getZip(MainActivity v) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses;


            addresses = geocoder.getFromLocation(lat, longitude, 10);

            if (!addresses.isEmpty()) {
                zip = addresses.get(0).getPostalCode();
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
            }

            Log.d(TAG, "onCreate: " + zip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHeader(String _city, String _state, String _zip){
        zip     = _zip;
        city    = _city;
        state   = _state;
        header.setText(city +", " + state + " " + zip);
    }

}



