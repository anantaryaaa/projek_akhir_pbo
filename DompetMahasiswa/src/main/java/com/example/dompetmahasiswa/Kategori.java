package com.example.dompetmahasiswa;

public class Kategori {
    private Integer id;
    private String kategori;
    private String nominal;

    public Kategori(String kategori, String nominal, int id) {
        this.kategori = kategori;
        this.nominal = nominal;
        this.id = id;
    }


    public String getKategori() {
        return kategori;
    }

    public String getNominal() {
        return nominal;
    }
    public Integer getId(){return  id;}
}
