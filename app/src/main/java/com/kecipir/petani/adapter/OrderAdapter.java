package com.kecipir.petani.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kecipir.petani.R;
import com.kecipir.petani.entities.OrderObject;
import com.kecipir.petani.fragment.OrderFragment;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    public List<OrderObject> allorders;
    private OrderFragment fragment;
    public OrderObject order;
    public OrderAdapter(Context context, List<OrderObject> allorders, OrderFragment fragment) {
        this.context = context;
        this.allorders = allorders;
        this.fragment = fragment;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView grade_order, item_order,price_order,price_order_real,quantity_available_order,satuan_order,ket_tambahan_order;
        public TextView plus_aksi_order,input_order_quantity,minus_aksi_order,id_barang_panen;
        public ImageView thumbnail_order;
        public int position_holder;
        private AdapterView.OnItemClickListener listener;

        public ViewHolder(View view) {
            super(view);
            grade_order = (TextView) view.findViewById(R.id.grade_order);
            item_order = (TextView) view.findViewById(R.id.item_order);
            price_order = (TextView) view.findViewById(R.id.price_order);
            price_order_real = (TextView) view.findViewById(R.id.price_order_real);
            quantity_available_order = (TextView) view.findViewById(R.id.quantity_available_order);
            satuan_order = (TextView) view.findViewById(R.id.satuan_order);
            ket_tambahan_order = (TextView) view.findViewById(R.id.ket_tambahan_order);
            id_barang_panen = (TextView) view.findViewById(R.id.id_barang_panen);
            thumbnail_order = (ImageView) view.findViewById(R.id.thumbnail_order);

            plus_aksi_order = (TextView) view.findViewById(R.id.plus_aksi_order);
            input_order_quantity = (TextView) view.findViewById(R.id.input_order_quantity);
            minus_aksi_order = (TextView) view.findViewById(R.id.minus_aksi_order);
        }
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        order = allorders.get(position);
        holder.grade_order.setText(order.getOrdergrades());
        holder.item_order.setText(order.getOrderItems());
        holder.price_order.setText(order.getOrderPricess());
        holder.price_order_real.setText(order.getOrderPricessReal());
        holder.quantity_available_order.setText(order.getPesanQuantitys());
        order.setOrderQuantitys(order.getPesanQuantitys());
        holder.input_order_quantity.setText(order.getOrderQuantitys());
        holder.satuan_order.setText(order.getOrderSatuans());
        holder.ket_tambahan_order.setText(order.getOrderKetTambahans());
        holder.position_holder = position;

        if (order.getPanenanThumbnailPath() != null) {
            Picasso.with(context)
                    .load(order.getPanenanThumbnailPath())
                    .fit() // Fix centerCrop issue: http://stackoverflow.com/a/20824141/1936697
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(holder.thumbnail_order);
        }

        holder.input_order_quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.toString().equals("")){
                    //setobject(holder.position_holder,Integer.parseInt(holder.quantity_available_order.getText().toString()));
                }else{
                    int news = Integer.parseInt(s.toString());
                    int old  = Integer.parseInt(allorders.get(holder.position_holder).getOrderQuantitys());

                    String minus_plus = ""; int selisih = 0;
                    minus_plus = (news > old )? "plus" : "minus";
                    minus_plus = (news == old )? "same" : "same";

                    selisih = (news > old )? news - old : old - news;
                    selisih = (news == old )? 0 : 0;

                    if(Integer.parseInt(s.toString()) <= Integer.parseInt(holder.quantity_available_order.getText().toString())){
                        if (Integer.parseInt(s.toString()) >= 0) {
                            int j = Integer.parseInt(s.toString());
                            setobject(holder.position_holder,j);
                        } else {
                            holder.input_order_quantity.setText("0");
                            setobject(holder.position_holder,0);
                        }
                    }else{
                        holder.input_order_quantity.setText(""+holder.quantity_available_order.getText());
                        setobject(holder.position_holder,Integer.parseInt(holder.quantity_available_order.getText().toString()));
                    }

                    int input = Integer.parseInt(holder.input_order_quantity.getText().toString());
                    int available = Integer.parseInt(holder.quantity_available_order.getText().toString());
                    if(input < available){
                        holder.input_order_quantity.setTextColor(Color.RED);
                    }else{
                        holder.input_order_quantity.setTextColor(Color.BLACK);
                    }
                    fragment.setJumlahTotal(Integer.parseInt(holder.price_order_real.getText().toString()) * selisih,minus_plus);

                }
            }
        });

        holder.minus_aksi_order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                holder.minus_aksi_order.setBackgroundResource(R.drawable.square_green);
                holder.minus_aksi_order.setTextColor(Color.WHITE);
                if (holder.input_order_quantity.getText().toString().equals("")){
                    holder.input_order_quantity.setText("0");
                    setobject(holder.position_holder,0);
                }else{
                    if (Integer.parseInt(holder.input_order_quantity.getText().toString()) >= 1 ) {
                        int j = Integer.parseInt(holder.input_order_quantity.getText().toString()) - 1;
                        holder.input_order_quantity.setText(""+j);
                        setobject(holder.position_holder,j);
                        fragment.setJumlahTotal(Integer.parseInt(holder.price_order_real.getText().toString()),"minus");
                    } else {holder.input_order_quantity.setText("0");
                        setobject(holder.position_holder,0);
                    }
                }
                int input = Integer.parseInt(holder.input_order_quantity.getText().toString()) - 1;
                int available = Integer.parseInt(holder.quantity_available_order.getText().toString()) - 1;

                if(input < available){
                    holder.input_order_quantity.setTextColor(Color.RED);
                }else{
                    holder.input_order_quantity.setTextColor(Color.BLACK);
                }
                holder.minus_aksi_order.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        holder.minus_aksi_order.setBackgroundResource(R.drawable.square_gray);
                        holder.minus_aksi_order.setTextColor(Color.BLACK);
                    }
                }, 200);
            }
        });

        holder.plus_aksi_order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                holder.plus_aksi_order.setBackgroundResource(R.drawable.square_green);
                holder.plus_aksi_order.setTextColor(Color.WHITE);
                if (holder.input_order_quantity.getText().toString().equals("")){
                    holder.input_order_quantity.setText("0");
                    setobject(holder.position_holder,0);
                }else{
                    if(Integer.parseInt(holder.input_order_quantity.getText().toString()) <= Integer.parseInt(holder.quantity_available_order.getText().toString())){
                        if (Integer.parseInt(holder.input_order_quantity.getText().toString()) >= 0) {
                            int j = Integer.parseInt(holder.input_order_quantity.getText().toString()) + 1;
                            holder.input_order_quantity.setText(""+j);
                            setobject(holder.position_holder,j);
                            fragment.setJumlahTotal(Integer.parseInt(holder.price_order_real.getText().toString()),"plus");
                        } else {holder.input_order_quantity.setText("0");
                            setobject(holder.position_holder,0);
                        }
                    }else{
                        Toast.makeText(view.getContext(), "Maximum Input" , Toast.LENGTH_SHORT).show(); //you can add data to the tag of your cardview in onBind... and retrieve it here with with.getTag().toString()..
                    }
                }
                int input = Integer.parseInt(holder.input_order_quantity.getText().toString()) - 1;
                int available = Integer.parseInt(holder.quantity_available_order.getText().toString()) - 1;

                if(input < available){
                    holder.input_order_quantity.setTextColor(Color.RED);
                }else{
                    holder.input_order_quantity.setTextColor(Color.BLACK);
                }
                holder.plus_aksi_order.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        holder.plus_aksi_order.setBackgroundResource(R.drawable.square_gray);
                        holder.plus_aksi_order.setTextColor(Color.BLACK);
                    }
                }, 200);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                int nh = (int) ( mIcon11.getHeight() * (512.0 / mIcon11.getWidth()) );
                mIcon11 = Bitmap.createScaledBitmap(mIcon11, 512, nh, true);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public int getItemCount() {
        return allorders.size();
    }

    public void setobject(int position,int quantity)
    {
        allorders.get(position).setOrderQuantitys(""+quantity);
    }

}
