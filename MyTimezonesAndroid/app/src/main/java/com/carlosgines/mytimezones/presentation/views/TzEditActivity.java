package com.carlosgines.mytimezones.presentation.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.carlosgines.mytimezones.R;
import com.carlosgines.mytimezones.presentation.di.DaggerActivityComponent;
import com.carlosgines.mytimezones.presentation.presenters.TzEditPresenter;
import com.carlosgines.mytimezones.presentation.presenters.TzEditView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Time zone edit Activity serving as TzEditView implementation.
 */
public class TzEditActivity extends BaseActivity implements TzEditView {

    // ========================================================================
    // Member variables
    // ========================================================================

    @Inject
    TzEditPresenter mPresenter;

    // UI references.
    @Bind(R.id.progress)
    View mProgressView;
    @Bind(R.id.main_content)
    View mMainContentView;
    @Bind(R.id.name)
    EditText mNameView;
    @Bind(R.id.city)
    EditText mCityView;
    @Bind(R.id.timeDiff)
    EditText mTimeDiffView;
    @Bind(R.id.action_button)
    Button mActionButton;

    // ========================================================================
    // Activity lifecycle methods
    // ========================================================================

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tz_edit);
        ButterKnife.bind(this);

        this.initViews();
        this.initInjector();
        mPresenter.onInit();
    }

    public void initViews() {
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

    @OnClick(R.id.action_button)
    public void onActionClick() {
        mPresenter.onActionClick(
                mNameView.getText().toString().trim(),
                mCityView.getText().toString().trim(),
                mTimeDiffView.getText().toString().trim()
        );
    }

    // ========================================================================
    // TzEditView implementation
    // ========================================================================

    @Override
    public void showProgress(final boolean show) {
        final int shortAnimTime = getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
        mMainContentView.setVisibility(show ? View.GONE : View.VISIBLE);
        mMainContentView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mMainContentView.setVisibility(
                        show ? View.GONE : View.VISIBLE
                );
            }
        });
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void setViewMode(ViewMode mode) {
        if (mode.equals(ViewMode.CREATE)) {
            mActionButton.setText(R.string.action_create_tz);
        } else {
            mActionButton.setText(R.string.action_edit_tz);
        }
    }

    @Override
    public void resetErrors() {
        mNameView.setError(null);
        mCityView.setError(null);
        mTimeDiffView.setError(null);
    }

    @Override
    public void setEmptyNameError() {
        mNameView.setError(getString(R.string.error_field_required));
        mNameView.requestFocus();
    }

    @Override
    public void setEmptyCityError() {
        mCityView.setError(getString(R.string.error_field_required));
        mCityView.requestFocus();
    }

    @Override
    public void setEmptyTimeDiffError() {
        mTimeDiffView.setError(getString(R.string.error_field_required));
        mTimeDiffView.requestFocus();
    }

    @Override
    public void setInvalidTimeDiffError() {
        mTimeDiffView.setError(getString(R.string.error_invalid_time_diff));
        mTimeDiffView.requestFocus();
    }
}