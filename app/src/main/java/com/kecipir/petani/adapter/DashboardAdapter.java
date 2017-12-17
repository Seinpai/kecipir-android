package com.kecipir.petani.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kecipir.petani.R;
import com.kecipir.petani.fragment.DashboardWeeklyFragment;
import com.kecipir.petani.fragment.DashboardMonthlyFragment;
import com.kecipir.petani.fragment.DashboardYearlyFragment;

public class DashboardAdapter extends FragmentPagerAdapter{

    private static final String TAG = DashboardAdapter.class.getSimpleName();
    private Context context;
    private static final int FRAGMENT_COUNT = 3;

    public DashboardAdapter(Context context, FragmentManager fragment) {
        super(fragment);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DashboardWeeklyFragment();
            case 1:
                return new DashboardMonthlyFragment();
            case 2:
                return new DashboardYearlyFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.period_daily);
            case 1:
                return context.getResources().getString(R.string.period_monthly);
            case 2:
                return context.getResources().getString(R.string.period_yearly);
        }
        return null;
    }

}
