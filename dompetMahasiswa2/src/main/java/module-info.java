module org.example.dompetmahasiswa2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.dompetmahasiswa2 to javafx.fxml;
    exports org.example.dompetmahasiswa2;
}