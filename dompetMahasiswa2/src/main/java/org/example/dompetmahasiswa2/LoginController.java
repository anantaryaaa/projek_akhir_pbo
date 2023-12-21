package org.example.dompetmahasiswa2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField Tf_username;

    @FXML
    private PasswordField Pf_password;

    @FXML
    private Button Btn_masuk;

    @FXML
    private Button Btn_daftar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mengatur event handler untuk tombol daftar
        Btn_daftar.setOnAction(event -> {
            try {
                // Memanggil metode gantiScene untuk beralih ke tampilan Signup.fxml
                Pergantian_scene.gantiScene(event, "Signup.fxml", "Dompet Mahasiswa");
            } catch (IOException e) {
                // Melempar RuntimeException jika terjadi IOException
                throw new RuntimeException(e);
            }
        });

        // Mengatur event handler untuk tombol masuk
        Btn_masuk.setOnAction(event -> Login_user(event, Tf_username.getText(), Pf_password.getText()));
    }

    public static void Login_user(ActionEvent event, String username, String password) {
        // Membuat objek koneksi database
        DBConnector koneksi = new DBConnector();

        // Menggunakan try-with-resources untuk otomatis menutup sumber daya yang diperoleh
        try (Connection connection = koneksi.getDatabaselink();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT Password_user FROM akun WHERE Username = ?")) {

            // Mengatur parameter pada PreparedStatement
            preparedStatement.setString(1, username);

            // Menggunakan try-with-resources untuk otomatis menutup sumber daya ResultSet
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    // Akun tidak ditemukan
                    showErrorAlert("Akun tidak ditemukan!");
                } else {
                    while (resultSet.next()) {
                        String ambilPassword = resultSet.getString("Password_user");
                        if (ambilPassword.equals(password)) {
                            // Password sesuai, pindah ke Dashboard
                            try {
                                Pergantian_scene.gantiScene(event, "Dashboard.fxml", "Dashboard");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Password tidak sesuai
                            showErrorAlert("Password tidak sesuai!");
                        }
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