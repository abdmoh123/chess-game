package com.abdmoh123.chessgame.utils;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.pieces.*;

public class ChessCSVReader {
    private static Piece convertSymbolToPiece(String piece_symbol_in) {
        switch (piece_symbol_in.replaceAll(" ", "")) { // remove whitespace
            // white pieces
            case "WK":
                return new King(true);
            case "WQ":
                return new Queen(true);
            case "WB":
                return new Bishop(true);
            case "WN":
                return new Knight(true);
            case "WR":
                return new Rook(true);
            case "WP":
                return new Pawn(true);
            // black pieces
            case "BK":
                return new King(false);
            case "BQ":
                return new Queen(false);
            case "BB":
                return new Bishop(false);
            case "BN":
                return new Knight(false);
            case "BR":
                return new Rook(false);
            case "BP":
                return new Pawn(false);
            default:
                return null;
        }
    }
    public static Piece[][] readBoardCSV(String file_path) throws URISyntaxException, IOException, CsvValidationException {
        Path path = Paths.get(ClassLoader.getSystemResource(file_path).toURI());

        // read csv file and put data into 2d list
        List<List<String>> layout = new ArrayList<List<String>>();
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csv_reader = new CSVReader(reader)) {
                String[] row = null;
                while ((row = csv_reader.readNext()) != null) {
                    layout.add(Arrays.asList(row));
                }
            }
        }
        
        // convert read string data into piece type and put into 2d square array
        Piece[][] board_contents = new Piece[layout.size()][layout.size()];
        for (int i = 0; i < layout.size(); ++i) {
            for (int j = 0; j < layout.size(); ++j) {
                String symbol = layout.get(i).get(j);
                // reverse order so bottom left = (0, 0)
                int y_coordinate = layout.size() - i - 1;

                Piece piece = convertSymbolToPiece(symbol);
                if (piece instanceof King && !((King) piece).isStartingPosition(new Space(j, y_coordinate))) {
                    ((King) piece).disableCastling();
                }
                board_contents[y_coordinate][j] = piece;
            }
        }

        return board_contents;
    }
}
