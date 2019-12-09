package com.example.knowyourgov;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfficialActivity extends AppCompatActivity {
    Official thisOfficial = null;
    private ImageView imageView;
    ArrayList<String> location;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_activity);
        Intent intent = getIntent();
        setTitle("Know Your Government");

        TextView officeName = findViewById(R.id.officeTitle);
        TextView name       = findViewById(R.id.Name);
        TextView party      = findViewById(R.id.Party);
        TextView email      = findViewById(R.id.emailIn);
        TextView phone      = findViewById(R.id.phoneIn);
        TextView website    = findViewById(R.id.webIn);
        TextView address    = findViewById(R.id.AddressIn);
        TextView address2   = findViewById(R.id.addressIn2);
        TextView address3   = findViewById(R.id.addressIn3);
        TextView CityStateZ = findViewById(R.id.CityStateZip);

        thisOfficial = ( Official) intent.getSerializableExtra("Official");
        location = (ArrayList<String>) intent.getSerializableExtra("location");
        TextView currentlocation = findViewById(R.id.location);
        currentlocation.setText(location.get(0) + ", " + location.get(1) + " " + location.get(2));

        //set Officials info based on data stored in thisOfficial
        officeName.setText(thisOfficial.title);
        name.setText(thisOfficial.name);
        party.setText(thisOfficial.party);
        email.setText(thisOfficial.email);

        if (thisOfficial.phones != null) {
            phone.setText(thisOfficial.phones);
        }
        if (thisOfficial.website != null) {
            website.setText(thisOfficial.website);
        }
        if (thisOfficial.address != null) {
            String csz = "";
            ArrayList<String> addresses = thisOfficial.address;
            address.setText(addresses.get(0));
            address2.setText(addresses.get(2));
            address3.setText(addresses.get(1));
            if ( !addresses.get(3).isEmpty()){
                csz += addresses.get(3) + " ";
            }
            if (addresses.size() > 4){
                csz += addresses.get(4) + " ";
            }
            if (addresses.size() > 5) {
                csz += addresses.get(5) + " ";
            }
            CityStateZ.setText(csz);
        }

        ImageView partyImage;
        ImageView officialImage = findViewById(R.id.officialImage);
        officialImage.setImageResource(R.drawable.missing);
        if (thisOfficial.photoURL != null) {
            loadRemoteImage(thisOfficial.photoURL);
        }
        ScrollView x = findViewById(R.id.linearLayout2);

        if (thisOfficial.party.equals("Democratic Party") || thisOfficial.party.equals("Democratic")){
            partyImage = findViewById(R.id.partyLogo);
            partyImage.setImageResource(R.drawable.dem_logo);

            x.setBackgroundColor(Color.BLUE);
        }
        else if (thisOfficial.party.equals("Republican Party")){
            partyImage = findViewById(R.id.partyLogo);
            partyImage.setImageResource(R.drawable.rep_logo);
            
            x.setBackgroundColor(Color.RED);
        }
        else{
            x.setBackgroundColor(Color.BLACK);
        }

        //social media links
        ImageView facebook  = findViewById(R.id.facebook);
        ImageView twitter   = findViewById(R.id.twitter);
        ImageView gplus     = findViewById(R.id.googlePlus);
        ImageView youtube   = findViewById(R.id.youtube);
        if (thisOfficial.facebook != null){
            facebook.setImageResource(R.drawable.facebook);
        }
        if (thisOfficial.youtube != null){
            youtube.setImageResource(R.drawable.youtube);
        }
        if (thisOfficial.twitter != null){
            twitter.setImageResource(R.drawable.twitter);
        }
        if (thisOfficial.gplus != null){
            gplus.setImageResource(R.drawable.googleplus);
        }
    }
    //called when the party logo is pressed, sends user to party web page
    public void openLink (View v)  {
        ImageView x = (ImageView) v;

        Uri uri = Uri.parse("http://www.google.com");
        if (thisOfficial.party.contains("Democratic")){
            uri = Uri.parse("https://democrats.org ");
        }
        if (thisOfficial.party.contains("Republican")){
            uri = Uri.parse("https://gop.com");
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
    //load headshot of official, or broken image photo if the API doesn't have an image for this person
    private void loadRemoteImage(final String imageURL) {
        imageView = findViewById(R.id.officialImage);
        Picasso picasso = new Picasso.Builder(this).build();
        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
    //next methods open when social media icons are selected
    public void openFB(View v) {
        Uri uri = Uri.parse("http://www.facebook.com/" + thisOfficial.facebook);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void openTwitter(View v) {
        Uri uri = Uri.parse("http://www.twitter.com/" + thisOfficial.twitter);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void openYT(View v) {
        Uri uri = Uri.parse("http://www.youtube.com/" + thisOfficial.youtube);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void openGP(View v) {
        Uri uri = Uri.parse("http://www.googleplus.com/" + thisOfficial.gplus);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openPhotoActivity(View v){
        Intent intent = new Intent (OfficialActivity.this, PhotoActivity.class);
        intent.putExtra("Official", thisOfficial);
        intent.putExtra("location", location);
        startActivity(intent);
    }
}
