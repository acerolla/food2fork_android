package com.acerolla.application;

import android.app.Application;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Acerolla (Evgeniy Solovev) on 30.03.2018.
 */
public class Food2ForkApplication extends Application {

    private static Food2ForkApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        initApi();
    }

    private void initApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://food2fork.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Food2ForkApi.class);
    }

    public static Food2ForkApi getApi() {
        return api;
    }
}
