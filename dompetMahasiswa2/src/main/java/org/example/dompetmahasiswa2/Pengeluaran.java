package org.example.dompetmahasiswa2;

import java.time.LocalDate;

import java.time.LocalDate;

public class Pengeluaran {
    // Variabel instance untuk menyimpan tanggal, keterangan, kategori, nominal, dan ID
    private LocalDate tanggal;
    private String keterangan;
    private String kategori;
    private String nominal;
    private Integer id;

    // Konstruktor untuk membuat objek Pengeluaran dengan nilai tertentu
    public Pengeluaran(LocalDate tanggal, String keterangan, String kategori, String nominal, Integer id) {
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.kategori = kategori;
        this.nominal = nominal;
        this.id = id;
    }

    // Metode getter untuk mendapatkan nilai tanggal
    public LocalDate getTanggal() {
        return tanggal;
    }

    // Metode getter untuk mendapatkan nilai keterangan
    public String getKeterangan() {
        return keterangan;
    }

    // Metode getter untuk mendapatkan nilai kategori
    public String getKategori() {
        return kategori;
    }

    // Metode getter untuk mendapatkan nilai nominal
    public String getNominal() {
        return nominal;
    }

    // Metode getter untuk mendapatkan nilai ID
    public Integer getId() {
        return id;
    }
}