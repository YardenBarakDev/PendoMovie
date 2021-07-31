package com.ybdev.pendomovie.room_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SingleMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SingleMovieModel singleMovieModel);

    @Delete
    void delete(SingleMovieModel singleMovieModel);

    @Query("SELECT * FROM movie_table")
    LiveData<List<SingleMovieModel>> getAllFavoriteMovies();

}
