package com.acerolla.application.model;

import android.util.Log;

import com.acerolla.application.Food2ForkApplication;
import com.acerolla.application.model.dataclasses.RecipeData;
import com.acerolla.application.model.dataclasses.RecipesData;
import com.acerolla.application.utils.Constants;
import com.acerolla.application.view.RecipesAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Acerolla (Evgeniy Solovev) on 30.03.2018.
 */
public class RecipesModel {

    private List<RecipeData> recipes;
    private int page = 0;

    private Realm realm;

    public RecipesModel(Realm realm) {
        this.realm = realm;
    }

    public List<RecipeData> getRecipes() {
        return recipes;
    }

    public void reloadRecipes(LoadRecipesCallback callback) {
        Log.wtf("WTF", "3");
        page = 0;
        recipes = new ArrayList<>();
        loadMoreRecipes(callback);
    }

    public void loadMoreRecipes(final LoadRecipesCallback callback) {
        Log.wtf("WTF", "4");
        Food2ForkApplication.getApi().getRecipes(Constants.API_KEY, ++page)
                .enqueue(new Callback<RecipesData>() {
                    @Override
                    public void onResponse(Call<RecipesData> call, Response<RecipesData> response) {
                        Log.wtf("WTF", "5");
                        if (response.raw().code() == 200) {
                            if (response.body() != null) {
                                List<RecipeData> loadedRecipes = response.body().getRecipes();
                                recipes.addAll(loadedRecipes);
                                callback.onLoad(loadedRecipes);
                            }
                            return;
                        }

                        callback.onFailure(response.code(), response.message());
                    }

                    @Override
                    public void onFailure(Call<RecipesData> call, Throwable t) {
                        realm.beginTransaction();
                        RealmResults<RecipeData> result = realm.where(RecipeData.class).findAll();
                        realm.commitTransaction();
                        recipes.addAll(result);
                        callback.onLoad(recipes);
                    }
                });
    }



    public interface LoadRecipesCallback{
        void onLoad(List<RecipeData> recipes);
        void onFailure(int responseCode, String responseMessage);
    }
}
