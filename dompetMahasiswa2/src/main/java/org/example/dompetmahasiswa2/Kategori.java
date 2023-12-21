package org.example.dompetmahasiswa2;

public class Kategori {
    // Variabel instance untuk menyimpan ID, kategori, dan nominal
    private Integer id;
    private String kategori;
    private String nominal;

    // Konstruktor untuk membuat objek Kategori dengan kategori, nominal, dan ID tertentu
    public Kategori(String kategori, String nominal, int id) {
        this.kategori = kategori;
        this.nominal = nominal;
        this.id = id;
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

