package com.ybdev.pendomovie.room_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class SingleMovieModel implements Parcelable {

    @PrimaryKey
    private int id;
    private String poster_path;
    private String overview;
    private String release_date;
    private String original_title;

    public SingleMovieModel() {
    }

    public SingleMovieModel(int id, String poster_path, String overview, String release_date, String original_title) {
        this.id = id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.original_title = original_title;
    }

    protected SingleMovieModel(Parcel in) {
        id = in.readInt();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        original_title = in.readString();
    }

    public static final Creator<SingleMovieModel> CREATOR = new Creator<SingleMovieModel>() {
        @Override
        public SingleMovieModel createFromParcel(Parcel in) {
            return new SingleMovieModel(in);
        }

        @Override
        public SingleMovieModel[] newArray(int size) {
            return new SingleMovieModel[size];
        }
    };

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

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(original_title);
    }
}
