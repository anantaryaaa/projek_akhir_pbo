package com.example.dompetmahasiswa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;

public class FormPemasukkanController implements Initializable {
    @FXML
    private Button Btn_keluar;

    @FXML
    private Button Btn_kirim;

    @FXML
    private DatePicker Dp_transaksi;

    @FXML
    private TextField Tf_keterangan;

    @FXML
    private TextField Tf_nominal;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Btn_keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "Pemasukkan.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Btn_kirim.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!Tf_keterangan.getText().trim().isEmpty() && !Tf_nominal.getText().trim().isEmpty() && Dp_transaksi.getValue() != null) {
                    LocalDate tanggalTransaksi = Dp_transaksi.getValue();
                    String keterangan = Tf_keterangan.getText();
                    String nominal = String.valueOf(Double.parseDouble(Tf_nominal.getText()));

                    Tambah_pemasukkan(event, tanggalTransaksi, keterangan, nominal);
                } else {
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
                }
            }
        });
    }

    public void Tambah_pemasukkan(ActionEvent event, LocalDate tanggal, String keterangan, String nominal){
        DBConnector Koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("INSERT INTO pemasukkan(tanggal, keterangan, nominal) VALUES (?, ?, ?)");
            preparedStatement.setDate(1, Date.valueOf(tanggal));
            preparedStatement.setString(2,keterangan);
            preparedStatement.setString(3,nominal);
            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sukses, pemasukkan berhasil ditambahkan ke database.");
            alert.setOnCloseRequest(alertEvent -> {
                try {
                    // Melakukan perpindahan scene setelah menekan tombol "OK"
                    Pergantian_scene.gantiScene(event, "Pemasukkan.fxml", "Dashboard");
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
