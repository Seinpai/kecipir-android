package com.kecipir.petani.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kecipir.petani.MainActivity;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.BaseActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.adapter.PiutangAdapter;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.callback.TokenCallback;
import com.kecipir.petani.rest.request.PiutangRequest;
import com.kecipir.petani.rest.response.Page;
import com.kecipir.petani.rest.response.PiutangResponse;
import com.kecipir.petani.util.EmptyDataViewer;
import com.kecipir.petani.util.EndlessScrollListener;
import retrofit2.Call;
import retrofit2.Response;

public class PiutangFragment extends Fragment implements PiutangAdapter.PageLoader{
    private static final String TAG = PiutangFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private View mLoadingView;
    private TextView mLoadingText;
    private TextView mEmptyView;
    private PiutangAdapter mAdapter;
    private EmptyDataViewer mEmptyViewer;

    private Api mApi;
    private AccountPreference mAccountPreference;

    private int mCurrentPage;
    private boolean mHasNext;
    private boolean mHasResponse;

    public PiutangFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((BaseActivity) getActivity()).getApp().getRestApi();
        mAccountPreference = ((BaseActivity) getActivity()).getApp().getAccountPreference();
        mHasResponse = false;
        mAdapter = new PiutangAdapter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).enableViews(false);
        View view = inflater.inflate(R.layout.fragment_piutang, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.piutang_list);
        mLoadingView = view.findViewById(R.id.loading_view);
        mLoadingText = (TextView) view.findViewById(R.id.loading_text);
        mLoadingText.setText("Loading...");
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mEmptyView.setText("Tidak ada Piutang");

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
                Log.d(getTag(), "onLoadMore: page " + page);
                if (mCurrentPage < page && mHasNext) {
                    getPiutang(page);
                }
            }
        });

        mEmptyViewer = new EmptyDataViewer(mEmptyView, mAdapter, 1);
        mAdapter.registerAdapterDataObserver(mEmptyViewer);

        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter.getItemCount() == 0 && !mHasResponse) {
            getPiutang();
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

    private void getPiutang() {
        getPiutang(1);
    }

    private void getPiutang(final int page) {
        final PiutangRequest params = new PiutangRequest();
        params.setPage(page);

        Call<Page<PiutangResponse>> call = mApi.piutang(params);

        call.enqueue(new TokenCallback<Page<PiutangResponse>>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<Page<PiutangResponse>> call, Response<Page<PiutangResponse>> response, boolean hasNewToken) {
                if (response.isSuccessful()) {
                    Page<PiutangResponse> page = response.body();
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
            public Call<Page<PiutangResponse>> recreateCall(String newToken) {
                final PiutangRequest params = new PiutangRequest();
                params.setPage(page);

                return mApi.piutang(params);
            }

            @Override
            public void onFailure(Call<Page<PiutangResponse>> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }


    @Override
    public void loadPage(int page) {
        Log.d(TAG, "loadPage " + page);
        getPiutang(page);
    }

}
