package com.abdmoh123.chessgame;

import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChessGame extends Application {
    public static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void start(Stage primary_stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/main_menu.fxml"));
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
