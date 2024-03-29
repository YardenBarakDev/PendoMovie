package com.ybdev.pendomovie.mvvm.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.repository.TMDBRepository;
import com.ybdev.pendomovie.util.MyConstants;
import java.util.ArrayList;

/**
 * Singleton class
 */
public class MainScreenViewModel extends ViewModel {

    private static MainScreenViewModel instance;
    public static TMDBRepository repository = TMDBRepository.getInstance();
    private String category = MyConstants.TOP_RATED;
    private int maxPages = 1;
    private int currentPage = 1;
    private final ArrayList<MovieList.ResultBean> fetchedMovies = new ArrayList<>();
    public MutableLiveData<MovieList> movieListViewModel = new MutableLiveData<>();


    private MainScreenViewModel(){observer();}

    public static MainScreenViewModel getInstance(){
        if (instance == null)
            instance = new MainScreenViewModel();
        return instance;
    }

    public void getNewData(){
        if (currentPage <= maxPages){
            currentPage++;
            fetchNewMovies();
        }
    }

    private void observer(){
        repository.getMovieList(currentPage,category).observeForever(movieList -> {
            if (movieList != null) {
                fetchedMovies.addAll(movieList.getResults());
                this.movieListViewModel.setValue(movieList);
            }
        });
    }
    public void fetchNewMovies(){
        repository.getMovieList(currentPage, category).getValue();
    }

    public void setCategory(String category) {
        this.category = category;
        currentPage = 0;
        fetchedMovies.clear();
    }

    public String getCategory() {
        return category;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public ArrayList<MovieList.ResultBean> getFetchedMovies() {
        return fetchedMovies;
    }

    public int getFetchedSize(){
        return fetchedMovies.size();
    }

    public boolean isLastPage() {
        return currentPage > maxPages;
    }
}
