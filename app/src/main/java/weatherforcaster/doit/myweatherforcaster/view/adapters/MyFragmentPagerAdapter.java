package weatherforcaster.doit.myweatherforcaster.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import weatherforcaster.doit.myweatherforcaster.view.activity.mainActivity.fragment.CurrentWeatherFragment;
import weatherforcaster.doit.myweatherforcaster.view.activity.mainActivity.fragment.FiveDayWeatherFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private double mLat;
    private double mLon;

    public MyFragmentPagerAdapter(FragmentManager fm,Context mContext,double mLat, double mLon) {
        super(fm);
        Log.d("TESTING","FragmentPagerAdapter");
        this.mContext = mContext;
        this.mLat = mLat;
        this.mLon = mLon;
    }

    @Override
    public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return CurrentWeatherFragment.newInstance(0,mLat,mLon);
                case 1:
                    return FiveDayWeatherFragment.newInstance(1,mLat,mLon);
            }
            return null;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "NOW";
            case 1:
                return "5 DAYS";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}