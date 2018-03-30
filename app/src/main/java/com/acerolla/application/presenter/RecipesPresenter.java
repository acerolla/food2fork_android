package com.acerolla.application.presenter;


import com.acerolla.application.model.RecipesModel;
import com.acerolla.application.model.dataclasses.RecipeData;
import com.acerolla.application.view.RecipesActivity;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Acerolla (Evgeniy Solovev) on 30.03.2018.
 */
public class RecipesPresenter {

    private RecipesModel model;
    private RecipesActivity activity;
    private Realm realm;

    public RecipesPresenter (RecipesModel model, RecipesActivity activity, Realm realm) {
        this.model = model;
        this.activity = activity;
        this.realm = realm;
    }

    public void viewIsReady() {

        model.reloadRecipes(new RecipesModel.LoadRecipesCallback() {
            @Override
            public void onLoad(List<RecipeData> recipes) {
                activity.reloadRecipes(model.getRecipes());
            }

            @Override
            public void onFailure(int responseCode, String responseMessage) {
                activity.showFailureMessage(responseCode, responseMessage);
            }
        });
    }

    public void viewIsReadyToDie() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.copyToRealm(model.getRecipes());
        realm.commitTransaction();
        realm.close();
    }

    public void scrollToBottom() {
        model.loadMoreRecipes(new RecipesModel.LoadRecipesCallback() {
            @Override
            public void onLoad(List<RecipeData> recipes) {
                activity.loadMoreRecipes(recipes);
            }

            @Override
            public void onFailure(int responseCode, String responseMessage) {
                //do nothing, just show previous
            }
        });
    }

}
