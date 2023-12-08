package com.example.dompetmahasiswa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable{
    @FXML
    private Button Btn_kategori;

    @FXML
    private Button Btn_keluar;

    @FXML
    private Button Btn_pemasukkan;

    @FXML
    private Button Btn_pengeluaran;

    @FXML
    private Label Lbl_kategori;

    @FXML
    private Label Lbl_pemasukkan;

    @FXML
    private Label Lbl_pengeluaran;

    @FXML
    private Label Lbl_uang;

    @FXML
    private TableColumn<?, ?> Tc_pemasukkan;

    @FXML
    private TableColumn<?, ?> Tc_pengeluaran;

    @FXML
    private TableColumn<?, ?> Tc_kategori;

    @FXML
    private TableView<?> Tv_kategori;

    @FXML
    private TableView<?> Tv_pemasukkan;

    @FXML
    private TableView<?> Tv_pengeluaran;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Btn_pemasukkan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "Pemasukkan.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Btn_pengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "Pengeluaran.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Btn_kategori.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "Kategori.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
