package com.U3160099.listviewactionbarmenuapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewTitleScore;
    private ImageView resultImageView;
    //String title;
    //double score;

    String id;
    String title;
    String uri;
    String score;

    Boolean editExisitng = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
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

        setTitle("Food Finder");

        textViewTitle = (TextView) findViewById(R.id.textItemTitle);
        textViewTitleScore = (TextView) findViewById(R.id.textItemTitleScore);
        resultImageView = (ImageView) findViewById(R.id.imageResult);
        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

        if (IBMWatson.isNewFood()) {
            textViewTitle.setText(IBMWatson.getFoodTitle());
            textViewTitleScore.setText("Detected Food: " + IBMWatson.getFoodTitle() + "\nDetected Score: " + IBMWatson.getFoodScore());
            resultImageView.setImageURI(IBMWatson.getFoodUri());
        }

        else{

            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    title= null;
                } else {
                    title= extras.getString("title");
                    id= extras.getString("id");
                    uri = extras.getString("uri");
                    score = extras.getString("score");
                }
            } else {
                title= (String) savedInstanceState.getSerializable("title");
                id= (String) savedInstanceState.getSerializable("id");
                uri= (String) savedInstanceState.getSerializable("uri");
                score= (String) savedInstanceState.getSerializable("score");
            }

            textViewTitle.setText(title);
            textViewTitleScore.setText("Detected Food: " + title + "\nDetected Score: " + score);
            resultImageView.setImageURI(Uri.parse(uri));

        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_listview) {
            Intent intent = new Intent(this, ListViewActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_add) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void edit(View view) {
        Intent intent = new Intent(view.getContext(), Edit.class);

        if (IBMWatson.isNewFood()) {
            intent.putExtra("title", IBMWatson.getFoodTitle());
            intent.putExtra("uri", IBMWatson.getFoodUri());
            intent.putExtra("score", IBMWatson.getFoodScore());
        }

        else{
            intent.putExtra("title", title);
            intent.putExtra("uri", uri);
            intent.putExtra("score", score);
            intent.putExtra("id", id);
        }




        startActivity(intent);
    }
}
