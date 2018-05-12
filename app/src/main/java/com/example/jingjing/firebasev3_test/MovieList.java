package com.example.jingjing.firebasev3_test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MovieList extends ArrayAdapter<Movie> {
    private Activity context;
    private List<Movie> movieList;

    public MovieList(Activity context, List<Movie> movieList) {
        super(context, R.layout.list_layout, movieList);
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflatoer = context.getLayoutInflater();

        View listViewItem = inflatoer.inflate(R.layout.list_layout, null, true);
        TextView textViewCity = (TextView) listViewItem.findViewById(R.id.textViewCity);
        TextView textViewMovie = (TextView) listViewItem.findViewById(R.id.textViewMovie);
        TextView textViewTheater = (TextView) listViewItem.findViewById(R.id.textViewTheater);

        Movie movie = movieList.get(position);
        textViewCity.setText(movie.getCity());
        textViewMovie.setText(movie.getMovie());
        textViewTheater.setText(movie.getTheater());

        return listViewItem;
    }
}
