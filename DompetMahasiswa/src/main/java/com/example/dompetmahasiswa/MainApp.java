package com.example.dompetmahasiswa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Membuat objek FXMLLoader untuk memuat tampilan dari file FXML
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("Login.fxml"));

        // Membuat objek Scene dengan menggunakan tampilan yang dimuat dari file FXML
        Scene scene = new Scene(fxmlLoader.load(), 1024, 700);

        // Mengatur judul jendela aplikasi
        stage.setTitle("Dompet Mahasiswa");

        // Mengatur Scene ke dalam Stage
        stage.setScene(scene);

        // Mengatur agar jendela aplikasi tidak dapat diubah ukurannya (tidak resizable)
        stage.setResizable(false);

        // Menampilkan jendela aplikasi
        stage.show();
    }

    public static void main(String[] args) {
        // Memanggil metode launch() untuk memulai eksekusi aplikasi JavaFX
        launch(args);
    }
}