package com.abdmoh123.chessgame.control;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.CastlingMove;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.moves.StandardMove;
import com.abdmoh123.chessgame.pieces.King;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final boolean IS_WHITE;
    private int points;

    protected Player(boolean is_white_in) {
        this.IS_WHITE = is_white_in;
    }

    public boolean canPlay(Move move_in, Board chess_board) {
        /* Check if move is fully legal (like pseudo legal check but also includes checks) */

        if (!move_in.isPseudoLegal(chess_board)) {
            return false;
        }

        if (isCheckAfterMove(move_in, chess_board)) {
            return false;
        }

        if (move_in instanceof CastlingMove) {
            // cannot castle if in check
            if (!isCheck(chess_board)) {
                return false;
            }

            // new rook space must also not be attacked by enemy
            Move temp_move = new StandardMove(
                move_in.getOldLocation(), ((CastlingMove) move_in).getNewRookSpace(), move_in.getMovingPiece()
            );
            if (isCheckAfterMove(temp_move, chess_board)) {
                return false;
            }
        }

        return true;
    }

    public List<Move> getLegalMoves(Space space_in, Board chess_board) {
        /* Filters out any moves that cannot be played (e.g. when king is in check) */

        // list of moves is empty if space is not controllable
        if (!chess_board.isSpaceFriendly(space_in, isWhite())) {
            return new ArrayList<>();
        }
        List<Move> possible_moves = chess_board.getPiece(space_in).getPossibleMoves(space_in, chess_board);

        // remove illegal moves
        List<Move> filtered_moves = new ArrayList<>();
        for (Move move : possible_moves) {
            if (canPlay(move, chess_board)) {
                filtered_moves.add(move);
            }
        }

        return filtered_moves;
    }

    public boolean isCheck(Board chess_board) {
        List<Space> attacked_spaces = chess_board.getCheckedSpaces(isWhite());

        for (Space attacked_space : attacked_spaces) {
            if (chess_board.getPiece(attacked_space) instanceof King) {
                if (chess_board.isSpaceFriendly(attacked_space, isWhite())) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isEnemyCheck(Board chess_board) {
        List<Space> attacked_spaces = chess_board.getCheckedSpaces(!isWhite());

        for (Space attacked_space : attacked_spaces) {
            if (chess_board.getPiece(attacked_space) instanceof King) {
                if (chess_board.isSpaceEnemy(attacked_space, isWhite())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckAfterMove(Move move_in, Board chess_board) {
        Board board_after = chess_board.after(move_in);
        return isCheck(board_after);
    }
    public boolean isEnemyCheckAfterMove(Move move_in, Board chess_board) {
        Board board_after = chess_board.after(move_in);
        return isEnemyCheck(board_after);
    }

    public boolean canPieceMove(Space space_in, Board chess_board) {
        List<Move> possible_moves = chess_board.getPiece(space_in).getPossibleMoves(space_in, chess_board);

        for (Move move : possible_moves) {
            if (!isCheckAfterMove(move, chess_board)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWhite() {
        return this.IS_WHITE;
    }
    public int getPoints() {
        return this.points;
    }
    public void addPoints(int points_in) {
        this.points += points_in;
    }

    public abstract Move startMove(Board chess_board);
}
