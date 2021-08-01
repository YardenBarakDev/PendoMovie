package com.ybdev.pendomovie.mvvm.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.model.MovieSearchModel;
import com.ybdev.pendomovie.repository.TMDBRepository;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private static SearchViewModel instance;
    private final TMDBRepository repository = TMDBRepository.getInstance();
    private String queryForPossibleResults = "";
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

    /**
     * decide if new data should be fetched. if the last page was fetched
     * it won't request new data from the api
     */
    public void getNewData(){
        if (currentPage <= maxPages && queryForRelated.length() > 1){
            fetchRelatedMovies();
            currentPage++;
        }
    }

    /**
     * observe data from the repository and update the view
     */
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
        return repository.movieNameSearch(page,queryForPossibleResults);
    }

    public boolean needToFetchNewMatchList(CharSequence s, int possibleMatches, int currentSize, int previousSize){
        return s.length() > 2 && s.length() < 35 &&
                currentSize > previousSize &&
                possibleMatches == 0 &&
                !Character.isWhitespace(s.charAt(s.length() - 1));
    }

    public boolean needToFetchNewMovies(int querySize, String query){
        return querySize > 1 && !query.equals(queryForRelated);
    }

    /**
     *
     * @param queryForPossibleResults new query for PossibleResults list
     */
    public void setQueryForPossibleResults(String queryForPossibleResults) {
        this.queryForPossibleResults = queryForPossibleResults;
    }

    /**
     *
     * @param queryForRelated new query for movie search
     */
    public void setQueryForRelated(String queryForRelated) {
        this.queryForRelated = queryForRelated;
        currentPage = 1;
    }

    /**
     *
     * @param maxPages number of pages the api returned for the pagination
     */
    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public boolean isLastPage(){
        return currentPage > maxPages;
    }
}
