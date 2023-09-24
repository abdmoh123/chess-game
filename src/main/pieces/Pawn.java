package main.pieces;

import main.Board;
import main.moves.DoublePawnMove;
import main.moves.EnPassantMove;
import main.moves.Move;
import main.Space;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean en_passant;

    public Pawn(boolean is_white_in, int id_in) {
        super(is_white_in, 1, id_in);
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

        Space horizontal_space, diagonal_space;
        // only check for en passant to left if space exists on the left
        if (location.getX() > 0) {
            horizontal_space = chess_board.getSpace(x_loc - 1, y_loc);
            diagonal_space = chess_board.getSpace(x_loc - 1, forward_y_loc);
            // check if an enemy piece exists in space
            if (horizontal_space.isEnemy(isWhite())) {
                // check if piece is a pawn
                if (horizontal_space.getPiece() instanceof Pawn) {
                    // check if en passant can be applied
                    if (((Pawn) horizontal_space.getPiece()).isEnPassant()) {
                        en_passant_moves.add(new EnPassantMove(location, diagonal_space, horizontal_space));
                    }
                }
            }
        }
        // only check for en passant to right if space exists on the right
        if (location.getX() < 7) {
            horizontal_space = chess_board.getSpace(x_loc + 1, y_loc);
            diagonal_space = chess_board.getSpace(x_loc + 1, forward_y_loc);
            // check if an enemy piece exists in space
            if (horizontal_space.isEnemy(isWhite())) {
                // check if piece is a pawn
                if (horizontal_space.getPiece() instanceof Pawn) {
                    // check if en passant can be applied
                    if (((Pawn) horizontal_space.getPiece()).isEnPassant()) {
                        en_passant_moves.add(new EnPassantMove(location, diagonal_space, horizontal_space));
                    }
                }
            }
        }

        return en_passant_moves;
    }

    public List<Space> getDiagonalSpaces(Space location, Board chess_board) {
        /* Diagonal killing */

        int x_loc = location.getX();
        int y_loc = location.getY();

        List<Space> diagonal_spaces = new ArrayList<>();

        // upwards if white
        if (isWhite()) {
            ++y_loc;
        }
        // downwards if black
        else {
            --y_loc;
        }

        Space diagonal_space;
        if (location.getX() > 0) {
            diagonal_space = chess_board.getSpace(x_loc - 1, y_loc);
            diagonal_spaces.add(diagonal_space);
        }
        if (location.getX() < 7) {
            diagonal_space = chess_board.getSpace(x_loc + 1, y_loc);
            diagonal_spaces.add(diagonal_space);
        }

        return diagonal_spaces;
    }

    public Move getForwardMove(Space location, Board chess_board) {
        int x_loc = location.getX();
        int y_loc = location.getY();

        // moves forward if white
        if (isWhite()) { ++y_loc; }
        // moves backwards if black
        else { --y_loc; }

        // check if move is possible/legal before adding it to list
        if (!Move.is_legal(location, chess_board.getSpace(x_loc, y_loc))) {
            return null;
        }

        // only moves forward if space is empty (cannot kill enemy piece)
        Space new_space = chess_board.getSpace(x_loc, y_loc);
        if (new_space.isEmpty()) {
            return new Move(location, chess_board.getSpace(x_loc, y_loc));
        }
        return null;
    }

    public DoublePawnMove getDoubleMove(Space location, Board chess_board) {
        int x_loc = location.getX();
        int y_loc = location.getY();

        if (isWhite()) {
            // if pawn is white, starting position should be on row 2 (1/7)
            if (location.getY() == 1) {
                Space new_space = chess_board.getSpace(x_loc, y_loc + 2);
                // only allows move if space is empty (cannot kill enemy pieces)
                if (new_space.isEmpty()) {
                    return new DoublePawnMove(location, new_space);
                }
            }
        }
        else {
            // if pawn is black, starting position should be on row 7 (6/7)
            if (location.getY() == 6) {
                Space new_space = chess_board.getSpace(x_loc, y_loc - 2);
                // only allows move if space is empty (cannot kill enemy pieces)
                if (new_space.isEmpty()) {
                    return new DoublePawnMove(location, new_space);
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
            if (Move.is_legal(location, diagonal_space)) {
                addVisibleSpace(diagonal_space);
            }
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        /* Basic forward moves */
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

        /* Diagonal killing */
        computeVision(location, chess_board);

        // convert all visible spaces into potential moves
        for (Space visible_space : getVisibleSpaces()) {
            if (!visible_space.isEmpty()) {
                possible_moves.add(new Move(location, visible_space));
            }
        }

        return possible_moves;
    }

    @Override
    public String getName() {
        return "Pawn";
    }

    public boolean isEnPassant() {
        return en_passant;
    }
    public void setEnPassant(boolean choice) {
        en_passant = choice;
    }
}
