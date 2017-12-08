package id.educo.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import id.educo.popularmovies.data.MovieContract;
import id.educo.popularmovies.model.Movie;
import id.educo.popularmovies.utils.NetworkUtils;
import id.educo.popularmovies.utils.RecyclerViewItemClickListener;

public class MainActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;
    @BindView(R.id.loading_bar)
    ProgressBar loadingProgress;

    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter adapter;
    private String sort_by = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new MovieAdapter(this, movieList, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        movieList.clear();
        if (sort_by == "Saved") {
            new GetDataTask().execute();
        } else {
            URL url = NetworkUtils.buildUrl(sort_by);
            new GetDataTask().execute(url);
        }
    }

    private class GetDataTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingProgress.setVisibility(View.VISIBLE);
            rvMovies.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String results = null;
            URL url = urls[0];

            if (sort_by == "saved") {
                results = getDatafromContentProvider();

            } else {
                try {
                    results = NetworkUtils.getResponseFromHttpUrl(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return results;
        }

        private String getDatafromContentProvider() {
            JSONArray movie_list_json_array = new JSONArray();
            Cursor cursor;
            try {
                cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

                //adapted from adonias's answer here. https://stackoverflow.com/questions/13070791/android-cursor-to-jsonarray
                cursor.moveToFirst();
                JSONObject rowObject;
                while (cursor.isAfterLast() == false) {
                    int totalColumn = cursor.getColumnCount();
                    rowObject = new JSONObject(); //i think reuse object is better. so i reuse rowObject variable rather than make another row object var like adonias's answer
                    for (int i = 0; i < totalColumn; i++) {
                        if (cursor.getColumnName(i) != null) {
                            try {
                                rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    movie_list_json_array.put(rowObject);
                    cursor.moveToNext();
                }
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return movie_list_json_array.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (sort_by == "saved") {
                JSONArray result = null;
                try {
                    result = new JSONArray(s);

                    for (int i = 0; i < result.length(); i++) {
                        movieList.add(new Movie(
                                result.getJSONObject(i).getInt("id"),
                                result.getJSONObject(i).getDouble("vote_average"),
                                result.getJSONObject(i).getString("title"),
                                result.getJSONObject(i).getString("poster_path"),
                                result.getJSONObject(i).getString("overview"),
                                result.getJSONObject(i).getString("release_date"),
                                result.getJSONObject(i).getString("backdrop_path"),
                                result.getJSONObject(i).getInt("vote_count"),
                                result.getJSONObject(i).getDouble("popularity")
                        ));
                    }

                    rvMovies.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.GONE);

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONArray result = new JSONObject(s)
                            .getJSONArray("results");

                    for (int i = 0; i < result.length(); i++) {
                        movieList.add(new Movie(
                                result.getJSONObject(i).getInt("id"),
                                result.getJSONObject(i).getDouble("vote_average"),
                                result.getJSONObject(i).getString("title"),
                                result.getJSONObject(i).getString("poster_path"),
                                result.getJSONObject(i).getString("overview"),
                                result.getJSONObject(i).getString("release_date"),
                                result.getJSONObject(i).getString("backdrop_path"),
                                result.getJSONObject(i).getInt("vote_count"),
                                result.getJSONObject(i).getDouble("popularity")
                        ));
                    }

                    rvMovies.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.GONE);

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        Movie movie = movieList.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_INTENT, movie);
        startActivity(intent);

        // Toast.makeText(this, "Posisi: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        return true;
    }

    /**
     * refresh data on menu selected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_popular) {
            sort_by = "popular";
            getSupportActionBar().setTitle("Popular Movie");
            loadData();
            return true;
        } else if (item.getItemId() == R.id.action_top_rated) {
            sort_by = "top_rated";
            getSupportActionBar().setTitle("Top Rated Movie");
            loadData();
            return true;
        } else if (item.getItemId() == R.id.action_saved) {
            sort_by = "saved";
            getSupportActionBar().setTitle("My Favorite Movie");
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
