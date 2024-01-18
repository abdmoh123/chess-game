package com.abdmoh123.chessgame.pieces;

public class PieceFactory {
    public static Piece createPiece(PieceType type, boolean is_white_in) {
        switch (type) {
            case BISHOP: return new Bishop(is_white_in);
            case KING: return new King(is_white_in);
            case KNIGHT: return new Knight(is_white_in);
            case PAWN: return new Pawn(is_white_in);
            case QUEEN: return new Queen(is_white_in);
            case ROOK: return new Rook(is_white_in);
            
            default:
                return null;
        }
    }
}
