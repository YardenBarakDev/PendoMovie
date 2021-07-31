package com.ybdev.pendomovie.mvvm.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.view_model.MovieDetailsViewModel;
import com.ybdev.pendomovie.util.MyConstants;

public class FragmentMovieDetails extends Fragment {

    protected View view;
    private MaterialToolbar fragmentMovieDetails_topAppBar;
    private ImageView movieDetailsFragment_image;
    private TextView movieDetailsFragment_movieOverview;
    private TextView movieDetailsFragment_releaseDate;
    private TextView movieDetailsFragment_voteAverage;
    private TextView movieDetailsFragment_totalVotes;
    private MovieList.ResultBean movie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        findViews();
        showMovieDetails();
        setTopBarClickListener();
        return view;
    }

    private void setTopBarClickListener() {
        fragmentMovieDetails_topAppBar.setNavigationOnClickListener(v ->
                NavHostFragment.findNavController(FragmentMovieDetails.this).popBackStack());


        fragmentMovieDetails_topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.addToFavorite){
                MovieDetailsViewModel.getInstance().addMovieToFavorite(movie);
            }
            return true;
        });
    }

    /**
     * fetch the data from the bundle and present it in the view
     */
    @SuppressLint("SetTextI18n")
    private void showMovieDetails() {
        try {
            movie = getArguments().getParcelable(MyConstants.MOVIE);
            fragmentMovieDetails_topAppBar.setTitle(movie.getTitle());
            setImage(movie.getPoster_path());
            movieDetailsFragment_movieOverview.setText(movie.getOverview());
            movieDetailsFragment_releaseDate.setText(movie.getRelease_date());
            movieDetailsFragment_voteAverage.setText("" + movie.getVote_average());
            movieDetailsFragment_totalVotes.setText("" + movie.getVote_count());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    /**
     * this function will present the movie poster from the url using Glide library
     */
    private void setImage(String poster_path) {
        Glide.with(getContext())
                .load("https://image.tmdb.org/t/p/original" + poster_path)
                .into(movieDetailsFragment_image);
    }

    private void findViews() {
        fragmentMovieDetails_topAppBar = view.findViewById(R.id.fragmentMovieDetails_topAppBar);
        movieDetailsFragment_image = view.findViewById(R.id.movieDetailsFragment_image);
        movieDetailsFragment_movieOverview = view.findViewById(R.id.movieDetailsFragment_movieOverview);
        movieDetailsFragment_releaseDate = view.findViewById(R.id.movieDetailsFragment_releaseDate);
        movieDetailsFragment_voteAverage = view.findViewById(R.id.movieDetailsFragment_voteAverage);
        movieDetailsFragment_totalVotes = view.findViewById(R.id.movieDetailsFragment_totalVotes);
    }

}