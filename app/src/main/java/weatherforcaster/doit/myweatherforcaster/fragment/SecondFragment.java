package weatherforcaster.doit.myweatherforcaster.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weatherforcaster.doit.myweatherforcaster.Common.Common;
import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.adapter.FiveDayAdapter;
import weatherforcaster.doit.myweatherforcaster.models.FiveDayThreeHourModel.Forecasted;
import weatherforcaster.doit.myweatherforcaster.network.GetConnect;
import weatherforcaster.doit.myweatherforcaster.network.WeatherAPI;


public class SecondFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PAGE_NUMBER = "number";
    private static final String LAT = "lat";
    private static final String LON = "long";
    private static final String UNIT = "unit";
    final String APP_ID = "ead6c284b33ca76b77085fb56365f06d";
    private boolean currentConnection = true;
    private double mLat;
    private double mLon;
    private String mUnits;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedPreferences mShared;
    private FiveDayAdapter mAdapter;

    private RecyclerView mRecycler;


    public static SecondFragment newInstance(int page, double latitude, double longitude) {
        SecondFragment fragment = new SecondFragment();
        Log.d("TESTING","newInstance SecondFragment");
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        args.putDouble(LAT, latitude);
        args.putDouble(LON, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TESTING","onCreate SecondFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TESTING","onStart SecondFragment");
        if (getArguments() != null) {
            mLat = getArguments().getDouble(LAT);
            mLon = getArguments().getDouble(LON);
            mShared = getActivity().getSharedPreferences(Common.SHARED_PREFERANCE_NAME,Context.MODE_PRIVATE);
            mUnits = mShared.getString("unit","");
        }
        OnRequest(String.valueOf(mLat),String.valueOf(mLon),mUnits);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TESTING","onCreateView SecondFragment");
        View mView = inflater.inflate(R.layout.fragment_second, container, false);

        InitView(mView);


        mRecycler.setAdapter(mAdapter);
        return mView;
    }

    private void InitView(View mView){
        Log.d("TESTING","InitView SecondFragment");
        mRecycler = mView.findViewById(R.id.id_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(true);
        mSwipeRefreshLayout = mView.findViewById(R.id.id_swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mAdapter = new FiveDayAdapter(getActivity());
    }

    private void OnRequest(String mLat, String mLon, String mUnits) {
        Log.d("TESTING","OnRequest SecondFragment");
        WeatherAPI api = GetConnect.getInstance().create(WeatherAPI.class);
        Call<Forecasted> call = api
                .getWeatherForFiveDays(mLat, mLon, APP_ID, mUnits);


        call.enqueue(new Callback<Forecasted>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Forecasted> call, @NonNull Response<Forecasted> response) {
                if (response.isSuccessful()) {
                    mAdapter.setData(response.body().getList(), (int) response.body().getCnt());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Forecasted> call, @NonNull Throwable t) {
                Snackbar snackbar;
                if (!Common.checkNetworkConnectionStatus(Objects.requireNonNull(getContext()))) {
                    snackbar = Snackbar.make(mRecycler, "No internet connection.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                    currentConnection = false;
                } else {
                    snackbar = Snackbar.make(mRecycler, "Something went wrong.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TESTING","OnRefresh SecondFragment");
                mSwipeRefreshLayout.setRefreshing(false);
                if (Common.checkNetworkConnectionStatus(Objects.requireNonNull(getContext()))) {
                    OnRequest(String.valueOf(mLat),String.valueOf(mLon),mUnits);
                    if (!currentConnection) {
                        Snackbar snackbar;
                        snackbar = Snackbar.make(mRecycler, "Connection returned.", Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_pos));
                        snackbar.show();
                        currentConnection = true;
                    }
                } else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(mRecycler, "No internet connection.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                }
            }
        }, 2000);
    }
}
