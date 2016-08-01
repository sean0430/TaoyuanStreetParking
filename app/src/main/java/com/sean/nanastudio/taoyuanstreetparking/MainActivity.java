package com.sean.nanastudio.taoyuanstreetparking;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_REQUEST_LOCATION = 10001;

    private MainPresenter presenter;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    private SearchView searchView;
    private SearchManager searchManager;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenterImpl(this, new MainModelImpl());
        presenter.onCreate();

    }

    @Override
    protected void onStart() {
        presenter.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.search(newText);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    getFabLocation().hide();

                } else {
                    getFabLocation().show();
                }
            }
        });


        searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                presenter.clearQueryStr();
                getStreetParkingInfosFromAPI();
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public void setLayoutContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void getStreetParkingInfosFromAPI() {
        new AsyncTask<Void, Void, List<StreetParkingInfo>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected List<StreetParkingInfo> doInBackground(Void... voids) {
                return presenter.getStreetParkingInfos();
            }

            @Override
            protected void onPostExecute(List<StreetParkingInfo> streetParkingInfos) {
                super.onPostExecute(streetParkingInfos);

                hideProgressAndRefresh();

                setRvStreetParking(streetParkingInfos);


            }
        }.execute();
    }

    @Override
    public void setRvStreetParking(List<StreetParkingInfo> streetParkingInfos) {

        getRvStreetParking().setHasFixedSize(true);
        getRvStreetParking().setLayoutManager(new LinearLayoutManager(this));
        getRvStreetParking().setAdapter(new StringParkingAdapter(this, streetParkingInfos));

        // Set listener
        setScrollListener();
        setSwipeRefreshLayout();
    }

    private void setScrollListener() {
        getRvStreetParking().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    getFabLocation().hide();

                } else {
                    getFabLocation().show();


                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        getFabLocation().show();

                        break;
                }


            }
        });
    }

    private void setSwipeRefreshLayout() {
        getSrlStreetParking().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
    }

    @Override
    public void setFabLocationListener() {
        getFabLocation().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation(lastLocation);
            }
        });
    }

    @Override
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.location_permission_title))
                        .setMessage(getResources().getString(R.string.location_permission_message))
                        .setPositiveButton(getResources().getString(R.string.grant), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        PERMISSION_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.deny), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);

            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation(lastLocation);

                } else {
                    Snackbar.make(getContainer(), getResources().getString(R.string.location_permission_deny), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.setting), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    startInstalledAppDetailsActivity(MainActivity.this);
                                }
                            })
                            .show();
                }
                break;
            }
        }
    }

    private void getLocation(final Location location) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                return presenter.getLocation(location);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!"".equals(s.trim())) {
                    if (!presenter.search(s)) {
                        showNoResult();
                    }

                } else {
                    showNoResult();

                }
            }
        }.execute();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }


    // Google api control

    @Override
    public void initialGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void connectGoogleApiClient() {
        if (googleApiClient != null) googleApiClient.connect();
    }

    @Override
    public void disconnectGoogleApiClient() {
        if (googleApiClient != null) googleApiClient.disconnect();
    }


    // Google service callback

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient
                );
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    // Ui control

    @Override
    public void showProgress() {
        getPbLoading().setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressAndRefresh() {
        getPbLoading().setVisibility(View.GONE);
        getSrlStreetParking().setRefreshing(false);
    }

    @Override
    public void showNoResult() {
        Snackbar.make(getContainer(),
                getResources().getString(R.string.no_result), Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.search_by_user_self), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        searchItem.expandActionView();

                        searchView.onActionViewExpanded();
                        searchView.setFocusable(true);
                        searchView.setIconified(false);
                        searchView.setQuery("", false);
                        searchView.requestFocus();
                        searchView.requestFocusFromTouch();
                    }
                })
                .show();
    }


    // Find view by ids..

    private CoordinatorLayout getContainer() {
        return (CoordinatorLayout) findViewById(R.id.container);
    }

    private ProgressBar getPbLoading() {
        return (ProgressBar) findViewById(R.id.pbLoading);
    }

    private SwipeRefreshLayout getSrlStreetParking() {
        return (SwipeRefreshLayout) findViewById(R.id.swlStreetParking);
    }

    private RecyclerView getRvStreetParking() {
        return (RecyclerView) findViewById(R.id.rvStreetParking);
    }

    private FloatingActionButton getFabLocation() {
        return (FloatingActionButton) findViewById(R.id.fabLocation);
    }


}