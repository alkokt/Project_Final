package com.example.appfinal.data.api.repository.callback;


public interface OnDetailCallback <T> {
    void onSuccess(T media, String message);

    void onFailure(String message);
}
