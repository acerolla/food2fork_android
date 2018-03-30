package com.acerolla.application.model.dataclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Acerolla (Evgeniy Solovev) on 30.03.2018.
 */
public class RecipesData {

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("recipes")
    @Expose
    private List<RecipeData> recipes;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RecipeData> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeData> recipes) {
        this.recipes = recipes;
    }
}
