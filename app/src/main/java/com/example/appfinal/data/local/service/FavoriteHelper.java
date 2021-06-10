package com.example.appfinal.data.local.service;

import android.content.Context;
import android.util.Log;

import com.example.appfinal.UI.activities.DetailActivity;
import com.example.appfinal.data.local.models.FavoriteMovie;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

public class FavoriteHelper {
    private static Realm backgroundThreadRealm;
    private static FavoriteHelper favHelper;

    private FavoriteHelper(Realm realm) {
        backgroundThreadRealm = realm;
    }

    public static FavoriteHelper getInstance(Realm realm) {

        if (favHelper == null) {
            favHelper = new FavoriteHelper(realm);
        }

        return favHelper;
    }

    public FavoriteMovie findMovieById(int id) {
        RealmQuery<FavoriteMovie> query = backgroundThreadRealm.where(FavoriteMovie.class);
                query.equalTo("id", id);
        FavoriteMovie result = query.findFirst();

        return result;
    }

    public void insertMovie(String title, String posterPath, int id) {
        FavoriteMovie movie = new FavoriteMovie();
        movie.setPosterPath(posterPath);
        movie.setTitle(title);
        movie.setId(id);
        backgroundThreadRealm.executeTransaction (transactionRealm -> {
            transactionRealm.insert(movie);
        });
    }

}
