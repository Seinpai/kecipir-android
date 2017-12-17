package com.kecipir.petani.rest;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import com.kecipir.petani.BuildConfig;
import com.kecipir.petani.entities.OrderSave;
import com.kecipir.petani.rest.request.DashboardRequest;
import com.kecipir.petani.rest.request.HistoryRequest;
import com.kecipir.petani.rest.request.NotificationReadRequest;
import com.kecipir.petani.rest.request.NotificationRequest;
import com.kecipir.petani.rest.request.OrderRequest;
import com.kecipir.petani.rest.request.OrderSaveRequest;
import com.kecipir.petani.rest.request.PanenanRequest;
import com.kecipir.petani.rest.request.PiutangRequest;
import com.kecipir.petani.rest.response.AuthResponse;
import com.kecipir.petani.rest.response.DashboardResponse;
import com.kecipir.petani.rest.response.HistoryResponse;
import com.kecipir.petani.rest.response.KategoriResponse;
import com.kecipir.petani.rest.response.NotificationReadResponse;
import com.kecipir.petani.rest.response.NotificationResponse;
import com.kecipir.petani.rest.response.OrderProsesResponse;
import com.kecipir.petani.rest.response.OrderResponse;
import com.kecipir.petani.rest.response.Page;
import com.kecipir.petani.rest.response.PanenanProsesResponse;
import com.kecipir.petani.rest.response.PanenanResponse;
import com.kecipir.petani.rest.response.PiutangResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Restful API Web Service.
 */
public interface Api {

    String BASE_URL = BuildConfig.BASE_URL;
    String AVATAR_URL = BASE_URL + "avatar/";
    String API = BASE_URL + "api/";
    String KEY = "base64:SKYhaYu7wl5mPTVRABy0TqULWTThcTcpKmmSuAImEVM=";

    @FormUrlEncoded
    @POST(API + "v1/auth/login")
    Call<AuthResponse> login(@Field("phone") String phone, @Field("password") String password, @Field("key") String key);

    @POST(API + "v1/kategori")
    Call<KategoriResponse> kategoriList();

    @POST(API + "v1/dashboard")
    Call<DashboardResponse> dashboard(@Body DashboardRequest request);

    @POST(API + "v1/notifikasi")
    Call<Page<NotificationResponse>> notification(@Body NotificationRequest request);

    @POST(API + "v1/notifikasi/baca")
    Call<NotificationReadResponse> notificationRead(@Body NotificationReadRequest request);

    @POST(API + "v1/panenan/list")
    Call<Page<PanenanResponse>> panenanList(@Body PanenanRequest request);

    @FormUrlEncoded
    @POST(API + "v1/panenan/proses")
    Call<PanenanProsesResponse> panenanProses(@Field("petaniBarangId") String petaniBarangId, @Field("tanggal") String tanggal, @Field("qty_awal") String qty_awal);

    @POST(API + "v1/order/list")
    Call<OrderResponse> orderList(@Body OrderRequest request);

    @POST(API + "v1/order/input")
    Call<OrderProsesResponse> orderProses(@Body OrderSaveRequest request);
    //Call<OrderProsesResponse> orderProses(@Field("id_barang_panen") List<String> id_barang_panen, @Field("qty_terkirim") List<String> qty_terkirim);

    @POST(API + "v1/pembayaran/piutang")
    Call<Page<PiutangResponse>> piutang(@Body PiutangRequest request);

    @POST(API + "v1/pembayaran/history")
    Call<Page<HistoryResponse>> history(@Body HistoryRequest request);

}