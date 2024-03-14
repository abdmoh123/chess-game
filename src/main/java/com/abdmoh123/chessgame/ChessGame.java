package com.abdmoh123.chessgame;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        primary_stage.setTitle("Chess");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        Text form_title = new Text("New game");
        form_title.setId("menu-title");
        grid.add(form_title, 0, 0, 3, 1);

        Button standard_chess_button = new Button();
        standard_chess_button.setText("Standard Chess");
        grid.add(standard_chess_button, 0, 1);
        Button chess_960_button = new Button();
        chess_960_button.setText("Chess 960");
        grid.add(chess_960_button, 1, 1);
        Button custom_chess_button = new Button();
        custom_chess_button.setText("Custom Game");
        grid.add(custom_chess_button, 2, 1);

        Label custom_FEN_label = new Label("Enter FEN string:");
        grid.add(custom_FEN_label, 0, 2);
        TextField custom_FEN_field = new TextField();
        grid.add(custom_FEN_field, 1, 2, 2, 1);

        Button start_game_button = new Button();
        start_game_button.setText("Start Game");
        grid.add(start_game_button, 1, 3);

        Text output_text = new Text();
        output_text.setTextAlignment(TextAlignment.CENTER);
        grid.add(output_text, 0, 4, 3, 1);

        start_game_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                output_text.setText("Chess!");
            }
        });

        Scene main_scene = new Scene(grid, 500, 400);
        primary_stage.setScene(main_scene);
        main_scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        primary_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
