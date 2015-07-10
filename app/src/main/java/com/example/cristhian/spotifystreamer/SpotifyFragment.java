package com.example.cristhian.spotifystreamer;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
            }
        });


        return rootView;

    }

    public void searchArtist(String artistName) {
        spotifyArtistTask = new SpotifyArtistTask();
        spotifyArtistTask.execute(artistName);
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
