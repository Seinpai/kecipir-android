package com.kecipir.petani.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kecipir.petani.R;
import com.kecipir.petani.fragment.NotificationFragment;
import com.kecipir.petani.fragment.OrderFragment;
import com.kecipir.petani.rest.response.NotificationResponse;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> implements Paging {
    private Context context;
    private NotificationFragment fragment;
    private List<NotificationResponse> list;
    private boolean hasNext;

    public interface PageLoader {
        void loadPage(int page);
    }

    public NotificationAdapter(Context context, NotificationFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int more) {
        if (more == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false);
            return new ViewHolder(view, true);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view, false);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder holder, int position) {
        if (position == list.size()) {
            if (hasNext) {
                holder.progress.setVisibility(View.VISIBLE);
            } else {
                holder.progress.setVisibility(View.GONE);
            }
            return;
        }

        NotificationResponse data = list.get(position);

        holder.order_date.setText(data.getOrderDate());
        holder.notification_date.setText(data.getDeliveryDate());
        holder.message.setText(data.getMessage());

        if(data.isRead() == 1){
            holder.item.setBackgroundColor(context.getResources().getColor(R.color.background_notification_read));
            holder.message.setTextColor(context.getResources().getColor(R.color.color_notification_read));
            holder.notification_date.setTextColor(context.getResources().getColor(R.color.color_notification_read));
            holder.thumbnail.setBackgroundResource(R.drawable.open_mail);
        }else{
            holder.item.setBackgroundColor(context.getResources().getColor(R.color.background_notification_unread));
            holder.message.setTextColor(context.getResources().getColor(R.color.color_notification_unread));
            holder.notification_date.setTextColor(context.getResources().getColor(R.color.color_notification_unread));
            holder.thumbnail.setBackgroundResource(R.drawable.closed_mail);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                OrderFragment of = new OrderFragment();
                Bundle args = new Bundle();
                args.putString("item_tanggal_notif_transition", holder.order_date.getText().toString());
                args.putString("from_notif_transition", "");
                of.setArguments(args);

                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, of).addToBackStack(null).commit();
            }
        });
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

    public void setList(List<NotificationResponse> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void appendList(List<NotificationResponse> list, boolean hasNext) {
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification_date;
        TextView order_date;
        TextView message, date;
        ImageView thumbnail;
        CardView card;
        RelativeLayout item;
        View progress;

        public ViewHolder(View view, boolean isProgress) {
            super(view);
            if (isProgress) {
                progress = view.findViewById(R.id.loading_progress);
            } else {
                message = (TextView) view.findViewById(R.id.notification_messages);
                notification_date = (TextView) view.findViewById(R.id.notification_date);
                order_date = (TextView) view.findViewById(R.id.order_date);
                thumbnail = (ImageView) view.findViewById(R.id.notification_thumbnail);
                card = (CardView)view.findViewById(R.id.notification_card_view);
                item = (RelativeLayout) view.findViewById(R.id.notification_item);
            }
        }
    }
}
