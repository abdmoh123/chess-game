package com.abdmoh123.chessgame.control.engine;

import java.util.ArrayList;
import java.util.List;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.CastlingMove;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.moves.StandardMove;
import com.abdmoh123.chessgame.pieces.King;

public class Engine {
    private Board chess_board;

    public Engine(Board chess_board_in) {
        this.chess_board = chess_board_in;
    }

    public boolean isCheck(boolean is_white_in) {
        List<Space> attacked_spaces = this.chess_board.getCheckedSpaces(is_white_in);

        for (Space attacked_space : attacked_spaces) {
            if (this.chess_board.getPiece(attacked_space) instanceof King) {
                if (this.chess_board.isSpaceFriendly(attacked_space, is_white_in)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isCheckAfterMove(Move move_in, boolean is_white_in) {
        this.chess_board = this.chess_board.after(move_in);
        boolean is_check = isCheck(is_white_in);
        this.chess_board = this.chess_board.before(move_in);

        return is_check;
    }

    private boolean isMoveLegal(Move move_in, boolean is_white_in) {
        /* Check if move is fully legal (like pseudo legal check but also includes checks) */

        if (!move_in.isPseudoLegal(this.chess_board)) {
            return false;
        }

        if (isCheckAfterMove(move_in, is_white_in)) {
            return false;
        }

        if (move_in instanceof CastlingMove) {
            // cannot castle if in check
            if (!isCheck(is_white_in)) {
                return false;
            }

            // new rook space must also not be attacked by enemy
            Move temp_move = new StandardMove(
                move_in.getOldLocation(), ((CastlingMove) move_in).getNewRookSpace(), move_in.getMovingPiece()
            );
            if (isCheckAfterMove(temp_move, is_white_in)) {
                return false;
            }
        }

        return true;
    }
    
    public List<Move> generateLegalMoves(Space space_in, boolean is_white_in) {
        /* Filters out any moves that cannot be played (e.g. when king is in check) */

        List<Move> pseudo_legal_moves = generatePseudoLegalMoves(space_in, is_white_in);

        // remove illegal moves
        List<Move> filtered_moves = new ArrayList<>();
        for (Move move : pseudo_legal_moves) {
            if (isMoveLegal(move, is_white_in)) {
                filtered_moves.add(move);
            }
        }

        return filtered_moves;
    }
    public List<Move> generatePseudoLegalMoves(Space space_in, boolean is_white_in) {
        /* Generate all pseudo-legal moves (don't take checks into account) */

        // list of moves is empty if space is not controllable
        if (!this.chess_board.isSpaceFriendly(space_in, is_white_in)) {
            return new ArrayList<>();
        }
        return this.chess_board.getPiece(space_in).getPossibleMoves(space_in, this.chess_board);
    }

    public void applyMoveToBoard(Move move) {
        this.chess_board = this.chess_board.after(move);
    }
    public void undoMoveToBoard(Move move) {
        this.chess_board = this.chess_board.before(move);
    }

    public Board getBoard() {
        return this.chess_board;
    }
}
