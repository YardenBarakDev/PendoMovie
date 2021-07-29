package com.ybdev.pendomovie.mvvm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleMovieSearchModel implements Parcelable {

    private String original_title;
    private String overview;
    private float popularity;
    private String poster_path;
    private String release_date;
    private float vote_average;
    private int vote_count;

    public SingleMovieSearchModel() {
    }

    protected SingleMovieSearchModel(Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        popularity = in.readFloat();
        poster_path = in.readString();
        release_date = in.readString();
        vote_average = in.readFloat();
        vote_count = in.readInt();
    }

    public static final Creator<SingleMovieSearchModel> CREATOR = new Creator<SingleMovieSearchModel>() {
        @Override
        public SingleMovieSearchModel createFromParcel(Parcel in) {
            return new SingleMovieSearchModel(in);
        }

        @Override
        public SingleMovieSearchModel[] newArray(int size) {
            return new SingleMovieSearchModel[size];
        }
    };

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeFloat(popularity);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeFloat(vote_average);
        dest.writeInt(vote_count);
    }
}
