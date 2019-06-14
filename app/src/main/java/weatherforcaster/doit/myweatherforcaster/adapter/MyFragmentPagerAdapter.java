package weatherforcaster.doit.myweatherforcaster.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import weatherforcaster.doit.myweatherforcaster.fragment.FirstFragment;
import weatherforcaster.doit.myweatherforcaster.fragment.SecondFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return FirstFragment.newInstance(0);
            case 1:
                return SecondFragment.newInstance(1);
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