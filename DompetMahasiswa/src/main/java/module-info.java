module com.example.dompetmahasiswa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.dompetmahasiswa to javafx.fxml;
    exports com.example.dompetmahasiswa;
}