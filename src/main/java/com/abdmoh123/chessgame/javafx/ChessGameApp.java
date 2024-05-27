package com.abdmoh123.chessgame.javafx;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ChessGameApp extends Application {

    @Override
    public void start(Stage primary_stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main_menu.fxml"));
        Parent root = loader.load();
        primary_stage.setTitle("Chess");

        Scene main_scene = new Scene(root);
        primary_stage.setScene(main_scene);
        main_scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        primary_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
