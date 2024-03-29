package com.ybdev.pendomovie.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.model.MovieSearchModel;
import com.ybdev.pendomovie.retrofit.ApiInterface;
import com.ybdev.pendomovie.retrofit.MyRetrofit;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * singleton class
 * this class will handle calls to TMDB api
 */
public class TMDBRepository {

    private static TMDBRepository instance;
    private final ApiInterface apiInterface;

    private final String TAG = "TMDBRepositoryLog";
    private final String API_KEY = "21fd2d263e811750bdb2445fe08d608e";
    private final String LANGUAGE = "en-US";

    public MutableLiveData<MovieList> list = new MutableLiveData<>();
    public MutableLiveData<MovieList> relatedMoviesList = new MutableLiveData<>();
    public MutableLiveData<List<MovieSearchModel.ResultBean>> queryList = new MutableLiveData<>();


    public static TMDBRepository getInstance(){
        if (instance == null)
            instance = new TMDBRepository();
        return instance;
    }

    private TMDBRepository(){
        apiInterface = MyRetrofit.getInterface();
    }


    //for main page
    public MutableLiveData<MovieList> getMovieList(int page, String category){
        Call<MovieList> call = apiInterface.getMovies(category, API_KEY, LANGUAGE, page);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
               if (response.body() != null)
                   list.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
        return list;
    }

    //for search page
    public MutableLiveData<MovieList> getRelatedMovies(String query, int page){

        Call<MovieList> call = apiInterface.getRelatedMovies(API_KEY,LANGUAGE, query, page);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                    relatedMoviesList.setValue(response.body());
                Log.d(TAG, "onResponse: ");

            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
        return relatedMoviesList;
    }


    public MutableLiveData<List<MovieSearchModel.ResultBean>> movieNameSearch(int page, String query){
        Call<MovieSearchModel> call = apiInterface.movieNameSearch(API_KEY,query,page);

        call.enqueue(new Callback<MovieSearchModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieSearchModel> call, @NonNull Response<MovieSearchModel> response) {
                MovieSearchModel movieSearchModel = response.body();
                try {
                    if (movieSearchModel.getResults() != null) {
                        queryList.setValue(movieSearchModel.getResults());
                    }
                }catch (Exception e) {
                    Log.d(TAG, e.getLocalizedMessage());
                    }
            }

            @Override
            public void onFailure(@NonNull Call<MovieSearchModel> call, @NonNull Throwable t) {
                Log.d(TAG, t.getLocalizedMessage());            }
        });
        return queryList;
    }
}
