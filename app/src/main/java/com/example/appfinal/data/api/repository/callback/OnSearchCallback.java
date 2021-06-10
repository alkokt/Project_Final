package com.example.appfinal.data.api.repository.callback;


import java.util.List;


public interface OnSearchCallback <T> {
    void onSuccess(List<T> list, String msg, int page);

    void onFailure(String msg);
}
