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

public class KategoriController implements Initializable {
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
        private TableColumn<Kategori, String> Tc_kategori;

        @FXML
        private TableColumn<Kategori, String > Tc_nominal;

        @FXML
        private TableColumn<Kategori, String> Tc_sisa_uang;

        @FXML
        private TableView<Kategori> Tv_kategori_pengeluaran;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                tampilkanKategori();

                Btn_edit.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {

                        }
                });

                Btn_kembali.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                                try {
                                        Pergantian_scene.gantiScene(actionEvent, "Dashboard.fxml", "Dompet Mahasiswa");
                                } catch (IOException e) {
                                        throw new RuntimeException(e);
                                }
                        }
                });

                Btn_tambah.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                                try {
                                        Pergantian_scene.gantiScene(actionEvent, "FormKategori.fxml", "Dompet Mahasiswa");
                                } catch (IOException e) {
                                        throw new RuntimeException(e);
                                }
                        }
                });

                Btn_hapus.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {

                        }
                });
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

                Tc_kategori.setCellValueFactory(new PropertyValueFactory<Kategori,String>("kategori"));
                Tc_nominal.setCellValueFactory(new PropertyValueFactory<Kategori, String>("nominal"));
                Tc_sisa_uang.setCellValueFactory(new PropertyValueFactory<Kategori, String>("sisaUang"));

                Tv_kategori_pengeluaran.setItems(list);
        }
}
