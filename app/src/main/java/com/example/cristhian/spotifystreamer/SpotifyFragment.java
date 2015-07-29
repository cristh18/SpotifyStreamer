package com.example.cristhian.spotifystreamer;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by Cristhian on 7/5/2015.
 * * A placeholder fragment containing a simple view.
 */
@SuppressLint("ValidFragment")
public class SpotifyFragment extends Fragment {

    SpotifyArtistTask spotifyArtistTask;

    private ListView myListView;
    private EditText searchArtistField;

    private CustomListAdapter customListAdapter;

    private final String LOG_TAG=SpotifyFragment.class.getSimpleName();

    public SpotifyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spotify, container, false);

        searchArtistField = (EditText) rootView.findViewById(R.id.searchArtistField);

        searchArtistField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    searchArtist(searchArtistField.getText().toString());
                }
                return false;
            }
        });

        final List<Artist> artists = new ArrayList<>();

        myListView = (ListView) rootView.findViewById(R.id.listview_spotify);

        customListAdapter = new CustomListAdapter(this.getActivity(), artists);

        // myListView.setAdapter(adapter);
        myListView.setAdapter(customListAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "You click: " + artists.get(i).name, Toast.LENGTH_SHORT).show();
                searchTopTracksByArtist(artists.get(i).id);
            }
        });


        return rootView;

    }

    public void searchArtist(String artistName) {
        spotifyArtistTask = new SpotifyArtistTask();
        spotifyArtistTask.execute(artistName);
    }

    public void searchTopTracksByArtist(String artistId){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try{

            final String FORECAST_BASE_URL = "https://api.spotify.com/v1/artists/" + artistId + "/top-tracks";
            final String COUNTRY = "country";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(COUNTRY, "ES").build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " +  builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);

        }catch(Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            forecastJsonStr = null;
        }finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        //return getWeatherDataFromJson(forecastJsonStr);
    }

    public class SpotifyArtistTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG = SpotifyArtistTask.class.getSimpleName();

        @Override
        protected List<Artist> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            return spotifyTest(params[0]);
        }

        public List<Artist> spotifyTest(String artistName) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = null;
            List<Artist> artists = new ArrayList<>();
            if (artistName != null && artistName != "") {
                results = spotify.searchArtists(artistName);
                if (results != null) {
                    artists = results.artists.items;
                } else {
                    artists = new ArrayList<>();
                }
            }
            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> result) {
            if (result != null) {
                customListAdapter.clear();
                for (Artist a : result) {
                    customListAdapter.add(a);
                }
            }
        }
    }
}
