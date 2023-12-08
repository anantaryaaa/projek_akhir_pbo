package com.example.dompetmahasiswa;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {
    public Connection Databaselink;

    public Connection getDatabaselink(){
        String DatabaseName ="pbo";
        String DatabaseUser ="root";
        String DatabasePassword = "*Novand12004";

        String url = "jdbc:mysql://localhost/" + DatabaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Databaselink = DriverManager.getConnection(url, DatabaseUser, DatabasePassword);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Databaselink;
    }
}
