package com.fibelatti.countries.helpers;

public interface AnalyticsHelper {
    void fireGetAllEvent();

    void fireDetailEvent(String countryName);

    void fireQueryEvent(String query);

    void fireRegionEvent(String region);
}
