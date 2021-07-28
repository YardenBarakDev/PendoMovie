package com.ybdev.pendomovie.mvvm.view.fragments;


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
import com.ybdev.pendomovie.util.MyConstants;

public class FragmentMovieDetails extends Fragment {

    protected View view;
    private MaterialToolbar fragmentMovieDetails_topAppBar;
    private ImageView movieDetailsFragment_image;
    private TextView movieDetailsFragment_movieTitle;
    private TextView movieDetailsFragment_movieOverview;

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
        fragmentMovieDetails_topAppBar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(FragmentMovieDetails.this).popBackStack();
                });
    }


    private void showMovieDetails() {
        MovieList.ResultBean movie = getArguments().getParcelable(MyConstants.MOVIE);
        movieDetailsFragment_movieTitle.setText(movie.getTitle());
        setImage(movie.getPoster_path());
        movieDetailsFragment_movieOverview.setText(movie.getOverview());
    }

    private void setImage(String poster_path) {

        Glide.with(getContext())
                .load("https://image.tmdb.org/t/p/w500"+poster_path)
                .into(movieDetailsFragment_image);

    }

    private void findViews() {
        fragmentMovieDetails_topAppBar = view.findViewById(R.id.fragmentMovieDetails_topAppBar);
        movieDetailsFragment_image = view.findViewById(R.id.movieDetailsFragment_image);
        movieDetailsFragment_movieTitle = view.findViewById(R.id.movieDetailsFragment_movieTitle);
        movieDetailsFragment_movieOverview = view.findViewById(R.id.movieDetailsFragment_movieOverview);

    }


}