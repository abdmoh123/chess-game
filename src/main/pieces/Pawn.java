package main.pieces;

import main.moves.DoublePawnMove;
import main.moves.EnPassantMove;
import main.moves.Move;
import main.moves.PromotePawnMove;
import main.moves.StandardMove;
import main.Space;
import main.boards.Board;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean en_passant;

    public Pawn(boolean is_white_in) {
        super(is_white_in, 1);
        this.en_passant = false;
    }

    public List<Move> getEnPassantMoves(Space location, Board chess_board) {
        int x_loc = location.getX();
        int y_loc, forward_y_loc;
        y_loc = forward_y_loc = location.getY();

        List<Move> en_passant_moves = new ArrayList<>();

        // upwards if white
        if (isWhite()) {
            ++forward_y_loc;
        }
        // downwards if black
        else {
            --forward_y_loc;
        }
        // ensure movement above stays within the board
        if (!chess_board.isSpaceWithinBoard(new Space(x_loc, forward_y_loc))) {
            return en_passant_moves;
        }

        Space horizontal_space, diagonal_space;
        // only check for en passant to left if space exists on the left
        if (x_loc > 0) {
            horizontal_space = new Space(x_loc - 1, y_loc);
            diagonal_space = new Space(x_loc - 1, forward_y_loc);
            // check if an enemy piece exists in space
            if (!chess_board.isSpaceFriendly(horizontal_space, isWhite())) {
                // check if piece is a pawn
                if (chess_board.getPiece(horizontal_space) instanceof Pawn) {
                    Pawn adjacent_piece = (Pawn) chess_board.getPiece(horizontal_space);
                    // check if en passant can be applied
                    if (adjacent_piece.isEnPassant()) {
                        en_passant_moves.add(new EnPassantMove(location, diagonal_space, horizontal_space, this, adjacent_piece));
                    }
                }
            }
        }
        // only check for en passant to right if space exists on the right
        if (x_loc < chess_board.getLength() - 1) {
            horizontal_space = new Space(x_loc + 1, y_loc);
            diagonal_space = new Space(x_loc + 1, forward_y_loc);
            // check if an enemy piece exists in space
            if (!chess_board.isSpaceFriendly(horizontal_space, isWhite())) {
                // check if piece is a pawn
                if (chess_board.getPiece(horizontal_space) instanceof Pawn) {
                    Pawn adjacent_piece = (Pawn) chess_board.getPiece(horizontal_space);
                    // check if en passant can be applied
                    if (adjacent_piece.isEnPassant()) {
                        en_passant_moves.add(new EnPassantMove(location, diagonal_space, horizontal_space, this, adjacent_piece));
                    }
                }
            }
        }

        return en_passant_moves;
    }

    public List<Space> getDiagonalSpaces(Space location, Board chess_board) {
        /* Diagonal killing */

        int x_loc = location.getX();
        int new_y_loc = location.getY();

        List<Space> diagonal_spaces = new ArrayList<>();

        // move upwards if white
        if (isWhite()) {
            ++new_y_loc;
        }
        // move downwards if black
        else {
            --new_y_loc;
        }
        // ensure movement above stays within the board
        if (!chess_board.isSpaceWithinBoard(new Space(x_loc, new_y_loc))) {
            return diagonal_spaces;
        }

        Space diagonal_space;
        if (x_loc > 0) {
            diagonal_space = new Space(x_loc - 1, new_y_loc);
            diagonal_spaces.add(diagonal_space);
        }
        if (x_loc < 7) {
            diagonal_space = new Space(x_loc + 1, new_y_loc);
            diagonal_spaces.add(diagonal_space);
        }

        return diagonal_spaces;
    }

    public Move getForwardMove(Space location, Board chess_board) {
        int x_loc = location.getX();
        int new_y_loc = location.getY();

        // move upwards if white
        if (isWhite()) {
            ++new_y_loc;
        }
        // move downwards if black
        else {
            --new_y_loc;
        }
        Space new_space = new Space(x_loc, new_y_loc);
        // ensure movement above stays within the board
        if (!chess_board.isSpaceWithinBoard(new_space)) {
            return null;
        }

        // only move forward if space is empty (cannot kill enemy piece)
        if (chess_board.isSpaceEmpty(new_space)) {
            // if pawn reaches horizontal edge of board, it can promote
            if (new_space.getY() == chess_board.getLength() - 1 || new_space.getY() == 0) {
                return new PromotePawnMove(location, new_space, this);
            }
            return new StandardMove(location, new_space, this, chess_board.getPiece(new_space));
        }
        return null;
    }

    public DoublePawnMove getDoubleMove(Space location, Board chess_board) {
        int x_loc = location.getX();
        int y_loc = location.getY();

        if (isWhite()) {
            // if pawn is white, starting position should be on row 2 (1/7)
            if (location.getY() == 1) {
                Space new_space = new Space(x_loc, y_loc + 2);
                // only allows move if space is empty (cannot kill enemy pieces)
                if (chess_board.isSpaceEmpty(new_space)) {
                    return new DoublePawnMove(location, new_space, this);
                }
            }
        }
        else {
            // if pawn is black, starting position should be on row 7 (6/7)
            if (location.getY() == 6) {
                Space new_space = new Space(x_loc, y_loc - 2);
                // only allows move if space is empty (cannot kill enemy pieces)
                if (chess_board.isSpaceEmpty(new_space)) {
                    return new DoublePawnMove(location, new_space, this);
                }
            }
        }
        return null;
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        resetVision();

        // check if pawn is not at leftmost side (can't have a left space if at edge)
        List<Space> diagonal_spaces = getDiagonalSpaces(location, chess_board);

        for (Space diagonal_space : diagonal_spaces) {
            if (Move.is_legal(chess_board, location, diagonal_space)) {
                addVisibleSpace(diagonal_space);
            }
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        /* Basic forward moves (+ promotion) */
        Move forward_move = getForwardMove(location, chess_board);
        if (forward_move != null) {
            possible_moves.add(forward_move);
        }

        /* Allow pawn to move 2 squares if at starting position */
        DoublePawnMove double_pawn_move = getDoubleMove(location, chess_board);
        if (double_pawn_move != null) {
            possible_moves.add(double_pawn_move);
        }

        /* Allow pawn to take using en passant if possible */
        possible_moves.addAll(getEnPassantMoves(location, chess_board));

        /* Diagonal killing (+ promotion) */
        computeVision(location, chess_board);

        // convert all visible spaces into potential moves
        for (Space visible_space : getVisibleSpaces()) {
            if (!chess_board.isSpaceEmpty(visible_space)) {
                // allow diagonal killing move to be pawn promotion
                if (visible_space.getY() == chess_board.getLength() - 1 || visible_space.getY() == 0) {
                    possible_moves.add(new PromotePawnMove(location, visible_space, this));
                }
                else {
                    possible_moves.add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
                }
            }
        }

        return possible_moves;
    }

    @Override
    public String getName() {
        return "Pawn";
    }

    @Override
    public Piece clone() {
        Pawn new_pawn = new Pawn(isWhite());
        new_pawn.setEnPassant(isEnPassant());
        new_pawn.setVisibleSpaces(getVisibleSpaces());
        return new_pawn;
    }

    public boolean isEnPassant() {
        return en_passant;
    }
    public void setEnPassant(boolean choice) {
        en_passant = choice;
    }
}
