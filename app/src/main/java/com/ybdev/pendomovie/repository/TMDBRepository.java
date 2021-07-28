package com.ybdev.pendomovie.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.retrofit.ApiInterface;
import com.ybdev.pendomovie.retrofit.MyRetrofit;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBRepository {

    private static TMDBRepository instance;

    private final String API_KEY = "21fd2d263e811750bdb2445fe08d608e";
    private final String LANGUAGE = "en-US";
    public MutableLiveData<List<MovieList.ResultBean>> list = new MutableLiveData<>();
    private ApiInterface apiInterface;


    public static TMDBRepository getInstance(){
        if (instance == null)
            instance = new TMDBRepository();
        return instance;
    }

    private TMDBRepository(){
        apiInterface = MyRetrofit.getInterface();
    }

    public MutableLiveData<List<MovieList.ResultBean>> getMovieList(int page, String category){
        Call<MovieList> call = apiInterface.getMovies(category, API_KEY, LANGUAGE, page);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                if (movieList.getResults() != null) {
                    list.setValue(movieList.getResults());
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return list;
    }
}
