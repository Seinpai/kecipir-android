package com.kecipir.petani.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.kecipir.petani.R;
import com.kecipir.petani.entities.TmpDbPanenan;
import com.kecipir.petani.fragment.PanenanDetailFragment;
import com.kecipir.petani.fragment.PanenanFragment;
import com.kecipir.petani.rest.response.PanenanResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


//class panenan {int id;int quantity;
//    public panenan(int id, int quantity) {
//        this.id = id;
//        this.quantity = quantity;
//    }
//}

public class PanenanAdapter extends RecyclerView.Adapter<PanenanAdapter.ViewHolder> implements Paging {




    private TmpDbPanenan Dbpanenan;

//    public List<JSONObject> myJSONObjects;

    public int counter;


    private Context context;
    private List<PanenanResponse> allpanenans;
    private boolean hasNext;
    private PanenanFragment fragment;
    public ArrayList<String[]> arrayList;

    public List<JSONObject> myJSONObjects = new  ArrayList<JSONObject> ();






    public interface PageLoader {
        void loadPage(int page);
    }





    public PanenanAdapter(Context context/*, List<PanenanResponse> allpanenans*/, PanenanFragment fragment) {
        this.context = context;
        this.allpanenans = allpanenans;
        this.fragment = fragment;

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView grade, item_name,price_panenan,satuan_panenan,ket_tambahan_panenan,id_petani_barang,qty_dipesan,btn_plus,btn_minus;
        public CardView cardView;
        public String tanggal_panenan;
        public EditText quantity_panenan;
        public RelativeLayout RlHolder;
        View progress;

        private AdapterView.OnItemClickListener listener;



        public ViewHolder(View view, boolean isProgress) {
            super(view);
            if (isProgress) {
                progress = view.findViewById(R.id.loading_progress);
            } else {
                RlHolder = (RelativeLayout)view.findViewById(R.id.RlHolder);
//                grade = (TextView) view.findViewById(R.id.grade_panenan_new);
                item_name = (TextView) view.findViewById(R.id.item_panenan_new);
                price_panenan = (TextView) view.findViewById(R.id.price_panenan_new);
                quantity_panenan = (EditText) view.findViewById(R.id.input_panenan_quantity_new);
                satuan_panenan = (TextView) view.findViewById(R.id.satuan_panenan_new);
                ket_tambahan_panenan = (TextView) view.findViewById(R.id.ket_tambahan_panenan_new);
                id_petani_barang = (TextView) view.findViewById(R.id.petaniBarangId_new);
                qty_dipesan = (TextView) view.findViewById(R.id.qtyDipesan_new);
                cardView = (CardView) view.findViewById(R.id.card_view_new);
                btn_plus = (TextView) view.findViewById(R.id.plus_aksi_panenan_new);
                btn_minus = (TextView) view.findViewById(R.id.minus_aksi_panenan_new);
            }
        }
    }

    @Override
    public PanenanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TmpDbPanenan DbPanenan = new TmpDbPanenan(context);

//        DbPanenan.DeletePanenans();


        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false);
            return new PanenanAdapter.ViewHolder(view, true);
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_panenan_new, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view,false);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        TmpDbPanenan DbPanenan = new TmpDbPanenan(context);


        myJSONObjects.clear();

        if (position == allpanenans.size()) {
            if (hasNext) {
                holder.progress.setVisibility(View.VISIBLE);

            } else {
                holder.progress.setVisibility(View.GONE);
            }
            return;
        }




//        DbPanenan.DeletePanenans();

//        arrayList = new ArrayList<String>();


//        final Cursor cursor = DbPanenan.getAllPanenans();

//        String [] columns = new String[] {
//                DbPanenan.PANENAN_COLUMN_ID,
//                DbPanenan.PANENAN_COLUMN_NAMA,
//                DbPanenan.PANENAN_COLUMN_HARGA,
//                DbPanenan.PANENAN_COLUMN_QUANTITY
//        };


//        final String id = data.getpetaniBarangId();
//        final String nama = data.getPanenanItemPanenans();
//        final String harga = data.getPanenanPricess();
//        final String quantity = data.getPanenanQuantitys();
//        holder.grade.setText(data.getPanenangrades());
        PanenanResponse data = allpanenans.get(position);
        holder.item_name.setText(data.getPanenanItemPanenans());
        holder.price_panenan.setText(data.getPanenanPricess());
        holder.quantity_panenan.setText(data.getPanenanQuantitys());
        holder.satuan_panenan.setText(data.getPanenanSatuans());
        holder.ket_tambahan_panenan.setText("");
        holder.id_petani_barang.setText(data.getpetaniBarangId());
        holder.qty_dipesan.setText(data.getPesananQuantitys());
        holder.tanggal_panenan = (fragment.tanggal);

//        DbPanenan.insertPanenan(Integer.parseInt(id), nama, harga, Integer.parseInt(quantity));
//        Log.i("Insert temporary db -> ", "Sukses 1");
//        Log.i("Page Load ", "Sukses");



        holder.quantity_panenan.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    counter = counter + 1;

                    final ArrayList<String> list = new ArrayList<String>();

                    final int qty = Integer.parseInt(holder.quantity_panenan.getText().toString());
                    final String barangId = String.valueOf(holder.id_petani_barang.getText());
                    final String tgl = String.valueOf(holder.tanggal_panenan);

                    list.add(barangId+"/"+String.valueOf(qty)+"/"+tgl);

                    Log.d("value", String.valueOf(list.size()));


                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("ProductId", barangId);
                        obj.put("Qty" , qty);
                        obj.put("Tgl" , tgl);
                        myJSONObjects.add(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



//                    List<JSONObject> myJSONObjects = new  ArrayList<JSONObject> (allpanenans.size());
//
//                    JSONObject obj = new JSONObject();
//                    try {
//                        obj.put("productid", barangId);
//                        obj.put("qty", qty);
//                        obj.put("tgl", tgl);
//                        myJSONObjects.add(obj);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    System.out.println(myJSONObjects.size());




//                    arrayList = new ArrayList<String[]>();
//                    arrayList.add(new String[]{String.valueOf(qty),barangId,tgl});

//                    Toast.makeText(context, "tidak fokus", Toast.LENGTH_SHORT).show();
//                    SaveNew(String.valueOf(barangId), tgl, String.valueOf(qty));

                }
            }
        });









//        arrayList.add(data.getPanenanQuantitys());


        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                List<panenan> list=new ArrayList<panenan>();
//                counter = counter + 1;

                holder.quantity_panenan.requestFocus();

                final int qty = Integer.parseInt(holder.quantity_panenan.getText().toString())+1;
                final String barangId = String.valueOf(holder.id_petani_barang.getText());
                final String tgl = String.valueOf(holder.tanggal_panenan);


                holder.quantity_panenan.setText(String.valueOf(qty));


                Log.i("","holder Object = " +  myJSONObjects);


//                Toast.makeText(context, "Loading ....", Toast.LENGTH_SHORT).show();
//                SaveNew(String.valueOf(barangId), tgl, String.valueOf(qty));

            }
        });

        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(holder.quantity_panenan.getText().toString()) < 1){
                    Toast.makeText(context, " Tidak Bisa Kurang Dari 0 ", Toast.LENGTH_SHORT).show();
                } else {
//                    counter = counter + 1;

                    holder.quantity_panenan.requestFocus();


                    final int qty = Integer.parseInt(holder.quantity_panenan.getText().toString())-1;
                    final String barangId = String.valueOf(holder.id_petani_barang.getText());
                    final String tgl = String.valueOf(holder.tanggal_panenan);

                    holder.quantity_panenan.setText(String.valueOf(qty));

//                    Toast.makeText(context, "Loading ....", Toast.LENGTH_SHORT).show();
//                    SaveNew(String.valueOf(barangId), tgl, String.valueOf(qty));
                }
            }
        });





//        holder.cardView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                PanenanDetailFragment myFragment = new PanenanDetailFragment();
//
//                //send data
//                Bundle args = new Bundle();
//                args.putString("item_id_transition", holder.id_petani_barang.getText().toString());
//                args.putString("item_tanggal_transition", holder.tanggal_panenan);
//                args.putString("item_name_transition", holder.item_name.getText().toString());
//                args.putString("item_grade_transition", holder.grade.getText().toString());
//                args.putString("item_price_transition", holder.price_panenan.getText().toString());
//                args.putString("item_thumbnail_transition", holder.thumbnail_path.getText().toString());
//                args.putString("item_satuan_transition", holder.satuan_panenan.getText().toString());
//                args.putString("item_quantity_transition", holder.quantity_panenan.getText().toString());
//                args.putString("item_qtyDipesan_transition", holder.qty_dipesan.getText().toString());
//                myFragment.setArguments(args);
//
//                //Create a bundle to pass data, add data, set the bundle to your fragment and:
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, myFragment).addToBackStack(null).commit();
//            }
//        });

//        holder.thumbnail.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                PanenanDetailFragment myFragment = new PanenanDetailFragment();
//
//                //send data
//                Bundle args = new Bundle();
//                args.putString("item_id_transition", holder.id_petani_barang.getText().toString());
//                args.putString("item_tanggal_transition", holder.tanggal_panenan);
//                args.putString("item_name_transition", holder.item_name.getText().toString());
//                args.putString("item_grade_transition", holder.grade.getText().toString());
//                args.putString("item_price_transition", holder.price_panenan.getText().toString());
//                args.putString("item_thumbnail_transition", holder.thumbnail_path.getText().toString());
//                args.putString("item_satuan_transition", holder.satuan_panenan.getText().toString());
//                args.putString("item_quantity_transition", holder.quantity_panenan.getText().toString());
//                args.putString("item_qtyDipesan_transition", holder.qty_dipesan.getText().toString());
//                myFragment.setArguments(args);
//
//                //Create a bundle to pass data, add data, set the bundle to your fragment and:
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, myFragment).addToBackStack(null).commit();
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return allpanenans == null ? 0 : allpanenans.size();
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




    public ArrayList<String[]> retreivedata(){
        return arrayList;
    }





    private void SaveNew(final String idbarang, final String tgl_panen, final String jumlah)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://petani.kecipir.com/api/petani/AppPanen.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
//                        showProgress(false);
//                        frameBuffer.setVisibility(View.INVISIBLE);
//                        rotateLoading.stop();

                        counter = counter - 1;

                        Log.i("Counter ", " Remaining - > " + counter);

                        fragment.panenanRequest.setText(String.valueOf(counter));

                        Log.i("Update ", " Berhasil ");

                        if (counter == 0){
                            Log.i("Request ", " Done ");
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());

                        Toast.makeText(context, "Request Timed Out", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Failed Insert or Update", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tag", "publish_panen");
                params.put("id_barang", idbarang);
                params.put("tgl_publish", tgl_panen);
                params.put("jml_publish", jumlah);


                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,2,2f));
        queue.add(postRequest);

    }


    public void SaveAll(){
        Log.i("","Masuk sini");


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("post",myJSONObjects);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        AndroidNetworking.post("https://httpbin.org/post")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("",response);

                    }

                    @Override
                    public void onError(ANError anError) {

                        anError.printStackTrace();
                    }
                });
    }


    public int getCounter(){
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
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
