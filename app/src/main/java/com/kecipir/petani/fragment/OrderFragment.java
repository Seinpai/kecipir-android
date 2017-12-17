package com.kecipir.petani.fragment;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kecipir.petani.BaseActivity;
import com.kecipir.petani.MainActivity;
import com.kecipir.petani.R;
import com.kecipir.petani.adapter.OrderAdapter;
import com.kecipir.petani.entities.OrderObject;
import com.kecipir.petani.entities.OrderSave;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.callback.TokenCallback;
import com.kecipir.petani.rest.request.NotificationReadRequest;
import com.kecipir.petani.rest.request.OrderRequest;
import com.kecipir.petani.rest.request.OrderSaveRequest;
import com.kecipir.petani.rest.response.KategoriResponse;
import com.kecipir.petani.rest.response.NotificationReadResponse;
import com.kecipir.petani.rest.response.OrderProsesResponse;
import com.kecipir.petani.rest.response.OrderResponse;
import com.kecipir.petani.util.EmptyDataViewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private static final String TAG = OrderFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Spinner spinner_category;
    private String[] arraySpinner;
    List<String> list;

    private View mLoadingView;
    private View mDateButton;
    private TextView mDateSelected;
    private TextView mLoadingText;
    private TextView jmlHargaOrder,jmlHargaOrderReal;
    private TextView mEmptyView;
    private EmptyDataViewer mEmptyViewer;
    private RecyclerView mRecyclerView;
    private View mFragmentView;
    private DatePicker datePicker;
    private Calendar calendar;
    private Button submitOrder;

    private OrderAdapter mAdapter;
    private AccountPreference mAccountPreference;
    private Api mApi;

    private String tanggal,tanggal_set,tanggal_calender;
    private String kategori, kategori_sebelumnya;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private int id;
    Calendar c = Calendar.getInstance();
    String month_name[] = {"JAN","FEB","MAR","APR","MEI","JUN","JUL","AUG","SEP","OKT","NOV","DES"};
    int yr = c.get(Calendar.YEAR);
    int mnth = c.get(Calendar.MONTH);
    int dy = c.get(Calendar.DAY_OF_MONTH);
    List<OrderSave> ordersave = new ArrayList<OrderSave>();


    public OrderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((BaseActivity) getActivity()).getApp().getRestApi();
        mAccountPreference = ((BaseActivity) getActivity()).getApp().getAccountPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_order, container, false);
        TextView mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        ((MainActivity)getActivity()).enableViews(false);
        getActivity().setTitle("");
        mTitle.setText("Order");

        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.order_list);
        mDateButton   = mFragmentView.findViewById(R.id.choose_date);
        mDateSelected = (TextView) mFragmentView.findViewById(R.id.options_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        mLoadingView = mFragmentView.findViewById(R.id.loading_view);
        mLoadingText = (TextView) mFragmentView.findViewById(R.id.loading_text);
        mLoadingText.setText("Loading...");
        mEmptyView = (TextView) mFragmentView.findViewById(R.id.empty_view);
        mEmptyView.setText("Tidak ada Item Order");
        jmlHargaOrder = (TextView) mFragmentView.findViewById(R.id.jml_harga_order);
        jmlHargaOrderReal = (TextView) mFragmentView.findViewById(R.id.jml_harga_order_real);
        submitOrder = (Button) mFragmentView.findViewById(R.id.submit_order);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row
        linearLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        getKategori();

        mDateSelected.setText(new StringBuilder()
                .append(tanggal_calender));
        tanggal             = tanggal_set;
        kategori            = "SEMUA";
        kategori_sebelumnya = "SEMUA";
        getOrder();

        submitOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int size = mAdapter.allorders.size();
                if (size > 0) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    submitOrder.setEnabled(false);
                    for (int i = 0; i < size; i++) {
                        //Log.d("AllOrders", mAdapter.allorders.get(i).getIdBarangPanen());
                        String id       = mAdapter.allorders.get(i).getIdBarangPanen();
                        String quantity = mAdapter.allorders.get(i).getOrderQuantitys();
                        ordersave.add(new OrderSave(id,quantity));
                    }
                    Ordersave();
                }
            }
        });
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try{
            if (savedInstanceState == null) {
                tanggal = getArguments().getString("item_tanggal_notif_transition");

                String[] separated = tanggal.split("-");
                yr = Integer.parseInt(separated[0]);
                mnth = Integer.parseInt(separated[1]) -1;
                dy = Integer.parseInt(separated[2]);

                mDateSelected.setText(new StringBuilder()
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
                kategori = "SEMUA";
                kategori_sebelumnya = "SEMUA";

                try {
                    getArguments().getString("from_notif_transition");
                    setReadNotif();
                }
                catch(Exception ex){}
                finally {
                    getOrder();
                }
            }
        }
        catch(Exception ex){
            if (savedInstanceState == null) {
                mDateSelected.setText(new StringBuilder()
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
                kategori = "SEMUA";
                kategori_sebelumnya = "SEMUA";
                getOrder();
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mEmptyViewer != null) {
            mAdapter.unregisterAdapterDataObserver(mEmptyViewer);
        }
        mRecyclerView = null;

        super.onDestroyView();
    }

    private void Ordersave() {
        final OrderSaveRequest update = new OrderSaveRequest();
        update.setSchedules(ordersave);

        Call<OrderProsesResponse> call = mApi.orderProses(update);
        call.enqueue(new TokenCallback<OrderProsesResponse>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<OrderProsesResponse> call, Response<OrderProsesResponse> response, boolean hasNewToken) {
                //if (response.isSuccessful()) {
                //Page<PanenanResponse> page = response.body();
                try {
                    OrderProsesResponse read = response.body();
//                    mAdapter.appendList(panenan.getData().getlistOrder());

                    if (read.getData().getMessage() != null) {
                        Toast.makeText(getContext(), "" + read.getData().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("OrderTrack", "2");
                        refreshFragment();
                    } else {
                        Toast.makeText(getContext(), "Failed Connection", Toast.LENGTH_SHORT).show();
                        Log.d("OrderTrack", "3");
                        refreshFragment();
                    }
                }catch (Exception ex) {
                    Log.d("commanderxx",""+ ex);
                    Log.d("OrderTrack", "4");
                    //}
                    //else{
                    Toast.makeText(getContext(), "FAILED Insert Data", Toast.LENGTH_SHORT).show();
                    refreshFragment();
                    //}
                }
            }

            @Override
            public Call<OrderProsesResponse> recreateCall(String newToken) {
                final OrderSaveRequest update = new OrderSaveRequest();
                update.setSchedules(ordersave);
                return mApi.orderProses(update);
            }

            @Override
            public void onFailure(Call<OrderProsesResponse> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }

    private void setReadNotif() {
        final NotificationReadRequest params = new NotificationReadRequest();
        params.setTanggalOrder(tanggal);
        Call<NotificationReadResponse> call;
        try {
            call = mApi.notificationRead(params);

        call.enqueue(new TokenCallback<NotificationReadResponse>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<NotificationReadResponse> call, Response<NotificationReadResponse> response, boolean hasNewToken) {

                if (response.isSuccessful()) {
                    //Page<PanenanResponse> page = response.body();
                    NotificationReadResponse read = response.body();
//                    mAdapter.appendList(panenan.getData().getlistOrder());

                    if (read.getData().getMessage() != null) {
                     //   Toast.makeText(getContext(), ""  + read.getData().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                    else{
                       // Toast.makeText(getContext(), "FAILED" , Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Toast.makeText(getContext(), "FAILED2" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public Call<NotificationReadResponse> recreateCall(String newToken) {
                final NotificationReadRequest params = new NotificationReadRequest();
                params.setTanggalOrder(tanggal);
                return mApi.notificationRead(params);
            }

            @Override
            public void onFailure(Call<NotificationReadResponse> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
        }
        catch(Exception ex)
        {
            Log.d("CheckThisOut", ""+ex);
        }
    }

    private void getOrder() {
        final OrderRequest params = new OrderRequest();
        params.setTanggal(tanggal);
        params.setKategori(kategori);

        Call<OrderResponse> call = mApi.orderList(params);
        call.enqueue(new TokenCallback<OrderResponse>(mApi, mAccountPreference) {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response, boolean hasNewToken) {
                spinner_category =  ((Spinner) mFragmentView.findViewById(R.id.options_category));

                ArrayAdapter<String> options = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner);
                options.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                options.add(getResources().getString(R.string.all).toUpperCase());
                
                if (response.isSuccessful()) {
                    OrderResponse order = response.body();

                    if(order.getData().getlistOrder() != null) {
                        List<OrderObject> recentSongs = new ArrayList<OrderObject>();
                        for (final OrderResponse.Listorders data : order.getData().getlistOrder()) {
                            //aArrayAdapter.add(data.getKategori());
                            Log.d("LISTORDER",data.getPanenanItemPanenans());
                            recentSongs.add(new OrderObject(data.getIdBarangPanen(),data.getPanenangrades(), data.getPanenanItemPanenans(),data.getPanenanPricess(),data.getQuantityDiesans(),data.getQuantityOrders(),"/"+data.getPanenanSatuans(),"", data.getPanenanThumbnailPath(),data.getOrderPricessReal()));
                        }
                        //Log.d("PanenanFragmentAdapter",""+ mAdapter);
                        mAdapter = new OrderAdapter(getActivity(), recentSongs, OrderFragment.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    jmlHargaOrder.setText(order.getData().getJumlah().getTotalAmountCurr());
                    jmlHargaOrderReal.setText(order.getData().getJumlah().getTotalAmount());
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public Call<OrderResponse> recreateCall(String newToken) {
                final OrderRequest params = new OrderRequest();
                params.setTanggal(tanggal);
                params.setKategori(kategori);


                return mApi.orderList(params);
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }

    public void setJumlahTotal(int jumlah, String aksi){
        int Price = Integer.parseInt(jmlHargaOrderReal.getText().toString());
        int amount = 0;

        if(aksi.equals("minus")) {
            amount = Price - jumlah;
        } else if(aksi.equals("plus")) {
            amount = Price + jumlah;
        } else {
            amount = Price - 0;
        }
        jmlHargaOrderReal.setText("" + amount);
        jmlHargaOrder.setText("Rp. " + amount);
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
                                //mAdapter.setList(null);
                                mLoadingView.setVisibility(View.VISIBLE);
                                mLoadingText.setText("Loading...");
                                getOrder();
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

    public void refreshFragment(){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        OrderFragment of = new OrderFragment();
        Bundle args = new Bundle();
        args.putString("item_tanggal_notif_transition", tanggal);
        of.setArguments(args);

        //Create a bundle to pass data, add data, set the bundle to your fragment and:
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, of).addToBackStack(null).commit();
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        //datePickerDialog.setDisabledDays();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDateSelected.setText(new StringBuilder()
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
                //mAdapter.setList(null);
                getOrder();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
