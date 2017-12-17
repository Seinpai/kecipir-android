package com.kecipir.petani.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.kecipir.petani.R;
import com.kecipir.petani.fragment.PanenanDetailFragment;
import com.kecipir.petani.fragment.PanenanFragment;
import com.kecipir.petani.rest.response.PanenanResponse;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class PanenanAdapter extends RecyclerView.Adapter<PanenanAdapter.ViewHolder> implements Paging {
    private Context context;
    private List<PanenanResponse> allpanenans;
    private boolean hasNext;
    private PanenanFragment fragment;

    public interface PageLoader {
        void loadPage(int page);
    }

    public PanenanAdapter(Context context/*, List<PanenanResponse> allpanenans*/, PanenanFragment fragment) {
        this.context = context;
        this.allpanenans = allpanenans;
        this.fragment = fragment;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView grade, item_name,price_panenan,quantity_panenan,satuan_panenan,ket_tambahan_panenan,thumbnail_path,id_petani_barang,qty_dipesan;
        public ImageView thumbnail;
        public CardView cardView;
        public String tanggal_panenan;
        View progress;

        private AdapterView.OnItemClickListener listener;

        public ViewHolder(View view, boolean isProgress) {
            super(view);
            if (isProgress) {
                progress = view.findViewById(R.id.loading_progress);
            } else {
                grade = (TextView) view.findViewById(R.id.grade_paanenan);
                item_name = (TextView) view.findViewById(R.id.item_panenan);
                price_panenan = (TextView) view.findViewById(R.id.price_panenan);
                quantity_panenan = (TextView) view.findViewById(R.id.quantity_panenan);
                satuan_panenan = (TextView) view.findViewById(R.id.satuan_panenan);
                ket_tambahan_panenan = (TextView) view.findViewById(R.id.ket_tambahan_panenan);
                thumbnail_path = (TextView) view.findViewById(R.id.thumbnail_path);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail_panenan);
                id_petani_barang = (TextView) view.findViewById(R.id.petaniBarangId);
                qty_dipesan = (TextView) view.findViewById(R.id.qtyDipesan);
                cardView = (CardView) view.findViewById(R.id.card_view);
            }
        }
    }

    @Override
    public PanenanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false);
            return new PanenanAdapter.ViewHolder(view, true);
        }

        /*View view = LayoutInflater.from(context).inflate(R.layout.panenan_fragment, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view, false);*/

        View view = LayoutInflater.from(context).inflate(R.layout.item_panenan, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view,false);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == allpanenans.size()) {
            if (hasNext) {
                holder.progress.setVisibility(View.VISIBLE);
            } else {
                holder.progress.setVisibility(View.GONE);
            }
            return;
        }

        PanenanResponse data = allpanenans.get(position);
        holder.grade.setText(data.getPanenangrades());
        holder.item_name.setText(data.getPanenanItemPanenans());
        holder.price_panenan.setText(data.getPanenanPricess());
        holder.quantity_panenan.setText(data.getPanenanQuantitys());
        holder.satuan_panenan.setText(data.getPanenanSatuans());
        holder.ket_tambahan_panenan.setText("");
        holder.thumbnail_path.setText(data.getPanenanThumbnailPath());
        holder.id_petani_barang.setText(data.getpetaniBarangId());
        holder.qty_dipesan.setText(data.getPesananQuantitys());
        holder.tanggal_panenan = (fragment.tanggal);

        if (data.getPanenanThumbnailPath() != null) {
            Picasso.with(context)
                    .load(data.getPanenanThumbnailPath())
                    //.transform(new CircleImageTransform())
                    .fit() // Fix centerCrop issue: http://stackoverflow.com/a/20824141/1936697
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(holder.thumbnail);
        }


        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PanenanDetailFragment myFragment = new PanenanDetailFragment();

                //send data
                Bundle args = new Bundle();
                args.putString("item_id_transition", holder.id_petani_barang.getText().toString());
                args.putString("item_tanggal_transition", holder.tanggal_panenan);
                args.putString("item_name_transition", holder.item_name.getText().toString());
                args.putString("item_grade_transition", holder.grade.getText().toString());
                args.putString("item_price_transition", holder.price_panenan.getText().toString());
                args.putString("item_thumbnail_transition", holder.thumbnail_path.getText().toString());
                args.putString("item_satuan_transition", holder.satuan_panenan.getText().toString());
                args.putString("item_quantity_transition", holder.quantity_panenan.getText().toString());
                args.putString("item_qtyDipesan_transition", holder.qty_dipesan.getText().toString());
                myFragment.setArguments(args);

                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, myFragment).addToBackStack(null).commit();
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PanenanDetailFragment myFragment = new PanenanDetailFragment();

                //send data
                Bundle args = new Bundle();
                args.putString("item_id_transition", holder.id_petani_barang.getText().toString());
                args.putString("item_tanggal_transition", holder.tanggal_panenan);
                args.putString("item_name_transition", holder.item_name.getText().toString());
                args.putString("item_grade_transition", holder.grade.getText().toString());
                args.putString("item_price_transition", holder.price_panenan.getText().toString());
                args.putString("item_thumbnail_transition", holder.thumbnail_path.getText().toString());
                args.putString("item_satuan_transition", holder.satuan_panenan.getText().toString());
                args.putString("item_quantity_transition", holder.quantity_panenan.getText().toString());
                args.putString("item_qtyDipesan_transition", holder.qty_dipesan.getText().toString());
                myFragment.setArguments(args);

                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, myFragment).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return allpanenans == null ? 0 : allpanenans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (allpanenans != null && position == allpanenans.size()) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    public void setList(List<PanenanResponse> allpanenans) {
        this.allpanenans = allpanenans;
        notifyDataSetChanged();
    }

    public void appendList(List<PanenanResponse> allpanenans, boolean hasNext) {
        if (allpanenans != null) {
            if (this.allpanenans == null) {
                this.allpanenans = allpanenans;
            } else {
                this.allpanenans.addAll(allpanenans);
            }
        }

        this.hasNext = hasNext;
        notifyDataSetChanged();
    }
}
