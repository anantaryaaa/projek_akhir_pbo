package com.example.dompetmahasiswa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FormPengeluaranController implements Initializable {
    @FXML
    private Button Btn_keluar;

    @FXML
    private Button Btn_kirim;

    @FXML
    private ComboBox<String> Cb_kategori;

    @FXML
    private DatePicker Dp_tanggal;

    @FXML
    private TextField Tf_keterangan;

    @FXML
    private TextField Tf_nominal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Daftar_kategori();

        Btn_keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "Pengeluaran.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Btn_kirim.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!Tf_keterangan.getText().trim().isEmpty() || !Tf_nominal.getText().trim().isEmpty() || Dp_tanggal.getValue() != null || Cb_kategori.getValue() != null) {
                    LocalDate tanggalTransaksi = Dp_tanggal.getValue();
                    String keterangan = Tf_keterangan.getText();
                    String nominal = String.valueOf(Double.parseDouble(Tf_nominal.getText()));
                    String kategori = Cb_kategori.getValue();

                    Tambah_pengeluaran(event, tanggalTransaksi, keterangan, kategori, nominal);
                } else {
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
                }
            }
        });
    }

    public void Daftar_kategori(){
            DBConnector koneksi = new DBConnector();
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try{
                connection = koneksi.getDatabaselink();
                preparedStatement = connection.prepareStatement("SELECT kategori FROM kategori_uang");
                resultSet = preparedStatement.executeQuery();

                ObservableList<String> kategori = FXCollections.observableArrayList();
                while (resultSet.next()){
                    String Nama_kategori = resultSet.getString("kategori");
                    kategori.add(Nama_kategori);
                }
                Cb_kategori.setItems(kategori);
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if (connection != null){
                    try {
                        connection.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }if (preparedStatement != null){
                    try {
                        preparedStatement.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }if (resultSet != null){
                    try {
                        resultSet.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }
    }

    public void Tambah_pengeluaran(ActionEvent event, LocalDate tanggal, String keterangan, String kategori, String nominal){
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("INSERT INTO pengeluaran(tanggal, keterangan, kategori, nominal) VALUES (?, ?, ?, ?)");
            preparedStatement.setDate(1, Date.valueOf(tanggal));
            preparedStatement.setString(2,keterangan);
            preparedStatement.setString(3,kategori);
            preparedStatement.setString(4,nominal);
            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sukses, pengeluaran berhasil ditambahkan ke database.");
            alert.setOnCloseRequest(alertEvent -> {
                try {
                    // Melakukan perpindahan scene setelah menekan tombol "OK"
                    Pergantian_scene.gantiScene(event, "Pengeluaran.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // Menampilkan alert dan menunggu sampai ditutup
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
