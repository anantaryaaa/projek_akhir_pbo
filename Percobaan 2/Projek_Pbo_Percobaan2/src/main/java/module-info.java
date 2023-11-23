module percobaan2.projek_pbo_percobaan2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens percobaan2.projek_pbo_percobaan2 to javafx.fxml;
    exports percobaan2.projek_pbo_percobaan2;
}