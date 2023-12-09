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
import java.time.LocalDate;
import java.util.Optional;
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
    private TableColumn<Kategori, String> colNominal;

    @FXML
    private TableColumn<Kategori, Integer> colId;

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
        hitungTotalNominal();
        tampilkanKategori();

        colKategori.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colNominal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));

        btnHapus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Mendapatkan item yang dipilih dari tabel
                Kategori selectedKategori = tvKategori.getSelectionModel().getSelectedItem();
                if (selectedKategori != null) {
                    // Menampilkan konfirmasi sebelum menghapus
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Anda yakin ingin menghapus data ini?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Panggil fungsi untuk menghapus data dari database
                        hapusDataKategori(selectedKategori);
                    }
                } else {
                    // Menampilkan pesan jika tidak ada item yang dipilih
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih data yang ingin dihapus.");
                    alert.show();
                }
            }
        });

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

        btnUbah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (tvKategori.getSelectionModel().getSelectedItem() != null) {
                    ubahdataKategori(tvKategori.getSelectionModel().getSelectedItem());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih kategori yang ingin diubah.");
                    alert.show();
                }
            }
        });

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
    }

//bagian ubah tabel data
    public void ubahNilaiField(Kategori kategori){
        tfKategori.setText(kategori.getKategori());
        tfNominal.setText(kategori.getNominal());
    }

    public void ubahdataKategori(Kategori kategori){
        try {

            String kategoriBaru = tfKategori.getText();
            String nominalBaru = tfNominal.getText();

            if (!isValidInput(nominalBaru)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nominal harus berupa angka.");
                alert.show();
                return;
            }

            DBConnector koneksi = new DBConnector();
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = koneksi.getDatabaselink();
                preparedStatement = connection.prepareStatement("UPDATE kategori_uang SET kategori = ?, nominal = ? WHERE ID = ?");

                preparedStatement.setString(3, kategoriBaru);
                preparedStatement.setString(4, nominalBaru);
                preparedStatement.setInt(5, kategori.getId());
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Sukses, data kategori berhasil diubah.");
                    alert.show();
                    tampilkanKategori(); // Memperbarui tampilan tabel setelah perubahan
                    tfNominal.clear();
                    tfKategori.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Gagal mengubah data kategori.");
                    alert.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean isValidInput(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//bagian tambah data tabel
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
            tampilkanKategori();
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


//    menampilkan nominal uang user
    public String formatNominal(double nominal) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(nominal);
    }

    public double ambilTotalNominal() {
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
        double totalNominal = ambilTotalNominal();

        String formattedNominal = formatNominal(totalNominal);

        // Set teks pada label
        saldoKeuangan.setText(formattedNominal);
    }

//bagian menampilkan data tabel
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
        ObservableList<Kategori> list = ambilDaftarKategori();

        colKategori.setCellValueFactory(new PropertyValueFactory<Kategori,String>("kategori"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Kategori, String>("nominal"));
        colId.setCellValueFactory(new PropertyValueFactory<Kategori, Integer>("id"));

        tvKategori.setItems(list);

        hitungTotalNominal();
    }

//bagian hapus data kategori
    public void hapusDataKategori(Kategori kategori){
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("DELETE FROM kategori_uang WHERE ID = ?");
            preparedStatement.setInt(1, kategori.getId());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sukses, data berhasil dihapus.");
                alert.show();
                tampilkanKategori(); // Memperbarui tampilan tabel setelah penghapusan
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Gagal menghapus data.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
