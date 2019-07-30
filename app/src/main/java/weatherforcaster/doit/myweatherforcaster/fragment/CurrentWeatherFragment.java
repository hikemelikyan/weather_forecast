package weatherforcaster.doit.myweatherforcaster.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weatherforcaster.doit.myweatherforcaster.common.Common;
import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.database.SevedInformation;
import weatherforcaster.doit.myweatherforcaster.models.TodayModel.All;
import weatherforcaster.doit.myweatherforcaster.network.GetConnect;
import weatherforcaster.doit.myweatherforcaster.network.WeatherAPI;


public class CurrentWeatherFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private double mLat;
    private double mLon;
    private boolean currentConnection = true;
    private String mUnits;
    final String APP_ID = "ead6c284b33ca76b77085fb56365f06d";
    public static final int REQUEST_FOR_ADDING_PLACE = 74;
    private static final String ARG_PAGE_NUMBER = "number";
    private static final String LAT = "lat";
    private static final String LON = "long";
    private static final String UNIT = "unit";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTextWind;
    private TextView mTextHumidity;
    private TextView mTextPressure;
    private TextView mSunrise;
    private TextView mSunset;
    private TextView mLocationName;
    private TextView mTemperature;
    private CardView mCardView;
    private TextView mTime;
    private All data;
    private Realm mRealm;
    private SharedPreferences mShared;
    private SharedPreferences.Editor editor;
    private Button mBtnAddPlace;
    private FromFirstListener listener;

    public interface FromFirstListener {
        void Clicked(int requestCode);
    }

    public static CurrentWeatherFragment newInstance(int page, double latitude, double longitude) {
        Log.d("TESTING", "newInstance CurrentWeatherFragment");
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
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
        Log.d("TESTING", "onCreate CurrentWeatherFragment");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TESTING", "onActivityCreated CurrentWeatherFragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TESTING", "onCreateView CurrentWeatherFragment");
        View mView = inflater.inflate(R.layout.fragment_first, container, false);
        InitView(mView);
        return mView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TESTING", "onDestroyView CurrentWeatherFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TESTING", "onStart CurrentWeatherFragment");
        if (getArguments() != null) {
            mLat = getArguments().getDouble(LAT);
            mLon = getArguments().getDouble(LON);
            mShared = getActivity().getSharedPreferences(Common.SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
            mUnits = mShared.getString("unit", "");
        }
        OnCallRequest(String.valueOf(mLat), String.valueOf(mLon), mUnits);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TESTING", "onResume CurrentWeatherFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TESTING", "onPause CurrentWeatherFragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TESTING", "onStop CurrentWeatherFragment");
    }


    private void InitView(View mView) {
        Log.d("TESTING", "IniView CurrentWeatherFragment");
        data = null;
        mBtnAddPlace = mView.findViewById(R.id.id_btn_new_location);
        mBtnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Clicked(REQUEST_FOR_ADDING_PLACE);
            }
        });
        mCardView = mView.findViewById(R.id.id_card_view);
        mSwipeRefreshLayout = mView.findViewById(R.id.id_swipe_refresh_layout_today);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mTextHumidity = mView.findViewById(R.id.id_field_humidity_today);
        mTextWind = mView.findViewById(R.id.id_field_wind_today);
        mTextPressure = mView.findViewById(R.id.id_field_pressure_today);
        mSunrise = mView.findViewById(R.id.id_field_sunrise_today);
        mSunset = mView.findViewById(R.id.id_field_sunset_today);
        mLocationName = mView.findViewById(R.id.id_location_name_today);
        mTemperature = mView.findViewById(R.id.id_day_temperature_today);
        mTime = mView.findViewById(R.id.id_field_time_today);
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.commitTransaction();
        LoadLast(mRealm);
    }


    private void LoadLast(Realm mRealm) {
        Log.d("TESTING", "LoadLast CurrentWeatherFragment");
        RealmResults<SevedInformation> results = mRealm.where(SevedInformation.class).findAll();
        if (results.size() > 0) {
            mTemperature.setText(results.last().getTemperature());
            mLocationName.setText(results.last().getLocationName());
            mTextPressure.setText(results.last().getPressure());
            mTextHumidity.setText(results.last().getHumidity());
            mTextWind.setText(results.last().getWind());
            mSunrise.setText(results.last().getSunrise());
            mSunset.setText(results.last().getSunset());
            mTime.setText(results.last().getTime());
        }
    }

    public void  OnCallRequest(String mLat, String mLon, String mUnits) {
        Log.d("TESTING", "OnCallRequest CurrentWeatherFragment");
        WeatherAPI api = GetConnect.getInstance().create(WeatherAPI.class);
        Call<All> call = api
                .getWeatherForNow(String.valueOf(mLat), String.valueOf(mLon), APP_ID, mUnits);

        call.enqueue(new Callback<All>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<All> call, @NonNull Response<All> response) {
                data = response.body();
                if (data != null) {
                    mTemperature.setText(String.valueOf(data.getMain().getTemp()));
                    mLocationName.setText(data.getName() + "," + data.getSys().getCountry());
                    mTextPressure.setText(String.valueOf(data.getMain().getPressure()));
                    mTextHumidity.setText(String.valueOf(data.getMain().getHumidity()));
                    mTextWind.setText(String.valueOf(data.getWind().getSpeed()));
                    mSunrise.setText(data.getSys().getSunrise());
                    mSunset.setText((data.getSys().getSunset()));
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                    String time = simpleDateFormat.format(System.currentTimeMillis());
                    mTime.setText(time);
                    SaveData(time);
                }else{
                    Toast.makeText(getActivity(), "data is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<All> call, @NonNull Throwable t) {
                Snackbar snackbar;
                if (!Common.checkNetworkConnectionStatus(Objects.requireNonNull(getContext()))) {
                    snackbar = Snackbar.make(mCardView, "No internet connection.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                    currentConnection = false;
                } else {
                    snackbar = Snackbar.make(mCardView, "Something went wrong. \nTry again later please.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SaveData(final String time) {
        Log.d("TESTING", "SaveData CurrentWeatherFragment");
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SevedInformation toSave = mRealm.createObject(SevedInformation.class);
                toSave.setHumidity(String.valueOf(data.getMain().getHumidity()));
                toSave.setLocationName(data.getName() + "," + data.getSys().getCountry());
                toSave.setPressure(String.valueOf(data.getMain().getPressure()));
                toSave.setSunrise(data.getSys().getSunrise());
                toSave.setSunset(data.getSys().getSunset());
                toSave.setTemperature(String.valueOf(data.getMain().getTemp()));
                toSave.setTime(time);
                toSave.setWind(String.valueOf(data.getWind().getSpeed()));
            }
        });
    }

    @Override
    public void onRefresh() {
        Log.d("TESTING", "OnRefresh CurrentWeatherFragment");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                if (Common.checkNetworkConnectionStatus(Objects.requireNonNull(getContext()))) {
                    OnCallRequest(String.valueOf(mLat), String.valueOf(mLon), mUnits);
                    if (!currentConnection) {
                        Snackbar snackbar;
                        snackbar = Snackbar.make(mCardView, "Connection returned.", Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_pos));
                        snackbar.show();
                        currentConnection = true;
                    }
                } else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(mCardView, "No internet connection.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.snackBar_neg));
                    snackbar.show();
                }
            }
        }, 2000);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRealm.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("TESTING", "onAttach CurrentWeatherFragment");
        try {
            listener = (FromFirstListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}