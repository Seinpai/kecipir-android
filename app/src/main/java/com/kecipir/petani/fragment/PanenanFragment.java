package com.kecipir.petani.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kecipir.petani.BaseActivity;
import com.kecipir.petani.MainActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.adapter.PanenanAdapter;
import com.kecipir.petani.dialog.LoadingDialog;
import com.kecipir.petani.entities.TmpDbPanenan;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.callback.TokenCallback;
import com.kecipir.petani.rest.request.PanenanRequest;
import com.kecipir.petani.rest.response.KategoriResponse;
import com.kecipir.petani.rest.response.Page;
import com.kecipir.petani.rest.response.PanenanResponse;
import com.kecipir.petani.util.EmptyDataViewer;
import com.kecipir.petani.util.EndlessScrollListener;
import com.kecipir.petani.rest.callback.TokenCallback;
import com.roomorama.caldroid.CaldroidFragment;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.POST;

public class PanenanFragment extends Fragment implements PanenanAdapter.PageLoader{
    //private ViewPagerAdapter viewPagerAdapter;
    private Spinner spinner_category;
    private String[] arraySpinner;
    List<String> list;
    ArrayList<String> list2;

    private String PanenId,Tgl;
    private int QtyPanen;

    //SET API AND FRAGMENT TAG
    private static final String TAG = PanenanFragment.class.getSimpleName();
    private View mFragmentView, mFragmentView2;
    private AccountPreference mAccountPreference;
    private Api mApi;

    private LinearLayout linearLayout;
    //SET CARD AND LOADING
    private RecyclerView mRecyclerView;
    private View mLoadingView;
    private TextView mLoadingText;
    private View mDateButton;
    private TextView mDateSelected;
    private TextView mEmptyView;
    private PanenanAdapter mAdapter;
    private EmptyDataViewer mEmptyViewer;

    private EditText qty;
    private TextView minus, plus;

    //SET DATE
    public String tanggal,tanggal_set,tanggal_calender, hari_now;
    String tmp_hari = "";
    private String kategori,kategori_sebelumnya;
    private int mCurrentPage;
    private boolean mHasNext;
    public boolean mHasResponse;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;

    public EditText panenanRequest;

    private RotateLoading rotateLoading;

    private LinearLayout focused;
    public int getCounter;

    int flag = 0;
    int test;

    private TextView fab_save;

    Calendar c = Calendar.getInstance();
    String month_name[] = {"JAN","FEB","MAR","APR","MEI","JUN","JUL","AUG","SEP","OKT","NOV","DES"};
    String day_name[] = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    String namaPetani;
    String Join;
    String dayOfTheWeek;
    String args;

    String[] hari;
    int yr = c.get(Calendar.YEAR);
    int mnth = c.get(Calendar.MONTH);
    int dy = c.get(Calendar.DAY_OF_MONTH);
    int ttl = c.get(Calendar.DAY_OF_WEEK);
    int counter = 0;

    int qty_panen,qty_dipesan;

    //SET FOR SEARCH ACTION
    MenuItem action_search;
    MenuItem action_close;
    TextView mTitle;
    EditText mSearch;
    Integer buka_tutup = 1; //1 = tutup field, 2 buka field

    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;


    public PanenanFragment() {
        // Required empty public constructor
    }

//

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mApi = ((BaseActivity) getActivity()).getApp().getRestApi();
        mAccountPreference = ((BaseActivity) getActivity()).getApp().getAccountPreference();
        mHasResponse = false;
        mAdapter = new PanenanAdapter(getContext(), this);
        namaPetani = mAccountPreference.getName();



//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
//        Date d = new Date();
//        dayOfTheWeek = sdf.format(d);
//        dayOfTheWeek = tmp_hari;
        //Log.d("PanenanFragment",""+ mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_panenan, container, false);
        mFragmentView2 = inflater.inflate(R.layout.item_panenan_new, container, false);

        mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mSearch = (EditText) getActivity().findViewById(R.id.input_search);
        ((MainActivity)getActivity()).enableViews(false);;
        getActivity().setTitle("");
        mTitle.setText("Input Panenan");

        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.panenan_list);
        linearLayout = (LinearLayout) mFragmentView.findViewById(R.id.filters);

        rotateLoading = (RotateLoading) mFragmentView.findViewById(R.id.rotateloading);


        focused = (LinearLayout) mFragmentView.findViewById(R.id.focused);

        minus = (TextView) mFragmentView2.findViewById(R.id.minus_aksi_panenan);
        plus = (TextView) mFragmentView2.findViewById(R.id.plus_aksi_panenan);



        panenanRequest = (EditText) mFragmentView.findViewById(R.id.PanenanReq);
        fab_save = (TextView) mFragmentView.findViewById(R.id.fab_save);





        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                focused.requestFocus();

                String loading = "Loading";
                final String simpan = "simpan";

                fab_save.setBackground(getResources().getDrawable(R.drawable.button_square_red));
                fab_save.setText(loading);

                Toast.makeText(getContext(), "Mohon Tunggu", Toast.LENGTH_LONG).show();

                mAdapter.SaveAll();

//                int array = mAdapter.getItemCount()-1;

//                ArrayList<String[]> arrayList1;
//                arrayList1 = mAdapter.retreivedata();

//                System.out.println(arrayList1.size());

//                for(int pos = 0; pos < arrayList1.size(); pos ++){
//
//
//                    String[] a = arrayList1.get(pos);
//
//                    System.out.println(Arrays.toString(a));
//
//                }


//                Toast.makeText(getContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();

//                panenanRequest.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//                        if (editable.toString().equals("0")){
//                            rotateLoading.stop();
//
//                            fab_save.setBackground(getResources().getDrawable(R.drawable.button_orange));
//                            fab_save.setText(simpan);
//
//                        }else {
//                            rotateLoading.start();
//                        }
//                    }
//                });



//                for (int childCount = mRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
//                    PanenanAdapter.ViewHolder vh = (PanenanAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
//                    panenid = vh.id_petani_barang.toString();
//                }

//                int iii;

//                PanenanAdapter.ViewHolder vh = (PanenanAdapter.ViewHolder) mRecyclerView.findViewHolderForLayoutPosition(6);
//                PanenId = vh.id_petani_barang.getText().toString();
//                QtyPanen = Integer.parseInt(vh.quantity_panenan.getText().toString());
//                Tgl = vh.tanggal_panenan;
////
//                Log.i("","List Object = " +  iii);
//                Log.i("","panenid = " + PanenId+" Qty "+QtyPanen+ " Tanggal panen "+Tgl );

//                iii = mAdapter.getItemCount()-2;
//                List<JSONObject> myJSONObjects = new  ArrayList<JSONObject> ();
//                Log.i("","List Object has next = " +  iii);
//                for(int i=0; i < iii; i++) {
//                    PanenanAdapter.ViewHolder vh = (PanenanAdapter.ViewHolder) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
//                    PanenId = vh.id_petani_barang.getText().toString();
//                    QtyPanen = Integer.parseInt(vh.quantity_panenan.getText().toString());
//                    Tgl = vh.tanggal_panenan;
//
//
//                    JSONArray array = new JSONArray();
//                    JSONObject obj = new JSONObject();
//
//                    try {
//                        obj.put("ProductId", PanenId);
//                        obj.put("Qty" , QtyPanen);
//                        obj.put("Tgl" , Tgl);
//                        array.put(obj);
//                        myJSONObjects.add(obj);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                Log.i("","List Object = " +  myJSONObjects);



//                Log.i("","panenid = " + PanenId+" Qty "+QtyPanen+ " Tanggal panen "+Tgl );

//                editor.putBoolean("done", true);
//                editor.apply();

//                new TextWatcher(){
//                    boolean _ignore = false;
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//                        if (_ignore)
//                            return;
//
//                        _ignore = true; // prevent infinite loop
//                        // Change your text here.
//                        // myTextView.setText(myNewText);
////                        panenanRequest.setText(getCounter);
//                        Toast.makeText(getContext(), String.valueOf(editable), Toast.LENGTH_SHORT).show();
//
//                        _ignore = false; // release, so the TextWatcher start to listen again.
//                    }
//                };
//
//                new TextWatcher() {
//                    boolean _ignore = false; // indicates if the change was made by the TextWatcher itself.
//
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if (_ignore)
//                            return;
//
//                        _ignore = true; // prevent infinite loop
//                        // Change your text here.
//                        // myTextView.setText(myNewText);
//                        getCounter = mAdapter.getCounter();
//                        Toast.makeText(getContext(), getCounter, Toast.LENGTH_SHORT).show();
//
//                        if (getCounter == 0 ){
//                            new android.support.v7.app.AlertDialog.Builder(getContext())
//                                    .setTitle("Berhasil")
//                                    .setMessage("Save Panen Berhasil")
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                        }
//                                    })
//                                    .show();
//                        } else{
//                            Toast.makeText(getContext(), "Loading ...", Toast.LENGTH_SHORT).show();
//                        }
//                        _ignore = false; // release, so the TextWatcher start to listen again.
//                    }
//
//                };
//
//


//                for (int i = 0; i < test; i++ ){
//
//                    qty = (EditText) mRecyclerView.getChildAt(i).findViewById(R.id.input_panenan_quantity_new);
//
//                    JSONObject quantity = new JSONObject();
//                    try {
//                        quantity.put("qty", qty.getText().toString());
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    JSONArray jsonArray = new JSONArray();
//
//                    jsonArray.put(quantity);
//
//
//                    JSONObject obj = new JSONObject();
//                    try {
//                        obj.put("panenan", jsonArray);
//                    } catch (JSONException e){
//                        e.printStackTrace();
//                    }
//
//                    String jsonStr = obj.toString();
//
//                    System.out.println("jsonString: " + jsonStr);
//
//                }




//                btnSave(test);
//                Toast.makeText(getContext(), "Fab Clicked"+ text, Toast.LENGTH_SHORT).show();
            }
        });

//        minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                minus.setBackgroundResource(R.drawable.button_green);
//                minus.setTextColor(Color.WHITE);
//                qty_panen = Integer.parseInt(qty.getText().toString());
//
//                if (qty_panen >= 0) {
//                    qty_panen = qty_panen - 1;
//                    qty.setText("" + qty_panen);
//                } else {
//                    Toast.makeText(view.getContext(), "Anda tidak bisa mengurangi Panenan dibawah angka 0 ", Toast.LENGTH_SHORT).show();
//                }
//
//                minus.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        minus.setBackgroundResource(R.drawable.button_gray);
//                        minus.setTextColor(Color.BLACK);
//                    }
//                }, 200);
//            }
//        });
//
//        plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                plus.setBackgroundResource(R.drawable.button_green);
//                plus.setTextColor(Color.WHITE);
//                qty_panen = Integer.parseInt(qty.getText().toString());
//
//
//                if (qty_panen >= 0) {
//                    qty_panen = qty_panen + 1;
//                    qty.setText("" + qty_panen);
//                } else {
//                    qty.setText("0");
//                }
//
//                plus.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        plus.setBackgroundResource(R.drawable.button_gray);
//                        plus.setTextColor(Color.BLACK);
//                    }
//                }, 200);
//            }
//        });

        mDateButton   = mFragmentView.findViewById(R.id.choose_date);
        mDateSelected = (TextView) mFragmentView.findViewById(R.id.options_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
                Log.i("nama","petani "+namaPetani);
            }
        });



        mLoadingView = mFragmentView.findViewById(R.id.loading_view);
        mLoadingText = (TextView) mFragmentView.findViewById(R.id.loading_text);
        mLoadingText.setText("Loading...");
        mEmptyView = (TextView) mFragmentView.findViewById(R.id.empty_view);
        mEmptyView.setText("Tidak ada Item Panenan");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row
//        linearLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
//        getKategori();

        mDateSelected.setText(new StringBuilder()
                    .append(tanggal_calender));

//        Log.i("TANGGAL :"," "+tanggal+ " TANGGAL KALENDER : "+ tanggal_calender + " TANGGAL SET :" + tanggal_set);

        tanggal             = tanggal_set;
            kategori            = "SEMUA";
            kategori_sebelumnya = "SEMUA";
        mAdapter.setList(null);
            getPanenan();

        //mLoadingView.setVisibility(View.GONE);
        if (mHasResponse == true) {
            mLoadingView.setVisibility(View.GONE);
        }

        return mFragmentView;
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
                    getPanenan(page);

                    if (flag == 0 ){
                        ViewGroup.LayoutParams var = mRecyclerView.getLayoutParams();
                        var.height = mRecyclerView.getHeight() - 180;
                        Log.i("","height = "+ var.height);
                        mRecyclerView.setLayoutParams(var);
                        flag = +1;
                    }
                }
            }
        });


        mEmptyViewer = new EmptyDataViewer(mEmptyView, mAdapter, 1);
        mAdapter.registerAdapterDataObserver(mEmptyViewer);

        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter.getItemCount() == 0 && !mHasResponse) {

            mDateSelected.setText(new StringBuilder()
//                    .append("").append(day_name[dy])
                    .append("").append(dy)
                    .append(" ").append(month_name[mnth])
                    .append(" ").append(yr));
            tanggal_calender = "" + mDateSelected.getText();
            if(mnth + 1 < 10){
                tanggal             = yr+"-0"+(mnth+1)+"-"+dy+"";
            }else{
                tanggal             = yr+"-"+(mnth+1)+"-"+dy+"";
            }
            tanggal_set = tanggal;
            kategori            = "SEMUA";
            kategori_sebelumnya = "SEMUA";
            getPanenan();

            hari_now = day_name[ttl-1];
            Log.i("TANGGAL ON CREATE:"," "+tanggal+ " TANGGAL KALENDER : "+ tanggal_calender + " TANGGAL SET :" + tanggal_set+" TANGGAL DISABLE " + hari_now+" "+ttl);
            enabledDate(namaPetani,hari_now);


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



    private void btnSave(final int test){

        Toast.makeText(getContext(), test, Toast.LENGTH_SHORT).show();

    }



    private void getKategori() {
        Call<KategoriResponse> call = mApi.kategoriList();
        call.enqueue(new TokenCallback<KategoriResponse>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response, boolean hasNewToken) {
                spinner_category =  ((Spinner) mFragmentView.findViewById(R.id.options_category));

                ArrayAdapter<String> options = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner);
                options.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                options.add(getResources().getString(R.string.all).toUpperCase());

                if (response.isSuccessful()) {
                    KategoriResponse kategorires = response.body();

                    if(kategorires.getData().getKategoriArr() != null) {
                        for (final KategoriResponse.Kategoris data : kategorires.getData().getKategoriArr()) {
                            options.add(data.getKategori().toUpperCase());
                        }
                    }
                    options.notifyDataSetChanged();
                    spinner_category.setAdapter(options);
                    spinner_category.setSelection(options.getPosition(kategori));

                    spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                            kategori = arg0.getItemAtPosition(position).toString();
                            
                            if(kategori_sebelumnya.equals(kategori)) {

                            } else {
                                mAdapter.setList(null);
                                mLoadingView.setVisibility(View.VISIBLE);
                                mLoadingText.setText("Loading...");
                                getPanenan();
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            }

            @Override
            public Call<KategoriResponse> recreateCall(String newToken) {
                return mApi.kategoriList();
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }


    private void getPanenan() {
        getPanenan(1);
    }
    
    private void getPanenan(final int page) {
//        mLoadingView.setVisibility(View.VISIBLE);
        Call<Page<PanenanResponse>> call = null;

        final PanenanRequest params = new PanenanRequest();
        params.setTanggal(tanggal);
        params.setKategori(kategori);
        params.setPage(page);
        params.setValue(mSearch.getText().toString());
        call = mApi.panenanList(params);
        call.enqueue(new TokenCallback<Page<PanenanResponse>>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<Page<PanenanResponse>> call, Response<Page<PanenanResponse>> response, boolean hasNewToken) {
                if (response.isSuccessful()) {
                    Page<PanenanResponse> panenan = response.body();
                    mCurrentPage = panenan.getCurrentPage();
                    mHasNext = panenan.getNextPageUrl() != null;

                    mAdapter.appendList(panenan.getData(), mHasNext);
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }


                }
                mHasResponse = true;
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public Call<Page<PanenanResponse>> recreateCall(String newToken) {
                final PanenanRequest params = new PanenanRequest();
                params.setTanggal(tanggal);
                params.setKategori(kategori);
                params.setPage(page);

                return mApi.panenanList(params);
            }

            @Override
            public void onFailure(Call<Page<PanenanResponse>> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }

    @Override
    public void loadPage(int page) {
        Log.d(TAG, "loadPage " + page);
        getPanenan(page);
    }


    private void enabledDate(final String idPetani, final String haridisable){
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        final String url = "http://petani.kecipir.com/api/petani/AppPanen.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String haripanen = jsonObject.getString("hari_panen");
//                            String hari2 = jsonObject.getString("hari");

                            JSONArray jArrDel = new JSONArray(haripanen);

                            int length = jArrDel.length();
                            final String[] hari = new String[length];
                            Log.i("length = >", ""+ length);

                            for (int i = 0; i < length; i++){
                                JSONObject jobj = (JSONObject) jArrDel.get(i);
                                hari[i] = jobj.getString("hari");
                                Log.i("hari = >", ""+ hari[i]);

                            }

//                            StringBuilder builder = new StringBuilder();
//                            for(String s : hari2) {
//                                builder.append(s);
//                            }
//                            String str = builder.toString();

//                            Log.i("Hari 2 = >", ""+ hari2);


                            String str = haripanen.replace(":","");
                            String str2 = str.replace("{","");
                            String str3 = str2.replace("}","");
                            String str4 = str3.replace("[","");
                            String str5 = str4.replace("]","");
                            String str6 = str5.replace("\"","");
                            String str7 = str6.replace("hari","");
                            String str8 = str7.replace(","," ");

//                            Toast.makeText(getContext(), haridisable.toUpperCase()  , Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(),  str8.toUpperCase().toString() , Toast.LENGTH_SHORT).show();

                            if (str8.toUpperCase().toString().contains(haridisable.toUpperCase())){
                                args = "Anda bisa panen";
//                                Toast.makeText(getContext(), args , Toast.LENGTH_SHORT).show();
                                counter = 1;
                            }else{
                                args="Anda Tidak Bisa Panen Pada Tanggal Ini";
//                                Toast.makeText(getContext(), args , Toast.LENGTH_SHORT).show();

                                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setTitle(args);
                                alertDialog.setMessage("Mohon Pilih Tanggal Panen Yang Lain");
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                mDateButton.performClick();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

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
                params.put("tag", "date_panen");
                params.put("petani", idPetani);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,2,2f));
        queue.add(postRequest);
    }


//    private void SaveAllPanens(final String id_panen, String quantity_panen, String tgl_panen){
//        JSONObject js = new JSONObject();
//
//        mAdapter.SaveAll();
//    }

    private DatePickerDialog.OnDateSetListener
            datePickerListener = new  DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay)
        {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Date date = new Date(selectedYear, selectedMonth, selectedDay-1);
            String dayOfWeek = simpledateformat.format(date);
        }
    };


    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
//        datePickerDialog.setDisabledDays();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                String dayOfWeek = DateFormat.format("EE", new GregorianCalendar(year, monthOfYear, dayOfMonth)).toString();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDateSelected.setText(new StringBuilder()
//                        .append(day_name[dayOfMonth]).append(" ")
                        .append(dayOfMonth).append(" ")
                        .append(month_name[monthOfYear]).append(" ")
                        .append(year));
                tanggal_calender = "" + mDateSelected.getText();

                if(monthOfYear+1 < 10){
                    tanggal          = year+"-0"+(monthOfYear+1)+"-"+dayOfMonth;
                }else{
                    tanggal          = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                }
                tanggal_set = tanggal;
                mAdapter.setList(null);
                getPanenan();


                int calc = monthOfYear+1;
                if (String.valueOf(calc).equals("1")){
                    Log.i("TANGGAL sukses","sukses :" + dayOfWeek);
                }else{
                    Log.i("TANGGAL gagal","gagal :" + dayOfWeek);
                }

                tmp_hari = dayOfWeek;

                Log.i("TANGGAL ON DPICKER:"," "+tanggal+ " TANGGAL KALENDER : "+ tanggal_calender + " TANGGAL SET :" + tanggal_set+" TANGGAL DISABLE " + hari_now+" "+ttl);

                enabledDate(namaPetani,tmp_hari);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
        datePickerDialog.setCancelable(false);
        Button buttonNo = datePickerDialog.getButton(datePickerDialog.BUTTON_NEGATIVE);
        buttonNo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        MenuItem envelope = menu.findItem(R.id.action_messages);
        envelope.setVisible(false);

        action_close = menu.findItem(R.id.action_close);
        action_search = menu.findItem(R.id.action_search);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //Toast.makeText(getContext(), " MASUK SINI ", Toast.LENGTH_SHORT).show();

            //action_search.setVisible(false);
            if (buka_tutup == 1) {
                mTitle.setVisibility(View.GONE);
                mSearch.setVisibility(View.VISIBLE);
                action_close.setVisible(true);
                buka_tutup = 2;
            }else{
                mAdapter.setList(null);
                getPanenan();
            }
        }

        if (id == R.id.action_close) {
            //Toast.makeText(getContext(), " MASUK SINI ", Toast.LENGTH_SHORT).show();

            //action_search.setVisible(false);
                mTitle.setVisibility(View.VISIBLE);
                mSearch.setVisibility(View.GONE);
                action_close.setVisible(false);
                mSearch.setText("");
                mAdapter.setList(null);
                getPanenan();
                buka_tutup = 1;
        }

        return super.onOptionsItemSelected(item);
    }

}
