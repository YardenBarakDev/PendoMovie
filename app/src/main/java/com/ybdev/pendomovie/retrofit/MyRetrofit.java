package com.ybdev.pendomovie.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * this class will manage the HTTP GET requests to TMDBRepository using the library Retrofit2
 *
 * this is a singleton class
 */
public class MyRetrofit {

    private final String BASE_URL = "https://api.themoviedb.org/";
    private static Retrofit retrofit;
    private static MyRetrofit instance;

    private MyRetrofit(){
        initRetrofit();
    }

    public static MyRetrofit getInstance() {
        if (instance == null)
            instance = new MyRetrofit();
        return instance;
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiInterface getInterface() {
        return retrofit.create(ApiInterface.class);
    }
}
