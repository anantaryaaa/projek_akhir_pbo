package org.example.dompetmahasiswa2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Pergantian_scene {
    // Metode untuk mengganti scene (layar) dalam aplikasi JavaFX
    public static void gantiScene(ActionEvent event, String fxmlFile, String title) throws IOException {
        // Inisialisasi root (akar) dari scene baru
        Parent root = null;

        // Membuat objek FXMLLoader untuk memuat file FXML
        FXMLLoader loader = new FXMLLoader(Pergantian_scene.class.getResource(fxmlFile));

        // Memuat file FXML dan mendapatkan root dari scene yang baru
        root = loader.load();

        // Membuat objek Stage baru untuk menampilkan scene yang baru
        Stage stage = new Stage();

        // Mengatur agar stage tidak dapat diubah ukurannya (non-resizable)
        stage.setResizable(false);

        // Mengatur judul (title) untuk stage sesuai dengan parameter yang diberikan
        stage.setTitle(title);

        // Mengatur scene baru untuk stage dengan menggunakan root yang telah dimuat
        stage.setScene(new Scene(root));

        // Menampilkan stage ke layar
        stage.show();

        // Menutup window (stage) saat ini
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
