package com.example.appfinal.UI.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.example.appfinal.ImageSize;
import com.example.appfinal.R;
import com.example.appfinal.data.api.repository.MovieRepository;
import com.example.appfinal.data.api.repository.TvShowRepository;
import com.example.appfinal.data.api.repository.callback.OnCallback;
import com.example.appfinal.data.api.repository.callback.OnSearchCallback;
import com.example.appfinal.data.local.models.FavoriteMovie;
import com.example.appfinal.data.local.models.FavoriteTv;
import com.example.appfinal.data.models.Movie;
import com.example.appfinal.data.models.TvShow;
import com.example.appfinal.UI.activities.DetailActivity;
import com.example.appfinal.UI.adapters.MainAdapter;
import com.example.appfinal.UI.adapters.clickListeners.OnItemClickListener;

public class MainFragment extends Fragment
        implements OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private TvShowRepository tvRepo;
    private MovieRepository movieRepo;
    private TextView tvError;
    private boolean isFetching;
    private int tvCurPage = 1;
    private int movieCurPage = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_main_activity, menu);
        MenuItem item = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) item.getActionView();
        setSearchView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.language));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_language_setting) {
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        refreshLayout = view.findViewById(R.id.swl_tv_show);
        recyclerView = view.findViewById(R.id.rv_tv_show);
        tvError = view.findViewById(R.id.tv_error);
        tvRepo = TvShowRepository.getInstance();
        movieRepo = MovieRepository.getInstance();
        if (getBundle().equals("tv_show")) {
            getTvRepositoryData("", tvCurPage);

        } else {
            getMovieRepositoryData("", movieCurPage);
        }

        onScrollListener();
        refreshLayout.setOnRefreshListener(this);

        return view;
    }

    private void onScrollListener() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItem = layoutManager.getItemCount();
                int visibleItem = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItem >= totalItem / 2) {
                    if (!isFetching) {
                        isFetching = true;
                        Log.d("TV SHOW PAGE", Integer.toString(tvCurPage));
                        Log.d("MOVIE PAGE", Integer.toString(movieCurPage));
                        // TODO: call repository with incremented page
                        if (getBundle().equals("tv_show")) {
                            tvCurPage++;
                            getTvRepositoryData("", tvCurPage);
                        } else {
                            movieCurPage++;
                            getMovieRepositoryData("", movieCurPage);
                        }
                        isFetching = false;

                    }
                }
            }
        });
    }


    private void getTvRepositoryData(String query, int page) {
        isFetching = true;
        if (query.equals("")) {

            tvRepo.getModel(page, new OnCallback<TvShow>() {
                public void onSuccess(int page, List<TvShow> tvShowList) {
                    // TODO: hide error text
                    tvError.setVisibility(View.GONE);
                    if (adapter == null) {
                        Log.d("Debugging", "adapter:null");
                        adapter = new MainAdapter(tvShowList, null);
                        adapter.setClickListener(MainFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.appendList(tvShowList, null);
                    }
                    tvCurPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(String message) {
                    // TODO: show error text
                    Log.d("Error Fetching", message);
                    tvError.setText(R.string.error);
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        } else {
            tvRepo.search(query, page, new OnSearchCallback<TvShow>() {
                @Override
                public void onSuccess(List<TvShow> tvShowList, String msg, int page) {
                    // TODO: hide error text
                    tvError.setVisibility(View.GONE);
                    if (adapter == null) {
                        adapter = new MainAdapter(tvShowList, null);
                        adapter.setClickListener(MainFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.appendList(tvShowList, null);
                    }
                    tvCurPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(String msg) {
                    // TODO: show error text
                    Log.d("Error Fetching", msg);
                    tvError.setText(R.string.error);
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void getMovieRepositoryData(String query, int page) {
        isFetching = true;
        if (query.equals("")) {
            movieRepo.getModel(page, new OnCallback<Movie>() {
                public void onSuccess(int page, List<Movie> movieList) {
                    // TODO: hide error text
                    tvError.setVisibility(View.GONE);
                    if (adapter == null) {
                        adapter = new MainAdapter(null, movieList);
                        adapter.setClickListener(MainFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.appendList(null, movieList);
                    }
                    movieCurPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(String message) {
                    // TODO: show error text
                    Log.d("Error Fetching", message);
                    tvError.setText(R.string.error);
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        } else {
            movieRepo.search(query, page, new OnSearchCallback<Movie>() {
                @Override
                public void onSuccess(List<Movie> movieList, String msg, int page) {
                    // TODO: hide error text
                    tvError.setVisibility(View.GONE);
                    if (adapter == null) {
                        adapter = new MainAdapter(null, movieList);
                        adapter.setClickListener(MainFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.appendList(null, movieList);
                    }
                    movieCurPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(String msg) {
                    // TODO: show error text
                    Log.d("Error Fetching", msg);
                    tvError.setText(R.string.error);
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private String getBundle() {
        if (getArguments() != null) {
            return getArguments().getString("SORT_BY");
        }
        return "tv_show";
    }

    @Override
    public void onClick(TvShow tvShow) {
        Intent detailActivity = new Intent(getContext(), DetailActivity.class);
        detailActivity.putExtra("ID", tvShow.getId());
        detailActivity.putExtra("SELECTED_FRAGMENT", getBundle());
        detailActivity.putExtra("TITLE", tvShow.getName());
        detailActivity.putExtra("POSTER_PATH", tvShow.getPosterPath(ImageSize.W154));
        startActivity(detailActivity);
    }

    @Override
    public void onClick(Movie movie) {
        Intent detailActivity = new Intent(getContext(), DetailActivity.class);
        detailActivity.putExtra("ID", movie.getId());
        detailActivity.putExtra("TITLE", movie.getTitle());
        detailActivity.putExtra("POSTER_PATH", movie.getPosterPath(ImageSize.W154));
        detailActivity.putExtra("SELECTED_FRAGMENT", getBundle());
        startActivity(detailActivity);
    }

    @Override
    public void onClick(FavoriteMovie movie) {}

    @Override
    public void onClick(FavoriteTv tv) {

    }

    @Override
    public void onRefresh() {
        adapter = null;
        tvCurPage = 1;
        movieCurPage = 1;
        if (getBundle().equals("tv_show")) {
            getTvRepositoryData("", tvCurPage);
        } else {
            getMovieRepositoryData("", movieCurPage);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 0) {
            adapter = null;
            if (getBundle().equals("tv_show")) {
                getTvRepositoryData(s, tvCurPage);
            } else {
                getMovieRepositoryData(s, movieCurPage);
            }
        } else {
            adapter = null;
            if (getBundle().equals("tv_show")) {
                getTvRepositoryData("", tvCurPage);
            } else {
                getMovieRepositoryData("", movieCurPage);
            }
        }
        return true;
    }
}
