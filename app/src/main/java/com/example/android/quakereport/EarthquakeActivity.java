
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    private EarthquakeAdapter mAdapter;
    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mAdapter =new EarthquakeAdapter(this , new ArrayList<Earthquake>());



        class EarthquakeAsyncTask extends AsyncTask<String , Void , List<Earthquake>>{
            protected List<Earthquake> doInBackground(String... urls){
                if (urls.length < 1 || urls[0] == null) {
                    return null;
                }

                List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
                return result;

            }
            protected void onPostExecute(List<Earthquake> data){
                mAdapter.clear();
                if (data != null && !data.isEmpty()) {
                    mAdapter.addAll(data);
                }
            }

        }




        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websieIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(websieIntent);
            }
        });

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }
}
