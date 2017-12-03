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
import android.widget.Toast;

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
import id.educo.popularmovies.adapter.TrailerAdapter;
import id.educo.popularmovies.model.Movie;
import id.educo.popularmovies.model.Trailer;
import id.educo.popularmovies.utils.NetworkUtils;
import id.educo.popularmovies.utils.RecyclerViewItemClickListener;

public class DetailActivity extends AppCompatActivity implements RecyclerViewItemClickListener{

    @BindView(R.id.tv_movie_title) TextView movieTitle;
    @BindView(R.id.tv_release_date) TextView releaseDate;
    @BindView(R.id.tv_vote_average) TextView voteAverage;
    @BindView(R.id.tv_overview) TextView overview;
    @BindView(R.id.iv_poster_path) ImageView posterPath;
    @BindView(R.id.iv_backdrop_path) ImageView backdropPath;

    @BindView(R.id.rv_trailers) RecyclerView rvTrailer;
    @BindView(R.id.loading_bar) ProgressBar loadingProgress;

    private List<Trailer> trailerList = new ArrayList<>();
    private TrailerAdapter trailerAdapter;

    public static final String MOVIE_INTENT = "movie.intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra(MOVIE_INTENT);
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvTrailer.setLayoutManager(layoutManager);
        rvTrailer.setAdapter(trailerAdapter);

        loadData(movie.getId());
    }

    private void loadData(int id){
        URL url = NetworkUtils.buildMovieVideoUrl(String.valueOf(id));
        Log.e("Data JSON:", url.toString());
        new getDataTask().execute(url);
    }

    @Override
    public void onItemClicked(int position) {
        Trailer trailer = trailerList.get(position);
        String key = trailer.getKey();
        Toast.makeText(this, "Posisi: " + position + "\nKey: " + key, Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
        // startActivity(intent);
    }

    private class getDataTask extends AsyncTask<URL, Void, String>{

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
            } catch (IOException e){
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
                for (int i = 0; i < result.length(); i++){
                    trailerList.add(new Trailer(
                            result.getJSONObject(i).getString("key"),
                            result.getJSONObject(i).getString("name"),
                            result.getJSONObject(i).getString("id")
                            ));
                }

                rvTrailer.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.GONE);

                trailerAdapter.notifyDataSetChanged();
                Log.i("Data: ", result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
