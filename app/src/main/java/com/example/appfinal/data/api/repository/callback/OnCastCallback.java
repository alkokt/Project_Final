package com.example.appfinal.data.api.repository.callback;

import com.example.appfinal.data.models.Cast;

import java.util.List;

public interface OnCastCallback {
    void onSuccess(List<Cast> castList, String message);
    void onFailure(String message);
}
