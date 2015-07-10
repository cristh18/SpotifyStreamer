package com.example.cristhian.spotifystreamer;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Cristhian on 7/5/2015.
 */
public class CustomListAdapter extends ArrayAdapter<Artist> {

    private final Activity context;
    //private final List<String> itemname;
    //private final List<String> imgid;
    private final List<Artist> artists;

    public CustomListAdapter(Activity context, List<Artist> artists) {
        super(context, R.layout.artist_list, artists);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.artists = artists;
        // this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.artist_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        if (artists != null) {
            if (artists.size() != 0 && !artists.isEmpty()) {
                // txtTitle.setText(itemname.get(position));
                txtTitle.setText(artists.get(position).name);

                // String url = imgid.get(position);

                //String item = getItem(position);

                if (artists.get(position).images != null) {
                    if (artists.get(position).images.size() != 0 && !artists.get(position).images.isEmpty())
                        try {
                            String item = artists.get(position).images.get(0).url;
                            Picasso.with(rowView.getContext()).load(item).noFade().into(imageView);
                            //String id = artists.get(position).id;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("Error", "Position :" + position);
                        }

                }


            }
        }
        return rowView;

    }
}