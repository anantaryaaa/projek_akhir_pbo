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
import java.text.DecimalFormat;
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
        hitungTotalNominaPemasukkan();
        hitungTotalNominaPengeluara();
        hitungSelisihNominal();
        tampilkanPemasukkan();
        tampilkanPengeluaran();
        tampilkanKategori();

        keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Keluar");
                alert.setHeaderText("Apakah Anda yakin ingin keluar?");
                alert.setContentText("Tekan OK untuk keluar, atau Batalkan untuk kembali.");

                // Menangani hasil dari dialog
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Aplikasi ditutup.");
                        System.exit(0);
                    } else {
                        System.out.println("Aplikasi tidak ditutup.");
                    }
                });
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

//    menampilkan tabel pemasukkan
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
                pemasukkan = new Pemasukkan(resultSet.getDate("tanggal").toLocalDate(), resultSet.getString("keterangan"), resultSet.getString("nominal"), resultSet.getInt("ID"));
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

//    menampilkan tabel pengeluaran
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
                pengeluaran = new Pengeluaran(resultSet.getDate("tanggal").toLocalDate(), resultSet.getString("keterangan"), resultSet.getString("kategori"),resultSet.getString("nominal"), resultSet.getInt("ID"));
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

//    menampilkan tabel kategori
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
                kategori = new Kategori(resultSet.getString("kategori"),resultSet.getString("nominal"), resultSet.getInt("ID"));
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

    //    menampilkan nominal uang user
    public String formatNominal(double nominal) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(nominal);
    }

    public double ambilTotalNominalKategori() {
        double totalNominal = 0;

        try {
            // Mengambil data dari database dan menjumlahkannya
            DBConnector koneksi = new DBConnector();
            Connection connection = koneksi.getDatabaselink();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM kategori_uang");

            if (resultSet.next()) {
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalNominal;
    }

    public void hitungTotalNominal() {
        double totalNominal = ambilTotalNominalKategori();

        String formattedNominal = formatNominal(totalNominal);

        // Set teks pada label
        saldoKategori.setText(formattedNominal);
    }

    public double ambilTotalNominalPengeluaran() {
        double totalNominal = 0;

        try {
            // Mengambil data dari database dan menjumlahkannya
            DBConnector koneksi = new DBConnector();
            Connection connection = koneksi.getDatabaselink();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pengeluaran");

            if (resultSet.next()) {
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalNominal;
    }

    public void hitungTotalNominaPengeluara() {
        double totalNominal = ambilTotalNominalPengeluaran();

        String formattedNominal = formatNominal(totalNominal);

        // Set teks pada label
        saldoPengeluaran.setText(formattedNominal);
    }

    public double ambilTotalNominalPemasukkan() {
        double totalNominal = 0;

        try {
            // Mengambil data dari database dan menjumlahkannya
            DBConnector koneksi = new DBConnector();
            Connection connection = koneksi.getDatabaselink();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pemasukkan");

            if (resultSet.next()) {
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalNominal;
    }

    public void hitungTotalNominaPemasukkan() {
        double totalNominal = ambilTotalNominalPemasukkan();

        String formattedNominal = formatNominal(totalNominal);

        // Set teks pada label
        saldoPemasukkan.setText(formattedNominal);
    }

    public void hitungSelisihNominal() {
        double totalNominalPemasukkan = ambilTotalNominalPemasukkan();
        double totalNominalPengeluaran = ambilTotalNominalPengeluaran();

        // Hitung selisihnya
        double selisihNominal = totalNominalPemasukkan - totalNominalPengeluaran;

        // Format angka dengan dua digit di belakang koma
        String formattedNominal = formatNominal(selisihNominal);

        // Set teks pada label
        saldoKeuangan.setText(formattedNominal);
    }
}