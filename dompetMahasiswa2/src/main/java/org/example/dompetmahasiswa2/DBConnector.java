package org.example.dompetmahasiswa2;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {
    // Variabel untuk menyimpan koneksi database
    public Connection Databaselink;

    // Metode untuk mendapatkan koneksi database
    public Connection getDatabaselink() {
        // Informasi database (nama, pengguna, kata sandi)
        String DatabaseName = "pbo";
        String DatabaseUser = "root";
        String DatabasePassword = "*Novand12004";

        // URL koneksi ke database MySQL
        String url = "jdbc:mysql://localhost/" + DatabaseName;

        try {
            // Meload driver JDBC untuk MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuat koneksi ke database
            Databaselink = DriverManager.getConnection(url, DatabaseUser, DatabasePassword);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mengembalikan koneksi database
        return Databaselink;
    }
}

