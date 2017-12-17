package com.kecipir.petani.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kecipir.petani.MainActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.adapter.PembayaranAdapter;

public class PembayaranFragment extends Fragment {
    private static final String TAG = PembayaranFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public PembayaranFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pembayaran, container, false);
        ((MainActivity)getActivity()).enableViews(false);
        TextView mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        getActivity().setTitle("");
        mTitle.setText("Pembayaran");

        tabLayout = (TabLayout) view.findViewById(R.id.tab_pembayaran);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_pembayaran);

        viewPager.setAdapter(new PembayaranAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
