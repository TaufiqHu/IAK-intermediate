package id.educo.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.educo.popularmovies.adapter.ReviewAdapter;
import id.educo.popularmovies.adapter.TrailerAdapter;
import id.educo.popularmovies.model.Movie;
import id.educo.popularmovies.model.Review;
import id.educo.popularmovies.model.Trailer;
import id.educo.popularmovies.utils.NetworkUtils;
import id.educo.popularmovies.utils.RecyclerViewItemClickListener;

public class DetailActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.tv_movie_title)
    TextView movieTitle;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    @BindView(R.id.tv_vote_average)
    TextView voteAverage;
    @BindView(R.id.tv_overview)
    TextView overview;
    @BindView(R.id.iv_poster_path)
    ImageView posterPath;
    @BindView(R.id.iv_backdrop_path)
    ImageView backdropPath;

    @BindView(R.id.rv_trailers)
    RecyclerView rvTrailer;
    @BindView(R.id.loading_bar)
    ProgressBar loadingProgress;

    @BindView(R.id.rv_reviews) RecyclerView rvReview;

    private List<Trailer> trailerList = new ArrayList<>();
    private TrailerAdapter trailerAdapter;

    private List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    public static final String MOVIE_INTENT = "movie.intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Movie movie = getIntent().getParcelableExtra(MOVIE_INTENT);
        getSupportActionBar().setTitle(movie.getTitle());

        movieTitle.setText(movie.getTitle());
        releaseDate.setText("Release Date: " + movie.getReleaseDate());
        voteAverage.setText("Rating: " + movie.getVoteAverate() + "/10");
        overview.setText(movie.getOverview());
        Glide.with(this)
                .load(NetworkUtils.buildMoviePosterUrl(movie.getPosterPath()))
                .into(posterPath);
        Glide.with(this)
                .load(NetworkUtils.buildMoviePosterUrl(movie.getBackdropPath()))
                .into(backdropPath);

        trailerAdapter = new TrailerAdapter(this, trailerList, this);
        reviewAdapter = new ReviewAdapter(this, reviewList);

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTrailer.setLayoutManager(layoutManagerHorizontal);
        rvTrailer.setAdapter(trailerAdapter);
        rvTrailer.setNestedScrollingEnabled(true);

        rvReview.setLayoutManager(layoutManagerVertical);
        rvReview.setAdapter(reviewAdapter);
        rvReview.setNestedScrollingEnabled(true);

        loadData(movie.getId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void loadData(int id) {
        URL videoUrl = NetworkUtils.buildMovieVideoUrl(String.valueOf(id));
        URL reviewUrl = NetworkUtils.buildMovieReviewUrl(String.valueOf(id));
        new getDataTrailer().execute(videoUrl);
        new getDataReview().execute(reviewUrl);
    }

    @Override
    public void onItemClicked(int position) {
        Trailer trailer = trailerList.get(position);
        String key = trailer.getKey();
        // Toast.makeText(this, "Posisi: " + position + "\nKey: " + key, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
        startActivity(intent);
    }

    private class getDataTrailer extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingProgress.setVisibility(View.VISIBLE);
            rvTrailer.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;

            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray result = new JSONObject(s)
                        .getJSONArray("results");
                for (int i = 0; i < result.length(); i++) {
                    trailerList.add(new Trailer(
                            result.getJSONObject(i).getString("key"),
                            result.getJSONObject(i).getString("name"),
                            result.getJSONObject(i).getString("id")
                    ));
                }

                rvTrailer.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.GONE);

                trailerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class getDataReview extends AsyncTask<URL, Void, String>{
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;

            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray result = new JSONObject(s)
                        .getJSONArray("results");
                for (int i = 0; i < result.length(); i++) {
                    reviewList.add(new Review(
                            result.getJSONObject(i).getString("author"),
                            result.getJSONObject(i).getString("content")
                    ));
                }
                rvReview.setVisibility(View.VISIBLE);

                reviewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
