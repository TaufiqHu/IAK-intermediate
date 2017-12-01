package id.educo.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.educo.popularmovies.adapter.MovieAdapter;
import id.educo.popularmovies.model.Movie;
import id.educo.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_movies) RecyclerView rvMovies;
    @BindView(R.id.loading_bar) ProgressBar loadingProgress;

    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new MovieAdapter(this, movieList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setAdapter(adapter);


        loadData();
    }

    private void loadData(){
        URL url = NetworkUtils.buildUrl("popular");
        new GetDataTask().execute(url);
    }
    private class GetDataTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingProgress.setVisibility(View.VISIBLE);
            rvMovies.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String results = null;

            try {
                results = NetworkUtils.getResponseFromHttpUrl(url);

            } catch (IOException e){
                e.printStackTrace();
            }



            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Log.e("Result", s);


            try {
                JSONArray result = new JSONObject(s)
                        .getJSONArray("results");

                for (int i = 0; i < result.length(); i++){
                    movieList.add(new Movie(result.getJSONObject(i).getString("poster_path")));
                }

                rvMovies.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();

                Log.e("Data", result.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
