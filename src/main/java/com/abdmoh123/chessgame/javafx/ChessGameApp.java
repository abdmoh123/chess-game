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
    private Popup createPromotionPopup() {
        Popup promote_popup = new Popup();
        VBox popup_content = new VBox();

        Label popup_title = new Label("Select a piece to promote to");

        HBox piece_list = new HBox();
        Button rook_button = new Button("Rook");
        Button knight_button = new Button("Knight");
        Button bishop_button = new Button("Bishop");
        Button queen_button = new Button("Queen");
        Button[] choices = { rook_button, knight_button, bishop_button, queen_button };

        for (Button button : choices) {
            button.getStyleClass().add("popup-buttons");
            button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    promote_popup.hide();
                }
            });
        }
        piece_list.getChildren().addAll(rook_button, knight_button, bishop_button, queen_button);

        popup_content.getChildren().addAll(popup_title, piece_list);
        Pane popup_content_wrapper = new Pane();
        popup_content_wrapper.getChildren().add(popup_content);

        promote_popup.getContent().add(popup_content_wrapper);

        return promote_popup;
    }

    @Override
    public void start(Stage primary_stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main_menu.fxml"));
        Parent root = loader.load();
        primary_stage.setTitle("Chess");

        Scene main_scene = new Scene(root);
        primary_stage.setScene(main_scene);
        main_scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        Popup promote_popup = createPromotionPopup();
        EventHandler<ActionEvent> promotion_event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (!promote_popup.isShowing()) {
                    promote_popup.show(primary_stage);
                }
            }
        };

        primary_stage.show();

        promote_popup.show(primary_stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
