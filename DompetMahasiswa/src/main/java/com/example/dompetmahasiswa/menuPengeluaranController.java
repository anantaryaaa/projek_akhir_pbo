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
    // Metode initialize untuk inisialisasi awal antarmuka pengguna pada saat pembukaan scene atau aplikasi
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Menghitung dan menampilkan total nominal pada label saldoKeuangan
        hitungTotalNominal();

        // Menampilkan daftar pengeluaran pada tabel
        tampilkanPengeluaran();

        // Menampilkan daftar kategori pada antarmuka pengguna
        Daftar_kategori();

        // Mengatur aksi edit pada kolom-kolom tertentu
        colNominal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colKategori.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colKeterangan.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colTanggal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));

        // Mengatur aksi tombol "Tambah" pada antarmuka pengguna
        btnTambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Memeriksa apakah semua informasi yang dibutuhkan telah diisi
                if (!tfKeterangan.getText().trim().isEmpty() || !tfNominal.getText().trim().isEmpty() || dpTanggal.getValue() != null || cbKategori.getValue() != null) {
                    // Jika informasi sudah diisi, mendapatkan nilai dari elemen antarmuka pengguna
                    LocalDate tanggalTransaksi = dpTanggal.getValue();
                    String keterangan = tfKeterangan.getText();
                    String nominal = String.valueOf(Double.parseDouble(tfNominal.getText()));
                    String kategori = cbKategori.getValue();

                    // Memanggil metode untuk menambahkan pengeluaran
                    Tambah_pengeluaran(actionEvent, tanggalTransaksi, keterangan, kategori, nominal);
                } else {
                    // Jika informasi belum diisi, menampilkan pesan error
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
                }
            }
        });

        // Mengatur aksi tombol "Ubah" pada antarmuka pengguna
        btnUbah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Memeriksa apakah ada item yang dipilih dari tabel pengeluaran
                if (tvPengeluaran.getSelectionModel().getSelectedItem() != null) {
                    // Jika ada, memanggil metode untuk mengubah data pengeluaran
                    ubahDataPengeluaran(tvPengeluaran.getSelectionModel().getSelectedItem());
                } else {
                    // Jika tidak ada item yang dipilih, menampilkan pesan peringatan
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih data serta isi data form yang ingin diubah.");
                    alert.show();
                }
            }
        });

        // Mengatur aksi tombol "Hapus" pada antarmuka pengguna
        btnHapus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Mendapatkan item yang dipilih dari tabel pengeluaran
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

        // Mengatur aksi tombol "dashboard" pada antarmuka pengguna
        dashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode untuk mengganti scene ke "Dashboard.fxml"
                    Pergantian_scene.gantiScene(actionEvent, "Dashboard.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan
                    throw new RuntimeException(e);
                }
            }
        });

        // Mengatur aksi tombol "pemasukkan" pada antarmuka pengguna
        pemasukkan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode untuk mengganti scene ke "Pemasukkan.fxml"
                    Pergantian_scene.gantiScene(actionEvent, "Pemasukkan.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan
                    throw new RuntimeException(e);
                }
            }
        });

        // Mengatur aksi tombol "kategoriPengeluaran" pada antarmuka pengguna
        kategoriPengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode untuk mengganti scene ke "Kategori.fxml"
                    Pergantian_scene.gantiScene(actionEvent, "Kategori.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan
                    throw new RuntimeException(e);
                }
            }
        });

        // Mengatur aksi tombol "keluar" pada antarmuka pengguna
        keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Menampilkan dialog konfirmasi keluar
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Keluar");
                alert.setHeaderText("Apakah Anda yakin ingin keluar?");
                alert.setContentText("Tekan OK untuk keluar, atau Batalkan untuk kembali.");

                // Menangani hasil dari dialog
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Jika pengguna memilih OK, menutup aplikasi
                        System.out.println("Aplikasi ditutup.");
                        System.exit(0);
                    } else {
                        // Jika pengguna memilih Batalkan, menampilkan pesan
                        System.out.println("Aplikasi tidak ditutup.");
                    }
                });
            }
        });
    }

    //    menampilkan saldo user
    // Metode untuk memformat nominal menjadi format mata uang dengan dua desimal
    private String formatNominal(double nominal) {
        // Membuat objek DecimalFormat dengan pola "#,##0.00"
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        // Menggunakan objek DecimalFormat untuk memformat nominal
        return decimalFormat.format(nominal);
    }

    // Metode untuk mengambil total nominal pengeluaran dari database
    public double ambilTotalNominal() {
        // Inisialisasi variabel totalNominal dengan nilai awal 0
        double totalNominal = 0;

        try {
            // Membuat objek koneksi ke database
            DBConnector koneksi = new DBConnector();
            Connection connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk mengambil total nominal pengeluaran
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pengeluaran");

            // Memeriksa apakah hasil query mengandung data
            if (resultSet.next()) {
                // Mengambil nilai total nominal dari hasil query
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            // Menangkap dan mencetak stack trace jika terjadi kesalahan SQL
            e.printStackTrace();
        }

        // Mengembalikan total nominal pengeluaran
        return totalNominal;
    }

    // Metode untuk menghitung dan menampilkan total nominal ke dalam label
    public void hitungTotalNominal() {
        // Mengambil total nominal dari metode ambilTotalNominal
        double totalNominal = ambilTotalNominal();

        // Memformat total nominal sebagai string dengan format mata uang
        String formattedNominal = formatNominal(totalNominal);

        // Menetapkan teks pada label saldoKeuangan
        saldoKeuangan.setText(formattedNominal);
    }

    //    bagian menampilkan tabel
    // Metode untuk mengambil dan menetapkan daftar kategori ke dalam ComboBox
    public void Daftar_kategori() {
        // Inisialisasi objek koneksi, koneksi, pernyataan, dan hasil query
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Membuat koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk mengambil daftar kategori
            preparedStatement = connection.prepareStatement("SELECT kategori FROM kategori_uang");
            resultSet = preparedStatement.executeQuery();

            // Membuat objek ObservableList untuk menyimpan daftar kategori
            ObservableList<String> kategori = FXCollections.observableArrayList();

            // Mengisi daftar kategori dari hasil query
            while (resultSet.next()) {
                String Nama_kategori = resultSet.getString("kategori");
                kategori.add(Nama_kategori);
            }

            // Menetapkan daftar kategori ke dalam ComboBox cbKategori
            cbKategori.setItems(kategori);

        } catch (Exception e) {
            // Menangkap dan mencetak stack trace jika terjadi kesalahan
            e.printStackTrace();

        } finally {
            // Menutup sumber daya (koneksi, pernyataan, dan hasil query) setelah digunakan
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

    // Metode untuk mengambil daftar pengeluaran dari database
    public ObservableList<Pengeluaran> ambilDaftarPengeluaran() {
        // Inisialisasi objek ObservableList untuk menyimpan daftar pengeluaran
        ObservableList<Pengeluaran> pengeluaranList = FXCollections.observableArrayList();

        // Inisialisasi objek koneksi, koneksi, pernyataan, dan hasil query
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Membuat koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk mengambil semua kolom dari tabel pengeluaran
            preparedStatement = connection.prepareStatement("SELECT * FROM pengeluaran");

            // Menjalankan query dan mendapatkan hasilnya
            resultSet = preparedStatement.executeQuery();

            // Membuat objek Pengeluaran dari setiap baris hasil query dan menambahkannya ke dalam daftar
            Pengeluaran pengeluaran;
            while (resultSet.next()) {
                pengeluaran = new Pengeluaran(resultSet.getDate("tanggal").toLocalDate(),
                        resultSet.getString("keterangan"),
                        resultSet.getString("kategori"),
                        resultSet.getString("nominal"),
                        resultSet.getInt("ID"));
                pengeluaranList.add(pengeluaran);
            }

        } catch (Exception e) {
            // Menangkap dan mencetak stack trace jika terjadi kesalahan
            e.printStackTrace();

        } finally {
            // Menutup sumber daya (koneksi, pernyataan, dan hasil query) setelah digunakan
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

        // Mengembalikan daftar pengeluaran
        return pengeluaranList;
    }

    // Metode untuk menampilkan daftar pengeluaran pada antarmuka pengguna
    public void tampilkanPengeluaran() {
        // Mengambil daftar pengeluaran dari database
        ObservableList<Pengeluaran> list = ambilDaftarPengeluaran();

        // Mengaitkan kolom pada tabel dengan properti pada kelas Pengeluaran
        colTanggal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, LocalDate>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("keterangan"));
        colKategori.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("kategori"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("nominal"));
        colId.setCellValueFactory(new PropertyValueFactory<Pengeluaran, Integer>("id"));

        // Mengatur item tabel dengan daftar pengeluaran
        tvPengeluaran.setItems(list);

        // Menghitung dan menampilkan total nominal pengeluaran
        hitungTotalNominal();
    }

    //    Fungsi untuk menambah data tabel
    // Metode untuk menambahkan data pengeluaran ke database
    public void Tambah_pengeluaran(ActionEvent event, LocalDate tanggal, String keterangan, String kategori, String nominal) {
        // Inisialisasi objek koneksi database
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Mempersiapkan pernyataan SQL untuk menambahkan data ke tabel pengeluaran
            preparedStatement = connection.prepareStatement("INSERT INTO pengeluaran(tanggal, keterangan, kategori, nominal) VALUES (?, ?, ?, ?)");
            preparedStatement.setDate(1, Date.valueOf(tanggal));
            preparedStatement.setString(2, keterangan);
            preparedStatement.setString(3, kategori);
            preparedStatement.setString(4, nominal);

            // Mengeksekusi pernyataan SQL untuk menambahkan data
            preparedStatement.executeUpdate();

            // Menampilkan pesan sukses dan memperbarui tampilan daftar pengeluaran
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sukses, pengeluaran berhasil ditambahkan ke database.");
            alert.showAndWait();
            tampilkanPengeluaran();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Menutup koneksi dan pernyataan SQL
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

    //    Fungsi untuk mengubah data tabel
    // Metode untuk mengisi nilai pada field dengan data pengeluaran yang dipilih
    public void ubahNilaiField(Pengeluaran pengeluaran) {
        // Mengatur nilai pada date picker, text field keterangan, combo box kategori, dan text field nominal
        dpTanggal.setValue(pengeluaran.getTanggal());
        tfKeterangan.setText(pengeluaran.getKeterangan());
        cbKategori.setValue(pengeluaran.getKategori());
        tfNominal.setText(pengeluaran.getNominal());
    }

    // Metode untuk mengubah data pengeluaran berdasarkan nilai-nilai baru dari field form
    public void ubahDataPengeluaran(Pengeluaran pengeluaran) {
        try {
            // Mengambil nilai baru dari field form
            LocalDate tanggalBaru = dpTanggal.getValue();
            String keteranganBaru = tfKeterangan.getText();
            String nominalBaru = tfNominal.getText();
            String kategoriBaru = cbKategori.getValue();

            // Validasi input nominal, harus berupa angka
            if (!isValidInput(nominalBaru)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nominal harus berupa angka.");
                alert.show();
                return;
            }

            // Menghubungkan ke database untuk melakukan update data pengeluaran
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

                // Menampilkan informasi ke pengguna berdasarkan hasil update
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

    // Metode untuk memvalidasi apakah input dapat diubah menjadi nilai double
    public boolean isValidInput(String input) {
        try {
            Double.parseDouble(input);
            return true; // Jika berhasil diubah menjadi double, mengembalikan true
        } catch (NumberFormatException e) {
            return false; // Jika terjadi NumberFormatException, mengembalikan false
        }
    }

    //    Fungsi untuk menghapus data tabel
    // Metode untuk menghapus data pengeluaran dari database berdasarkan ID.
    public void hapusDataPengeluaran(Pengeluaran pengeluaran){
        // Membuat objek untuk koneksi ke database
        DBConnector koneksi = new DBConnector();

        // Membuat objek koneksi dan preparedStatement yang akan digunakan
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Membuka koneksi ke database
            connection = koneksi.getDatabaselink();

            // Mempersiapkan pernyataan SQL untuk menghapus data berdasarkan ID
            preparedStatement = connection.prepareStatement("DELETE FROM pengeluaran WHERE ID = ?");
            preparedStatement.setInt(1, pengeluaran.getId());

            // Melakukan penghapusan data dan mendapatkan jumlah baris yang terpengaruh
            int rowsAffected = preparedStatement.executeUpdate();

            // Menampilkan pesan sukses atau kesalahan
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sukses, data berhasil dihapus.");
                alert.show();

                // Memperbarui tampilan tabel setelah penghapusan
                tampilkanPengeluaran();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Gagal menghapus data.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Menutup koneksi dan preparedStatement
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