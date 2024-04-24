package com.abdmoh123.chessgame.pieces;

public class PieceFactory {
    public static Piece createPiece(char piece_symbol) {
        boolean is_white = Character.isUpperCase(piece_symbol);
        switch (Character.toLowerCase(piece_symbol)) {
            case 'b':
                return new Bishop(is_white);
            case 'k':
                return new King(is_white);
            case 'n':
                return new Knight(is_white);
            case 'p':
                return new Pawn(is_white);
            case 'q':
                return new Queen(is_white);
            case 'r':
                return new Rook(is_white);

            default:
                return null;
        }
    }
}
