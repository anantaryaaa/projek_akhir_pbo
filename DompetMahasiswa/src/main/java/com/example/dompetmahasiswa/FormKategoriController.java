package com.example.dompetmahasiswa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FormKategoriController implements Initializable {

    @FXML
    private Button Btn_keluar1;

    @FXML
    private Button Btn_kirim;

    @FXML
    private TextField Tf_keterangan;

    @FXML
    private TextField Tf_nominal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Btn_keluar1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "Kategori.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Btn_kirim.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!Tf_keterangan.getText().trim().isEmpty() && !Tf_nominal.getText().trim().isEmpty()){
                    TambahKategori(event,Tf_keterangan.getText(), String.valueOf(Double.parseDouble(Tf_nominal.getText())));
                }
                else{
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
                }
            }
        });
    }
    public void TambahKategori(ActionEvent event, String kategori, String nominal){
        DBConnector Koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("INSERT INTO kategori_uang(kategori, nominal) VALUES (?, ?)");
            preparedStatement.setString(1,kategori);
            preparedStatement.setString(2,nominal);
            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sukses, kategori berhasil ditambahkan ke database.");
            alert.setOnCloseRequest(alertEvent -> {
                try {
                    // Melakukan perpindahan scene setelah menekan tombol "OK"
                    Pergantian_scene.gantiScene(event, "Kategori.fxml", "Dashboard");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // Menampilkan alert dan menunggu sampai ditutup
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
