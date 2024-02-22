package com.abdmoh123.chessgame;

import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;

public class ChessGame extends Application {
    public static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void start(Stage primary_stage) throws Exception {
        primary_stage.setTitle("Chess");

        primary_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
