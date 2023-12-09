package com.example.dompetmahasiswa;

import java.time.LocalDate;

public class Pemasukkan {

    private LocalDate tanggal;
    private String keterangan;
    private String nominal;
    private Integer id;

    public Pemasukkan(LocalDate tanggal, String keterangan, String nominal, Integer id) {
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.nominal = nominal;
        this.id = id;
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
    public Integer getId(){return  id;}
}
