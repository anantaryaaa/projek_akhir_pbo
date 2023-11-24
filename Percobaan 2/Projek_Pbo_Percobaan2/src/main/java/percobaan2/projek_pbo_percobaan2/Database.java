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
    public static void gantiScene(ActionEvent event, String fxmlFile, String title, String username, String password) {
        Parent root = null;

        if (username != null && password != null) {
            try {
                FXMLLoader loader = new FXMLLoader(Database.class.getResource(fxmlFile));
                root = loader.load();
                DashboardController dashboardController = loader.getController();
                dashboardController.setUserInformation(username);
            }catch (IOException e){
                e.printStackTrace();
            }
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
                psInsert.executeUpdate();

                gantiScene(event, "dashboard.fxml", "Dompet Mahasiswa", username, password);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try {
                    psInsert.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psCheckUserExist != null){
                try {
                    psCheckUserExist.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static void loginUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekakhirpbo", "root", "*Novand12004");
            preparedStatement = connection.prepareStatement("SELECT password, password_validate FROM data_user WHERE username = ?");
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()){
                System.out.println("User tidak ditemukan dalam database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Akun tidak valid!");
                alert.show();
            }else {
                while (resultSet.next()){
                    String temukanLagiPassword = resultSet.getString("password");
                    String temukanLagiPassValidasi = resultSet.getString("validate_password");
                    if (temukanLagiPassword.equals(password)){
                        gantiScene(event, "dashboard.fxml", "Dompet Mahasiswa", username, temukanLagiPassValidasi);
                    }else {
                        System.out.println("Password tidak sesuai");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Akun tidak valid!");
                        alert.show();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
