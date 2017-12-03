package id.educo.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tito on 01/12/17.
 */

public class Movie implements Parcelable {
    private int id;
    private  double voteAverate;
    private  String title;
    private  String posterPath;
    private  String overview;
    private  String releaseDate;
    private  String backdropPath;
    private  int voteCount;
    private  double popularity;

    public Movie(int id, double voteAverate, String title, String posterPath, String overview, String releaseDate, String backdropPath, int voteCount, double popularity) {
        this.id = id;
        this.voteAverate = voteAverate;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.voteCount = voteCount;
        this.popularity = popularity;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        voteAverate = in.readDouble();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        backdropPath = in.readString();
        voteCount = in.readInt();
        popularity = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverate() {
        return voteAverate;
    }

    public void setVoteAverate(double voteAverate) {
        this.voteAverate = voteAverate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(voteAverate);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(backdropPath);
        parcel.writeInt(voteCount);
        parcel.writeDouble(popularity);
    }
}
