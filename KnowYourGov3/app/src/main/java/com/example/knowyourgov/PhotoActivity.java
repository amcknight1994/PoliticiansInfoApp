package com.example.knowyourgov;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {
    Official thisOfficial = null;
    private ImageView imageView;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        Intent intent = getIntent();
        setTitle("Know Your Government");
        ArrayList<String> location = (ArrayList<String>) intent.getSerializableExtra("location");
        TextView currentlocation = findViewById(R.id.location);
        currentlocation.setText(location.get(0) + ", " + location.get(1) + " " + location.get(2));

        TextView officeName = findViewById(R.id.officetitle);
        TextView name       = findViewById(R.id.name);


        thisOfficial = ( Official) intent.getSerializableExtra("Official");

        officeName.setText(thisOfficial.title);
        name.setText(thisOfficial.name);

        loadRemoteImage(thisOfficial.photoURL);

        ImageView partyImage;
        ImageView officialImage = findViewById(R.id.officialImage);
        officialImage.setImageResource(R.drawable.missing);
        if (thisOfficial.photoURL != null) {
            loadRemoteImage(thisOfficial.photoURL);
        }
        ConstraintLayout x = findViewById(R.id.linearLayout4);
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
    }


    public void openLink (View v)  {
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




    private void loadRemoteImage(final String imageURL) {

        imageView = findViewById(R.id.officialImage);
        Picasso picasso = new Picasso.Builder(this).build();
        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
}
