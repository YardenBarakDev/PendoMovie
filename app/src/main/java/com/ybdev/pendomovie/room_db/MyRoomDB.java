package com.ybdev.pendomovie.room_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SingleMovieModel.class}, version = 1)
public abstract class MyRoomDB extends RoomDatabase {

    private static MyRoomDB instance;

    public abstract SingleMovieDao singleMovieDao();

    public static synchronized MyRoomDB getInstance(){ return instance;}

    public static MyRoomDB init(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MyRoomDB.class, "PendoMovie").build();
        }
        return instance;
    }

}