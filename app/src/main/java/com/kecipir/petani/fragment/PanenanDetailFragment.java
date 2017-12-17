package com.kecipir.petani.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kecipir.petani.BaseActivity;
import com.kecipir.petani.MainActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.callback.TokenCallback;
import com.kecipir.petani.rest.response.PanenanProsesResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.victor.loading.rotate.RotateLoading;

import retrofit2.Call;
import retrofit2.Response;

public class PanenanDetailFragment extends Fragment {
    private static final String TAG = PanenanFragment.class.getSimpleName();
    public EditText jumlah;
    private TextView minus, plus, submit;
    private Spinner spinner_category;
    private String[] arraySpinner;
    private String tanggal_panenan, pesan;
    private View mFragmentView;
    private AccountPreference mAccountPreference;
    private Api mApi;
    private ProgressDialog mProgressView;
    private AlertDialog alertDialog;
    private RotateLoading rotateLoading;



    //    private Context context;
    private FragmentManager fragmentManager;
    private Fragment fragment = null;

    public TextView grade, item_name, price_panenan, satuan_panenan_detail, ket_tambahan_panenan, id_petani_barang, qty_dipesan;
    public ImageView thumbnail;
    public int qty_panen, qtyPesan;

    Handler textNextHandler = new Handler();

    public PanenanDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mApi = ((BaseActivity) getActivity()).getApp().getRestApi();
        mAccountPreference = ((BaseActivity) getActivity()).getApp().getAccountPreference();

//        frameLoading = (RelativeLayout) View.findViewById(R.id.frame_loading);
//        frameBuffer = (RelativeLayout) view.findViewById(R.id.progress_view);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panenan_detail, container, false);
        TextView mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);

        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateloading);



        ((MainActivity) getActivity()).enableViews(true);
        getActivity().setTitle("");
        mTitle.setText(getArguments().getString("item_name_transition"));



        jumlah = (EditText) view.findViewById(R.id.input_panenan_quantity);
        minus = (TextView) view.findViewById(R.id.minus_aksi_panenan);
        plus = (TextView) view.findViewById(R.id.plus_aksi_panenan);
        submit = (TextView) view.findViewById(R.id.submit_panenan);
        grade = (TextView) view.findViewById(R.id.grade_paanenan_detail);
        item_name = (TextView) view.findViewById(R.id.item_panenan_detail);
        price_panenan = (TextView) view.findViewById(R.id.price_panenan_detail);
        satuan_panenan_detail = (TextView) view.findViewById(R.id.satuan_panenan_detail);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail_panenan_detail);
        id_petani_barang = (TextView) view.findViewById(R.id.petaniBarangId);
        qty_dipesan = (TextView) view.findViewById(R.id.qtyDipesan);

        //getArguments().getString("item_thumbnail_transition")));
        id_petani_barang.setText(getArguments().getString("item_id_transition"));
        grade.setText(getArguments().getString("item_grade_transition"));
        item_name.setText(getArguments().getString("item_name_transition"));
        price_panenan.setText(getArguments().getString("item_price_transition"));
        satuan_panenan_detail.setText("/" + getArguments().getString("item_satuan_transition"));
        jumlah.setText(getArguments().getString("item_quantity_transition"));
        qty_dipesan.setText(getArguments().getString("item_qtyDipesan_transition"));
        tanggal_panenan = getArguments().getString("item_tanggal_transition");

        /*if (getArguments().getString("item_thumbnail_transition") != null)
            new DownloadImageTask(thumbnail)
                    .execute(getArguments().getString("item_thumbnail_transition"));*/
        if (getArguments().getString("item_thumbnail_transition") != null) {
            Picasso.with(getContext())
                    .load(getArguments().getString("item_thumbnail_transition"))
                    //.transform(new CircleImageTransform())
                    .fit() // Fix centerCrop issue: http://stackoverflow.com/a/20824141/1936697
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(thumbnail);
        }

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minus.setBackgroundResource(R.drawable.button_green);
                minus.setTextColor(Color.WHITE);
                qty_panen = Integer.parseInt(jumlah.getText().toString());
                qtyPesan = Integer.parseInt(qty_dipesan.getText().toString());

                if (jumlah.getText().toString().equals("")) {
                    jumlah.setText("0");
                } else {
                    if ((qty_panen >= 1) && (qty_panen > qtyPesan)) {
                        qty_panen = qty_panen - 1;
                        jumlah.setText("" + qty_panen);
                    } else {
                        Toast.makeText(view.getContext(), "Anda tidak bisa mengurangi Panenan dibawah angka Pesanan ", Toast.LENGTH_SHORT).show();
                    }
                }
                minus.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        minus.setBackgroundResource(R.drawable.button_gray);
                        minus.setTextColor(Color.BLACK);
                    }
                }, 200);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plus.setBackgroundResource(R.drawable.button_green);
                plus.setTextColor(Color.WHITE);
                qty_panen = Integer.parseInt(jumlah.getText().toString());
                qtyPesan = Integer.parseInt(qty_dipesan.getText().toString());

                if (jumlah.getText().toString().equals("")) {
                    jumlah.setText("0");
                } else {
                    if (qty_panen >= 0) {
                        qty_panen = qty_panen + 1;
                        jumlah.setText("" + qty_panen);
                    } else {
                        jumlah.setText("0");
                    }
                }
                plus.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        plus.setBackgroundResource(R.drawable.button_gray);
                        plus.setTextColor(Color.BLACK);
                    }
                }, 200);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty_panen = Integer.parseInt(jumlah.getText().toString());
                qtyPesan = Integer.parseInt(qty_dipesan.getText().toString());

                if (qty_panen < qtyPesan) {
                    Toast.makeText(view.getContext(), "Anda tidak bisa mengurangi Panenan dibawah angka Pesanan ", Toast.LENGTH_SHORT).show();
                } else {
//                    showProgress(true);
//                    frameBuffer.setVisibility(View.VISIBLE);
                    rotateLoading.start();
                    submit.setEnabled(false);
                    SaveNew(id_petani_barang.getText().toString().trim(),tanggal_panenan, jumlah.getText().toString());





                    //Toast.makeText(view.getContext(), "" + psn , Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        if (savedInstanceState == null) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void save() {
        Call<PanenanProsesResponse> call = mApi.panenanProses(id_petani_barang.getText().toString().trim(), tanggal_panenan, jumlah.getText().toString());
        call.enqueue(new TokenCallback<PanenanProsesResponse>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<PanenanProsesResponse> call, Response<PanenanProsesResponse> response, boolean hasNewToken) {
                //if (response.isSuccessful()) {
                //Page<PanenanResponse> page = response.body();
                try {
                    PanenanProsesResponse read = response.body();
//                    mAdapter.appendList(panenan.getData().getlistOrder());

                    if (read.getData().getMessage() != null) {
                        Toast.makeText(getContext(), "" + read.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed Connection", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.d("commanderxx", "" + ex);
                    //}
                    //else{
                    Toast.makeText(getContext(), "Failed Insert or Update", Toast.LENGTH_SHORT).show();
                    //}
                }
            }

            @Override
            public Call<PanenanProsesResponse> recreateCall(String newToken) {
                return mApi.panenanProses(id_petani_barang.getText().toString().trim(), tanggal_panenan, jumlah.getText().toString());
            }

            @Override
            public void onFailure(Call<PanenanProsesResponse> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
                Toast.makeText(getContext(), "Connection Lost", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void SaveNew(final String idbarang, final String tgl_panen, final String jumlah)
    {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
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
                        rotateLoading.stop();

                        new AlertDialog.Builder(getContext())
                                .setTitle("Berhasil")
                                .setMessage("Tambah Panen Berhasil")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        submit.setEnabled(true);
                                    }
                                })
                                .show();
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
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
}
