package com.abdmoh123.chessgame.ui.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class SpacePane extends VBox {
    private ImageView piece_image;
    private final int IMAGE_SIZE;

    public SpacePane(boolean is_white_in, int square_size_in) {
        super();

        this.IMAGE_SIZE = square_size_in; // size of spaces

        if (is_white_in) setBackground(new Background(new BackgroundFill(Paint.valueOf("#ebdbb2"), null, null)));
        else setBackground(new Background(new BackgroundFill(Paint.valueOf("#504945"), null, null)));
        
        piece_image = new ImageView();
        piece_image.setPreserveRatio(true);
        piece_image.setSmooth(false);

        piece_image.setFitWidth(IMAGE_SIZE);
        piece_image.setFitHeight(IMAGE_SIZE);

        getChildren().add(piece_image);
    }

    public ImageView getPieceImage() {
        return this.piece_image;
    }

    public void setPieceImage(char piece_symbol) {
        String file_path = "../../images/pieces/";
        if (Character.isUpperCase(piece_symbol)) file_path += "white/";
        else file_path += "black/";

        switch (Character.toLowerCase(piece_symbol)) {
            case 'b': 
                file_path += "bishop.png";
                break;
            case 'k': 
                file_path += "king.png";
                break;
            case 'n': 
                file_path += "knight.png";
                break;
            case 'p': 
                file_path += "pawn.png";
                break;
            case 'q': 
                file_path += "queen.png";
                break;
            case 'r': 
                file_path += "rook.png";
                break;
            
            default: return;
        }

        try {
            String image_path = getClass().getResource(file_path).toString();
            this.piece_image.setImage(new Image(image_path, IMAGE_SIZE, IMAGE_SIZE, true, false));
        }
        catch (Exception e) {
            System.out.println("Failed to find texture!");
            e.printStackTrace();
        }
    }
}
