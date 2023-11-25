package percobaan2.projek_pbo_percobaan2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
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
        Btn_daftar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!Tf_username.getText().trim().isEmpty() && Pf_password.getText().trim().isEmpty() && Pf_validasi_password.getText().trim().isEmpty()) {
                    Database.signUpUser(event, Tf_username.getText(), Pf_password.getText(), Pf_validasi_password.getText());
                }else {
                    System.out.println("Tolong isi semua informasi pendaftaran akun!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Tolong isi semua informasi pendafitaran akun!");
                    alert.show();
                }
            }
        });
        Btn_masuk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Database.gantiScene(event, "login.fxml", "Dompet Mahasiswa", null, null);
            }
        });
    }
}
