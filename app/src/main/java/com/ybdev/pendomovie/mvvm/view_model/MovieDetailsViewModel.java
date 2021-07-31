package com.ybdev.pendomovie.mvvm.view_model;

import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.repository.RoomDBRepository;
import com.ybdev.pendomovie.room_db.SingleMovieModel;

public class MovieDetailsViewModel {

    private static MovieDetailsViewModel instance;
    private final RoomDBRepository roomDBRepository = RoomDBRepository.getInstance();

    private MovieDetailsViewModel(){}

    public static MovieDetailsViewModel getInstance(){
        if (instance == null)
            instance = new MovieDetailsViewModel();
        return instance;
    }

    public void addMovieToFavorite(MovieList.ResultBean movie){
        try{
            SingleMovieModel singleMovieModel = new SingleMovieModel();
            singleMovieModel.setId(movie.getId());
            singleMovieModel.setOriginal_title(movie.getOriginal_title());
            singleMovieModel.setOverview(movie.getOverview());
            singleMovieModel.setPoster_path(movie.getPoster_path());
            singleMovieModel.setRelease_date(movie.getRelease_date());
            roomDBRepository.addNewFavorite(singleMovieModel);
        }catch (Exception e){e.printStackTrace();}
    }

}
