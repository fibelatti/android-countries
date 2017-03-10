package com.fibelatti.countries.presentation.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fibelatti.countries.Constants;
import com.fibelatti.countries.R;
import com.fibelatti.countries.data.models.Country;
import com.fibelatti.countries.data.models.Currency;
import com.fibelatti.countries.data.models.Language;
import com.fibelatti.countries.presentation.presenters.CountryDetailPresenter;
import com.fibelatti.countries.presentation.presenters.CountryDetailPresenterView;
import com.fibelatti.countries.presentation.presenters.impl.CountryDetailPresenterImpl;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CountryDetailActivity
        extends AppCompatActivity
        implements CountryDetailPresenterView {
    public static final String TAG = CountryDetailActivity.class.getSimpleName();

    private final String BASE_IMG_URL_250_PX = "https://github.com/hjnilsson/country-flags/blob/master/png250px/";
    private final String BASE_IMG_URL_1000_PX = "https://github.com/hjnilsson/country-flags/blob/master/png1000px/";

    private CountryDetailPresenter countryDetailPresenter;
    private String country;

    private Animator currentAnimator;
    private int shortAnimationDuration;

    //region layout bindings
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_placeholder)
    RelativeLayout layoutPlaceholder;
    @BindView(R.id.layout_content_details)
    ScrollView layoutContent;
    @BindView(R.id.image_view_country_flag)
    ImageView countryFlag;
    @BindView(R.id.image_view_expanded)
    ImageView countryFlagExpanded;
    @BindView(R.id.text_view_country_name)
    TextView countryName;
    @BindView(R.id.text_view_country_region)
    TextView countryRegion;
    @BindView(R.id.text_view_country_capital)
    TextView countryCapital;
    @BindView(R.id.text_view_country_population)
    TextView countryPopulation;
    @BindView(R.id.text_view_country_native_name)
    TextView countryNativeName;
    @BindView(R.id.text_view_country_demonym)
    TextView countryDemonym;
    @BindView(R.id.text_view_country_calling_codes)
    TextView countryCallingCodes;
    @BindView(R.id.text_view_country_currencies)
    TextView countryCurrencies;
    @BindView(R.id.text_view_country_languages)
    TextView countryLanguages;
    @BindView(R.id.text_view_country_timezones)
    TextView countryTimezones;
    //endregion

    public static Intent getCallingIntent(Context context, String countryName) {
        Intent intent = new Intent(context, CountryDetailActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_COUNTRY_NAME, countryName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        countryDetailPresenter = CountryDetailPresenterImpl.createPresenter(this);
        countryDetailPresenter.setUp();

        country = fetchDataFromIntent(savedInstanceState);

        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setUpLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countryDetailPresenter.getCountryByName(country);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countryDetailPresenter.tearDown();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.INTENT_EXTRA_COUNTRY_NAME, country);
    }

    private void setUpLayout() {
        setContentView(R.layout.activity_country_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String fetchDataFromIntent(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return savedInstanceState.getString(Constants.INTENT_EXTRA_COUNTRY_NAME);
        } else if (getIntent().hasExtra(Constants.INTENT_EXTRA_COUNTRY_NAME)) {
            return getIntent().getStringExtra(Constants.INTENT_EXTRA_COUNTRY_NAME);
        }

        return null;
    }

    @Override
    public void displayData(Country country) {
        showContentLayout();

        Picasso.with(this)
                .load(BASE_IMG_URL_250_PX + country.getAlpha2Code().toLowerCase() + ".png?raw=true")
                .into(countryFlag);

        Picasso.with(this)
                .load(BASE_IMG_URL_1000_PX + country.getAlpha2Code().toLowerCase() + ".png?raw=true")
                .into(countryFlagExpanded);

        countryName.setText(country.getName());
        countryRegion.setText(country.getRegion());
        countryCapital.setText(country.getCapital());
        countryPopulation.setText(country.getPopulation());
        countryNativeName.setText(country.getNativeName());
        countryDemonym.setText(country.getDemonym());
        countryCallingCodes.setText(android.text.TextUtils.join(", ", country.getCallingCodes()));

        String currencies = "";
        for (Currency currency : country.getCurrencies()) {
            if (!currencies.isEmpty()) currencies += ", ";
            currencies += currency.getName();
            if (currency.getSymbol() != null) currencies += " (" + currency.getSymbol() + ")";
        }
        countryCurrencies.setText(currencies);

        String languages = "";
        for (Language language : country.getLanguages()) {
            if (!languages.isEmpty()) languages += ", ";
            languages += language.getName();
        }
        countryLanguages.setText(languages);

        countryTimezones.setText(android.text.TextUtils.join(", ", country.getTimezones()));
    }

    @Override
    public void handleError(Throwable throwable) {
        showPlaceholderLayout();
        Log.e(getString(R.string.generic_log_error, TAG, "displayData"), throwable.getMessage());
    }

    private void showContentLayout() {
        layoutContent.setVisibility(View.VISIBLE);
        layoutPlaceholder.setVisibility(View.GONE);
    }

    private void showPlaceholderLayout() {
        layoutContent.setVisibility(View.GONE);
        layoutPlaceholder.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.image_view_country_flag)
    public void handlePosterClick() {
        zoomImageFromThumb(countryFlag);
    }

    private void zoomImageFromThumb(final View thumbView) {
        if (currentAnimator != null) currentAnimator.cancel();

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.root_layout).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        countryFlagExpanded.setVisibility(View.VISIBLE);

        countryFlagExpanded.setPivotX(0f);
        countryFlagExpanded.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(countryFlagExpanded, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(countryFlagExpanded, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(countryFlagExpanded, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(countryFlagExpanded,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        final float startScaleFinal = startScale;
        countryFlagExpanded.setOnClickListener(view -> {
            if (currentAnimator != null) currentAnimator.cancel();

            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator
                    .ofFloat(countryFlagExpanded, View.X, startBounds.left))
                    .with(ObjectAnimator
                            .ofFloat(countryFlagExpanded,
                                    View.Y, startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(countryFlagExpanded,
                                    View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(countryFlagExpanded,
                                    View.SCALE_Y, startScaleFinal));
            set1.setDuration(shortAnimationDuration);
            set1.setInterpolator(new DecelerateInterpolator());
            set1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    countryFlagExpanded.setVisibility(View.GONE);
                    currentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    countryFlagExpanded.setVisibility(View.GONE);
                    currentAnimator = null;
                }
            });
            set1.start();
            currentAnimator = set1;
        });
    }

    @OnClick(R.id.button_try_again)
    public void tryReload() {
        countryDetailPresenter.getCountryByName(country);
    }
}
