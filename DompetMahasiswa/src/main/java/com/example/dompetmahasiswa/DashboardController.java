package com.example.dompetmahasiswa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label Label_username;

    @FXML
    private Button Btn_dashboard;
    @FXML
    private Button Btn_pemasukkan;
    @FXML
    private Button Btn_pengeluaran;
    @FXML
    private Button Btn_budget;
    @FXML
    private Button Btn_pelacak_budget;
    @FXML
    private Button Btn_keluar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Btn_keluar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Pergantian_scene.gantiScene(event, "login.fxml", "Dashboard");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void setUserInformation(String username){
        Label_username.setText("@ "+ username);
    }
}
