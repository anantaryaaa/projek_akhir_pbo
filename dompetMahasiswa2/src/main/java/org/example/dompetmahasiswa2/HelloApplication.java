package org.example.dompetmahasiswa2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Membuat FXMLLoader untuk memuat tata letak UI dari file "Login.fxml"
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));

        // Membuat Scene dengan tata letak UI yang dimuat dan mengatur dimensi stage
        Scene scene = new Scene(fxmlLoader.load(), 1024, 700);

        // Mengatur judul jendela aplikasi
        stage.setTitle("Dompet Mahasiswa");

        // Mengatur scene untuk stage
        stage.setScene(scene);

        // Menampilkan stage
        stage.show();
    }

    public static void main(String[] args) {
        // Menjalankan aplikasi JavaFX
        launch();
    }
}
