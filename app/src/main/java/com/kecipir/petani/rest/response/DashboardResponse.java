package com.kecipir.petani.rest.response;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import java.util.List;

public class DashboardResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        private Harvest harvest;
        private Order order;
        private Shipment shipment;
        private Loss loss;

        public Harvest getHarvest() {
            return harvest;
        }

        public Order getOrder() {
            return order;
        }

        public Shipment getShipment() {
            return shipment;
        }

        public Loss getLoss() {
            return loss;
        }
    }

    public static class Harvest {
        private String total;
        private List<Details> data;

        public String getTotal() {
            return total;
        }

        public List<Details> getDetails() {
            return data;
        }

        public static class Details {
            private float total;
            private int index;
            private String label;

            public float getTotal() {
                return total;
            }

            public int getIndex() {
                return index;
            }

            public String getLabel() {
                return label;
            }
        }
    }

    public static class Order{
        private String total;
        private List<Details> data;

        public String getTotal() {
            return total;
        }

        public List<Details> getDetails() {
            return data;
        }

        public static class Details {
            private float total;
            private int index;
            private String label;

            public float getTotal() {
                return total;
            }

            public int getIndex() {
                return index;
            }

            public String getLabel() {
                return label;
            }
        }
    }

    public static class Shipment {
        private String total;
        private List<Details> data;

        public String getTotal() {
            return total;
        }

        public List<Details> getDetails() {
            return data;
        }

        public static class Details {
            private float total;
            private int index;
            private String label;

            public float getTotal() {
                return total;
            }

            public int getIndex() {
                return index;
            }

            public String getLabel() {
                return label;
            }
        }
    }

    public static class Loss {
        private String total;
        private List<Details> data;

        public String  getTotal() {
            return total;
        }

        public List<Details> getDetails() {
            return data;
        }

        public static class Details {
            private float total;
            private int index;
            private String label;

            public float getTotal() {
                return total;
            }

            public int getIndex() {
                return index;
            }

            public String getLabel() {
                return label;
            }
        }
    }
}