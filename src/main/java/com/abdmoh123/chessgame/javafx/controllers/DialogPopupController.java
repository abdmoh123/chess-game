package com.abdmoh123.chessgame.javafx.controllers;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DialogPopupController extends VBox {
    @FXML
    private Text menu_title;
    @FXML
    private Text description;

    public DialogPopupController() {
        setupFXMLForm();
    }

    public DialogPopupController(@NamedArg("title") String titleIn, @NamedArg("description") String messageIn) {
        setupFXMLForm();

        setTitle(titleIn);
        setDescription(messageIn);
    }

    private void setupFXMLForm() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/dialog_form.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String titleIn) {
        if (this.menu_title == null)
            this.menu_title = new Text("Title");
        this.menu_title.setText(titleIn);
    }

    public void setDescription(String messageIn) {
        if (this.description == null)
            this.description = new Text("Description");
        this.description.setText(messageIn);
    }

    @FXML
    public void closeDialog() {
        this.setVisible(false);
    }

    public void showDialog() {
        this.setVisible(true);
    }
}
