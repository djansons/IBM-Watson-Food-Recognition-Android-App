package com.U3160099.listviewactionbarmenuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class ListViewActivity extends AppCompatActivity {

    public ArrayList<CanberraEvent> events = new ArrayList<CanberraEvent>();
    public CanberraEventDbHelper mydb = new CanberraEventDbHelper(this, "food", null, 7);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
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
        //events.add(new CanberraEvent("Night Noodle Market",R.drawable.noodle_market, new Date(2019,3,1)));
        //events.add(new CanberraEvent("Lights! Canberra! Actions!", R.drawable.lights_canberra_actions, new Date(2019,3,8)));
        //events.add(new CanberraEvent("Craft Beer & Cider Festival", R.drawable.craft_beer_cider, new Date(2019,3,16)));

        events = mydb.getAllEvents();
        if (events.isEmpty()) {
            //mydb.insertEvent(new CanberraEvent("Night Noodle Market", R.drawable.noodle_market,1.0));
            //mydb.insertEvent(new CanberraEvent("Lights! Canberra! Actions!", R.drawable.lights_canberra_actions, new Date(2019,3,8)));
            //mydb.insertEvent(new CanberraEvent("Craft Beer & Cider Festival", R.drawable.craft_beer_cider, new Date(2019,3,16)));
            events = mydb.getAllEvents();
        }


        //ArrayAdapter<CanberraEvent> adapter = new ArrayAdapter<CanberraEvent>(
          //      this, android.R.layout.simple_list_item_1, events);
        CanberraEventAdapter adapter = new CanberraEventAdapter(
                this, R.layout.my_listview_item, events);


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CanberraEvent cbrevent = events.get(position);
                        Intent intent = new Intent(view.getContext(), Results.class);
                        intent.putExtra("id", cbrevent.getId());
                        intent.putExtra("title", cbrevent.getTitle());
                        intent.putExtra("uri", cbrevent.getUri());
                        intent.putExtra("score", cbrevent.getScore());
                        startActivity(intent);
                    }
                });

    }

    public void addImage(View v) {
        Intent intent = new Intent(getApplicationContext(), IBMWatson.class);
        startActivity(intent);
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




}
