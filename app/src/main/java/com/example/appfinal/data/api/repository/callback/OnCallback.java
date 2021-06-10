package com.example.appfinal.data.api.repository.callback;

import java.util.List;

public interface OnCallback <T>{
    void onSuccess(int page, List<T> list);

    void onFailure(String message);
}
