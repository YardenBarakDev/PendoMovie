package com.ybdev.pendomovie.room_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * this class represent a table in Room DB
 */
@Entity(tableName = "movie_table")
public class SingleMovieModel {

    @PrimaryKey
    private int id;
    private String poster_path;
    private String overview;
    private String release_date;
    private String title;

    public SingleMovieModel() {
    }

    public SingleMovieModel(int id, String poster_path, String overview, String release_date, String title) {
        this.id = id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
    }

    protected SingleMovieModel(Parcel in) {
        id = in.readInt();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        title = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
