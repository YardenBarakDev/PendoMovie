package com.ybdev.pendomovie.retrofit;

import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.model.MovieSearchModel;
import com.ybdev.pendomovie.mvvm.model.SingleMovieSearchModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/movie/{category}/")
    Call<MovieList> getMovies(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("3/search/keyword")
    Call<MovieSearchModel> movieNameSearch(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page
            );

    @GET("3/search/movie/")
    Call<MovieList> getRelatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );
}
