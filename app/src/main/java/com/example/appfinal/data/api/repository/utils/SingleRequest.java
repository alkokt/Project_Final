package com.example.appfinal.data.api.repository.utils;

import com.example.appfinal.Consts;
import com.example.appfinal.data.api.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleRequest {
    private static Service service;

    public static Service getInstance() {
        if(service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Consts.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(Service.class);
        }

        return service;
    }

}
