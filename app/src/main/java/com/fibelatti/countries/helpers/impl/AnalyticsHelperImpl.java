package com.fibelatti.countries.helpers.impl;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fibelatti.countries.helpers.AnalyticsHelper;

public class AnalyticsHelperImpl
        implements AnalyticsHelper {
    private final String ANALYTICS_KEY_ALL = "All Countries";

    private final String ANALYTICS_KEY_DETAIL = "Country Detail";
    private final String ANALYTICS_PARAM_DETAIL = "Country Name";

    private final String ANALYTICS_KEY_QUERY = "Country Search";
    private final String ANALYTICS_PARAM_QUERY = "Country Name";

    private final String ANALYTICS_KEY_REGION = "Region";
    private final String ANALYTICS_PARAM_REGION = "Region Name";


    private static AnalyticsHelperImpl instance;

    private AnalyticsHelperImpl() {
    }

    public static AnalyticsHelperImpl getInstance() {
        if (instance == null) {
            instance = new AnalyticsHelperImpl();
        }
        return instance;
    }

    @Override
    public void fireGetAllEvent() {
        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_ALL));
    }

    @Override
    public void fireDetailEvent(String countryName) {
        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_DETAIL)
                .putCustomAttribute(ANALYTICS_PARAM_DETAIL, countryName));
    }

    @Override
    public void fireQueryEvent(String query) {
        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_QUERY)
                .putCustomAttribute(ANALYTICS_PARAM_QUERY, query));
    }

    @Override
    public void fireRegionEvent(String region) {
        Answers.getInstance().logCustom(new CustomEvent(ANALYTICS_KEY_REGION)
                .putCustomAttribute(ANALYTICS_PARAM_REGION, region));
    }
}
