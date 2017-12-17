package com.kecipir.petani.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kecipir.petani.BaseActivity;
import com.kecipir.petani.MainActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.adapter.NotificationAdapter;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.callback.TokenCallback;
import com.kecipir.petani.rest.request.NotificationRequest;
import com.kecipir.petani.rest.response.NotificationResponse;
import com.kecipir.petani.rest.response.Page;
import com.kecipir.petani.util.EmptyDataViewer;
import com.kecipir.petani.util.EndlessScrollListener;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements NotificationAdapter.PageLoader {
    private static final String TAG = com.kecipir.petani.fragment.NotificationFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private View mLoadingView;
    private TextView mLoadingText;
    private TextView mEmptyView;
    private NotificationAdapter mAdapter;
    private EmptyDataViewer mEmptyViewer;

    private Api mApi;
    private AccountPreference mAccountPreference;

    private int mCurrentPage;
    private boolean mHasNext;
    private boolean mHasResponse;

    public NotificationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mApi = ((BaseActivity) getActivity()).getApp().getRestApi();
        mAccountPreference = ((BaseActivity) getActivity()).getApp().getAccountPreference();
        mHasResponse = false;
        mAdapter = new NotificationAdapter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        ((MainActivity) getActivity()).enableViews(true);
        getActivity().setTitle("");
        mTitle.setText("Notifikasi");

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLoadingView = view.findViewById(R.id.loading_view);
        mLoadingText = (TextView) view.findViewById(R.id.loading_text);
        mLoadingText.setText("Loading...");
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mEmptyView.setText("Tidak ada notifikasi");
        if (mAdapter.getItemCount() > 0) {
            mLoadingView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int spanCount = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (mCurrentPage < page && mHasNext) {
                    getNotification(page);
                }
            }
        });
        mEmptyViewer = new EmptyDataViewer(mEmptyView, mAdapter, 1);
        mAdapter.registerAdapterDataObserver(mEmptyViewer);

        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter.getItemCount() == 0 && !mHasResponse) {
            getNotification();
        }
    }

    @Override
    public void onDestroyView() {
        if (mEmptyViewer != null) {
            mAdapter.unregisterAdapterDataObserver(mEmptyViewer);
        }
        mRecyclerView = null;
        mLoadingView = null;
        mLoadingText = null;
        mEmptyView = null;

        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        MenuItem envelope = menu.findItem(R.id.action_messages);
        envelope.setVisible(false);
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getNotification() {
        getNotification(1);
    }

    private void getNotification(final int page) {
        final NotificationRequest params = new NotificationRequest();
        params.setPage(page);

        Call<Page<NotificationResponse>> call = mApi.notification(params);

        call.enqueue(new TokenCallback<Page<NotificationResponse>>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<Page<NotificationResponse>> call, Response<Page<NotificationResponse>> response, boolean hasNewToken) {
                if (response.isSuccessful()) {
                    Page<NotificationResponse> page = response.body();
                    mCurrentPage = page.getCurrentPage();
                    mHasNext = page.getNextPageUrl() != null;
                    mAdapter.appendList(page.getData(), mHasNext);
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                }

                mHasResponse = true;
            }

            @Override
            public Call<Page<NotificationResponse>> recreateCall(String newToken) {
                final NotificationRequest params = new NotificationRequest();
                params.setPage(page);

                return mApi.notification(params);
            }

            @Override
            public void onFailure(Call<Page<NotificationResponse>> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }

    @Override
    public void loadPage(int page) {
        getNotification(page);
    }
}
