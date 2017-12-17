package com.kecipir.petani.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kecipir.petani.MainActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.adapter.DashboardAdapter;

public class DashboardFragment extends Fragment {

    private static final String TAG = DashboardFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view = null;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        ((MainActivity)getActivity()).enableViews(false);
        getActivity().setTitle("");
        mTitle.setText(R.string.company_name);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);

        DashboardAdapter adapter =new DashboardAdapter(getContext(), getChildFragmentManager());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        adapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        Log.d("DASHBOARD_FRAGMENT", "onCreateView");

        return view;
    }
}
