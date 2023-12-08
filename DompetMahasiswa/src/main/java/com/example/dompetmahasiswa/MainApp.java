package com.example.dompetmahasiswa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("Pemasukkan.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 700);
        stage.setTitle("Dompet Mahasiswa");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

//        stage.setOnCloseRequest(windowEvent -> {
//            windowEvent.consume();
//            Keluar(stage);
//        });
    }
//
//    public void Keluar(Stage stage){
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Keluar");
//        alert.setHeaderText("Kamu akan keluar!");
//        alert.setContentText("Apakah kamu ingin keluar aplikasi?: ");
//
//        if (alert.showAndWait().get() == ButtonType.OK) {
//            System.out.println("Terima kasih sudah menggunakan aplikasi, sampai jumpa.");
//            stage.close();
//        }
//    }

    public static void main(String[] args) {
        launch();
    }
}