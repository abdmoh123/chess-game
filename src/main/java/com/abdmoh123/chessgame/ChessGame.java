package com.abdmoh123.chessgame;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ChessGame extends Application {
    public static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void start(Stage primary_stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/new_game_form.fxml"));
        primary_stage.setTitle("Chess");

        Scene main_scene = new Scene(root, 500, 400);
        primary_stage.setScene(main_scene);
        main_scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        primary_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
