package weatherforcaster.doit.myweatherforcaster.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.adapter.TodayAdapter;
import weatherforcaster.doit.myweatherforcaster.models.FiveDayThreeHourModel.Forecasted;
import weatherforcaster.doit.myweatherforcaster.network.GetConnect;
import weatherforcaster.doit.myweatherforcaster.network.WeatherAPI;


public class SecondFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PAGE_NUMBER = "number";
    private int mNumber;
    private static final String ARG_ADDRESS_STRING = "address";
    final String APP_ID = "ead6c284b33ca76b77085fb56365f06d";
    private TextView mPickedLocation;
    private boolean currentConnection = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TodayAdapter mAdapter;

    private RecyclerView mRecycler;


    public static SecondFragment newInstance(int page) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = getArguments().getInt(ARG_PAGE_NUMBER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_second, container, false);

        mRecycler = mView.findViewById(R.id.id_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(true);
        mSwipeRefreshLayout = mView.findViewById(R.id.id_swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mAdapter = new TodayAdapter(getActivity());


        OnRequest();


        mRecycler.setAdapter(mAdapter);
        return mView;
    }

    private void OnRequest() {
        WeatherAPI api = GetConnect.getInstance().create(WeatherAPI.class);
        Call<Forecasted> call = api
                .getWeatherForCelsius("40.298800", "44.580117", APP_ID, "metric");


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
                if (!checkNetworkConnectionStatus()) {
                    snackbar = Snackbar.make(mPickedLocation, "No internet connection.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                    currentConnection = false;
                } else {
                    snackbar = Snackbar.make(mPickedLocation, "Something went wrong.", Snackbar.LENGTH_SHORT);
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
                mSwipeRefreshLayout.setRefreshing(false);
                if (checkNetworkConnectionStatus()) {
                    if (!currentConnection) {
                        OnRequest();
                        Snackbar snackbar;
                        snackbar = Snackbar.make(mPickedLocation, "Connection returned.", Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                        snackbar.show();
                        currentConnection = true;
                    }
                } else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(mPickedLocation, "No internet connection.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                }
            }
        }, 2000);
    }

    protected boolean checkNetworkConnectionStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else return false;
    }

}
