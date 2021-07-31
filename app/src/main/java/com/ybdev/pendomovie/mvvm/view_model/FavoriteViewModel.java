package com.ybdev.pendomovie.mvvm.view_model;

import androidx.lifecycle.LiveData;
import com.ybdev.pendomovie.repository.RoomDBRepository;
import com.ybdev.pendomovie.room_db.SingleMovieModel;

import java.util.List;

public class FavoriteViewModel {

    private static FavoriteViewModel instance;
    private final RoomDBRepository roomDBRepository = RoomDBRepository.getInstance();
    public LiveData<List<SingleMovieModel>> favoriteMoviesList = RoomDBRepository.getInstance().getAllFavoriteMovies();

    private FavoriteViewModel(){}

    public static FavoriteViewModel getInstance(){
        if (instance == null)
            instance = new FavoriteViewModel();
        return instance;
    }


}
