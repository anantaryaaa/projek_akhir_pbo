package com.example.dompetmahasiswa;

import java.time.LocalDate;

public class Pemasukkan {

    private LocalDate tanggal;
    private String keterangan;
    private String nominal;

    public Pemasukkan(LocalDate tanggal, String keterangan, String nominal) {
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.nominal = nominal;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getNominal() {
        return nominal;
    }
}
