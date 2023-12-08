package com.example.dompetmahasiswa;

public class Kategori {
    private String kategori;
    private String nominal;
    private String sisaUang;

    public Kategori(String kategori, String nominal, String sisaUang) {
        this.kategori = kategori;
        this.nominal = nominal;
        this.sisaUang = sisaUang;
    }


    public String getKategori() {
        return kategori;
    }

    public String getNominal() {
        return nominal;
    }

    public String getSisaUang() {
        return sisaUang;
    }
}
