package com.acerolla.application.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.acerolla.application.R;
import com.acerolla.application.model.RecipesModel;
import com.acerolla.application.model.dataclasses.RecipeData;
import com.acerolla.application.presenter.RecipesPresenter;

import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by Acerolla (Evgeniy Solovev) on 29.03.2018.
 */
public class RecipesActivity extends AppCompatActivity {

    private RecipesPresenter mPresenter;
    private Realm mRealm;       //??? где realm должен быть???!!!

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mPbLoading;
    private TextView mTvNoRecipes;
    private RecyclerView mRvRecipes;
    private RecipesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.viewIsReadyToDie();
    }

    private void init(){
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mPbLoading = findViewById(R.id.pb_loading);
        mTvNoRecipes = findViewById(R.id.tv_no_recipes);
        mRvRecipes = findViewById(R.id.rv_recipes);

        mRvRecipes.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvRecipes.setLayoutManager(mLayoutManager);
        mRvRecipes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (((LinearLayoutManager)mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                if (!mLoading && totalItemCount <= lastVisibleItem + 10) {
                    mLoading = true;
                    mPresenter.scrollToBottom();
                }
            }
        });
        mAdapter = new RecipesAdapter();
        mRvRecipes.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        mRealm = Realm.getDefaultInstance();

        mPresenter = new RecipesPresenter(
                new RecipesModel(mRealm),
                this,
                mRealm);
        mPresenter.viewIsReady();
    }

    public void reloadRecipes(List<RecipeData> recipes) {
        if (recipes.size() == 0) {
            mPbLoading.setVisibility(View.GONE);
            mTvNoRecipes.setVisibility(View.VISIBLE);
        }
        mSwipeRefreshLayout.setRefreshing(false);
        mPbLoading.setVisibility(View.GONE);
        mTvNoRecipes.setVisibility(View.GONE);
        mRvRecipes.setVisibility(View.VISIBLE);
        mAdapter.setRecipes(recipes);
    }

    public void showFailureMessage(int responseCode, String responseMessage) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(String.format(Locale.getDefault(), "Error code: %1$s", responseCode))
                .setMessage(responseMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    public void loadMoreRecipes(List<RecipeData> recipes) {
        mLoading = false;
        mAdapter.addData(recipes);
    }

    private void refresh() {
        mRvRecipes.setVisibility(View.GONE);
        mTvNoRecipes.setVisibility(View.GONE);
        mPresenter.viewIsReady();
    }

}
