package com.kecipir.petani.rest.response;

/**
 * Created by UMAR on 7/27/2017.
 */
        import android.net.Uri;

        import java.net.URI;
        import java.util.List;

public class OrderResponse {
    private OrderResponse.Data data;

    public OrderResponse.Data getData() {
        return data;
    }

    public static class Data {
        private List<Listorders> listorder;
        private Jumlah jumlah;

        public List<Listorders> getlistOrder() {
            return listorder;
        }

        public Jumlah getJumlah() {
            return jumlah;
        }
    }

    public static class Jumlah {
        private String total_amount;
        private String total_amount_cur;
        public String getTotalAmount() {
            return total_amount;
        }
        public String getTotalAmountCurr() {return total_amount_cur;}
    }

    public static class Listorders{
        private String id_barang_panen;
        private String grade;
        private String nama_barang;
        private String harga_beli;
        private String harga_beli_real;
        private String qty_dipesan;
        private String quantity_order;
        private String quantity_order_input;
        private String satuan_barang;
        private String foto;

        public String getPanenangrades() {
            return grade;
        }

        public String getPanenanItemPanenans() {
            return nama_barang;
        }

        public String getPanenanPricess() {return harga_beli;}

        public String getOrderPricessReal() {return harga_beli_real;}

        public String getQuantityDiesans() {return qty_dipesan;}

        public String getQuantityOrders() {return quantity_order;}

        public String getQuantityOrdersInput() {return quantity_order_input;}

        public void setQuantityOrdersInput(String quantity) { this.quantity_order_input = quantity;}

        public String getIdBarangPanen() {
            return id_barang_panen;
        }

        public String getPanenanSatuans() {return satuan_barang;}

        public String getPanenanThumbnailPath() {return foto;}
    }

}

