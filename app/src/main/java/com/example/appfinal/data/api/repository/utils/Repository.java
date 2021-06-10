package com.example.appfinal.data.api.repository.utils;

import com.example.appfinal.data.api.Service;
import com.example.appfinal.data.api.repository.callback.OnCallback;
import com.example.appfinal.data.api.repository.callback.OnCastCallback;
import com.example.appfinal.data.api.repository.callback.OnDetailCallback;
import com.example.appfinal.data.api.repository.callback.OnSearchCallback;

public abstract class Repository<T> {
    protected Service service;

    protected abstract void getModel(int page, final OnCallback<T> callback);
    protected abstract void getModelDetail(int id, final OnDetailCallback<T> callback);
    protected abstract void search(String query, int page, final OnSearchCallback<T> callback);
    protected abstract void getCasts(int id, OnCastCallback callback);
}
