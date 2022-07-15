package com.example.newsapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    // Api Interface

    // Base Url
    String BASE_URL = "https://newsapi.org/v2/";

    // Get Request
    @GET("top-headlines")
    Call<MainNews> getNews(
            // Query
            @Query("country") String country,
            @Query("pageSize") int pagesize,
            @Query("apiKey") String apikey

            );


}
