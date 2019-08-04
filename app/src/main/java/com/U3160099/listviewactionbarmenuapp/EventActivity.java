package com.U3160099.listviewactionbarmenuapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        //int imageRes = extras.getInt("imageResource");
        String uri = extras.getString("uri");
        Double score = extras.getDouble("score");

        TextView tv = (TextView) findViewById(R.id.textViewLarge);
        tv.setText(title);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        //image.setImageResource(imageRes);
        image.setImageURI(Uri.parse(uri));
        tv = (TextView) findViewById(R.id.textViewSmall);
        tv.setText(Double.toString(score));

    }

}
