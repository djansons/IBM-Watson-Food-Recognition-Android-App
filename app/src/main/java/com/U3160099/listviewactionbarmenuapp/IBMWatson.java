package com.U3160099.listviewactionbarmenuapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.android.library.camera.GalleryHelper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class IBMWatson extends AppCompatActivity {



    private static ImageView imageView;



    private TextView textView;

    private static String foodTitle;
    private static Double foodScore;
    private static Uri foodUri;



    private static boolean newFood = false;

    private Uri contentUri;

    private VisualRecognition visualRecognition;
    private CameraHelper cameraHelper;
    private GalleryHelper galleryHelper;
    private File photoFile;
    private final String api_key = "YOUR API KEY HERE";

    public static Double getFoodScore() {
        return foodScore;
    }
    public static String getFoodTitle() {
        return foodTitle;
    }
    public static Uri getFoodUri() {
        return foodUri;
    }
    public static boolean isNewFood()
    { return newFood;
    }
    public static void setNewFood(boolean newFood) {
        IBMWatson.newFood = newFood;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibmwatson);
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

        imageView = (ImageView) findViewById(R.id.imageView2);
        textView = (TextView) findViewById(R.id.textView3);
        cameraHelper = new CameraHelper(this);
        galleryHelper = new GalleryHelper(this);
        IamOptions options = new IamOptions.Builder()
                .apiKey(api_key)
                .build();
        visualRecognition = new VisualRecognition("2018-03-19", options);

    }

    public void capture(View view){
        cameraHelper.dispatchTakePictureIntent();
    }

    public void load(View view){
        galleryHelper.dispatchGalleryIntent();
    }

    public File getPhotoFile() {
        return photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
            final Bitmap photo = cameraHelper.getBitmap(resultCode);

            photoFile = cameraHelper.getFile(resultCode);
            imageView.setImageBitmap(photo);
        }*/

        if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
            final Bitmap photo = cameraHelper.getBitmap(resultCode);
            photoFile = cameraHelper.getFile(resultCode);
//      imageView.setImageBitmap(photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(photoFile);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            imageView.setImageURI(contentUri);
        }


        /*if (requestCode == GalleryHelper.PICK_IMAGE_REQUEST){
            final Bitmap photo = galleryHelper.getBitmap(resultCode, data);
            photoFile = galleryHelper.getFile(resultCode, data);
            imageView.setImageBitmap(photo);
        }*/

        if (requestCode == GalleryHelper.PICK_IMAGE_REQUEST){
            final Bitmap photo = galleryHelper.getBitmap(resultCode, data);
            photoFile = galleryHelper.getFile(resultCode, data);
            //imageView.setImageBitmap(photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(photoFile);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            imageView.setImageURI(contentUri);
        }

        runBackgroundThread();
    }



    private void runBackgroundThread(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                InputStream imagesStream = null;
                try {
                    imagesStream = new FileInputStream(photoFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                        .imagesFile(imagesStream)
                        .imagesFilename(photoFile.getName())
                        .threshold((float) 0.6)
                        .classifierIds(Arrays.asList("food"))
                        .build();
                ClassifiedImages result = visualRecognition.classify(classifyOptions).execute();

                Gson gson = new Gson();
                String json = gson.toJson(result);
                Log.d("json", json);
                String name = null;
                double score = 0;
                String gender = null;
                int age = 0;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("classifiers");
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("classes");
                    JSONObject jsonObject3 = jsonArray2.getJSONObject(0);
                    name = jsonObject3.getString("class");
                    score = jsonObject3.getDouble("score");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String finalName = name;
                final double finalScore = score;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(
                                "Detected Food: " + finalName + "\n" +
                                        "Detected Score: " + finalScore);
                        foodTitle = finalName;
                        foodScore = finalScore;
                        foodUri = contentUri;

                        Intent intent = new Intent(getApplicationContext(), Results.class);
                        newFood = true;

                        startActivity(intent);

                    }
                });
            }
        });
    }





}
