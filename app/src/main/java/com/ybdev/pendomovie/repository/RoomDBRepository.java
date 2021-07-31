package com.ybdev.pendomovie.repository;

import androidx.lifecycle.LiveData;
import com.ybdev.pendomovie.room_db.MyRoomDB;
import com.ybdev.pendomovie.room_db.SingleMovieDao;
import com.ybdev.pendomovie.room_db.SingleMovieModel;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RoomDBRepository {

    private static RoomDBRepository instance;
    private final SingleMovieDao singleMovieDao;

    private RoomDBRepository(){
        MyRoomDB myRoomDB = MyRoomDB.getInstance();
        singleMovieDao = myRoomDB.singleMovieDao();
    }

    public static RoomDBRepository getInstance(){
        if (instance == null)
            instance = new RoomDBRepository();
        return instance;
    }

    public LiveData<List<SingleMovieModel>> getAllFavoriteMovies(){
        return singleMovieDao.getAllFavoriteMovies();
    }

    public void addNewFavorite(SingleMovieModel movie){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> singleMovieDao.insert(movie));
    }

    public void deleteMovieFromFavorite(SingleMovieModel movie){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> singleMovieDao.delete(movie));
    }
}
