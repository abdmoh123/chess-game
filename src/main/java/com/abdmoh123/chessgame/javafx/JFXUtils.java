package com.abdmoh123.chessgame.javafx;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class JFXUtils {
    public static void setChildPane(Pane parent, Node child) {
        parent.getChildren().clear();
        parent.getChildren().add(child);
    }
}
