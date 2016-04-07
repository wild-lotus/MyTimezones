package com.carlosgines.mytimezones.presentation.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.carlosgines.mytimezones.R;
import com.carlosgines.mytimezones.domain.models.Timezone;
import com.carlosgines.mytimezones.presentation.di.DaggerActivityComponent;
import com.carlosgines.mytimezones.presentation.presenters.SigninPresenter;
import com.carlosgines.mytimezones.presentation.presenters.TzListPresenter;
import com.carlosgines.mytimezones.presentation.presenters.TzListView;
import com.carlosgines.mytimezones.presentation.views.adapters.TzListViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TzListActivity extends BaseActivity implements TzListView {

    // ========================================================================
    // Member variables
    // ========================================================================

    @Inject
    TzListPresenter mPresenter;

    @Bind(android.R.id.list)
    ListView mTzListView;

    /**
     * Adapter of the timezones list view.
     */
    private TzListViewAdapter mAdapter;


    // ========================================================================
    // Activity lifecycle methods
    // ========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tz_list);
        ButterKnife.bind(this);

        this.initViews();
        this.initInjector();
        mPresenter.onInit();
    }

    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTzListView.setEmptyView(findViewById(android.R.id.empty));
    }

    private void initInjector() {
        DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build().inject(this);
    }

    // ========================================================================
    // User input
    // ========================================================================

    @OnClick(R.id.fab)
    public void onFabClick(View v) {
        mPresenter.onCreateTzClick();
//        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }

    // ========================================================================
    // Options menu
    // ========================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tz_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                mPresenter.onSignoutClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ========================================================================
    // TzListView implementation
    // ========================================================================

    @Override
    public void showProgress(boolean show) {
    }

    @Override
    public void showNoConnection(boolean show) {
    }

    @Override
    public void render(List<Timezone> timezones) {
        mAdapter = new TzListViewAdapter(this, timezones);
        mTzListView.setAdapter(mAdapter);
    }
}
