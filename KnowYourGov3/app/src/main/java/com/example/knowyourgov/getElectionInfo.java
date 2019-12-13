package com.example.knowyourgov;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class getElectionInfo extends AsyncTask<String, Void, String> {
    //call to google elections api

    private MainActivity mainActivity;
    JSONObject normalized;
    private static final String DATA_URL = //change address to get electives
            //address = Street number, City, State, USA ex: 678 South Jason Street, Denver, CO, USA
            "https://www.googleapis.com/civicinfo/v2/voterinfo?address=678%20South%20Jason%20Street%2C%20Denver%2C%20CO%2C%20USA&electionId=2000&key=AIzaSyA0y-7pLPUtoJME0najH7SFE2fvMnur2xU";


    private static final String TAG = "AsyncSymbolLoader";

    getElectionInfo(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPostExecute(String s) {

        ArrayList<Official> electionList = parseJSON(s);
        if (electionList != null)
            mainActivity.updateData(electionList);
        if (normalized != null) {
            try {
                mainActivity.setHeader(normalized.getString("city"),normalized.getString("state"), normalized.getString("zip"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(String... params) {

        Uri dataUri = Uri.parse(DATA_URL + params[0]);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: ResponseCode: " + conn.getResponseCode());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d  (TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }

    private ArrayList<Official> parseJSON(String s) {

        ArrayList<Official> electionList = new ArrayList();
        JSONArray indices;

        try {
            JSONObject x = new JSONObject(s);
            return electionList;
        } catch (Exception e) {

            Log.d(TAG, "parseJSONX: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
