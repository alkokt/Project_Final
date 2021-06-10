package com.example.appfinal.UI.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.appfinal.ImageSize;
import com.example.appfinal.R;
import com.example.appfinal.UI.adapters.CastAdapter;
import com.example.appfinal.UI.adapters.GenreAdapter;
import com.example.appfinal.data.api.repository.MovieRepository;
import com.example.appfinal.data.api.repository.TvShowRepository;
import com.example.appfinal.data.api.repository.callback.OnCastCallback;
import com.example.appfinal.data.api.repository.callback.OnDetailCallback;
import com.example.appfinal.data.local.models.FavoriteMovie;
import com.example.appfinal.data.local.models.FavoriteTv;
import com.example.appfinal.data.local.service.FavoriteHelper;
import com.example.appfinal.data.models.Cast;
import com.example.appfinal.data.models.Genre;
import com.example.appfinal.data.models.Movie;
import com.example.appfinal.data.models.TvShow;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DetailActivity extends AppCompatActivity {
    private ImageView ivBackdrop;
    private ImageView ivPoster;
    private TextView tvName;
    private RatingBar rbRating;
    private TextView tvFirstAirDate;
    private TextView tvLastAirDate;
    private TextView tvSeason;
    private TextView tvEpisode;
    private TextView tvOverview;
    private TextView tvError;
    private TextView tvLabelFirstAirDate;
    private TextView tvLabelLastAirDate;
    private TextView tvLabelEpisode;
    private TextView tvLabelSeason;
    private TvShowRepository tvRepo;
    private MovieRepository movieRepo;
    private ArrayList<String> genres;
    private RecyclerView rvGenre;
    private RecyclerView rvCast;
//    private FavoriteHelper dbService;
    private Realm backgroundThreadRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Realm.init(DetailActivity.this);

        String realmName = "appfinal";
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).
                name(realmName).
                build();

        backgroundThreadRealm = Realm.getInstance(config);

        ivBackdrop = findViewById(R.id.iv_backdrop);
        ivPoster = findViewById(R.id.iv_poster);
        tvName = findViewById(R.id.tv_name);
        rbRating = findViewById(R.id.rb_rating);
        tvFirstAirDate = findViewById(R.id.tv_first_air_date);
        tvLastAirDate = findViewById(R.id.tv_last_air_date);
        tvSeason = findViewById(R.id.tv_season);
        tvEpisode = findViewById(R.id.tv_episode);
        tvOverview = findViewById(R.id.tv_overview);
        tvError = findViewById(R.id.tv_error);
        tvLabelEpisode = findViewById(R.id.label_episode);
        tvLabelSeason = findViewById(R.id.label_season);
        tvLabelFirstAirDate = findViewById(R.id.label_first_air_date);
        tvLabelLastAirDate = findViewById(R.id.label_last_air_date);
        genres = new ArrayList<>();
        rvGenre = findViewById(R.id.rv_genre);
        rvCast = findViewById(R.id.rv_cast);
        tvRepo = TvShowRepository.getInstance();
        movieRepo = MovieRepository.getInstance();
//        dbService = FavoriteHelper.getInstance(backgroundThreadRealm);
    }

    private FavoriteMovie filterFavMovieById(int id) {
        RealmQuery<FavoriteMovie> query = backgroundThreadRealm.where(FavoriteMovie.class);
        query.equalTo("id", getIntent().getIntExtra("ID", 0));

        FavoriteMovie favMovie = query.findFirst();

        return favMovie;
    }

    private FavoriteTv filterFavTvById(int id) {
        RealmQuery<FavoriteTv> query = backgroundThreadRealm.where(FavoriteTv.class);
        query.equalTo("id", getIntent().getIntExtra("ID", 0));

        FavoriteTv favTv = query.findFirst();

        return favTv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_detail_activity, menu);
        // TODO: switch favourite button state
        String selectedFragment = getIntent().getStringExtra("SELECTED_FRAGMENT");
        if (selectedFragment.equals("movie")) {
            FavoriteMovie favMovie = filterFavMovieById(getIntent().getIntExtra("ID", 0));
            if (favMovie != null) {
                menu.findItem(R.id.item_favorite).setIcon(R.drawable.ic_favorite_checked);
            }
        } else {
            FavoriteTv favTv = filterFavTvById(getIntent().getIntExtra("ID", 0));
            if (favTv != null) {
                menu.findItem(R.id.item_favorite).setIcon(R.drawable.ic_favorite_checked);
            }
        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainActivity = new Intent(DetailActivity.this, MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainActivity.putExtra("SELECTED_FRAGMENT", getIntent().getStringExtra("SELECTED_FRAGMENT"));
                startActivity(mainActivity);
                return true;
            case R.id.item_favorite:
                if (getIntent().getStringExtra("SELECTED_FRAGMENT").equals("movie")) {
                    FavoriteMovie favMovie = filterFavMovieById(getIntent().getIntExtra("ID", 0));

                    if (favMovie != null) {
                        // TODO: delete favMovie here
                        backgroundThreadRealm.executeTransaction(transactionRealm -> {
                            favMovie.deleteFromRealm();
                        });
                        item.setIcon(R.drawable.ic_favorite_unchecked);
                    } else {
                        FavoriteMovie movie = new FavoriteMovie();
                        String title = getIntent().getStringExtra("TITLE");
                        String posterPath = getIntent().getStringExtra("POSTER_PATH");
                        int id = getIntent().getIntExtra("ID", 0);
                        movie.setId(id);
                        movie.setTitle(title);
                        movie.setPosterPath(posterPath);

                        backgroundThreadRealm.executeTransaction (transactionRealm -> transactionRealm.insert(movie));
//                    dbService.insertMovie(title, posterPath, id);
                        Log.d("Favorite Movie", movie.getTitle());
                        item.setIcon(R.drawable.ic_favorite_checked);

                    }
                } else {
                    FavoriteTv favTv = filterFavTvById(getIntent().getIntExtra("ID", 0));

                    if (favTv != null) {
                        // TODO: delete favMovie here
                        backgroundThreadRealm.executeTransaction(transactionRealm -> {
                            favTv.deleteFromRealm();
                        });
                        item.setIcon(R.drawable.ic_favorite_unchecked);
                    } else {
                        FavoriteTv tv = new FavoriteTv();
                        String title = getIntent().getStringExtra("TITLE");
                        String posterPath = getIntent().getStringExtra("POSTER_PATH");
                        int id = getIntent().getIntExtra("ID", 0);
                        tv.setId(id);
                        tv.setTitle(title);
                        tv.setPosterPath(posterPath);

                        backgroundThreadRealm.executeTransaction (transactionRealm -> transactionRealm.insert(tv));
//                    dbService.insertMovie(title, posterPath, id);
                        Log.d("Favorite Movie", tv.getTitle());
                        item.setIcon(R.drawable.ic_favorite_checked);
                    }
                }

//                FavoriteMovie favMovie = dbService.findMovieById(getIntent().getIntExtra("ID", 0));

//
                return true;
            case R.id.item_language_setting:
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Integer id = getIntent().getIntExtra("ID", 0);
        Log.d("MEDIA ID", id.toString());
        String selectedFragment = getIntent().getStringExtra("SELECTED_FRAGMENT");
        Log.d("SELECTED FRAGMENT", selectedFragment);

        load(id, selectedFragment);
    }



    private void load(Integer id, String selectedFragment) {
        if (selectedFragment.equals("tv_show")) {
            tvRepo.getModelDetail(id, new OnDetailCallback<TvShow>() {
                @Override
                public void onSuccess(TvShow media, String message) {
                    String imageUri = media.getPosterPath(ImageSize.W154);
                    String backdropUri = media.getBackdropPath(ImageSize.W200);
                    float rating = (float) (media.getVoteAverage() / 2.0);
                    Log.d("RATING", Float.toString(rating));
                    Glide.with(DetailActivity.this)
                            .load(imageUri)
                            .into(ivPoster);
                    Glide.with(DetailActivity.this)
                            .load(backdropUri)
                            .into(ivBackdrop);
                    tvName.setText(media.getName());
                    tvFirstAirDate.setText(media.getFirstAirDate());
                    tvLastAirDate.setText(media.getLastAirDate());
                    tvEpisode.setText(Integer.toString(media.getNumberOfEpisode()));
                    tvOverview.setText(media.getOverview());
                    tvSeason.setText(Integer.toString(media.getNumberOfSeaon()));
                    rbRating.setRating(rating);
                    setGenres(media.getGenres());
                    Log.d("Genre", media.getGenres().get(0).getName());
                    rvGenre.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL, false));
                    rvGenre.setAdapter(new GenreAdapter(genres, DetailActivity.this));
                    loadCastData(id, selectedFragment);
                    setActionBar(media.getName());
                }

                @Override
                public void onFailure(String message) {

                }
            });
        } else {
            movieRepo.getModelDetail(id, new OnDetailCallback<Movie>() {
                @Override
                public void onSuccess(Movie media, String message) {
                    Log.d("MOVIE TITLE", media.getTitle());
                    String imageUri = media.getPosterPath(ImageSize.W154);
                    String backdropUri = media.getBackdropPath(ImageSize.W200);
                    float rating = (float) (media.getVoteAverage() / 2.0);
                    Log.d("RATING", Float.toString(rating));
                    Glide.with(DetailActivity.this)
                            .load(imageUri)
                            .into(ivPoster);
                    Glide.with(DetailActivity.this)
                            .load(backdropUri)
                            .into(ivBackdrop);
                    tvName.setText(media.getTitle());
                    tvOverview.setText(media.getOverview());
                    rbRating.setRating(rating);
                    tvLabelEpisode.setVisibility(View.GONE);
                    tvLabelSeason.setVisibility(View.GONE);
                    tvLabelFirstAirDate.setVisibility(View.GONE);
                    tvLabelLastAirDate.setVisibility(View.GONE);
                    setGenres(media.getGenres());
                    Log.d("Genre", media.getGenres().get(0).getName());
                    rvGenre.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL, false));
                    rvGenre.setAdapter(new GenreAdapter(genres, DetailActivity.this));
                    loadCastData(id, selectedFragment);
                    setActionBar(media.getTitle());
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }
    }

    private void setActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setGenres(List<Genre> genresList){
        for(int i = 0; i< genresList.size(); i++){
            genres.add(genresList.get(i).getName());
        }
    }

    private void loadCastData(int id, String type) {
        if (type.equals("movie")) {
            movieRepo.getCasts(id, new OnCastCallback() {
                @Override
                public void onSuccess(List<Cast> castList, String message) {
                    Log.d("Cast", castList.get(0).getName());
                    rvCast.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL, false));
                    rvCast.setAdapter(new CastAdapter(castList, DetailActivity.this));
                }

                @Override
                public void onFailure(String message) {
                    Log.d("Error Fetching Cast", message);
                }
            });
        } else {
            tvRepo.getCasts(id, new OnCastCallback() {
                @Override
                public void onSuccess(List<Cast> castList, String message) {
                    rvCast.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL, false));
                    rvCast.setAdapter(new CastAdapter(castList, DetailActivity.this));
                }

                @Override
                public void onFailure(String message) {
                    Log.d("Error Fetching Cast", message);
                }
            });
        }
    }
}