package com.ybdev.pendomovie.mvvm.view_model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.repository.TMDBRepository;

import java.util.List;

public class MainScreenViewModel extends ViewModel {

    private static MainScreenViewModel instance;
    TMDBRepository repository = TMDBRepository.getInstance();
    private int page = 0;

    private String category;

    private MainScreenViewModel(){}

    public static MainScreenViewModel getInstance(){
        if (instance == null)
            instance = new MainScreenViewModel();
        return instance;
    }

    public MutableLiveData<List<MovieList.ResultBean>> getMovieList(){
        page++;
        Log.d("jjjj", "page =" + page);
        return repository.getMovieList(page, category);
    }


    public void setPage(int page) {
        this.page = page;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
