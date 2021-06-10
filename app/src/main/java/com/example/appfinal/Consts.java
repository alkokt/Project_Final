package com.example.appfinal;

import java.util.Locale;

public class Consts {

    public static final String API_KEY = "ec7c6b14753099ace354a3be71323280";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMG_URL = "https://image.tmdb.org/t/p/";

    public static String getLang() {
        switch (Locale.getDefault().toString()) {
            case "in_ID":
                return "id-ID";
            default:
                return "en-US";
        }
    }

}
