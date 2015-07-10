package com.example.cristhian.spotifystreamer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Cristhian on 6/24/2015.
 */
public class SpotifyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        getFragmentManager().beginTransaction().add(R.id.spoti_container, new SpotifyFragment()).commit();
    }
}
