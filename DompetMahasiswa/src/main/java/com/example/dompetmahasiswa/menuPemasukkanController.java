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
    private TableColumn<Pemasukkan, Integer> colId;

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
        hitungTotalNominal();
        tampilkanPemasukkan();

        colNominal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colKeterangan.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colTanggal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));

        btnUbah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (tvPemasukkan.getSelectionModel().getSelectedItem() != null) {
                    ubahDataPemasukkan(tvPemasukkan.getSelectionModel().getSelectedItem());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih kategori yang ingin diubah.");
                    alert.show();
                }
            }
        });

        btnHapus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Mendapatkan item yang dipilih dari tabel
                Pemasukkan selectedPemasukkan = tvPemasukkan.getSelectionModel().getSelectedItem();
                if (selectedPemasukkan != null) {
                    // Menampilkan konfirmasi sebelum menghapus
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Anda yakin ingin menghapus data ini?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Panggil fungsi untuk menghapus data dari database
                        hapusDataPemasukkan(selectedPemasukkan);
                    }
                } else {
                    // Menampilkan pesan jika tidak ada item yang dipilih
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih data yang ingin dihapus.");
                    alert.show();
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

//    bagian menambah data
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
            tampilkanPemasukkan();
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
    private String formatNominal(double nominal) {
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
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pemasukkan");

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

//    bagian menampilkan data tabel
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
        ObservableList<Pemasukkan> listpemasukkan = ambilDaftarPemasukkan();

        colTanggal.setCellValueFactory(new PropertyValueFactory<Pemasukkan, LocalDate>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("keterangan"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("nominal"));
        colId.setCellValueFactory(new PropertyValueFactory<Pemasukkan,Integer>("id"));

        tvPemasukkan.setItems(listpemasukkan);
        hitungTotalNominal();
    }

//    bagian mengubah data pemasukkan
    public void ubahNilaiField(Pemasukkan pemasukkan) {
        dpTanggal.setValue(pemasukkan.getTanggal());
        tfKeterangan.setText(pemasukkan.getKeterangan());
        tfNominal.setText(pemasukkan.getNominal());
    }
    public void ubahDataPemasukkan(Pemasukkan pemasukkan) {
        try {
            LocalDate tanggalBaru = dpTanggal.getValue();
            String keteranganBaru = tfKeterangan.getText();
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
                preparedStatement = connection.prepareStatement("UPDATE pemasukkan SET tanggal = ?, keterangan = ?, nominal = ? WHERE ID = ?");
                preparedStatement.setDate(1, Date.valueOf(tanggalBaru));
                preparedStatement.setString(2, keteranganBaru);
                preparedStatement.setString(3, nominalBaru);
                preparedStatement.setInt(4, pemasukkan.getId());
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Sukses, data pemasukkan berhasil diubah.");
                    alert.show();
                    tampilkanPemasukkan(); // Memperbarui tampilan tabel setelah perubahan
                    tfNominal.clear();
                    tfKeterangan.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Gagal mengubah data pemasukkan.");
                    alert.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
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

//bagian menghapus data
    public void hapusDataPemasukkan(Pemasukkan pengeluaran){
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("DELETE FROM pemasukkan WHERE ID = ?");
            preparedStatement.setInt(1, pengeluaran.getId());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sukses, data berhasil dihapus.");
                alert.show();
                tampilkanPemasukkan(); // Memperbarui tampilan tabel setelah penghapusan
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
