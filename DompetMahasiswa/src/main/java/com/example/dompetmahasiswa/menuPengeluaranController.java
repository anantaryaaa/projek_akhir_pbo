package com.example.dompetmahasiswa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;
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
    private TableColumn<Pengeluaran, Integer> colId;

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
        hitungTotalNominal();
        tampilkanPengeluaran();
        Daftar_kategori();
        colNominal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colKategori.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colKeterangan.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colTanggal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));

        btnUbah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (tvPengeluaran.getSelectionModel().getSelectedItem() != null) {
                    ubahDataPengeluaran(tvPengeluaran.getSelectionModel().getSelectedItem());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih kategori yang ingin diubah.");
                    alert.show();
                }
            }
        });

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

        btnHapus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Mendapatkan item yang dipilih dari tabel
                Pengeluaran selectedPengeluaran = tvPengeluaran.getSelectionModel().getSelectedItem();
                if (selectedPengeluaran != null) {
                    // Menampilkan konfirmasi sebelum menghapus
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Anda yakin ingin menghapus data ini?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Panggil fungsi untuk menghapus data dari database
                        hapusDataPengeluaran(selectedPengeluaran);
                    }
                } else {
                    // Menampilkan pesan jika tidak ada item yang dipilih
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih data yang ingin dihapus.");
                    alert.show();
                }
            }
        });
    }

//    bagian tambah pengeluaran
    public void Tambah_pengeluaran(ActionEvent event, LocalDate tanggal, String keterangan, String kategori, String nominal) {
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("INSERT INTO pengeluaran(tanggal, keterangan, kategori, nominal) VALUES (?, ?, ?, ?)");
            preparedStatement.setDate(1, Date.valueOf(tanggal));
            preparedStatement.setString(2, keterangan);
            preparedStatement.setString(3, kategori);
            preparedStatement.setString(4, nominal);
            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sukses, pengeluaran berhasil ditambahkan ke database.");
            // Menampilkan alert dan menunggu sampai ditutup
            alert.showAndWait();
            tampilkanPengeluaran();
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
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pengeluaran");

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

//    bagian menampilkan tabel
    public void Daftar_kategori() {
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("SELECT kategori FROM kategori_uang");
            resultSet = preparedStatement.executeQuery();

            ObservableList<String> kategori = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String Nama_kategori = resultSet.getString("kategori");
                kategori.add(Nama_kategori);
            }
            cbKategori.setItems(kategori);
        } catch (Exception e) {
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
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ObservableList<Pengeluaran> ambilDaftarPengeluaran() {
        ObservableList<Pengeluaran> pengeluaranList = FXCollections.observableArrayList();
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("SELECT * FROM pengeluaran");
            resultSet = preparedStatement.executeQuery();
            Pengeluaran pengeluaran;
            while (resultSet.next()) {
                pengeluaran = new Pengeluaran(resultSet.getDate("tanggal").toLocalDate(), resultSet.getString("keterangan"), resultSet.getString("kategori"), resultSet.getString("nominal"), resultSet.getInt("ID"));
                pengeluaranList.add(pengeluaran);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pengeluaranList;
    }

    public void tampilkanPengeluaran() {
        ObservableList<Pengeluaran> list = ambilDaftarPengeluaran();

        colTanggal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, LocalDate>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("keterangan"));
        colKategori.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("kategori"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("nominal"));
        colId.setCellValueFactory(new PropertyValueFactory<Pengeluaran, Integer>("id"));

        tvPengeluaran.setItems(list);
        hitungTotalNominal();

    }

//    bagian mengubah data pengeluaran
    public void ubahNilaiField(Pengeluaran pengeluaran) {
        dpTanggal.setValue(pengeluaran.getTanggal());
        tfKeterangan.setText(pengeluaran.getKeterangan());
        cbKategori.setValue(pengeluaran.getKategori());
        tfNominal.setText(pengeluaran.getNominal());
    }
    public void ubahDataPengeluaran(Pengeluaran pengeluaran) {
        try {
            LocalDate tanggalBaru = dpTanggal.getValue();
            String keteranganBaru = tfKeterangan.getText();
            String nominalBaru = tfNominal.getText();
            String kategoriBaru = cbKategori.getValue();

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
                preparedStatement = connection.prepareStatement("UPDATE pengeluaran SET tanggal = ?, keterangan = ?, kategori = ?, nominal = ? WHERE ID = ?");
                preparedStatement.setDate(1, Date.valueOf(tanggalBaru));
                preparedStatement.setString(2, keteranganBaru);
                preparedStatement.setString(3, kategoriBaru);
                preparedStatement.setString(4, nominalBaru);
                preparedStatement.setInt(5, pengeluaran.getId());
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Sukses, data pengeluaran berhasil diubah.");
                    alert.show();
                    tampilkanPengeluaran(); // Memperbarui tampilan tabel setelah perubahan
                    tfNominal.clear();
                    tfKeterangan.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Gagal mengubah data pengeluaran.");
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

//    bagian menghapus data tabel
    public void hapusDataPengeluaran(Pengeluaran pengeluaran){
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = koneksi.getDatabaselink();
            preparedStatement = connection.prepareStatement("DELETE FROM pengeluaran WHERE ID = ?");
            preparedStatement.setInt(1, pengeluaran.getId());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sukses, data berhasil dihapus.");
                alert.show();
                tampilkanPengeluaran(); // Memperbarui tampilan tabel setelah penghapusan
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