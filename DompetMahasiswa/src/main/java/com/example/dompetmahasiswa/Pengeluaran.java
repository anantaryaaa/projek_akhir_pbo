package com.example.menupengeluaran;

import java.time.LocalDate;

public class Pengeluaran {
    private static int id;
    private LocalDate tanggal;
    private String keterangan;
    private String kategori;
    private String nominal;
    private Integer Id;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public static void setId(int id) {
        Pengeluaran.id = id;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getKategori() {
        return kategori;
    }

    public String getNominal() {
        return nominal;
    }

    public Pengeluaran(LocalDate tanggal, String keterangan, String kategori, String nominal) {
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.kategori = kategori;
        this.nominal = nominal;
    }
}
