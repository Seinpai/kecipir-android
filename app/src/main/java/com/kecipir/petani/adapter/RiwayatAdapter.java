package com.kecipir.petani.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kecipir.petani.R;
import com.kecipir.petani.fragment.RiwayatFragment;
import com.kecipir.petani.rest.response.HistoryResponse;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> implements Paging{
    private Context context;
    private RiwayatFragment fragment;
    private List<HistoryResponse> list;
    private boolean hasNext;

    public interface PageLoader {
        void loadPage(int page);
    }

    public RiwayatAdapter(Context context, RiwayatFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RiwayatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false);
            return new RiwayatAdapter.ViewHolder(view, true);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_riwayat, parent, false);
        return new RiwayatAdapter.ViewHolder(view, false);
    }

    @Override
    public void onBindViewHolder(final RiwayatAdapter.ViewHolder holder, final int position) {
        if (position == list.size()) {
            if (hasNext) {
                holder.progress.setVisibility(View.VISIBLE);
            } else {
                holder.progress.setVisibility(View.GONE);
            }
            return;
        }
        HistoryResponse data = list.get(position);
        holder.Riwayatdays.setText(data.getHari());
        holder.Riwayatdate.setText(data.getTanggalPembayaran());
        holder.Riwayatprice.setText(data.getNominal());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null && position == list.size()) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    public void setList(List<HistoryResponse> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void appendList(List<HistoryResponse> list, boolean hasNext) {
        if (list != null) {
            if (this.list == null) {
                this.list = list;
            } else {
                this.list.addAll(list);
            }
        }

        this.hasNext = hasNext;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Riwayatdays;
        public TextView Riwayatdate;
        public TextView Riwayatprice;
        View progress;

        public ViewHolder(View view, boolean isProgress) {
            super(view);

            if (isProgress) {
                progress = view.findViewById(R.id.loading_progress);
            } else {

                Riwayatdays = (TextView)itemView.findViewById(R.id.riwayat_day);
                Riwayatdate = (TextView)itemView.findViewById(R.id.riwayat_date);
                Riwayatprice = (TextView)itemView.findViewById(R.id.riwayat_price);
            }
        }
    }
}
