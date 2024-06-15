package com.example.javafx_demo;

public class Barang {
    int id_barang;
    String NamaBarang;
    String TipeBarang;
    double berat;
    double harga;
    String kdsupplier;

    public Barang(int id_barang, String namaBarang, String tipeBarang, double berat, double harga, String kdsupplier) {
        this.id_barang = id_barang;
        NamaBarang = namaBarang;
        TipeBarang = tipeBarang;
        this.berat = berat;
        this.harga = harga;
        this.kdsupplier = kdsupplier;
    }
}