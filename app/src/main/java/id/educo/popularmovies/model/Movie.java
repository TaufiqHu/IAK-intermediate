package id.educo.popularmovies.model;

/**
 * Created by Tito on 01/12/17.
 */

public class Movie {
    private int id;
    private  String title;
    private  float popularity;
    private  String posterPath;
    private  String backdropPath;
    private  String overview;
    private  String releaseDate;
    private  int voteCount;
    private  float voteAverate;

    public Movie(String posterPath) {
        this.posterPath = posterPath;
    }

    //    public Movie(int id, String title, float popularity, String posterPath, String backdropPath, String overview, String releaseDate, int voteCount, float voteAverate) {
//        this.id = id;
//        this.title = title;
//        this.popularity = popularity;
//        this.posterPath = posterPath;
//        this.backdropPath = backdropPath;
//        this.overview = overview;
//        this.releaseDate = releaseDate;
//        this.voteCount = voteCount;
//        this.voteAverate = voteAverate;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAverate() {
        return voteAverate;
    }

    public void setVoteAverate(float voteAverate) {
        this.voteAverate = voteAverate;
    }
}
