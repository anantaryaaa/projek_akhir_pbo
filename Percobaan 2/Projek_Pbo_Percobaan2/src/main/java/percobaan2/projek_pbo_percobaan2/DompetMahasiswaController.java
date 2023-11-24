package percobaan2.projek_pbo_percobaan2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DompetMahasiswaController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
