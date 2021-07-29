package com.ybdev.pendomovie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.util.MyConstants;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static List<MovieList.ResultBean> movieList;
    private final Context context;

    public SearchAdapter(List<MovieList.ResultBean> movieList, Context context){
        SearchAdapter.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.movie_cell, parent, false);
        //}
        //else{
        //    view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
       // }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {

        try{
            MovieList.ResultBean movie = movieList.get(position);
            if (movie.getPoster_path() != null)
                Glide.with(context)
                        .load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path())
                        .into(holder.movie_poster);

            holder.movie_poster.setClipToOutline(true);
            holder.movie_vote_average.setText(movie.getVote_average() + "");
            holder.movie_name.setText(movie.getTitle());
            holder.movie_overview.setText(movie.getOverview());
        }catch (NullPointerException e){
            Log.d("jjjj", "end of list");
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovieArray(List<MovieList.ResultBean> movieArray){
        int size = movieList.size() -1;
        movieList.addAll(movieArray);
        movieList.add(null);
        notifyItemChanged(size, movieList.size() -1);
    }

    public void removeData(){
        movieList.clear();
        notifyDataSetChanged();
    }
    public void removeLast(){
        movieList.remove(movieList.size()-1);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView movie_vote_average;
        ImageView movie_poster;
        TextView movie_name;
        TextView movie_overview;

        ViewHolder(View view) {
            super(view);

            movie_vote_average = view.findViewById(R.id.movie_vote_average);
            movie_poster = view.findViewById(R.id.movie_poster);
            movie_name = view.findViewById(R.id.movie_name);
            movie_overview = view.findViewById(R.id.movie_overview);

            view.setOnClickListener(v ->{
                Bundle bundle = new Bundle();
                bundle.putParcelable(MyConstants.MOVIE, movieList.get(getAdapterPosition()));
                Navigation.findNavController(view).navigate(R.id.action_fragmentSearch_to_fragmentMovieDetails, bundle);
            });
        }
    }
}
