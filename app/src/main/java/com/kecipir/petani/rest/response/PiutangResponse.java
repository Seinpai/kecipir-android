package com.kecipir.petani.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by UMAR on 7/25/2017.
 */

public class PiutangResponse implements Parcelable {

    private String hari;
    private String tanggal_terdaftar;
    private String nominal;
    private String tanggal_jatuh_tempo;

    public PiutangResponse() {}

    protected PiutangResponse(Parcel in) {
        hari = in.readString();
        tanggal_terdaftar = in.readString();
        nominal = in.readString();
        tanggal_jatuh_tempo = in.readString();
    }

    public static final Creator<PiutangResponse> CREATOR = new Creator<PiutangResponse>() {
        @Override
        public PiutangResponse createFromParcel(Parcel in) {
            return new PiutangResponse(in);
        }

        @Override
        public PiutangResponse[] newArray(int size) {
            return new PiutangResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hari);
        dest.writeString(tanggal_terdaftar);
        dest.writeString(nominal);
        dest.writeString(tanggal_jatuh_tempo);
    }

    public String getHari() {
        return hari;
    }

    public String getTerdaftar() {
        return tanggal_terdaftar;
    }

    public String getNominal() {
        return nominal;
    }

    public String getJatuhTempo() {
        return tanggal_jatuh_tempo;
    }

}
