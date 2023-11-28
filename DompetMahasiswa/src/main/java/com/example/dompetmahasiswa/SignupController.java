package com.example.dompetmahasiswa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private TextField Tf_username;

    @FXML
    private PasswordField Pf_password_user;
    @FXML
    private PasswordField Pf_validasi_password;

    @FXML
    private Button Btn_daftar;
    @FXML
    private Button Btn_masuk;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Btn_daftar.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                String Username = Tf_username.getText();
//                String Pf_password = Pf_password_user.getText();
//                String Pf_valpass = Pf_validasi_password.getText();
//
//                if (!Pf_password.equals(Pf_valpass)){
//                    showAlert("Password tidak sesuai!", "Validasi password tidak cocok");
//                    return;
//                }
//
//                try (Connection connection = Konektor_database.getConnection()){
//                    String insertQuery
//                }
//            }
//        });

        Btn_masuk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "login.fxml", "Dompet Mahasiswa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
}
