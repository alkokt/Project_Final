package com.example.appfinal.UI.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appfinal.ImageSize;
import com.example.appfinal.R;
import com.example.appfinal.UI.activities.DetailActivity;
import com.example.appfinal.UI.adapters.FavoriteMovieAdapter;
import com.example.appfinal.UI.adapters.FavoriteTvAdapter;
import com.example.appfinal.UI.adapters.clickListeners.OnItemClickListener;
import com.example.appfinal.data.local.models.FavoriteMovie;
import com.example.appfinal.data.local.models.FavoriteTv;
import com.example.appfinal.data.models.Movie;
import com.example.appfinal.data.models.TvShow;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FavoriteMovieFragment extends Fragment implements OnItemClickListener {
    private RecyclerView recyclerView;
    private Realm backgroundThreadRealm;
    private FavoriteMovieAdapter adapter;
    private TextView tvError;

    public FavoriteMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Realm.init(getContext());

        String realmName = "final project";
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).
                name(realmName).
                build();

        backgroundThreadRealm = Realm.getInstance(config);

        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        recyclerView = view.findViewById(R.id.rv_favorite);
        tvError = view.findViewById(R.id.tv_error);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        List<FavoriteMovie> favoriteMovies = (List) backgroundThreadRealm.where(FavoriteMovie.class).findAll();
        if (favoriteMovies.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(R.string.no_favorite);
        } else {
            adapter = new FavoriteMovieAdapter(favoriteMovies);
            adapter.setClickListener(FavoriteMovieFragment.this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        List<FavoriteMovie> favoriteMovies = (List) backgroundThreadRealm.where(FavoriteMovie.class).findAll();
        if (favoriteMovies.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(R.string.no_favorite);
        } else {
            adapter = new FavoriteMovieAdapter(favoriteMovies);
            adapter.setClickListener(FavoriteMovieFragment.this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(TvShow tvShow) {

    }

    @Override
    public void onClick(Movie movie) {

    }

    @Override
    public void onClick(FavoriteMovie movie) {
        Intent detailActivity = new Intent(getContext(), DetailActivity.class);
        detailActivity.putExtra("ID", movie.getId());
        detailActivity.putExtra("TITLE", movie.getTitle());
        detailActivity.putExtra("POSTER_PATH", movie.getPosterPath(ImageSize.W154));
        detailActivity.putExtra("SELECTED_FRAGMENT", "movie");
        startActivity(detailActivity);
    }

    @Override
    public void onClick(FavoriteTv tv) {

    }


}
