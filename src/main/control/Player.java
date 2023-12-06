package main.control;

import main.moves.CastlingMove;
import main.moves.Move;
import main.moves.StandardMove;
import main.boards.Board;
import main.boards.Space;
import main.pieces.King;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final boolean IS_WHITE;

    private List<Move> move_history;
    private int points;

    protected Player(boolean is_white_in) {
        this.IS_WHITE = is_white_in;
        this.move_history = new ArrayList<>();
    }

    public abstract Move startMove(Board chess_board);

    private List<Move> filterMoves(List<Move> move_list, Board chess_board) {
        /* Remove moves that cause check (for the current player) and ensure castling is legal */

        List<Move> filtered_moves = new ArrayList<>();

        for (Move move : move_list) {
            if (move instanceof CastlingMove) {
                Move temp_move = new StandardMove(move.getOldLocation(), ((CastlingMove) move).getNewRookSpace(), move.getChessPiece());
                // cannot castle if king is in check
                if (!isCheck(chess_board)) {
                    // both the new king and rook space must not be in check
                    if (!doesMoveCauseCheck(temp_move, chess_board) && !doesMoveCauseCheck(move, chess_board)) {
                        filtered_moves.add(move);
                    }
                }
            }
            else if (!doesMoveCauseCheck(move, chess_board)) {
                filtered_moves.add(move);
            }
        }

        return filtered_moves;
    }

    public List<Move> getMoves(Space space_in, Board chess_board) {
        // list of moves is empty if space is not controllable
        if (!chess_board.isSpaceFriendly(space_in, isWhite())) {
            return new ArrayList<>();
        }
        List<Move> possible_moves = chess_board.getPiece(space_in).getPossibleMoves(space_in, chess_board);

        return filterMoves(possible_moves, chess_board);
    }

    public boolean isWhite() {
        return IS_WHITE;
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

    public boolean doesMoveCauseCheck(Move move_in, Board chess_board) {
        Board board_after = chess_board.after(move_in);
        return isCheck(board_after);
    }
    public boolean doesMoveCauseEnemyCheck(Move move_in, Board chess_board) {
        Board board_after = chess_board.after(move_in);
        return isEnemyCheck(board_after);
    }

    public boolean canPieceMove(Space space_in, Board chess_board) {
        List<Move> possible_moves = chess_board.getPiece(space_in).getPossibleMoves(space_in, chess_board);

        for (Move move : possible_moves) {
            if (!doesMoveCauseCheck(move, chess_board)) {
                return true;
            }
        }
        return false;
    }

    public List<Move> getMoveHistory() {
        return move_history;
    }
    public void recordMove(Move move_in) {
        this.move_history.add(move_in);
    }

    public int getPoints() {
        return points;
    }
    public void addPoints(int points_in) {
        points += points_in;
    }
}
