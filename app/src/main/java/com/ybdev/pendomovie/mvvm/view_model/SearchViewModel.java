package com.ybdev.pendomovie.mvvm.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.model.MovieSearchModel;
import com.ybdev.pendomovie.mvvm.model.SingleMovieSearchModel;
import com.ybdev.pendomovie.repository.TMDBRepository;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private static SearchViewModel instance;
    TMDBRepository repository = TMDBRepository.getInstance();
    private String query = "   ";
    private final int page = 1;
    private String queryForRelated = "";


    private SearchViewModel(){}

    public static SearchViewModel getInstance(){
        if (instance == null)
            instance = new SearchViewModel();
        return instance;
    }

    public MutableLiveData<List<MovieSearchModel.ResultBean>> movieNameSearch(){
        return repository.movieNameSearch(page,query);
    }

    public MutableLiveData<MovieList> getRelatedMovies(int pageForRelated){
        return repository.getRelatedMovies(queryForRelated, pageForRelated);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setQueryForRelated(String queryForRelated) {
        this.queryForRelated = queryForRelated;
    }
}
