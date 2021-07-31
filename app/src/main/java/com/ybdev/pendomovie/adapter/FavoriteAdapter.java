package com.ybdev.pendomovie.adapter;

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
import com.ybdev.pendomovie.room_db.SingleMovieModel;
import com.ybdev.pendomovie.util.MyConstants;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{


    private static List<SingleMovieModel> movieList;
    private final Context context;

    public FavoriteAdapter(List<SingleMovieModel> movieList, Context context){
        FavoriteAdapter.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_cell, parent, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {

        try{
           SingleMovieModel movie = movieList.get(position);

            if (movie.getPoster_path() != null)
                Glide.with(context)
                        .load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path())
                        .into(holder.movie_poster);

           holder.movie_poster.setClipToOutline(true);
           holder.movie_vote_average.setText(movie.getRelease_date());
           holder.movie_name.setText(movie.getOriginal_title());
           holder.movie_overview.setText(movie.getOverview());
        }catch (NullPointerException e){
            Log.d("jjjj", "end of list");
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovieArray(List<SingleMovieModel> movieArray){
        int size = movieList.size() -1;
        movieList.addAll(movieArray);
        notifyItemRangeChanged(size, movieList.size() -1);
    }

    public void clearArray(){
        movieList.clear();
        notifyDataSetChanged();
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
                Navigation.findNavController(view).navigate(R.id.action_global_fragmentMovieDetails, bundle);
            });
        }
    }
}
