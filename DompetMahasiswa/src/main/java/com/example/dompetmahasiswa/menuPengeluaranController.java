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

public class menuPengeluaranController implements Initializable {

    @FXML
    private Label akun;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnUbah;

    @FXML
    private ComboBox<String> cbKategori;

    @FXML
    private TableColumn<Pengeluaran, String> colKategori;

    @FXML
    private TableColumn<Pengeluaran, String> colKeterangan;

    @FXML
    private TableColumn<Pengeluaran, String> colNominal;

    @FXML
    private TableColumn<Pengeluaran, LocalDate> colTanggal;

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
    private TableView<Pengeluaran> tvPengeluaran;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tampilkanPengeluaran();
        Daftar_kategori();

        btnTambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfKeterangan.getText().trim().isEmpty() || !tfNominal.getText().trim().isEmpty() || dpTanggal.getValue() != null || cbKategori.getValue() != null) {
                    LocalDate tanggalTransaksi = dpTanggal.getValue();
                    String keterangan = tfKeterangan.getText();
                    String nominal = String.valueOf(Double.parseDouble(tfNominal.getText()));
                    String kategori = cbKategori.getValue();

                    Tambah_pengeluaran(actionEvent, tanggalTransaksi, keterangan, kategori, nominal);
                } else {
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
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
            cbKategori.setItems(kategori);
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

        colTanggal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, LocalDate>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("keterangan"));
        colKategori.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String >("kategori"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("nominal"));

        tvPengeluaran.setItems(list);
    }
}