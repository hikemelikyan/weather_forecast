package weatherforcaster.doit.myweatherforcaster.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker.IntentBuilder;

import java.util.ArrayList;

import weatherforcaster.doit.myweatherforcaster.Common.Common;
import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.adapter.MyFragmentPagerAdapter;
import weatherforcaster.doit.myweatherforcaster.fragment.FirstFragment;
import weatherforcaster.doit.myweatherforcaster.service.NotificationService;

import static com.google.android.gms.location.places.ui.PlacePicker.getPlace;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, FirstFragment.FromFirstListener {

    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private int SETTINGS_ACTIVITY_REQUEST = 1;
    private Location mLocation;
    public GoogleApiClient mApiClient;
    private LocationRequest mLocationRequest;
    private static final long UPDATE_INTERVAL = 2500000;
    private static final long FASTEST_INTERVAL = 2500000;
    private double mLon;
    private double mLat;
    private ArrayList<String> mPermissionsToRequest;
    private ArrayList<String> mPermissionsRejected = new ArrayList<>();
    private ArrayList<String> mPermissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 7878;
    SharedPreferences mSPref;
    SharedPreferences.Editor editor;
    Intent intent;
    private static final int PLACEPICKER_REQUEST = 148;

    //1.Create Init
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TESTING", "onCreate Main");
        mSPref = getSharedPreferences(Common.SHARED_PREFERANCE_NAME, MODE_PRIVATE);
        if (mSPref.getBoolean("first_use", true)) {
            editor = mSPref.edit();
            editor.putBoolean("switch", true);
            editor.putString("unit", "metric");
            editor.putString("frequency", "Once a day");
            editor.putBoolean("first_use", false);
            editor.apply();
            Log.d("TESTING", "LLLLLL");
            intent = new Intent(MainActivity.this, NotificationService.class);
            intent.putExtra("frequency", mSPref.getString("frequency", ""));
            startService(intent);
        }
        InitView();

    }

    //2.start connect
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TESTING", "onStart Main");
        if (mApiClient != null)
            if (!mApiClient.isConnected()) {
                mApiClient.connect();
            }
    }

    //3.connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d("TESTING", "onConnected");
        startLocationUpdate();
    }

    //4.location updated
    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need a permissions to show location!", Toast.LENGTH_SHORT).show();
        }
        Log.d("TESTING", "startLocationUpdate");
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
    }

    //location updated
    @Override
    public void onLocationChanged(Location location) {
        if (mLocation != null) {
            mLat = mLocation.getLatitude();
            mLon = mLocation.getLongitude();
        }
        if(location!=null){

        }
        Log.d("TESTING", "onLocationChanged");
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this, mLat, mLon);
        mPager.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TESTING", "onPause Main");
        if (mApiClient != null && mApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
            mApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d("TESTING", "onRequestPermissionResult");
        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perm : permissions) {
                if (!hasPermission(perm)) {
                    mPermissionsRejected.add(perm);
                }
            }
            if (mPermissionsRejected.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(mPermissionsRejected.get(0))) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("You need to allow this permission")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(mPermissionsRejected
                                                .toArray(new String[mPermissionsRejected.size()]
                                                ), ALL_PERMISSIONS_RESULT);
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create();
                    }
                }
            } else {
                if (mApiClient != null) {
                    Log.d("TESTING", "FUCK");
                    startLocationUpdate();
                }
            }
        }
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        Log.d("TESTING", "permissionsToRequest");
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) result.add(perm);
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        Log.d("TESTING", "hasPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void InitView() {
        Log.d("TESTING", "InitView Main");

        //viewPager
        mPager = findViewById(R.id.id_view_pager);

        //toolbar
        Toolbar toolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);

        //tabLayout
        TabLayout mTabLayout = findViewById(R.id.id_tab_layout);
        mTabLayout.setupWithViewPager(mPager);

        //Google API Client Initialization
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //adding permission in Manifest
        mPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        mPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        mPermissionsToRequest = permissionsToRequest(mPermissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsToRequest.size() > 0) {
                requestPermissions(mPermissionsToRequest.toArray(new String[mPermissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.id_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_ACTIVITY_REQUEST);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("TESTING", "OnActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SETTINGS_ACTIVITY_REQUEST) {
                mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this, mLat, mLon);
                mPager.setAdapter(mAdapter);
            }
            if (requestCode == PLACEPICKER_REQUEST) {
                Place place = getPlace(this, data);
                Log.d("TESTING", "EKAV " + place.getLatLng().longitude);
                mLon = place.getLatLng().longitude;
                mLat = place.getLatLng().latitude;
                Toast.makeText(this, "" + place.getLatLng().latitude + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
                Location location  = new Location("");
                location.setLatitude(mLat);
                location.setLongitude(mLon);
                onLocationChanged(location);
//                mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),this,mLat,mLon);
//                mPager.setAdapter(mAdapter);
//                FirstFragment.newInstance(0,mLat,mLon);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TESTING", "onResume Main");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TESTING", "onStop Main");
    }

    @Override
    public void Clicked(int requestCode) {
        IntentBuilder builder = new IntentBuilder();
        try {
            startActivityForResult(builder.build(MainActivity.this), PLACEPICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TESTING", "onRestart Main");
    }
}
