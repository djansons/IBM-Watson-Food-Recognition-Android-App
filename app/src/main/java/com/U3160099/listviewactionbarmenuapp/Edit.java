package com.U3160099.listviewactionbarmenuapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Edit extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextTitleScore;
    private ImageView editImageView;
    public CanberraEventDbHelper mydb = new CanberraEventDbHelper(this, "food", null, 7);

    String id;
    String title;
    String uri;
    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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

        if (IBMWatson.isNewFood())
        {
            editTextTitle = (EditText) findViewById(R.id.editTextItemTitle);
            editTextTitleScore = (EditText) findViewById(R.id.editTextItemTitleScore);

            editTextTitle.setText(IBMWatson.getFoodTitle(), TextView.BufferType.EDITABLE);
            editTextTitleScore.setText(IBMWatson.getFoodScore().toString(), TextView.BufferType.EDITABLE);

            editImageView = (ImageView) findViewById(R.id.imageResult);
            editImageView.setImageURI(IBMWatson.getFoodUri());



        }

        else
        {


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

            editTextTitle = (EditText) findViewById(R.id.editTextItemTitle);
            editTextTitle.setText(title);

            editTextTitleScore = findViewById(R.id.editTextItemTitleScore);
            editTextTitleScore.setText(score);

            editImageView = (ImageView) findViewById(R.id.imageResult);
            editImageView.setImageURI(Uri.parse(uri));
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

    public void save(View view) {
        if (IBMWatson.isNewFood())//Create new entry
        {
            mydb.insertEvent(new CanberraEvent(editTextTitle.getText().toString(), IBMWatson.getFoodUri().toString(), IBMWatson.getFoodScore().toString()));
            IBMWatson.setNewFood(false);
        }

        else // update existing entry
        {
            mydb.updateEvent(id, editTextTitle.getText().toString(), editTextTitleScore.getText().toString());
            //mydb.updateEvent(id, new CanberraEvent(id, title, uri, score  ));
        }

        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
        startActivity(intent);
    }

    public void cancel(View view){
        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
        startActivity(intent);
    }



}
