package com.kecipir.petani.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kecipir.petani.adapter.Paging;

/**
 * Created by Patriot Muslim on 20/07/2017.
 * Empty Data Observer.
 */

public class EmptyDataViewer extends RecyclerView.AdapterDataObserver {

    private final View emptyView;
    private final RecyclerView.Adapter adapter;
    private final int emptyThreshold;

    public EmptyDataViewer(View emptyView, RecyclerView.Adapter adapter, int emptyThreshold) {
        this.emptyView = emptyView;
        this.adapter = adapter;
        this.emptyThreshold = emptyThreshold;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        if (adapter.getItemCount() == emptyThreshold) {
            if (adapter instanceof Paging && ((Paging) adapter).hasNext()) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }
}