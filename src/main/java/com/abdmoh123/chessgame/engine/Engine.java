package com.abdmoh123.chessgame.engine;

import java.util.ArrayList;
import java.util.List;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.CastlingMove;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.moves.StandardMove;
import com.abdmoh123.chessgame.pieces.Bishop;
import com.abdmoh123.chessgame.pieces.King;
import com.abdmoh123.chessgame.pieces.Pawn;
import com.abdmoh123.chessgame.pieces.Piece;

public class Engine {
    private Board chess_board;

    private class MoveWithEval {
        /*
         * Class grouping evaluation values with given moves (used for finding best
         * moves)
         */

        private Move move;
        private int eval_value;

        public MoveWithEval(Move move_in, int eval_in) {
            this.move = move_in;
            this.eval_value = eval_in;
        }

        public Move getMove() {
            return this.move;
        }

        public int getEvalValue() {
            return this.eval_value;
        }
    }

    public Engine(Board chess_board_in) {
        this.chess_board = chess_board_in;
    }

    public boolean isCheckMate(boolean is_white_in) {
        // can't be checkmate if not in check
        if (!isCheck(is_white_in)) {
            return false;
        }

        List<Space> friendly_spaces = getBoard().getFriendlySpaces(is_white_in);
        for (Space space : friendly_spaces) {
            List<Move> legal_moves = generateLegalMoves(space, is_white_in);
            if (legal_moves.size() > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isCheckMateAfterMove(Move move_in, boolean is_white_in) {
        this.chess_board = this.chess_board.after(move_in);
        boolean is_check_mate = isCheckMate(is_white_in);
        this.chess_board = this.chess_board.before(move_in);

        return is_check_mate;
    }

    public boolean isStalemate(boolean is_white_in) {
        /* Check if player has any moves that don't lead getting checked */

        // can't be stalemate if player is in check
        if (isCheck(is_white_in)) {
            return false;
        }

        List<Space> friendly_spaces = getBoard().getFriendlySpaces(is_white_in);
        for (Space friendly_space : friendly_spaces) {
            List<Move> possible_moves = getBoard().getPiece(friendly_space).getPossibleMoves(
                    friendly_space, getBoard());

            if (possible_moves.size() > 0) {
                for (Move move : possible_moves) {
                    if (!isCheckAfterMove(move, is_white_in)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean hasOnlyBishopOrKnight(List<Space> spaces) {
        if (spaces.size() == 2) {
            for (Space space : spaces) {
                // if only other piece is bishop or knight, then player cannot checkmate
                if (this.chess_board.getPiece(space).getValue() == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Space> removeKingFromList(List<Space> spaces) {
        List<Space> new_list = new ArrayList<>();
        for (Space space : spaces) {
            if (!(this.chess_board.getPiece(space) instanceof King)) {
                new_list.add(space);
            }
        }
        return new_list;
    }

    private boolean areBishopsSameColour(List<Space> white_spaces, List<Space> black_spaces) {
        /*
         * If King + Bishop vs King + Bishop, bishops need to be on the same coloured
         * tiles to be dead position
         */

        List<Space> white_spaces_without_king = removeKingFromList(white_spaces);
        List<Space> black_spaces_without_king = removeKingFromList(black_spaces);

        Space white_piece_space = white_spaces_without_king.get(0);
        Space black_piece_space = black_spaces_without_king.get(0);
        Piece white_piece = this.chess_board.getPiece(white_piece_space);
        Piece black_piece = this.chess_board.getPiece(black_piece_space);

        if (white_piece instanceof Bishop && black_piece instanceof Bishop) {
            if (((Bishop) white_piece).isDark(white_piece_space) == ((Bishop) black_piece).isDark(black_piece_space)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeadPosition() {
        List<Space> white_spaces = this.chess_board.getFriendlySpaces(true);
        List<Space> black_spaces = this.chess_board.getFriendlySpaces(false);

        // only 2 kings remain
        if (white_spaces.size() + black_spaces.size() == 2) {
            return true;
        }

        // check white king vs black king + bishop/knight
        if (white_spaces.size() == 1) {
            if (hasOnlyBishopOrKnight(black_spaces)) {
                return true;
            }
        }
        // check white king + bishop/knight vs black king
        if (black_spaces.size() == 1) {
            if (hasOnlyBishopOrKnight(white_spaces)) {
                return true;
            }
        }

        // check white king + bishop vs black king + bishop (same colour tile)
        if (white_spaces.size() == 2 && black_spaces.size() == 2) {
            return areBishopsSameColour(white_spaces, black_spaces);
        }

        return false;
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
        /*
         * Check if move is fully legal (like pseudo legal check but also includes
         * checks)
         */

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
                    move_in.getOldLocation(), ((CastlingMove) move_in).getNewRookSpace(), move_in.getMovingPiece());
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

    private int countMaterialValues(boolean is_white_in) {
        int total_points = 0;

        List<Space> friendly_spaces = getBoard().getFriendlySpaces(is_white_in);
        for (Space space : friendly_spaces) {
            total_points += getBoard().getPiece(space).getValue();
        }
        return total_points;
    }

    public int evaluate(boolean is_white_playing) {
        // checkmate moves are priority
        if (isCheckMate(!is_white_playing))
            return 999;
        if (isCheckMate(is_white_playing))
            return -999;

        // difference in points is general way of evaluating position (encourages
        // captures)
        int white_points = countMaterialValues(true);
        int black_points = countMaterialValues(false);
        int evaluation = white_points - black_points;
        if (!is_white_playing) {
            evaluation *= -1;
        }

        if (isCheck(!is_white_playing)) {
            ++evaluation;
        } // encourage check moves
        else if (isCheck(is_white_playing)) {
            --evaluation;
        } // discourage getting checked

        return evaluation;
    }

    private int evaluateAfterMove(boolean is_white_playing, Move move_in) {
        applyMoveToBoard(move_in);
        int eval = -evaluate(!is_white_playing);
        undoMoveToBoard(move_in);
        return eval;
    }

    public Move getBestMove(boolean is_white_playing, int depth) {
        return generateBestMove(is_white_playing, depth).getMove();
    }

    public MoveWithEval generateBestMove(boolean is_white_playing, int depth) {
        List<Space> friendly_spaces = getBoard().getFriendlySpaces(is_white_playing);

        List<Move> all_legal_moves = new ArrayList<>();
        for (Space space : friendly_spaces) {
            all_legal_moves.addAll(generateLegalMoves(space, is_white_playing));
        }

        int best_eval = -999;
        Move best_move = null;

        if (depth == 0) {
            for (Move legal_move : all_legal_moves) {
                int current_eval = evaluateAfterMove(is_white_playing, legal_move);
                if (current_eval > best_eval) {
                    best_move = legal_move;
                    best_eval = current_eval;
                }
            }
            return new MoveWithEval(best_move, best_eval);
        }

        for (Move legal_move : all_legal_moves) {
            applyMoveToBoard(legal_move);

            MoveWithEval wrapped_enemy_best_move = generateBestMove(!is_white_playing, depth - 1);

            if (-wrapped_enemy_best_move.getEvalValue() > best_eval) {
                best_move = legal_move;
                best_eval = -wrapped_enemy_best_move.getEvalValue();
            }

            undoMoveToBoard(legal_move);
        }
        return new MoveWithEval(best_move, best_eval);
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

    public boolean canMultiplePiecesMoveToSameSpace(
            Space chosen_space,
            Space destination_space) {
        /*
         * Check if no other pieces (of same type) can move to the same destination
         * space
         */

        // if pawns are not killing, then no other pawn piece can move to the same space
        if (getBoard().getPiece(chosen_space) instanceof Pawn && chosen_space.getX() == destination_space.getX()) {
            return false;
        }

        List<Space> friendly_spaces_with_same_piece_type = getBoard().getFriendlySpacesBySymbol(
                getBoard().getPiece(chosen_space).getSymbol());
        // if player only has 1 bishop, then no other friendly bishops can move to the
        // same square
        if (friendly_spaces_with_same_piece_type.size() <= 1) {
            return false;
        }

        for (Space space : friendly_spaces_with_same_piece_type) {
            // only check pieces excluding chosen piece
            if (!chosen_space.equals(space)) {
                Piece other_piece = this.chess_board.getPiece(space);
                List<Move> other_piece_possible_moves = other_piece.getPossibleMoves(
                        space, this.chess_board);
                for (Move other_move : other_piece_possible_moves) {
                    if (other_move.getNewLocation().equals(destination_space)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
