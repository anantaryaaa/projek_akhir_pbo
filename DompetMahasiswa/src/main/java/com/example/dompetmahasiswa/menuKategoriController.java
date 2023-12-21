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
    // Metode initialize untuk inisialisasi tampilan awal pada suatu kontrol JavaFX.
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Menghitung dan menampilkan total nominal pada tampilan
        hitungTotalNominal();

        // Menampilkan daftar kategori pada tampilan
        tampilkanKategori();

        // Menetapkan aksi ketika nilai kolom Kategori dan Nominal diubah pada tabel
        colKategori.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));
        colNominal.setOnEditCommit(t -> ubahNilaiField(t.getRowValue()));

        // Aksi yang dilakukan saat tombol tambah ditekan pada formulir kategori.
        btnTambah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Memastikan bahwa kolom Kategori dan Nominal tidak kosong sebelum menambahkan data.
                if (!tfKategori.getText().trim().isEmpty() && !tfNominal.getText().trim().isEmpty()){
                    // Memanggil fungsi untuk menambahkan kategori dengan data yang valid.
                    TambahKategori(actionEvent, tfKategori.getText(), String.valueOf(Double.parseDouble(tfNominal.getText())));
                }
                else{
                    // Menampilkan pesan kesalahan jika ada kolom yang kosong.
                    System.out.println("Isi semua informasi yang dibutuhkan!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Isi form untuk tambah data!");
                    alert.show();
                }
            }
        });

        // Aksi yang dilakukan saat tombol hapus ditekan pada tabel kategori.
        btnHapus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Mendapatkan item kategori yang dipilih dari tabel
                Kategori selectedKategori = tvKategori.getSelectionModel().getSelectedItem();
                if (selectedKategori != null) {
                    // Menampilkan konfirmasi sebelum menghapus
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Anda yakin ingin menghapus data ini?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Panggil fungsi untuk menghapus data kategori dari database
                        hapusDataKategori(selectedKategori);
                    }
                } else {
                    // Menampilkan pesan jika tidak ada item kategori yang dipilih
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih data kategori yang ingin dihapus.");
                    alert.show();
                }
            }
        });

        // Aksi yang dilakukan saat tombol ubah ditekan pada tabel kategori.
        btnUbah.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (tvKategori.getSelectionModel().getSelectedItem() != null) {
                    // Panggil fungsi untuk mengubah data kategori yang dipilih
                    ubahdataKategori(tvKategori.getSelectionModel().getSelectedItem());
                } else {
                    // Menampilkan pesan peringatan jika tidak ada kategori yang dipilih
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pilih kategori yang ingin diubah.");
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

        // Menetapkan aksi untuk tombol pemasukkan
        pemasukkan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode untuk mengganti tampilan/scene
                    Pergantian_scene.gantiScene(actionEvent, "Pemasukkan.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Menangani pengecualian dengan menghentikan eksekusi dan mencetak jejak kesalahan
                    throw new RuntimeException(e);
                }
            }
        });
        // Menetapkan aksi untuk tombol pengeluaran
        pengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode untuk mengganti tampilan/scene
                    Pergantian_scene.gantiScene(actionEvent, "Pengeluaran.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Menangani pengecualian dengan menghentikan eksekusi dan mencetak jejak kesalahan
                    throw new RuntimeException(e);
                }
            }
        });

        // Menetapkan aksi untuk tombol keluar
        keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Membuat dialog konfirmasi
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Keluar");
                alert.setHeaderText("Apakah Anda yakin ingin keluar?");
                alert.setContentText("Tekan OK untuk keluar, atau Batalkan untuk kembali.");

                // Menangani hasil dari dialog
                alert.showAndWait().ifPresent(response -> {
                    // Memeriksa hasil dialog
                    if (response == ButtonType.OK) {
                        // Tindakan jika pengguna menekan OK (menutup aplikasi)
                        System.out.println("Aplikasi ditutup.");
                        System.exit(0);
                    } else {
                        // Tindakan jika pengguna membatalkan keluar
                        System.out.println("Aplikasi tidak ditutup.");
                    }
                });
            }
        });
    }

// fungsi menampilkan saldo user
    /**
     * Memformat nilai nominal menjadi string dengan pemisah ribuan dan dua digit di belakang koma.
     * @param nominal Nilai nominal yang akan diformat.
     * @return String yang telah diformat.
     */
    public String formatNominal(double nominal) {
        // Membuat objek DecimalFormat dengan pola format tertentu
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        // Menggunakan objek DecimalFormat untuk memformat nilai nominal
        return decimalFormat.format(nominal);
    }

    /**
     * Mengambil total nominal dari tabel kategori_uang dalam database.
     * @return Total nominal dari tabel kategori_uang.
     */
    public double ambilTotalNominal() {
        // Inisialisasi totalNominal dengan nilai awal 0
        double totalNominal = 0;

        try {
            // Membuat koneksi ke database
            DBConnector koneksi = new DBConnector();
            Connection connection = koneksi.getDatabaselink();

            // Membuat statement untuk mengeksekusi query
            Statement statement = connection.createStatement();

            // Mengeksekusi query untuk mendapatkan total nominal
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM kategori_uang");

            // Jika terdapat hasil dari query, mengambil nilai totalNominal dari hasil query
            if (resultSet.next()) {
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            // Menangani pengecualian SQL dengan mencetak jejak kesalahan
            e.printStackTrace();
        }

        // Mengembalikan totalNominal
        return totalNominal;
    }


    /**
     * Menghitung total nominal, memformat nilai, dan menampilkan pada label.
     */
    public void hitungTotalNominal() {
        // Memanggil metode untuk mengambil total nominal dari suatu sumber (misalnya, database)
        double totalNominal = ambilTotalNominal();

        // Memanggil metode untuk memformat nilai nominal menjadi string dengan pemisah ribuan dan dua digit di belakang koma
        String formattedNominal = formatNominal(totalNominal);

        // Mengatur teks pada suatu label (mungkin di antarmuka pengguna) dengan nilai yang telah diformat
        saldoKeuangan.setText(formattedNominal);
    }


// fungsi menampilkan tabel database
    /**
     * Mengambil daftar kategori_uang dari database.
     * @return ObservableList<Kategori> yang berisi daftar kategori_uang.
     */
    public ObservableList<Kategori> ambilDaftarKategori() {
        // Membuat objek ObservableList untuk menampung daftar kategori
        ObservableList<Kategori> kategoriList = FXCollections.observableArrayList();

        // Membuat objek untuk koneksi ke database
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Mempersiapkan statement SQL
            preparedStatement = connection.prepareStatement("SELECT * FROM kategori_uang");

            // Mengeksekusi query SQL
            resultSet = preparedStatement.executeQuery();

            // Iterasi melalui hasil query dan membuat objek Kategori untuk setiap baris
            Kategori kategori;
            while (resultSet.next()) {
                kategori = new Kategori(resultSet.getString("kategori"), resultSet.getString("nominal"), resultSet.getInt("ID"));
                kategoriList.add(kategori);
            }

        } catch (Exception e) {
            // Menangani pengecualian dengan mencetak jejak kesalahan
            e.printStackTrace();
        } finally {
            // Menutup sumber daya (Connection, PreparedStatement, ResultSet) setelah digunakan
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Mengembalikan daftar kategori sebagai ObservableList<Kategori>
        return kategoriList;
    }

    /**
     * Menampilkan daftar kategori pada TableView dan menghitung total nominal.
     */
    public void tampilkanKategori() {
        // Mengambil daftar kategori menggunakan metode ambilDaftarKategori()
        ObservableList<Kategori> list = ambilDaftarKategori();

        // Menetapkan properti nilai kolom pada TableView berdasarkan atribut Kategori
        colKategori.setCellValueFactory(new PropertyValueFactory<Kategori, String>("kategori"));
        colNominal.setCellValueFactory(new PropertyValueFactory<Kategori, String>("nominal"));
        colId.setCellValueFactory(new PropertyValueFactory<Kategori, Integer>("id"));

        // Menetapkan data daftar kategori pada TableView
        tvKategori.setItems(list);

        // Memanggil metode untuk menghitung total nominal
        hitungTotalNominal();
    }

// fungsi untuk menambah data tabel kategori keuangan
    /**
     * Menambahkan kategori baru ke dalam database.
     * @param event ActionEvent yang terkait dengan aksi penambahan kategori.
     * @param kategori Nama kategori yang akan ditambahkan.
     * @param nominal Nilai nominal yang terkait dengan kategori.
     */
    public void TambahKategori(ActionEvent event, String kategori, String nominal) {
        // Membuat objek koneksi ke database
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Mempersiapkan statement SQL untuk menambahkan data baru ke dalam tabel
            preparedStatement = connection.prepareStatement("INSERT INTO kategori_uang(kategori, nominal) VALUES (?, ?)");
            preparedStatement.setString(1, kategori);
            preparedStatement.setString(2, nominal);

            // Mengeksekusi pernyataan SQL untuk menambahkan data
            preparedStatement.executeUpdate();

            // Menampilkan alert informasi bahwa kategori berhasil ditambahkan ke database
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sukses, kategori berhasil ditambahkan ke database.");
            alert.showAndWait();

            // Menampilkan kembali daftar kategori setelah penambahan
            tampilkanKategori();

        } catch (SQLException e) {
            // Menangani pengecualian SQL dengan mencetak jejak kesalahan
            e.printStackTrace();
        } finally {
            // Menutup sumber daya (Connection, PreparedStatement) setelah digunakan
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

// fungsi untuk mengubah data tabel kategori keuangan
    /**
     * Mengubah nilai pada dua field (TextField) berdasarkan data dari objek Kategori.
     * @param kategori Objek Kategori yang menyimpan data kategori.
     */
    public void ubahNilaiField(Kategori kategori) {
        // Mengatur nilai TextField tfKategori dengan nilai kategori dari objek Kategori
        tfKategori.setText(kategori.getKategori());

        // Mengatur nilai TextField tfNominal dengan nilai nominal dari objek Kategori
        tfNominal.setText(kategori.getNominal());
    }

    /**
     * Mengubah data kategori pada database berdasarkan input pengguna.
     * @param kategori Objek Kategori yang menyimpan data kategori.
     */
    public void ubahdataKategori(Kategori kategori) {
        try {
            // Mengambil nilai baru dari TextField tfKategori dan tfNominal
            String kategoriBaru = tfKategori.getText();
            String nominalBaru = tfNominal.getText();

            // Validasi input nominal harus berupa angka
            if (!isValidInput(nominalBaru)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nominal harus berupa angka.");
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

                // Mempersiapkan pernyataan SQL untuk mengubah data kategori berdasarkan ID
                preparedStatement = connection.prepareStatement("UPDATE kategori_uang SET kategori = ?, nominal = ? WHERE ID = ?");

                // Menetapkan nilai parameter pada pernyataan SQL
                preparedStatement.setString(1, kategoriBaru);
                preparedStatement.setString(2, nominalBaru);
                preparedStatement.setInt(3, kategori.getId());

                // Mengeksekusi pernyataan SQL untuk mengubah data
                int rowsAffected = preparedStatement.executeUpdate();

                // Menampilkan alert informasi berdasarkan hasil dari eksekusi SQL
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Sukses, data kategori berhasil diubah.");
                    alert.show();

                    // Memperbarui tampilan tabel setelah perubahan
                    tampilkanKategori();
                    tfNominal.clear();
                    tfKategori.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Gagal mengubah data kategori.");
                    alert.show();
                }
            } catch (SQLException e) {
                // Menangani pengecualian SQL dengan mencetak jejak kesalahan
                e.printStackTrace();
            }
        } catch (Exception e) {
            // Menangani pengecualian umum dengan mencetak jejak kesalahan
            e.printStackTrace();
        }
    }

    /**
     * Memvalidasi apakah suatu string dapat diubah menjadi tipe data double atau tidak.
     * @param input String yang akan divalidasi.
     * @return true jika dapat diubah menjadi tipe data double, false jika tidak.
     */
    public boolean isValidInput(String input) {
        try {
            // Mencoba mengubah string menjadi tipe data double
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            // Menangkap pengecualian jika string tidak dapat diubah menjadi tipe data double
            return false;
        }
    }

// fungsi menghapus data tabel kategori keuangan
    /**
     * Menghapus data kategori dari database berdasarkan objek Kategori.
     * @param kategori Objek Kategori yang akan dihapus.
     */
    public void hapusDataKategori(Kategori kategori) {
        // Membuat objek koneksi ke database
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Mempersiapkan pernyataan SQL untuk menghapus data kategori berdasarkan ID
            preparedStatement = connection.prepareStatement("DELETE FROM kategori_uang WHERE ID = ?");
            preparedStatement.setInt(1, kategori.getId());

            // Mengeksekusi pernyataan SQL untuk menghapus data
            int rowsAffected = preparedStatement.executeUpdate();

            // Menampilkan alert informasi berdasarkan hasil dari eksekusi SQL
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sukses, data berhasil dihapus.");
                alert.show();

                // Memperbarui tampilan tabel setelah penghapusan
                tampilkanKategori();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Gagal menghapus data.");
                alert.show();
            }
        } catch (SQLException e) {
            // Menangani pengecualian SQL dengan mencetak jejak kesalahan
            e.printStackTrace();
        } finally {
            // Menutup sumber daya (Connection, PreparedStatement) setelah digunakan
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
