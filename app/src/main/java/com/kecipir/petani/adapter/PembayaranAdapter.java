package com.kecipir.petani.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kecipir.petani.fragment.RiwayatFragment;
import com.kecipir.petani.fragment.PiutangFragment;

public class PembayaranAdapter extends FragmentPagerAdapter{
    private static final String TAG = PembayaranAdapter.class.getSimpleName();

    private static final int FRAGMENT_COUNT = 2;

    public PembayaranAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PiutangFragment();
            case 1:
                return new RiwayatFragment();
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
                return "Piutang";
            case 1:
                return "Riwayat";
        }
        return null;
    }
}
