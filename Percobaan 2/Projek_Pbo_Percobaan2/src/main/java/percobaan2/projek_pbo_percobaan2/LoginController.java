package percobaan2.projek_pbo_percobaan2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
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
        Btn_masuk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }
}
