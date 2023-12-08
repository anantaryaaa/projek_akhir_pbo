package com.example.dompetmahasiswa;

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
                Pergantian_scene.gantiScene(event, "login.fxml", "Dompet Mahasiswa");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Mengatur event handler untuk tombol daftar
        Btn_daftar.setOnAction(event -> {
            if (!Tf_username.getText().trim().isEmpty() && !Pf_password.getText().trim().isEmpty()) {
                Signup_user(event, Tf_username.getText(), Pf_password.getText(), Pf_validasi_password.getText());
            } else {
                showErrorAlert("Isi semua informasi untuk daftar!");
            }
        });
    }

    // Metode untuk signup
    public static void Signup_user(ActionEvent event, String username, String password, String validasiPassword) {
        DBConnector koneksi = new DBConnector();
        try (Connection connection = koneksi.getDatabaselink();
             PreparedStatement psCheckUserExist = connection.prepareStatement("SELECT * FROM akun WHERE Username = ?");
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO akun(Username, Password_user, Validasi_password) VALUES(?,?,?)")) {

            psCheckUserExist.setString(1, username);
            try (ResultSet resultSet = psCheckUserExist.executeQuery()) {
                if (resultSet.isBeforeFirst()) {
                    // Akun sudah terdaftar
                    showErrorAlert("Kamu tidak bisa menggunakan username ini");
                } else if (!validasiPassword.equals(password)) {
                    // Validasi password tidak sesuai
                    showErrorAlert("Validasi password tidak sesuai!");
                } else {
                    // Insert ke database
                    psInsert.setString(1, username);
                    psInsert.setString(2, password);
                    psInsert.setString(3, validasiPassword);
                    psInsert.executeUpdate();

                    try {
                        Pergantian_scene.gantiScene(event, "Dashboard.fxml", "Dashboard");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showErrorAlert(String message) {
        System.out.println(message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
