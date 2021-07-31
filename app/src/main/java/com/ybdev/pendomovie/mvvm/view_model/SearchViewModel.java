package com.ybdev.pendomovie.mvvm.view_model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.model.MovieSearchModel;
import com.ybdev.pendomovie.repository.TMDBRepository;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private static SearchViewModel instance;
    private final TMDBRepository repository = TMDBRepository.getInstance();
    private String query = "";
    private int currentPage = 1;
    private int maxPages = 1;
    private String queryForRelated = "";
    public MutableLiveData<MovieList> movieListViewModel = new MutableLiveData<>();


    private SearchViewModel(){observe();}

    public static SearchViewModel getInstance(){
        if (instance == null)
            instance = new SearchViewModel();
        return instance;
    }

    public void getNewData(){
        if (currentPage <= maxPages && queryForRelated.length() > 1){
            fetchRelatedMovies();
            currentPage++;
        }
    }

    private void observe() {
        repository.getRelatedMovies(queryForRelated, currentPage).observeForever(movieList -> {
            if (movieList != null){
                this.movieListViewModel.setValue(movieList);
            }
        });
    }

    public void fetchRelatedMovies(){
        repository.getRelatedMovies(queryForRelated, currentPage).getValue();
    }

    public MutableLiveData<List<MovieSearchModel.ResultBean>> movieNameSearch(){
        int page = 1;
        return repository.movieNameSearch(page,query);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setQueryForRelated(String queryForRelated) {
        this.queryForRelated = queryForRelated;
        currentPage = 1;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }
}
