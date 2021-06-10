package com.example.appfinal.data.api.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appfinal.Consts;
import com.example.appfinal.data.api.Service;
import com.example.appfinal.data.api.repository.callback.OnCallback;
import com.example.appfinal.data.api.repository.callback.OnCastCallback;
import com.example.appfinal.data.api.repository.callback.OnDetailCallback;
import com.example.appfinal.data.api.repository.callback.OnSearchCallback;
import com.example.appfinal.data.api.repository.utils.Repository;
import com.example.appfinal.data.api.repository.utils.SingleRequest;
import com.example.appfinal.data.models.Cast;
import com.example.appfinal.data.models.CastResponse;
import com.example.appfinal.data.models.TvShow;
import com.example.appfinal.data.models.TvShowResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowRepository extends Repository<TvShow> {
    private static TvShowRepository repository;

    private TvShowRepository(Service service) {
        this.service = service;
    }

    public static TvShowRepository getInstance() {
        if (repository == null) {
            Service retrofit = SingleRequest.getInstance();
            repository = new TvShowRepository(retrofit);
        }
        return repository;
    }

    public void getModel(int page, final OnCallback<TvShow> callback) {
        service.getTvResults(Consts.API_KEY, Consts.getLang(), page)
                .enqueue(new Callback<TvShowResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getResults() != null) {
                                    Log.d("TV Show Response", response.body().getResults().toString());
                                    callback.onSuccess(response.body().getPage(), response.body().getResults());
                                } else {
                                    callback.onFailure("response.body().getResults() is null");
                                }
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }

    public void getModelDetail(int id, final OnDetailCallback<TvShow> callback) {
        service.getTvDetail(id, Consts.API_KEY, Consts.getLang())
                .enqueue(new Callback<TvShow>() {
                    @Override
                    public void onResponse(@NonNull Call<TvShow> call, @NonNull Response<TvShow> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                callback.onSuccess(response.body(), response.message());
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message() + ", Error Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvShow> call, @NonNull Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }

    public void search(String query, int page, final OnSearchCallback<TvShow> callback) {
        service.searchTV(Consts.API_KEY, query, Consts.getLang(), page)
                .enqueue(new Callback<TvShowResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getResults() != null) {
                                    callback.onSuccess(response.body().getResults(), response.message(), response.body().getPage());
                                } else {
                                    callback.onFailure("No Results");
                                }
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message() + " : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }

    public void getCasts(int tvId, final OnCastCallback callback) {
        service.getCasts(tvId, Consts.API_KEY, Consts.getLang())
                .enqueue(new Callback<CastResponse>() {
                    @Override
                    public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body() != null) {
                                    callback.onSuccess(response.body().getCastList(), response.message());
                                } else {
                                    callback.onFailure("No Casts");
                                }
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message() + " : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<CastResponse> call, Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }
}