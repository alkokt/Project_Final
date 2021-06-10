package com.example.appfinal.UI.adapters.clickListeners;

import com.example.appfinal.data.local.models.FavoriteMovie;
import com.example.appfinal.data.local.models.FavoriteTv;
import com.example.appfinal.data.models.Movie;
import com.example.appfinal.data.models.TvShow;

public interface OnItemClickListener {
    void onClick(TvShow tvShow);
    void onClick(Movie movie);
    void onClick(FavoriteMovie movie);
    void onClick(FavoriteTv tv);
}
