package com.example.dompetmahasiswa;

import java.time.LocalDate;

public class Pengeluaran {
    private LocalDate tanggal;
    private String keterangan;
    private String kategori;
    private String nominal;
    private Integer id;

    public Pengeluaran(LocalDate tanggal, String keterangan, String kategori, String nominal, Integer id) {
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.kategori = kategori;
        this.nominal = nominal;
        this.id = id;

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
    public Integer getId() {return id;}
}
