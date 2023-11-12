package jfx1.belajarjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class HelloController {

    @FXML
    private Label feedbackMessage;
    @FXML
    private Button buttonKeluar;
    @FXML
    private TextField kolomUsername;
    @FXML
    private PasswordField kolomPassword;


    public void buttonMasukOnAction(ActionEvent e){
        if (kolomUsername.getText().isBlank() == false && kolomPassword.getText().isBlank() == false){
            feedbackMessage.setText("Masukkan akun");
        }else {
            feedbackMessage.setText("Masukkan Username dan Password dengan benar");
        }
    }

    public void setButtonKeluar(ActionEvent e){
        Stage stage = (Stage) buttonKeluar.getScene().getWindow();
        stage.close();
    }
}