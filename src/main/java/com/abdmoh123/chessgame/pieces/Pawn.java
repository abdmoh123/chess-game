package com.abdmoh123.chessgame.pieces;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.DoublePawnMove;
import com.abdmoh123.chessgame.moves.EnPassantMove;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.moves.PromotePawnMove;
import com.abdmoh123.chessgame.moves.StandardMove;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean en_passant;

    public Pawn(boolean is_white_in) {
        super(is_white_in, 1);
        this.en_passant = false;
    }

    public int getStartingRow() {
        if (isWhite()) {
            return 1;
        }
        return 6;
    }

    public boolean isStartingPosition(int current_row) {
        return (current_row == getStartingRow());
    }

    private int getForwardOffset() {
        int forward_offset;
        if (isWhite()) {
            forward_offset = 1;
        } else {
            forward_offset = -1;
        }
        return forward_offset;
    }

    private int getDoubleForwardOffset() {
        int double_forward_offset;
        if (isWhite()) {
            double_forward_offset = 2;
        } else {
            double_forward_offset = -2;
        }
        return double_forward_offset;
    }

    public boolean isEnPassant() {
        return this.en_passant;
    }

    public void setEnPassant(boolean is_en_passant_in) {
        this.en_passant = is_en_passant_in;
    }

    public List<Move> getEnPassantMoves(Space location, Board chess_board) {
        /* Search for possible en passant moves. Return empty list if none are found */

        int x_loc = location.getX();
        int y_loc = location.getY();

        int forward_offset = getForwardOffset();

        List<Move> en_passant_moves = new ArrayList<>();

        if (!chess_board.isSpaceWithinBoard(new Space(x_loc, y_loc + forward_offset))) {
            return en_passant_moves;
        }

        Space horizontal_space, diagonal_space;
        // only check for en passant to left if space exists on the left
        if (x_loc > 0) {
            horizontal_space = new Space(x_loc - 1, y_loc);
            diagonal_space = new Space(x_loc - 1, y_loc + forward_offset);

            if (chess_board.isSpaceEnemy(horizontal_space, isWhite())) {
                if (chess_board.getPiece(horizontal_space) instanceof Pawn) {
                    Pawn adjacent_piece = (Pawn) chess_board.getPiece(horizontal_space);
                    if (adjacent_piece.isEnPassant()) {
                        en_passant_moves.add(
                                new EnPassantMove(location, diagonal_space, horizontal_space, this, adjacent_piece));
                    }
                }
            }
        }
        // only check for en passant to right if space exists on the right
        if (x_loc < chess_board.getLength() - 1) {
            horizontal_space = new Space(x_loc + 1, y_loc);
            diagonal_space = new Space(x_loc + 1, y_loc + forward_offset);

            if (chess_board.isSpaceEnemy(horizontal_space, isWhite())) {
                if (chess_board.getPiece(horizontal_space) instanceof Pawn) {
                    Pawn adjacent_piece = (Pawn) chess_board.getPiece(horizontal_space);
                    if (adjacent_piece.isEnPassant()) {
                        en_passant_moves.add(
                                new EnPassantMove(location, diagonal_space, horizontal_space, this, adjacent_piece));
                    }
                }
            }
        }

        return en_passant_moves;
    }

    public List<Space> getDiagonalSpaces(Space location, Board chess_board) {
        /* Search for killing moves (diagonally). Return empty list if none are found */

        int x_loc = location.getX();
        int y_loc = location.getY();

        int forward_offset = getForwardOffset();

        List<Space> diagonal_spaces = new ArrayList<>();

        if (!chess_board.isSpaceWithinBoard(new Space(x_loc, y_loc + forward_offset))) {
            return diagonal_spaces;
        }

        Space diagonal_space;
        // only check for attack to left if space exists on the left
        if (x_loc > 0) {
            diagonal_space = new Space(x_loc - 1, y_loc + forward_offset);
            diagonal_spaces.add(diagonal_space);
        }
        // only check for attack to right if space exists on the right
        if (x_loc < 7) {
            diagonal_space = new Space(x_loc + 1, y_loc + forward_offset);
            diagonal_spaces.add(diagonal_space);
        }

        return diagonal_spaces;
    }

    public Move[] getForwardMove(Space location, Board chess_board) {
        int x_loc = location.getX();
        int y_loc = location.getY();

        int forward_offset = getForwardOffset();
        Space new_space = new Space(x_loc, y_loc + forward_offset);

        // cannot move forward if out of bounds or if space is not empty (cannot kill
        // enemy piece)
        if (!chess_board.isSpaceWithinBoard(new_space) || !chess_board.isSpaceEmpty(new_space)) {
            return null;
        }

        // if pawn reaches top of board (only white), it can promote
        if (new_space.getY() == chess_board.getLength() - 1) {
            // 4 white pieces to promote to
            return new Move[] {
                    new PromotePawnMove(location, new_space, this, 'R'),
                    new PromotePawnMove(location, new_space, this, 'N'),
                    new PromotePawnMove(location, new_space, this, 'B'),
                    new PromotePawnMove(location, new_space, this, 'Q')
            };
        }
        // if pawn reaches bottom of board (only black), it can promote
        if (new_space.getY() == 0) {
            // 4 black pieces to promote to
            return new Move[] {
                    new PromotePawnMove(location, new_space, this, 'r'),
                    new PromotePawnMove(location, new_space, this, 'n'),
                    new PromotePawnMove(location, new_space, this, 'b'),
                    new PromotePawnMove(location, new_space, this, 'q')
            };
        }
        return new Move[] { new StandardMove(location, new_space, this, chess_board.getPiece(new_space)) };
    }

    public DoublePawnMove getDoubleMove(Space location, Board chess_board) {
        int x_loc = location.getX();
        int y_loc = location.getY();

        // cannot make double pawn move if pawn is within this bound
        if (y_loc > 1 && y_loc < 6) {
            return null;
        }

        int forward_offset = getForwardOffset();
        int double_forward_offset = getDoubleForwardOffset();

        // starting position for white pawn should be on row 2 (1/7)
        if (isStartingPosition(y_loc)) {
            Space new_space = new Space(x_loc, y_loc + double_forward_offset);
            Space space_in_front = new Space(x_loc, y_loc + forward_offset);
            // cannot move forward if space is not empty (cannot kill enemy pieces), also
            // cannot move if blocked by other piece
            if (chess_board.isSpaceEmpty(new_space) && chess_board.isSpaceEmpty(space_in_front)) {
                return new DoublePawnMove(location, new_space, this);
            }
        }
        return null;
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        /* Compute and find all spaces that are attackable by the Pawn */

        resetVision();

        List<Space> diagonal_spaces = getDiagonalSpaces(location, chess_board);

        for (Space diagonal_space : diagonal_spaces) {
            if (Move.isValid(chess_board, location, diagonal_space)) {
                addVisibleSpace(diagonal_space);
            }
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        /* Basic forward moves (+ promotion) */
        Move[] forward_moves = getForwardMove(location, chess_board);
        if (forward_moves != null) {
            for (Move move : forward_moves) {
                possible_moves.add(move);
            }
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

        for (Space visible_space : getVisibleSpaces()) {
            if (!chess_board.isSpaceEmpty(visible_space)) {
                Piece killed_piece = chess_board.getPiece(visible_space);

                // allow diagonal killing move to be a pawn promotion move
                if (visible_space.getY() == chess_board.getLength() - 1) {
                    // 4 white pieces to promote to
                    Move[] promotion_moves = {
                            new PromotePawnMove(location, visible_space, this, 'R', killed_piece),
                            new PromotePawnMove(location, visible_space, this, 'N', killed_piece),
                            new PromotePawnMove(location, visible_space, this, 'B', killed_piece),
                            new PromotePawnMove(location, visible_space, this, 'Q', killed_piece)
                    };
                    for (Move move : promotion_moves) {
                        possible_moves.add(move);
                    }
                }
                if (visible_space.getY() == 0) {
                    // 4 black pieces to promote to
                    Move[] promotion_moves = {
                            new PromotePawnMove(location, visible_space, this, 'r', killed_piece),
                            new PromotePawnMove(location, visible_space, this, 'n', killed_piece),
                            new PromotePawnMove(location, visible_space, this, 'b', killed_piece),
                            new PromotePawnMove(location, visible_space, this, 'q', killed_piece)
                    };
                    for (Move move : promotion_moves) {
                        possible_moves.add(move);
                    }
                } else {
                    possible_moves
                            .add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
                }
            }
        }

        return possible_moves;
    }

    @Override
    public boolean canMove(Space current_location_in, Space new_location_in, Board chess_board) {
        /* Overriden due to complex pawn behaviour */

        int forward_offset = getForwardOffset();
        int double_forward_offset = getDoubleForwardOffset();

        // basic forward move
        if (new_location_in.getY() == current_location_in.getY() + forward_offset &&
                new_location_in.getX() == current_location_in.getX()) {
            return chess_board.isSpaceEmpty(new_location_in);
        }

        // double forward move
        int starting_row = getStartingRow();
        if (new_location_in.getY() == current_location_in.getY() + double_forward_offset &&
                new_location_in.getX() == current_location_in.getX()) {
            return current_location_in.getY() == starting_row && chess_board.isSpaceEmpty(new_location_in);
        }

        // diagonal moves
        List<Space> diagonal_spaces = getDiagonalSpaces(current_location_in, chess_board);
        for (Space diagonal_space : diagonal_spaces) {
            if (diagonal_space.equals(new_location_in) && chess_board.isSpaceEnemy(diagonal_space, isWhite())) {
                return true;
            }
        }

        // en passant moves
        List<Move> en_passant_moves = getEnPassantMoves(current_location_in, chess_board);
        for (Move move : en_passant_moves) {
            if (move.getNewLocation().equals(new_location_in)) {
                return true;
            }
        }

        return false;
    }

    public char getSymbol() {
        if (isWhite()) {
            return 'P';
        }
        return 'p';
    }

    @Override
    public Piece copy() {
        Pawn new_pawn = new Pawn(isWhite());
        new_pawn.setEnPassant(isEnPassant());
        new_pawn.setVisibleSpaces(getVisibleSpaces());
        return new_pawn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pawn) {
            if (isEnPassant() != ((Pawn) obj).isEnPassant()) {
                return false;
            }
        }
        return super.equals(obj);
    }
}
