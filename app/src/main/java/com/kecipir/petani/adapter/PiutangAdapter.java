package com.kecipir.petani.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
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
import com.kecipir.petani.fragment.PiutangFragment;
import com.kecipir.petani.rest.response.PiutangResponse;

import java.util.List;

public class PiutangAdapter extends RecyclerView.Adapter<PiutangAdapter.ViewHolder> implements Paging {
    private Context context;
    private PiutangFragment fragment;
    private List<PiutangResponse> list;
    private boolean hasNext;

    public interface PageLoader {
        void loadPage(int page);
    }

    public PiutangAdapter(Context context, PiutangFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView piutang_day, piutang_date, piutang_price, piutang_tempo;
        public RelativeLayout piutang_item, piutang_detail;
        public ImageView piutang_expand;
        private AdapterView.OnItemClickListener listener;
        View progress;

        public ViewHolder(View view, boolean isProgress) {
            super(view);

            if (isProgress) {
                progress = view.findViewById(R.id.loading_progress);
            } else {

                piutang_day = (TextView) view.findViewById(R.id.piutang_day);
                piutang_date = (TextView) view.findViewById(R.id.piutang_date);
                piutang_price = (TextView) view.findViewById(R.id.piutang_price);
                piutang_tempo = (TextView) view.findViewById(R.id.piutang_tempo_number);
                piutang_expand = (ImageView) view.findViewById(R.id.piutang_expand);

                piutang_item = (RelativeLayout) view.findViewById(R.id.piutang_item);
                piutang_detail = (RelativeLayout) view.findViewById(R.id.piutang_detail);
            }
        }
    }

    @Override
    public PiutangAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false);
            return new PiutangAdapter.ViewHolder(view, true);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_piutang, parent, false);
        return new PiutangAdapter.ViewHolder(view, false);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == list.size()) {
            if (hasNext) {
                holder.progress.setVisibility(View.VISIBLE);
            } else {
                holder.progress.setVisibility(View.GONE);
            }
            return;
        }

        PiutangResponse data = list.get(position);
        holder.piutang_day.setText(data.getHari());
        holder.piutang_date.setText(data.getTerdaftar());
        holder.piutang_price.setText(data.getNominal());
        holder.piutang_tempo.setText(data.getJatuhTempo());
        holder.piutang_expand.setImageResource(R.drawable.dropdown);
        holder.piutang_detail.setVisibility(View.GONE);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        holder.piutang_detail.measure(widthSpec, heightSpec);
        final int measuredHeight = holder.piutang_detail.getMeasuredHeight();

        holder.piutang_item.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Here goes your desired onClick behaviour. Like:
                if (holder.piutang_detail.getVisibility() == View.GONE) {
                    ValueAnimator animator = ValueAnimator.ofInt(0, measuredHeight);

                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int value = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = holder.piutang_detail.getLayoutParams();
                            layoutParams.height = value;
                            holder.piutang_detail.setLayoutParams(layoutParams);
                        }
                    });

                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            holder.piutang_expand.setImageResource(R.drawable.dropup);
                            holder.piutang_detail.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();

                } else {
                    ValueAnimator animator = ValueAnimator.ofInt(measuredHeight, 0);

                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            //Update Height
                            int value = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = holder.piutang_detail.getLayoutParams();
                            layoutParams.height = value;
                            holder.piutang_detail.setLayoutParams(layoutParams);
                        }
                    });

                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            holder.piutang_detail.setVisibility(View.GONE);
                            holder.piutang_expand.setImageResource(R.drawable.dropdown);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();
                }
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

    public void setList(List<PiutangResponse> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void appendList(List<PiutangResponse> list, boolean hasNext) {
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
}
