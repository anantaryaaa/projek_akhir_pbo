package com.example.dompetmahasiswa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class menuKategoriController implements Initializable {

    @FXML
    private Label akun;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnUbah;

    @FXML
    private TableColumn<Kategori, String> colKategori;

    @FXML
    private TableColumn<Kategori, String> colSisaUang;

    @FXML
    private TableColumn<Kategori, String> colNominal;

    @FXML
    private Button dashboard;

    @FXML
    private Button kategoriPengeluaran;

    @FXML
    private Button keluar;

    @FXML
    private Button pemasukkan;

    @FXML
    private Button pengeluaran;

    @FXML
    private Label saldoKeuangan;

    @FXML
    private TextField tfKategori;

    @FXML
    private TextField tfNominal;

    @FXML
    private TableView<Kategori> tvKategori;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tampilkanKategori();

        btnTambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfKategori.getText().trim().isEmpty() && !tfNominal.getText().trim().isEmpty()){
                    TambahKategori(actionEvent,tfKategori.getText(), String.valueOf(Double.parseDouble(tfNominal.getText())));
                }
                else{
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
                }
            }
        });

        keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            }
        });

        pemasukkan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Pergantian_scene.gantiScene(actionEvent, "Pemasukkan.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        pengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Pergantian_scene.gantiScene(actionEvent, "Pengeluaran.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    throw new RuntimeException(e);
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

    public ObservableList<Kategori> ambilDaftarKategori(){
        ObservableList<Kategori> kategoriList = FXCollections.observableArrayList();
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("SELECT * FROM kategori_uang");
            resultSet = preparedStatement.executeQuery();
            Kategori kategori;
            while (resultSet.next()){
                kategori = new Kategori(resultSet.getString("kategori"),resultSet.getString("nominal"), resultSet.getString("sisa_uang"));
                kategoriList.add(kategori);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return kategoriList;
    }

    public void tampilkanKategori(){
        ObservableList<Kategori> list = ambilDaftarKategori();

        colKategori.setCellValueFactory(new PropertyValueFactory<Kategori,String>("kategori"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Kategori, String>("nominal"));
        colSisaUang.setCellValueFactory(new PropertyValueFactory<Kategori, String>("sisaUang"));

        tvKategori.setItems(list);
    }
}
