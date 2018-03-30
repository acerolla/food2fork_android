package com.acerolla.application.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acerolla.application.R;
import com.acerolla.application.model.dataclasses.RecipeData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Acerolla (Evgeniy Solovev) on 30.03.2018.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<RecipeData> mRecipes;

    RecipesAdapter() {
        mRecipes = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvRecipeImage;
        TextView mTvRecipeTitle;
        TextView mTvRecipePublisher;
        TextView mTvRecipeRating;


        ViewHolder(View itemView) {
            super(itemView);

            mIvRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
            mTvRecipeTitle = itemView.findViewById(R.id.tv_recipe_title);
            mTvRecipePublisher = itemView.findViewById(R.id.tv_recipe_publisher);
            mTvRecipeRating = itemView.findViewById(R.id.tv_recipe_rating);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recipe, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeData recipe = mRecipes.get(position);
        Picasso.get().load(recipe.getImageUrl()).fit().centerCrop().into(holder.mIvRecipeImage);
        holder.mTvRecipeTitle.setText(recipe.getTitle());
        holder.mTvRecipePublisher.setText(recipe.getPublisher());
        holder.mTvRecipeRating.setText(String.format(Locale.getDefault(),
                "Rating: %1$,.2f", recipe.getSocialRank()));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setRecipes(List<RecipeData> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public void addData(List<RecipeData> recipes) {
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void notifyRemoveEach() {
        for (int i = 0; i < mRecipes.size() + 1; i++) {
            notifyItemRemoved(i);
        }
    }

    public void notifyAddEach() {
        for (int i = 0; i < mRecipes.size() + 1; i++) {
            notifyItemInserted(i);
        }
    }

}
