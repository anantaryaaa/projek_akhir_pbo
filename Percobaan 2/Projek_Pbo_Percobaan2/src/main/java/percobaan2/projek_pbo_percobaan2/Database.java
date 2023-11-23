package percobaan2.projek_pbo_percobaan2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.*;
import java.util.*;


import java.io.IOException;

public class Database {
    public static void gantiScene(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;

        if (username != null) {

        } else {
            try {
                root = FXMLLoader.load(Database.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password, String password_validate){
        java.sql.Connection connection = null;
        java.sql.PreparedStatement psInsert = null;
        java.sql.PreparedStatement psCheckUserExist = null;
        java.sql.ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekakhirpbo", "root", "*Novand12004");
            psCheckUserExist = connection.prepareCall("SELECT * FROM data_user WHERE username = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("Akun sudah terdaftar");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Anda tidak bisa menggunakan username ini.");
                alert.show();
            }else {
                psInsert = connection.prepareStatement("INSERT INTO data_user(username, password, password_validate VALUES(?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, password_validate);
            }
        }

    }
}
