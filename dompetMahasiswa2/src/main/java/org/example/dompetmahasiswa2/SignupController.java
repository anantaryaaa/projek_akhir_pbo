package org.example.dompetmahasiswa2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private TextField Tf_username;

    @FXML
    private PasswordField Pf_password;

    @FXML
    private PasswordField Pf_validasi_password;

    @FXML
    private Button Btn_daftar;

    @FXML
    private Button Btn_masuk;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mengatur event handler untuk tombol masuk
        Btn_masuk.setOnAction(event -> {
            try {
                // Memanggil metode gantiScene untuk beralih ke tampilan login.fxml
                Pergantian_scene.gantiScene(event, "login.fxml", "Dompet Mahasiswa");
            } catch (IOException e) {
                // Melempar RuntimeException jika terjadi IOException
                throw new RuntimeException(e);
            }
        });

        // Mengatur event handler untuk tombol daftar
        Btn_daftar.setOnAction(event -> {
            if (!Tf_username.getText().trim().isEmpty() && !Pf_password.getText().trim().isEmpty()) {
                // Memanggil metode Signup_user dengan menyertakan informasi dari elemen teks
                Signup_user(event, Tf_username.getText(), Pf_password.getText(), Pf_validasi_password.getText());
            } else {
                // Menampilkan pesan kesalahan jika informasi tidak lengkap
                showErrorAlert("Isi semua informasi untuk daftar!");
            }
        });
    }

    // Metode untuk signup
    public static void Signup_user(ActionEvent event, String username, String password, String validasiPassword) {
        // Membuat objek koneksi database
        DBConnector koneksi = new DBConnector();

        // Menggunakan try-with-resources untuk otomatis menutup sumber daya yang diperoleh
        try (Connection connection = koneksi.getDatabaselink();
             PreparedStatement psCheckUserExist = connection.prepareStatement("SELECT * FROM akun WHERE Username = ?");
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO akun(Username, Password_user, Validasi_password) VALUES(?,?,?)")) {

            // Menetapkan parameter pada PreparedStatement untuk memeriksa keberadaan pengguna
            psCheckUserExist.setString(1, username);

            // Menggunakan try-with-resources untuk otomatis menutup sumber daya ResultSet
            try (ResultSet resultSet = psCheckUserExist.executeQuery()) {
                if (resultSet.isBeforeFirst()) {
                    // Akun sudah terdaftar
                    showErrorAlert("Kamu tidak bisa menggunakan username ini");
                } else if (!validasiPassword.equals(password)) {
                    // Validasi password tidak sesuai
                    showErrorAlert("Validasi password tidak sesuai!");
                } else {
                    // Insert data pengguna ke dalam database
                    psInsert.setString(1, username);
                    psInsert.setString(2, password);
                    psInsert.setString(3, validasiPassword);
                    psInsert.executeUpdate();

                    // Pindah ke tampilan "Dashboard" setelah pendaftaran berhasil
                    try {
                        Pergantian_scene.gantiScene(event, "Dashboard.fxml", "Dashboard");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            // Menangani kesalahan SQL
            e.printStackTrace();
        }
    }

    // Metode utilitas untuk menampilkan alert error
    private static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
