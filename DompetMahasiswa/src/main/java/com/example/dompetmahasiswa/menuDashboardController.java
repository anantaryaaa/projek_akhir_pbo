package com.example.dompetmahasiswa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class menuDashboardController implements Initializable {
    @FXML
    private Label akun;

    @FXML
    private Button dashboard;

    @FXML
    private Button kategoriPengeluaran;

    @FXML
    private Button keluar;

    @FXML
    private TableColumn<Kategori, String> kolomKategori;

    @FXML
    private TableColumn<Pemasukkan, String> kolomPemasukkan;

    @FXML
    private TableColumn<Pengeluaran, String> kolomPengeluaran;

    @FXML
    private Button pemasukkan;

    @FXML
    private Button pengeluaran;

    @FXML
    private Label saldoKategori;

    @FXML
    private Label saldoKeuangan;

    @FXML
    private Label saldoPemasukkan;

    @FXML
    private Label saldoPengeluaran;

    @FXML
    private TableView<Kategori> tabelKategori;

    @FXML
    private TableView<Pemasukkan> tabelPemasukkan;

    @FXML
    private TableView<Pengeluaran> tabelPengeluaran;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tampilkanPemasukkan();
        tampilkanPengeluaran();
        tampilkanKategori();

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

        kategoriPengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Pergantian_scene.gantiScene(actionEvent, "Kategori.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public ObservableList<Pemasukkan> ambilDaftarPemasukkan(){
        ObservableList<Pemasukkan> pemasukkanList = FXCollections.observableArrayList();
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("SELECT * FROM pemasukkan");
            resultSet = preparedStatement.executeQuery();
            Pemasukkan pemasukkan;
            while (resultSet.next()){
                pemasukkan = new Pemasukkan(resultSet.getDate("tanggal").toLocalDate(), resultSet.getString("keterangan"), resultSet.getString("nominal"));
                pemasukkanList.add(pemasukkan);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pemasukkanList;
    }

    public void tampilkanPemasukkan(){
        ObservableList<Pemasukkan> listPemasukkan = ambilDaftarPemasukkan();

        kolomPemasukkan.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("keterangan"));

        tabelPemasukkan.setItems(listPemasukkan);
    }

    public ObservableList<Pengeluaran> ambilDaftarPengeluaran(){
        ObservableList<Pengeluaran> pengeluaranList = FXCollections.observableArrayList();
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("SELECT * FROM pengeluaran");
            resultSet = preparedStatement.executeQuery();
            Pengeluaran pengeluaran;
            while (resultSet.next()){
                pengeluaran = new Pengeluaran(resultSet.getDate("tanggal").toLocalDate(), resultSet.getString("keterangan"), resultSet.getString("kategori"),resultSet.getString("nominal"));
                pengeluaranList.add(pengeluaran);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pengeluaranList;
    }

    public void tampilkanPengeluaran(){
        ObservableList<Pengeluaran> listPengeluaran = ambilDaftarPengeluaran();

        kolomPengeluaran.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("keterangan"));

        tabelPengeluaran.setItems(listPengeluaran);
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
        ObservableList<Kategori> listKategori = ambilDaftarKategori();

        kolomKategori.setCellValueFactory(new PropertyValueFactory<Kategori,String>("kategori"));

        tabelKategori.setItems(listKategori);
    }
}