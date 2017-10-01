package com.example.yuda.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Making the objects to be global
    EditText titleET;
    Button goSearchBtn;
    ListView myListLV;
    ArrayList<MyMovie> allmovies;
    ArrayAdapter<MyMovie> adapter;
    ArrayAdapter<String> listAdapter;
    int id, year;
    String name, category;
    List <String> names;
    AutoCompleteTextView acTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the list view
        myListLV = (ListView) findViewById(R.id.myListLV);

        // init the array lisr
        allmovies = new ArrayList();
        names = new ArrayList<>();

        // calling to download class
        Download download = new Download();
        download.execute("http://x-mode.co.il/exam/allMovies/allMovies.txt");
    }

    //create a class for asyncTask for download
    public class Download extends AsyncTask <String, Long, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "jninj", "Loading. Please wait...", false);
        }

        @Override
        protected String doInBackground(String... params)
        {
            StringBuilder response = new StringBuilder();
            try {
                //getting infermation from web site
                URL webSite = new URL(params[0]);
                URLConnection connection = webSite.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String resultJSON)
        {
            allmovies.clear();
            names.clear();
            try
            {
                JSONObject mainObject = new JSONObject(resultJSON);
                JSONArray resultArray = mainObject.getJSONArray("movies");

                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject currentObject = resultArray.getJSONObject(i);
                    // insert all the data from 
                    id = currentObject.getInt("id");
                    name = currentObject.getString("name");
                    year = currentObject.getInt("year");
                    category = currentObject.getString("category");

                    allmovies.add(new MyMovie(id, name, year, category));
                    names.add(name);
                }
                adapter = new ArrayAdapter<MyMovie>(MainActivity.this, android.R.layout.simple_list_item_1, allmovies);
                myListLV.setAdapter(adapter);

                //Create Array Adapter
                listAdapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.select_dialog_singlechoice, names);
                //Find TextView control
                acTextView= (AutoCompleteTextView) findViewById(R.id.titleET);
                //Set the number of characters the user must type before the drop down list is shown
                acTextView.setThreshold(1);
                //Set the adapter
                acTextView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                myListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MyMovie currentMovie = allmovies.get(position);
                        Intent goToDescraptionActivity = new Intent(MainActivity.this, DescraptionActivity.class);
                        goToDescraptionActivity.putExtra("id", currentMovie.getId());
                        startActivity(goToDescraptionActivity);
                    }
                });
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "finish download!!!", Toast.LENGTH_SHORT).show();
        }
    }
}

