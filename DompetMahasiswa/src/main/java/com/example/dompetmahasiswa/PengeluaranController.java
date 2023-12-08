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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PengeluaranController implements Initializable {
    @FXML
    private Button Btn_edit;

    @FXML
    private Button Btn_hapus;

    @FXML
    private Button Btn_kembali;

    @FXML
    private Button Btn_tambah;

    @FXML
    private Label Lbl_uang;

    @FXML
    private TableColumn<Pengeluaran, String > Tc_kategori;

    @FXML
    private TableColumn<Pengeluaran, String> Tc_keterangan;

    @FXML
    private TableColumn<Pengeluaran, String> Tc_nominal;

    @FXML
    private TableColumn<Pengeluaran, LocalDate> Tc_tanggal;

    @FXML
    private TableView<Pengeluaran> Tv_pengeluaran;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tampilkanPengeluaran();

        Btn_hapus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                inisialisasinya disini, metodenya dibawah
            }
        });

        Btn_edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                inisialisasinya disini, metodenya dibawah
            }
        });

        Btn_kembali.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "Dashboard.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Btn_tambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "FormPengeluaran.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
        ObservableList<Pengeluaran> list = ambilDaftarPengeluaran();

        Tc_tanggal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, LocalDate>("tanggal"));
        Tc_keterangan.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("keterangan"));
        Tc_kategori.setCellValueFactory(new PropertyValueFactory<Pengeluaran,String>("kategori"));
        Tc_nominal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("nominal"));

        Tv_pengeluaran.setItems(list);
    }
}