package com.ybdev.pendomovie.mvvm.view_model;

import androidx.lifecycle.LiveData;
import com.ybdev.pendomovie.repository.RoomDBRepository;
import com.ybdev.pendomovie.room_db.SingleMovieModel;
import java.util.List;

public class FavoriteViewModel {

    private static FavoriteViewModel instance;
    public LiveData<List<SingleMovieModel>> favoriteMoviesList = RoomDBRepository.getInstance().getAllFavoriteMovies();

    private FavoriteViewModel(){}

    public static FavoriteViewModel getInstance(){
        if (instance == null)
            instance = new FavoriteViewModel();
        return instance;
    }

    /**
     *delete movie from Room db.
     */
    public void deleteMovieFromFavorites(SingleMovieModel singleMovieModel){
        RoomDBRepository.getInstance().deleteMovieFromFavorite(singleMovieModel);
    }
}
