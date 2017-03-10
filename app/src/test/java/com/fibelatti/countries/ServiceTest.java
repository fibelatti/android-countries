package com.fibelatti.countries;

import com.annimon.stream.Stream;
import com.fibelatti.countries.data.models.Country;
import com.fibelatti.countries.data.remote.impl.CountryRepositoryRemoteImpl;
import com.fibelatti.countries.data.repository.CountryRepository;
import com.fibelatti.countries.data.repository.impl.CountryRepositoryImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ServiceTest {
    CountryRepository countryRepository;

    @Before
    public void setUp() throws Exception {
        // Override RxJava schedulers
        RxJavaHooks.setOnIOScheduler(scheduler -> Schedulers.immediate());

        RxJavaHooks.setOnComputationScheduler(scheduler -> Schedulers.immediate());

        RxJavaHooks.setOnNewThreadScheduler(scheduler -> Schedulers.immediate());

        // Override RxAndroid schedulers
        final RxAndroidPlugins rxAndroidPlugins = RxAndroidPlugins.getInstance();
        rxAndroidPlugins.registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        countryRepository = new CountryRepositoryImpl(new CountryRepositoryRemoteImpl());
    }

    @After
    public void tearDown() throws Exception {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();

        countryRepository = null;
    }

    @Test
    public void getAllCountries() throws Exception {
        // Adding sleep to avoid HTTP 429 when running all tests
        Thread.sleep(3000);

        TestSubscriber<List<Country>> testSubscriber = new TestSubscriber<>();

        countryRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        assertEquals(testSubscriber.getValueCount() > 0, true);

        Stream.of(testSubscriber.getOnNextEvents())
                .forEach(this::assertCountries);
    }

    private void assertCountries(List<Country> countries) {
        for (Country country : countries) {
            assertFalse(country.getName().isEmpty());
            assertFalse(country.getAlpha2Code().isEmpty());
        }
    }
}
