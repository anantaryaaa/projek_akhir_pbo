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
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class menuPemasukkanController implements Initializable {
    @FXML
    private Label akun;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnUbah;

    @FXML
    private TableColumn<Pemasukkan, String> colKeterangan;

    @FXML
    private TableColumn<Pemasukkan, String> colNominal;

    @FXML
    private TableColumn<Pemasukkan, LocalDate> colTanggal;

    @FXML
    private Button dashboard;

    @FXML
    private DatePicker dpTanggal;

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
    private TextField tfKeterangan;

    @FXML
    private TextField tfNominal;

    @FXML
    private TableView<Pemasukkan> tvPemasukkan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tampilkanPemasukkan();

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

        dashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Pergantian_scene.gantiScene(actionEvent, "Dashboard.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        
        btnTambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfKeterangan.getText().trim().isEmpty() && !tfNominal.getText().trim().isEmpty() && dpTanggal.getValue() != null) {
                    LocalDate tanggalTransaksi = dpTanggal.getValue();
                    String keterangan = tfKeterangan.getText();
                    String nominal = String.valueOf(Double.parseDouble(tfNominal.getText()));

                    Tambah_pemasukkan(actionEvent, tanggalTransaksi, keterangan, nominal);
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
        ObservableList<Pemasukkan> listpemasukkan = ambilDaftarPemasukkan();

        colTanggal.setCellValueFactory(new PropertyValueFactory<Pemasukkan, LocalDate>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("keterangan"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("nominal"));

        tvPemasukkan.setItems(listpemasukkan);
    }
}
