package com.kecipir.petani.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.kecipir.petani.BaseActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.callback.TokenCallback;
import com.kecipir.petani.rest.request.DashboardRequest;
import com.kecipir.petani.rest.response.DashboardResponse;
import com.kecipir.petani.util.LargeValueFormatter;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DashboardWeeklyFragment extends Fragment {

    private static final String TAG = DashboardWeeklyFragment.class.getSimpleName();
    private AccountPreference mAccountPreference;
    private Api mApi;

    private SwipeRefreshLayout mSwipeLayout;
    private View mLoadingView;
    private TextView mLoadingText;
    private View mFragmentView;
    private LineChart mChart;
    private boolean mHasResponse;

    // the labels that should be drawn on the XAxis
    final String[] quarters = new String[] { "senin", "selasa", "rabu", "kamis", "jumat", "sabtu", "minggu" };

    // Api request parameters
    private LocalDate startDate;
    private LocalDate endDate;
    private String show;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountPreference = ((BaseActivity) getActivity()).getApp().getAccountPreference();
        mApi = ((BaseActivity) getActivity()).getApp().getRestApi();
        mHasResponse = false;

        LocalDate now = new LocalDate();
        startDate = now.withDayOfWeek(DateTimeConstants.MONDAY);
        endDate = now.withDayOfWeek(DateTimeConstants.SUNDAY);
        show = "daily";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView___");

        mFragmentView = inflater.inflate(R.layout.fragment_dashboard_weekly, container, false);
        mLoadingView = mFragmentView.findViewById(R.id.loading_view);
        mLoadingText = (TextView) mFragmentView.findViewById(R.id.loading_text);
        mLoadingText.setText("Loading...");
        mSwipeLayout = (SwipeRefreshLayout) mFragmentView.findViewById(R.id.swipe_refresh);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent);

        mChart = (LineChart) mFragmentView.findViewById(R.id.chart);

        // disable description text
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        // enable scaling and dragging
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value - 1];
            }
        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(getResources().getColor(R.color.colorPrimary));
        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setValueFormatter(new LargeValueFormatter());
        yAxis.setGridColor(getResources().getColor(R.color.colorPrimary));

        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshChart();
            }
        });
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (!mHasResponse) {
            refreshChart();
//        }
    }

    private void refreshChart() {
        final DashboardRequest params = new DashboardRequest();
        params.setStart(startDate.toString());
        params.setEnd(endDate.toString());
        params.setShow(show);

        Call<DashboardResponse> call = mApi.dashboard(params);
        call.enqueue(new TokenCallback<DashboardResponse>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response, boolean hasNewToken) {
                // Catch error: Fragment is not attached to activity
                if (!isAdded()) {
                    return;
                }

                if (response.isSuccessful()) {
                    DashboardResponse dashboard = response.body();

                    ((TextView) mFragmentView.findViewById(R.id.total_harvest)).setText(
                            dashboard.getData().getHarvest().getTotal());
                    ((TextView) mFragmentView.findViewById(R.id.total_order)).setText(
                            dashboard.getData().getOrder().getTotal());
                    ((TextView) mFragmentView.findViewById(R.id.total_shipment)).setText(
                            dashboard.getData().getShipment().getTotal());
                    ((TextView) mFragmentView.findViewById(R.id.total_loss)).setText(
                            dashboard.getData().getLoss().getTotal());

                    // Chart
                    List<Entry> entriesHarvest = new ArrayList<Entry>();
                    List<Entry> entriesOrder= new ArrayList<Entry>();
                    List<Entry> entriesShipment= new ArrayList<Entry>();
                    List<Entry> entriesLoss= new ArrayList<Entry>();

                    if(dashboard.getData().getHarvest().getDetails() != null) {
                        for (final DashboardResponse.Harvest.Details data : dashboard.getData().getHarvest().getDetails()) {
                            entriesHarvest.add(new Entry(data.getIndex(), data.getTotal()));
                        }
                    }
                    if(dashboard.getData().getOrder().getDetails() != null) {
                        for (final DashboardResponse.Order.Details data : dashboard.getData().getOrder().getDetails()) {
                            entriesOrder.add(new Entry(data.getIndex(), data.getTotal()));
                        }
                    }
                    if(dashboard.getData().getShipment().getDetails() != null) {
                        for (final DashboardResponse.Shipment.Details data : dashboard.getData().getShipment().getDetails()) {
                            entriesShipment.add(new Entry(data.getIndex(), data.getTotal()));
                        }
                    }
                    if(dashboard.getData().getLoss().getDetails() != null) {
                        for (final DashboardResponse.Loss.Details data : dashboard.getData().getLoss().getDetails()) {
                            entriesLoss.add(new Entry(data.getIndex(), data.getTotal()));
                        }
                    }

                    LineDataSet dataSetHarvest = new LineDataSet(entriesHarvest, null); // add entries to dataset
                    dataSetHarvest.setColor(getResources().getColor(R.color.colorHarvest));
                    dataSetHarvest.setValueTextColor(getResources().getColor(R.color.colorHarvest));
                    dataSetHarvest.setCircleColor(getResources().getColor(R.color.colorHarvest));
                    dataSetHarvest.setCircleColorHole(getResources().getColor(R.color.colorHarvest));

                    LineDataSet dataSetOrder = new LineDataSet(entriesOrder, null); // add entries to dataset
                    dataSetOrder.setColor(getResources().getColor(R.color.colorOrder));
                    dataSetOrder.setValueTextColor(getResources().getColor(R.color.colorOrder));
                    dataSetOrder.setCircleColor(getResources().getColor(R.color.colorOrder));
                    dataSetOrder.setCircleColorHole(getResources().getColor(R.color.colorOrder));

                    LineDataSet dataSetShipment = new LineDataSet(entriesShipment, null); // add entries to dataset
                    dataSetShipment.setColor(getResources().getColor(R.color.colorShipment));
                    dataSetShipment.setValueTextColor(getResources().getColor(R.color.colorShipment));
                    dataSetShipment.setCircleColor(getResources().getColor(R.color.colorShipment));
                    dataSetShipment.setCircleColorHole(getResources().getColor(R.color.colorShipment));

                    LineDataSet dataSetLoss = new LineDataSet(entriesLoss, null); // add entries to dataset
                    dataSetLoss.setColor(getResources().getColor(R.color.colorLoss));
                    dataSetLoss.setValueTextColor(getResources().getColor(R.color.colorLoss));
                    dataSetLoss.setCircleColor(getResources().getColor(R.color.colorLoss));
                    dataSetLoss.setCircleColorHole(getResources().getColor(R.color.colorLoss));

                    // use the interface ILineDataSet
                    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(dataSetHarvest);
                    dataSets.add(dataSetOrder);
                    dataSets.add(dataSetShipment);
                    dataSets.add(dataSetLoss);

                    LineData lineData= new LineData(dataSets);
                    lineData.setValueFormatter(new LargeValueFormatter());
                    mChart.setData(lineData);
                    mChart.setVisibleXRangeMaximum(7);
                    mChart.invalidate();

                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }

                    if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
                        mSwipeLayout.setRefreshing(false);
                    }
                }

                mHasResponse = true;
            }

            @Override
            public Call<DashboardResponse> recreateCall(String newToken) {
                final DashboardRequest params = new DashboardRequest();
                params.setStart(startDate.toString());
                params.setEnd(endDate.toString());
                params.setShow(show);

                return mApi.dashboard(params);
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }
}
