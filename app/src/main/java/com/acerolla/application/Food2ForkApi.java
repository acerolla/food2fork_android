package com.acerolla.application;

import com.acerolla.application.model.dataclasses.RecipesData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Acerolla (Evgeniy Solovev) on 30.03.2018.
 */
public interface Food2ForkApi {

    @GET("search")
    Call<RecipesData> getRecipes(@Query("key") String apiKey, @Query("page") int page);
}
