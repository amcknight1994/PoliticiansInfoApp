package com.example.knowyourgov;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        setTitle("Know Your Government");
        TextView textView = findViewById(R.id.InfoLink);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://developers.google.com/civic-information/'> Google Civic Information API </a>";
        textView.setText(Html.fromHtml(text));

    }
}