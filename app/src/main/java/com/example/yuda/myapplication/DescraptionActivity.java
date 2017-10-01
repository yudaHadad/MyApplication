package com.example.yuda.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DescraptionActivity extends AppCompatActivity {

    TextView descTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descraption);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        descTV = (TextView) findViewById(R.id.descTV);

        DownloadPoster downloadPoster = new DownloadPoster();
        downloadPoster.execute("http://x-mode.co.il/exam/descriptionMovies/"+id+".txt");
    }


    public class DownloadPoster extends AsyncTask<String, Long, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(DescraptionActivity.this, "jninj", "Loading. Please wait...", false);
        }

        @Override
        protected String doInBackground(String... params)
        {
            StringBuilder response = new StringBuilder();
            try {
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
            try
            {
                JSONObject mainObject = new JSONObject(resultJSON);
                String description = (String)mainObject.get("description");
                descTV.setText(description);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            dialog.dismiss();
            Toast.makeText(DescraptionActivity.this, "finish download!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
