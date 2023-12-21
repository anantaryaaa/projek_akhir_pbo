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
    // Metode initialize untuk inisialisasi awal antarmuka pengguna pada saat pembukaan scene atau aplikasi
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Menghitung dan menampilkan total nominal pada label saldoKeuangan
        hitungTotalNominal();

        // Menampilkan daftar pemasukkan pada tabel
        tampilkanPemasukkan();

        // Mengatur aksi edit pada kolom-kolom tertentu
        colNominal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colKeterangan.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colTanggal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));

        // Mengatur aksi ketika tombol "Tambah" ditekan
        btnTambah.setOnAction(new EventHandler<ActionEvent>() {
            // Jika ya, ambil nilai dari input pengguna
            @Override
            public void handle(ActionEvent actionEvent) {
                // Memeriksa apakah semua informasi yang diperlukan telah diisi
                if (!tfKeterangan.getText().trim().isEmpty() && !tfNominal.getText().trim().isEmpty() && dpTanggal.getValue() != null) {
                    LocalDate tanggalTransaksi = dpTanggal.getValue();
                    String keterangan = tfKeterangan.getText();

                    // Mengonversi nilai nominal menjadi String untuk diproses
                    String nominal = String.valueOf(Double.parseDouble(tfNominal.getText()));

                    // Panggil metode Tambah_pemasukkan untuk memproses data
                    Tambah_pemasukkan(actionEvent, tanggalTransaksi, keterangan, nominal);
                } else {
                    // Jika tidak, tampilkan pesan kesalahan kepada pengguna
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
                }
            }
        });

        // Mengatur aksi ketika tombol "Ubah" ditekan
        btnUbah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Memeriksa apakah item terpilih di dalam TableView
                if (tvPemasukkan.getSelectionModel().getSelectedItem() != null) {
                    // Jika ya, panggil metode untuk mengubah data pemasukkan
                    ubahDataPemasukkan(tvPemasukkan.getSelectionModel().getSelectedItem());
                } else {
                    // Jika tidak ada item terpilih, tampilkan pesan peringatan kepada pengguna
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih data serta isi data form yang ingin diubah.");
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

        // Mengatur aksi ketika tombol "Dashboard" ditekan
        dashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil fungsi untuk mengganti tampilan (scene) ke Dashboard.fxml
                    // Nama aplikasi ditetapkan sebagai "Dompet Mahasiswa"
                    Pergantian_scene.gantiScene(actionEvent, "Dashboard.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan
                    throw new RuntimeException(e);
                }
            }
        });

        // Mengatur aksi ketika tombol "Pengeluaran" ditekan
        pengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil fungsi untuk mengganti tampilan (scene) ke Pengeluaran.fxml
                    // Nama aplikasi ditetapkan sebagai "Dompet Mahasiswa"
                    Pergantian_scene.gantiScene(actionEvent, "Pengeluaran.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan
                    throw new RuntimeException(e);
                }
            }
        });

        // Mengatur aksi ketika tombol "Kategori Pengeluaran" ditekan
        kategoriPengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil fungsi untuk mengganti tampilan (scene) ke Kategori.fxml
                    // Nama aplikasi ditetapkan sebagai "Dompet Mahasiswa"
                    Pergantian_scene.gantiScene(actionEvent, "Kategori.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan
                    throw new RuntimeException(e);
                }
            }
        });

        // Mengatur aksi ketika tombol "Keluar" ditekan
        keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Membuat dialog konfirmasi untuk keluar
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Keluar");
                alert.setHeaderText("Apakah Anda yakin ingin keluar?");
                alert.setContentText("Tekan OK untuk keluar, atau Batalkan untuk kembali.");

                // Menangani hasil dari dialog
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Menutup aplikasi jika pengguna menekan tombol OK
                        System.out.println("Aplikasi ditutup.");
                        System.exit(0);
                    } else {
                        // Menampilkan pesan jika pengguna membatalkan keluar
                        System.out.println("Aplikasi tidak ditutup.");
                    }
                });
            }
        });
    }

    // fungsi untuk menampilkan saldo user
    // Metode untuk memformat nominal menjadi string dengan pemisah ribuan dan dua angka desimal
    public String formatNominal(double nominal) {
        // Membuat objek DecimalFormat dengan pola format "#,##0.00"
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        // Menggunakan objek DecimalFormat untuk memformat nominal dan mengembalikan hasilnya
        return decimalFormat.format(nominal);
    }

    // Metode untuk mengambil total nominal dari database (contoh: tabel pemasukkan)
    public double ambilTotalNominal() {
        double totalNominal = 0;
        try {
            // Membuat objek koneksi ke database
            DBConnector koneksi = new DBConnector();

            // Mendapatkan koneksi ke database
            Connection connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL
            Statement statement = connection.createStatement();

            // Mengeksekusi query SQL untuk mengambil jumlah total nominal dari tabel pemasukkan
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pemasukkan");

            // Memeriksa apakah ada hasil dari query
            if (resultSet.next()) {
                // Mengambil nilai total nominal dari hasil query
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            // Menangkap dan mencetak stack trace jika terjadi kesalahan SQL
            e.printStackTrace();
        }

        // Mengembalikan total nominal
        return totalNominal;
    }

    // Metode untuk menghitung dan menampilkan total nominal pada label saldoKeuangan
    public void hitungTotalNominal() {
        // Mengambil total nominal dari metode ambilTotalNominal
        double totalNominal = ambilTotalNominal();

        // Memformat total nominal menjadi string dengan pemisah ribuan dan dua angka desimal
        String formattedNominal = formatNominal(totalNominal);

        // Menetapkan teks pada label saldoKeuangan
        saldoKeuangan.setText(formattedNominal);
    }

    // fungsi untuk menampilkan tabel data user
    // Metode untuk mengambil daftar pemasukkan dari database
    public ObservableList<Pemasukkan> ambilDaftarPemasukkan() {
        // Membuat ObservableList untuk menyimpan objek Pemasukkan
        ObservableList<Pemasukkan> pemasukkanList = FXCollections.observableArrayList();

        // Membuat objek koneksi ke database
        DBConnector koneksi = new DBConnector();

        // Mendeklarasikan objek-objek JDBC yang akan digunakan
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk mengambil semua data dari tabel pemasukkan
            preparedStatement = connection.prepareStatement("SELECT * FROM pemasukkan");

            // Mengeksekusi query dan mendapatkan hasilnya
            resultSet = preparedStatement.executeQuery();

            // Membuat objek Pemasukkan untuk setiap baris hasil query dan menambahkannya ke daftar
            Pemasukkan pemasukkan;
            while (resultSet.next()) {
                pemasukkan = new Pemasukkan(
                        resultSet.getDate("tanggal").toLocalDate(),
                        resultSet.getString("keterangan"),
                        resultSet.getString("nominal"),
                        resultSet.getInt("ID")
                );
                pemasukkanList.add(pemasukkan);
            }

        } catch (Exception e) {
            // Menangkap dan mencetak stack trace jika terjadi kesalahan
            e.printStackTrace();
        } finally {
            // Menutup sumber daya JDBC (koneksi, pernyataan, resultSet)
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Mengembalikan daftar pemasukkan
        return pemasukkanList;
    }

    // Metode untuk menampilkan daftar pemasukkan pada tabel dan menghitung total nominal
    public void tampilkanPemasukkan() {
        // Mengambil daftar pemasukkan dari database
        ObservableList<Pemasukkan> listpemasukkan = ambilDaftarPemasukkan();

        // Menetapkan properti-nilai untuk masing-masing kolom pada tabel
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colNominal.setCellValueFactory(new PropertyValueFactory<>("nominal"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Menetapkan daftar pemasukkan ke dalam tabel
        tvPemasukkan.setItems(listpemasukkan);

        // Menghitung dan menampilkan total nominal pada label saldoKeuangan
        hitungTotalNominal();
    }

    // fungsi untuk menambah data tabel pemasukkan
    // Metode untuk menambahkan data pemasukkan ke database
    public void Tambah_pemasukkan(ActionEvent event, LocalDate tanggal, String keterangan, String nominal) {

        // Membuat objek koneksi ke database
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk memasukkan data ke tabel pemasukkan
            preparedStatement = connection.prepareStatement("INSERT INTO pemasukkan(tanggal, keterangan, nominal) VALUES (?, ?, ?)");

            // Menetapkan parameter pada pernyataan SQL
            preparedStatement.setDate(1, Date.valueOf(tanggal));
            preparedStatement.setString(2, keterangan);
            preparedStatement.setString(3, nominal);

            // Mengeksekusi pernyataan SQL untuk menambahkan data
            preparedStatement.executeUpdate();

            // Menampilkan pesan sukses menggunakan Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sukses, pemasukkan berhasil ditambahkan ke database.");

            // Menampilkan alert dan menunggu sampai ditutup
            alert.showAndWait();

            // Menampilkan kembali daftar pemasukkan yang sudah diperbarui
            tampilkanPemasukkan();

        } catch (SQLException e) {
            // Menangkap dan mencetak stack trace jika terjadi kesalahan SQL
            e.printStackTrace();
        } finally {
            // Menutup sumber daya JDBC (koneksi, preparedStatement)
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

    // fungsi untuk mengubah data data tabel user
    // Metode untuk mengubah nilai pada field (DatePicker dan TextField) berdasarkan data Pemasukkan
    public void ubahNilaiField(Pemasukkan pemasukkan) {
        // Mengatur nilai pada DatePicker dpTanggal dengan nilai tanggal dari objek Pemasukkan
        dpTanggal.setValue(pemasukkan.getTanggal());

        // Mengatur nilai pada TextField tfKeterangan dengan nilai keterangan dari objek Pemasukkan
        tfKeterangan.setText(pemasukkan.getKeterangan());

        // Mengatur nilai pada TextField tfNominal dengan nilai nominal dari objek Pemasukkan
        tfNominal.setText(pemasukkan.getNominal());
    }

    // Metode untuk mengubah data pemasukkan berdasarkan objek Pemasukkan yang dipilih
    public void ubahDataPemasukkan(Pemasukkan pemasukkan) {
        try {
            // Mendapatkan nilai baru dari elemen antarmuka pengguna
            LocalDate tanggalBaru = dpTanggal.getValue();
            String keteranganBaru = tfKeterangan.getText();
            String nominalBaru = tfNominal.getText();

            // Memeriksa validitas input nominal
            if (!isValidInput(nominalBaru)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Format angka tidak valid. Harap masukkan angka yang benar.");
                alert.show();
                return;
            }

            // Membuat objek koneksi ke database
            DBConnector koneksi = new DBConnector();
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Mendapatkan koneksi ke database
                connection = koneksi.getDatabaselink();

                // Membuat pernyataan SQL untuk memperbarui data pemasukkan berdasarkan ID
                preparedStatement = connection.prepareStatement("UPDATE pemasukkan SET tanggal = ?, keterangan = ?, nominal = ? WHERE ID = ?");
                preparedStatement.setDate(1, Date.valueOf(tanggalBaru));
                preparedStatement.setString(2, keteranganBaru);
                preparedStatement.setString(3, nominalBaru);
                preparedStatement.setInt(4, pemasukkan.getId());

                // Mengeksekusi pernyataan SQL untuk memperbarui data
                int rowsAffected = preparedStatement.executeUpdate();

                // Menampilkan pesan informasi berdasarkan hasil operasi perubahan data
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
                // Menangkap dan mencetak stack trace jika terjadi kesalahan SQL
                e.printStackTrace();
            }

        } catch (Exception e) {
            // Menangkap dan mencetak stack trace jika terjadi kesalahan umum
            e.printStackTrace();
        }
    }

    // Metode untuk memeriksa apakah input dapat diubah menjadi tipe data Double
    public boolean isValidInput(String input) {
        try {
            // Mencoba mengonversi input menjadi tipe data Double
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            // Menangkap NumberFormatException jika konversi gagal
            return false;
        }
    }

    // fungsi untuk menghapus data
    // Metode untuk menghapus data pemasukkan berdasarkan objek Pemasukkan yang dipilih
    public void hapusDataPemasukkan(Pemasukkan pemasukkan) {
        // Membuat objek koneksi ke database
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk menghapus data pemasukkan berdasarkan ID
            preparedStatement = connection.prepareStatement("DELETE FROM pemasukkan WHERE ID = ?");
            preparedStatement.setInt(1, pemasukkan.getId());

            // Mengeksekusi pernyataan SQL untuk menghapus data
            int rowsAffected = preparedStatement.executeUpdate();

            // Menampilkan pesan informasi berdasarkan hasil operasi penghapusan data
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
            // Menangkap dan mencetak stack trace jika terjadi kesalahan SQL
            e.printStackTrace();
        } finally {
            // Menutup sumber daya JDBC (koneksi, preparedStatement)
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
