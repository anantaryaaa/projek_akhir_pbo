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

public class PemasukkanControler implements Initializable {
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
    private TableColumn<Pemasukkan, String> Tc_keterangan;

    @FXML
    private TableColumn<Pemasukkan, String> Tc_nominal;

    @FXML
    private TableColumn<Pemasukkan, LocalDate> Tc_tanggal;

    @FXML
    private TableView<Pemasukkan> Tv_pemasukkan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tampilkanPemasukkan();

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
                    Pergantian_scene.gantiScene(event, "FormPemasukkan.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
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
        ObservableList<Pemasukkan> list = ambilDaftarPemasukkan();

        Tc_tanggal.setCellValueFactory(new PropertyValueFactory<Pemasukkan, LocalDate>("tanggal"));
        Tc_keterangan.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("keterangan"));
        Tc_nominal.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("nominal"));

        Tv_pemasukkan.setItems(list);
    }
}
