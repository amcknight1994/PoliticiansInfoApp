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

public class getOfficialInfo extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    JSONObject normalized;
    private static final String DATA_URL =
            "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyA0y-7pLPUtoJME0najH7SFE2fvMnur2xU&address=";


    private static final String TAG = "AsyncSymbolLoader";

    getOfficialInfo(MainActivity ma) {
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
            normalized = x.getJSONObject("normalizedInput");
            JSONArray offices = x.getJSONArray("offices");
            JSONArray officials = x.getJSONArray("officials");

            for (int i = 0; i < offices.length(); i++){
                JSONObject p = offices.getJSONObject(i);
                indices = p.getJSONArray("officialIndices");

               for (int j = 0; j < indices.length(); j++){
                    JSONObject thisOfficial = officials.getJSONObject(indices.getInt(j));
                    Official toAdd = new Official(thisOfficial.getString("name"),p.getString("name"),thisOfficial.getString("party"));
                    if (thisOfficial.has("address")){
                         ArrayList<String> address = new ArrayList<>();
                         JSONObject thisAddress = thisOfficial.getJSONArray("address").getJSONObject(0);
                         String line1 = thisAddress.getString("line1");
                         address.add(line1);
                         if (thisAddress.has("line2")){ String line2 = thisAddress.getString("line2");
                         address.add(line2);}
                         if (thisAddress.has ("line3")){String line3 = thisAddress.getString("line3");
                         address.add(line3);}
                         if (thisAddress.has("city")) {
                             address.add(thisAddress.getString("city"));}
                         if (thisAddress.has("state")) {
                             address.add(thisAddress.getString("state"));}
                         if (thisAddress.has("zip")) {
                             address.add(thisAddress.getString("zip"));}
                         toAdd.setAddress(address);
                    }
                    if (thisOfficial.has("emails")){
                         String email = thisOfficial.getJSONArray("emails").getString(0);
                         toAdd.setEmail(email);
                    }
                    if (thisOfficial.has("phones")) {
                        toAdd.setPhones(thisOfficial.getJSONArray("phones").getString(0));
                    }
                   if (thisOfficial.has("urls")) {
                       toAdd.setWebsite(thisOfficial.getJSONArray("urls").getString(0));
                   }
                   if (thisOfficial.has("photoUrl")) {
                       toAdd.setPhotoURL(thisOfficial.getString("photoUrl"));
                   }
                   if (thisOfficial.has("channels")) {
                       JSONArray channels = thisOfficial.getJSONArray("channels");
                       for (int z = 0; z < channels.length(); z++){
                           JSONObject channel = channels.getJSONObject(z);

                           if (channel.getString("type").equals("Facebook")){
                               toAdd.setFacebook(channel.getString("id"));
                           }
                           if (channel.getString("type").equals("GooglePlus")){
                               toAdd.setgplus(channel.getString("id"));
                           }
                           if (channel.getString("type").equals("YouTube")){
                               toAdd.setYoutube(channel.getString("id"));
                           }
                           if (channel.getString("type").equals("Twitter")){
                               toAdd.setTwitter(channel.getString("id"));
                           }
                       }
                   }
                    electionList.add(toAdd);
                }
              }

            return electionList;
            } catch (Exception e) {

            Log.d(TAG, "parseJSONX: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}

