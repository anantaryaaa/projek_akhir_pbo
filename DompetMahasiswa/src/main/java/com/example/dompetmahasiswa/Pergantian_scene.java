package com.example.dompetmahasiswa;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Pergantian_scene {
    public static void gantiScene(ActionEvent event, String fxmlFile, String title) throws IOException {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(Pergantian_scene.class.getResource(fxmlFile));
        root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
