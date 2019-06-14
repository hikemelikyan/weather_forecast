package weatherforcaster.doit.myweatherforcaster.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.adapter.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
        , GoogleApiClient.ConnectionCallbacks, LocationListener {

    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private int SETTINGS_ACTIVITY_REQUEST = 1;
    private Location mLocation;
    private GoogleApiClient mApiClient;
    private static final int GOOGLE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;

    private ArrayList<String> mPermissionsToRequest;
    private ArrayList<String> mPermissionsRejected = new ArrayList<>();
    private ArrayList<String> mPermissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 7878;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();

        mPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        mPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        mPermissionsToRequest = permissionsToRequest(mPermissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsToRequest.size() > 0) {
                requestPermissions(mPermissionsToRequest.toArray(new String[mPermissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
            }
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mApiClient!=null)
            mApiClient.connect();
    }

    private boolean checkPlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if(resultCode != ConnectionResult.SUCCESS){
            if(googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, GOOGLE_RESOLUTION_REQUEST);
            }else finish();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mApiClient != null && mApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient,this);
            mApiClient.disconnect();
        }
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) result.add(perm);
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void InitView() {
        mPager = findViewById(R.id.id_view_pager);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);

        Toolbar toolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);

        TabLayout mTabLayout = findViewById(R.id.id_tab_layout);
        mTabLayout.setupWithViewPager(mPager);
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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
