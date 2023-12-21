package org.example.dompetmahasiswa2;

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
        // Menghitung total nominal pemasukkan
        hitungTotalNominalPemasukkan();

        // Menghitung total nominal pengeluaran
        hitungTotalNominalPengeluaran();

        // Menghitung total nominal kategori
        hitungTotalNominalKategori();

        // Menghitung selisih nominal (pemasukkan - pengeluaran)
        hitungSelisihNominal();

        // Menampilkan daftar pemasukkan
        tampilkanPemasukkan();

        // Menampilkan daftar pengeluaran
        tampilkanPengeluaran();

        // Menampilkan daftar kategori
        tampilkanKategori();

        pemasukkan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode gantiScene untuk beralih ke tampilan Pemasukkan.fxml
                    Pergantian_scene.gantiScene(actionEvent, "Pemasukkan.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan selama proses pergantian scene
                    throw new RuntimeException(e);
                }
            }
        });

        pengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode gantiScene untuk beralih ke tampilan Pengeluaran.fxml
                    Pergantian_scene.gantiScene(actionEvent, "Pengeluaran.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan selama proses pergantian scene
                    throw new RuntimeException(e);
                }
            }
        });

        kategoriPengeluaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Memanggil metode gantiScene untuk beralih ke tampilan Kategori.fxml
                    Pergantian_scene.gantiScene(actionEvent, "Kategori.fxml", "Dompet Mahasiswa");
                } catch (Exception e) {
                    // Melempar RuntimeException jika terjadi kesalahan selama proses pergantian scene
                    throw new RuntimeException(e);
                }
            }
        });

        keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Membuat objek Alert dengan tipe CONFIRMATION
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Keluar");
                alert.setHeaderText("Apakah Anda yakin ingin keluar?");
                alert.setContentText("Tekan OK untuk keluar, atau Batalkan untuk kembali.");

                // Menangani hasil dari dialog
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Aksi ketika pengguna menekan tombol OK
                        System.out.println("Aplikasi ditutup.");
                        System.exit(0);
                    } else {
                        // Aksi ketika pengguna membatalkan
                        System.out.println("Aplikasi tidak ditutup.");
                    }
                });
            }
        });
    }

    // Menampilkan saldo user
    public String formatNominal(double nominal) {
        // Membuat objek DecimalFormat dengan pola format tertentu
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        // Menggunakan DecimalFormat untuk memformat nilai nominal
        return decimalFormat.format(nominal);
    }

    public double ambilTotalNominalKategori() {
        double totalNominal = 0;

        try {
            // Membuat objek DBConnector untuk koneksi ke database
            DBConnector koneksi = new DBConnector();

            // Mendapatkan objek koneksi ke database
            Connection connection = koneksi.getDatabaselink();

            // Membuat objek Statement untuk mengeksekusi query SQL
            Statement statement = connection.createStatement();

            // Mengeksekusi query SQL untuk menghitung total nominal
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM kategori_uang");

            // Jika hasil query tersedia, mengambil totalNominal dari ResultSet
            if (resultSet.next()) {
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            // Menangani kesalahan SQL dengan mencetak stack trace
            e.printStackTrace();
        }

        // Mengembalikan totalNominal
        return totalNominal;
    }

    public void hitungTotalNominalKategori() {
        // Mengambil total nominal dari kategori uang
        double totalNominal = ambilTotalNominalKategori();

        // Memformat totalNominal menjadi teks dengan pemisah ribuan dan dua angka di belakang koma
        String formattedNominal = formatNominal(totalNominal);

        // Menetapkan teks yang diformat pada label saldoKategori
        saldoKategori.setText(formattedNominal);
    }

    public double ambilTotalNominalPengeluaran() {
        double totalNominal = 0;

        try {
            // Membuat objek DBConnector untuk koneksi ke database
            DBConnector koneksi = new DBConnector();

            // Mendapatkan objek koneksi ke database
            Connection connection = koneksi.getDatabaselink();

            // Membuat objek Statement untuk mengeksekusi query SQL
            Statement statement = connection.createStatement();

            // Mengeksekusi query SQL untuk menghitung total nominal pengeluaran
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pengeluaran");

            // Jika hasil query tersedia, mengambil totalNominal dari ResultSet
            if (resultSet.next()) {
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            // Menangani kesalahan SQL dengan mencetak stack trace
            e.printStackTrace();
        }

        // Mengembalikan totalNominal
        return totalNominal;
    }

    public void hitungTotalNominalPengeluaran() {
        // Mengambil total nominal pengeluaran
        double totalNominal = ambilTotalNominalPengeluaran();

        // Memformat totalNominal menjadi teks dengan pemisah ribuan dan dua angka di belakang koma
        String formattedNominal = formatNominal(totalNominal);

        // Menetapkan teks yang diformat pada label saldoPengeluaran
        saldoPengeluaran.setText(formattedNominal);
    }

    public double ambilTotalNominalPemasukkan() {
        double totalNominal = 0;

        try {
            // Membuat objek DBConnector untuk koneksi ke database
            DBConnector koneksi = new DBConnector();

            // Mendapatkan objek koneksi ke database
            Connection connection = koneksi.getDatabaselink();

            // Membuat objek Statement untuk mengeksekusi query SQL
            Statement statement = connection.createStatement();

            // Mengeksekusi query SQL untuk menghitung total nominal pemasukkan
            ResultSet resultSet = statement.executeQuery("SELECT SUM(nominal) FROM pemasukkan");

            // Jika hasil query tersedia, mengambil totalNominal dari ResultSet
            if (resultSet.next()) {
                totalNominal = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            // Menangani kesalahan SQL dengan mencetak stack trace
            e.printStackTrace();
        }

        // Mengembalikan totalNominal
        return totalNominal;
    }

    public void hitungTotalNominalPemasukkan() {
        // Mengambil total nominal pemasukkan
        double totalNominal = ambilTotalNominalPemasukkan();

        // Memformat totalNominal menjadi teks dengan pemisah ribuan dan dua angka di belakang koma
        String formattedNominal = formatNominal(totalNominal);

        // Menetapkan teks yang diformat pada label saldoPemasukkan
        saldoPemasukkan.setText(formattedNominal);
    }

    public void hitungSelisihNominal() {
        // Mengambil total nominal pemasukkan dan pengeluaran
        double totalNominalPemasukkan = ambilTotalNominalPemasukkan();
        double totalNominalPengeluaran = ambilTotalNominalPengeluaran();

        // Hitung selisihnya
        double selisihNominal = totalNominalPemasukkan - totalNominalPengeluaran;

        // Format angka dengan dua digit di belakang koma
        String formattedNominal = formatNominal(selisihNominal);

        // Set teks pada label saldoKeuangan
        saldoKeuangan.setText(formattedNominal);
    }

// menampilkan data tabel menu-menu user
    /**
     * Metode ini mengambil daftar pemasukkan dari tabel database pemasukkan
     * dan mengonversinya ke dalam bentuk ObservableList<Pemasukkan>.
     * Setiap baris hasil query direpresentasikan sebagai objek Pemasukkan
     * dan ditambahkan ke dalam ObservableList.
     *
     * @return ObservableList<Pemasukkan> berisi data pemasukkan dari database.
     */
    public ObservableList<Pemasukkan> ambilDaftarPemasukkan(){
        ObservableList<Pemasukkan> pemasukkanList = FXCollections.observableArrayList();
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk mengambil data pemasukkan
            preparedStatement = connection.prepareStatement("SELECT * FROM pemasukkan");

            // Mengeksekusi query SQL dan mendapatkan hasilnya
            resultSet = preparedStatement.executeQuery();

            Pemasukkan pemasukkan;
            // Membaca setiap baris hasil query
            while (resultSet.next()){
                // Membuat objek Pemasukkan dari hasil query
                pemasukkan = new Pemasukkan(resultSet.getDate("tanggal").toLocalDate(), resultSet.getString("keterangan"), resultSet.getString("nominal"), resultSet.getInt("ID"));
                // Menambahkan objek Pemasukkan ke dalam ObservableList
                pemasukkanList.add(pemasukkan);
            }
        }catch (Exception e){
            // Menangani pengecualian dengan mencetak stack trace
            e.printStackTrace();
        } finally {
            // Menutup sumber daya seperti koneksi dan pernyataan SQL
            // Pastikan penutupan sumber daya dilakukan dengan benar
            // pada implementasi yang sebenarnya
            closeResources(connection, preparedStatement, resultSet);
        }
        // Mengembalikan ObservableList<Pemasukkan> yang telah diisi
        return pemasukkanList;
    }

    /**
     * Metode ini mengambil daftar pemasukkan dari metode ambilDaftarPemasukkan
     * dan menetapkan sumber data untuk suatu kontrol tabel (tabelPemasukkan).
     * Pada contoh ini, kolom keterangan dari tabel pemasukkan dihubungkan
     * dengan properti "keterangan" dari kelas Pemasukkan menggunakan PropertyValueFactory.
     */
    public void tampilkanPemasukkan(){
        // Mengambil daftar pemasukkan dari metode ambilDaftarPemasukkan
        ObservableList<Pemasukkan> listPemasukkan = ambilDaftarPemasukkan();

        // Menghubungkan kolom keterangan dengan properti "keterangan" pada kelas Pemasukkan
        kolomPemasukkan.setCellValueFactory(new PropertyValueFactory<Pemasukkan, String>("keterangan"));

        // Menetapkan sumber data untuk tabelPemasukkan menggunakan daftar pemasukkan
        tabelPemasukkan.setItems(listPemasukkan);
    }

    /**
     * Metode ini mengambil daftar pengeluaran dari tabel database pengeluaran
     * dan mengonversinya ke dalam bentuk ObservableList<Pengeluaran>.
     * Setiap baris hasil query direpresentasikan sebagai objek Pengeluaran
     * dan ditambahkan ke dalam ObservableList.
     *
     * @return ObservableList<Pengeluaran> berisi data pengeluaran dari database.
     */
    public ObservableList<Pengeluaran> ambilDaftarPengeluaran(){
        ObservableList<Pengeluaran> pengeluaranList = FXCollections.observableArrayList();
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk mengambil data pengeluaran
            preparedStatement = connection.prepareStatement("SELECT * FROM pengeluaran");

            // Mengeksekusi query SQL dan mendapatkan hasilnya
            resultSet = preparedStatement.executeQuery();

            Pengeluaran pengeluaran;
            // Membaca setiap baris hasil query
            while (resultSet.next()){
                // Membuat objek Pengeluaran dari hasil query
                pengeluaran = new Pengeluaran(resultSet.getDate("tanggal").toLocalDate(), resultSet.getString("keterangan"), resultSet.getString("kategori"), resultSet.getString("nominal"), resultSet.getInt("ID"));
                // Menambahkan objek Pengeluaran ke dalam ObservableList
                pengeluaranList.add(pengeluaran);
            }
        }catch (Exception e){
            // Menangani pengecualian dengan mencetak stack trace
            e.printStackTrace();
        } finally {
            // Menutup sumber daya seperti koneksi dan pernyataan SQL
            // Pastikan penutupan sumber daya dilakukan dengan benar
            // pada implementasi yang sebenarnya
            closeResources(connection, preparedStatement, resultSet);
        }
        // Mengembalikan ObservableList<Pengeluaran> yang telah diisi
        return pengeluaranList;
    }

    /**
     * Metode ini mengambil daftar pengeluaran dari metode ambilDaftarPengeluaran
     * dan menetapkan sumber data untuk suatu kontrol tabel (tabelPengeluaran).
     * Pada contoh ini, kolom keterangan dari tabel pengeluaran dihubungkan
     * dengan properti "keterangan" dari kelas Pengeluaran menggunakan PropertyValueFactory.
     */
    public void tampilkanPengeluaran(){
        // Mengambil daftar pengeluaran dari metode ambilDaftarPengeluaran
        ObservableList<Pengeluaran> listPengeluaran = ambilDaftarPengeluaran();

        // Menghubungkan kolom keterangan dengan properti "keterangan" pada kelas Pengeluaran
        kolomPengeluaran.setCellValueFactory(new PropertyValueFactory<Pengeluaran, String>("keterangan"));

        // Menetapkan sumber data untuk tabelPengeluaran menggunakan daftar pengeluaran
        tabelPengeluaran.setItems(listPengeluaran);
    }

    /**
     * Metode ini mengambil daftar kategori uang dari tabel database kategori_uang
     * dan mengonversinya ke dalam bentuk ObservableList<Kategori>.
     * Setiap baris hasil query direpresentasikan sebagai objek Kategori
     * dan ditambahkan ke dalam ObservableList.
     *
     * @return ObservableList<Kategori> berisi data kategori uang dari database.
     */
    public ObservableList<Kategori> ambilDaftarKategori(){
        ObservableList<Kategori> kategoriList = FXCollections.observableArrayList();
        DBConnector koneksi = new DBConnector();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // Mendapatkan koneksi ke database
            connection = koneksi.getDatabaselink();

            // Membuat pernyataan SQL untuk mengambil data kategori uang
            preparedStatement = connection.prepareStatement("SELECT * FROM kategori_uang");

            // Mengeksekusi query SQL dan mendapatkan hasilnya
            resultSet = preparedStatement.executeQuery();

            Kategori kategori;
            // Membaca setiap baris hasil query
            while (resultSet.next()){
                // Membuat objek Kategori dari hasil query
                kategori = new Kategori(resultSet.getString("kategori"), resultSet.getString("nominal"), resultSet.getInt("ID"));
                // Menambahkan objek Kategori ke dalam ObservableList
                kategoriList.add(kategori);
            }
        }catch (Exception e){
            // Menangani pengecualian dengan mencetak stack trace
            e.printStackTrace();
        } finally {
            // Menutup sumber daya seperti koneksi dan pernyataan SQL
            // Pastikan penutupan sumber daya dilakukan dengan benar
            // pada implementasi yang sebenarnya
            closeResources(connection, preparedStatement, resultSet);
        }
        // Mengembalikan ObservableList<Kategori> yang telah diisi
        return kategoriList;
    }

    /**
     * Metode ini mengambil daftar kategori uang dari metode ambilDaftarKategori
     * dan menetapkan sumber data untuk suatu kontrol tabel (tabelKategori).
     * Pada contoh ini, kolom kategori dari tabel kategori_uang dihubungkan
     * dengan properti "kategori" dari kelas Kategori menggunakan PropertyValueFactory.
     */
    public void tampilkanKategori(){
        // Mengambil daftar kategori uang dari metode ambilDaftarKategori
        ObservableList<Kategori> listKategori = ambilDaftarKategori();

        // Menghubungkan kolom kategori dengan properti "kategori" pada kelas Kategori
        kolomKategori.setCellValueFactory(new PropertyValueFactory<Kategori,String>("kategori"));

        // Menetapkan sumber data untuk tabelKategori menggunakan daftar kategori uang
        tabelKategori.setItems(listKategori);
    }

    /**
     * Metode ini digunakan untuk menutup sumber daya seperti koneksi, pernyataan SQL, dan hasil query.
     * Pastikan penutupan sumber daya dilakukan dengan benar pada implementasi yang sebenarnya.
     */
    private void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}