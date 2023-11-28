package com.example.dompetmahasiswa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.sql.*;

import static com.example.dompetmahasiswa.DBConnector.*;

public class LoginController implements Initializable {
    @FXML
    private TextField Tf_username;

    @FXML
    private PasswordField Pf_password;

    @FXML
    private Button Btn_masuk;

    @FXML
    private Button Btn_daftar;

    public void Btn_daftar(ActionEvent event){
        try {
            Pergantian_scene.gantiScene(event,"Signup.fxml","Daftar akun");
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Btn_masuk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = Tf_username.getText().trim();
                String password = Pf_password.getText().trim();

                if (isValidated(username, password)) {
                    try {
                        Pergantian_scene.gantiScene(event, "Signup.fxml", "Dompet Mahasiswa");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    private boolean isValidated(String username, String password) {
        Konektor_database dbc = getDatabaseConnection();
        Connection connection = dbc.getConnection();

        try {
            String sql = String.format("SELECT username, password FROM akun WHERE username='%s' AND password='%s'", username, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            result.next();
            if (result.getString(1) != null) {
                System.out.println(result.getString(username));
                return true;
            }


        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return false;
    }
}
