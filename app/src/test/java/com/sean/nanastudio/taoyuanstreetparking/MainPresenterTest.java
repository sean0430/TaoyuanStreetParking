package com.sean.nanastudio.taoyuanstreetparking;

import android.location.Location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * TaoyuanStreetParking
 * Created by Sean on 2016/7/26上午9:46.
 */
public class MainPresenterTest {

    private MainModel model;
    private MainView view;
    private MainPresenter presenter;


    @Before
    public void setUp() throws Exception {

        model = Mockito.mock(MainModel.class);
        view = Mockito.mock(MainView.class);
        presenter = new MainPresenterImpl(view, model);

    }

    @Test
    public void testOnCreate() throws Exception {

        presenter.onCreate();

        Mockito.verify(view).setLayoutContentView();
        Mockito.verify(view).setActionBar();
        Mockito.verify(view).getStreetParkingInfosFromAPI();

    }

    @Test
    public void testGetStreetParkingInfos() throws Exception {

        presenter.getStreetParkingInfos();

        Mockito.verify(model).getStreetParkingInfos();

    }

    @Test
    public void testSearch() throws Exception {

        List<StreetParkingInfo> expected = new ArrayList<>();

        List<StreetParkingInfo> actual = model.getSearchStreetParkingInfos("");

        Assert.assertEquals(expected, actual);

    }


    @Test
    public void testRefresh() throws Exception {

        presenter.refresh();

        Mockito.verify(view).setRvStreetParking(Mockito.anyListOf(StreetParkingInfo.class));

    }

    @Test
    public void testClearQueryStr() throws Exception {

        presenter.clearQueryStr();

        Mockito.verify(model).setQueryStr(Mockito.anyString());

    }

    @Test
    public void testOnStart() throws Exception {

        presenter.onStart();

        Mockito.verify(view).connectGoogleApiClient();

    }

    @Test
    public void testOnStop() throws Exception {

        presenter.onStop();

        Mockito.verify(view).disconnectGoogleApiClient();

    }

    @Test
    public void testGetLocationWithLocation() throws Exception {


        Location location = Mockito.mock(Location.class);
        presenter.getLocation(location);

        Mockito.verify(model).getGeocodeRoadName(location);

    }

    @Test
    public void testGetLocationViewNoLocation() throws Exception {

        presenter.getLocation(null);

        Mockito.verify(view).checkLocationPermission();
        Mockito.verify(view).initialGoogleApiClient();
        Mockito.verify(view).connectGoogleApiClient();

    }
}
