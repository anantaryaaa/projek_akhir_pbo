module jfx1.belajarjfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens jfx1.belajarjfx to javafx.fxml;
    exports jfx1.belajarjfx;
}