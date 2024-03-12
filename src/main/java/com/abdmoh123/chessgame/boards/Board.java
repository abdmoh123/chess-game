package com.abdmoh123.chessgame.boards;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.exceptions.CsvValidationException;

import com.abdmoh123.chessgame.moves.*;
import com.abdmoh123.chessgame.pieces.*;
import com.abdmoh123.chessgame.utils.ChessCSVReader;

public abstract class Board {
    protected Piece[][] spaces;
    private final int LENGTH;

    public Board(int length_in) {
        this.spaces = new Piece[length_in][length_in]; // length_in x length_in grid
        this.LENGTH = length_in;
    }

    public Piece[][] getAllSpaces() {
        return spaces;
    }
    public List<Space> getAllSpacesWithPieces() {
        List<Space> spaces_with_pieces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (!isSpaceEmpty(space)) {
                    spaces_with_pieces.add(space);
                }
            }
        }
        return spaces_with_pieces;
    }
    public List<Space> getFriendlySpaces(boolean is_white) {
        List<Space> friendly_spaces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (isSpaceFriendly(space, is_white)) {
                    friendly_spaces.add(space);
                }
            }
        }
        return friendly_spaces;
    }
    public List<Space> getEnemySpaces(boolean is_white) {
        List<Space> enemy_spaces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (isSpaceEnemy(space, is_white)) {
                    enemy_spaces.add(space);
                }
            }
        }
        return enemy_spaces;
    }
    public int getLength() {
        return LENGTH;
    }

    public Piece getPiece(Space space_in) {
        /* Return piece by reference (changes made to a piece affect the piece on the board) */

        if (!isSpaceValid(space_in)) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinate! (" + space_in.getX() + ", " + space_in.getY() + ")");
        }
        return spaces[space_in.getY()][space_in.getX()];
    }
    public Piece[] getRow(int y) {
        return this.spaces[y];
    }
    public Piece[] getColumn(int x) {
        Piece[] column = new Piece[8];

        for (int i = 0; i < getLength(); ++i) {
            Space space = new Space(x, i);
            column[i] = getPiece(space);
        }
        return column;
    }
    public List<Space> getCheckedSpaces(boolean is_white_turn) {
        /* Find all spaces attacked by the enemy */

        List<Space> checked_spaces = new ArrayList<>();
        List<Space> enemy_spaces = getEnemySpaces(is_white_turn);

        for (Space space : enemy_spaces) {
            // update vision of each enemy piece
            getPiece(space).computeVision(space, this);
            List<Space> visible_spaces = getPiece(space).getVisibleSpaces();

            for (Space visible_space : visible_spaces) {
                // prevent duplicate spaces
                if (!checked_spaces.contains(visible_space)) {
                    checked_spaces.add(visible_space);
                }
            }
        }

        return checked_spaces;
    }
    public List<Space> getAllSpacesBySymbol(char piece_symbol_in) {
        /* Search through board and return all spaces that hold a given piece type */

        List<Space> selected_pieces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (!isSpaceEmpty(space)) {
                    // force both symbols to be same case (ignore team)
                    if (Character.toUpperCase(getPiece(space).getSymbol()) == Character.toUpperCase(piece_symbol_in)) {
                        selected_pieces.add(space);
                    }
                }
            }
        }

        return selected_pieces;
    }
    public List<Space> getFriendlySpacesBySymbol(char piece_symbol_in) {
        /* Search through board and return all friendly spaces that hold a given piece type */

        List<Space> selected_pieces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (!isSpaceEmpty(space)) {
                    if (getPiece(space).getSymbol() == piece_symbol_in) {
                        selected_pieces.add(space);
                    }
                }
            }
        }

        return selected_pieces;
    }

    public boolean isSpaceEmpty(Space space_in) {
        if (getPiece(space_in) == null) {
            return true;
        }
        return false;
    }
    public boolean isSpaceWithinBoard(Space space_in) {
        if (space_in.getX() > getLength() - 1 || space_in.getX() < 0) {
            return false;
        }
        if (space_in.getY() > getLength() - 1 || space_in.getY() < 0) {
            return false;
        }
        return true;
    }
    public boolean isSpaceValid(Space space_in) {
        if (space_in == null) {
            return false;
        }
        if (!isSpaceWithinBoard(space_in)) {
            return false;
        }
        return true;
    }
    public boolean isSpaceFriendly(Space space_in, boolean player_is_white) {
        if (!isSpaceValid(space_in)) {
            return false;
        }
        if (isSpaceEmpty(space_in)) {
            return false;
        }
        return getPiece(space_in).isWhite() == player_is_white;
    }
    public boolean isSpaceEnemy(Space space_in, boolean player_is_white) {
        if (!isSpaceValid(space_in)) {
            return false;
        }
        if (isSpaceEmpty(space_in)) {
            return false;
        }
        return getPiece(space_in).isWhite() != player_is_white;
    }
    public boolean isPieceUniqueOnRow(Space space_in) {
        Piece[] row = getRow(space_in.getY());
        
        int count = 0;
        for (Piece piece : row) {
            if (count > 1) {
                break;
            }
            if (piece != null) {
                if (piece.getSymbol() == getPiece(space_in).getSymbol()) {
                    ++count;
                }
            }
        }
        return count == 1;
    }

    public void updateSpace(Space space_in, Piece piece_in) {
        if (isSpaceValid(space_in)) {
            spaces[space_in.getY()][space_in.getX()] = piece_in;
        }
    }

    public void display() {
        /* Display board layout on terminal */

        for (int j = getLength() - 1; j >= 0; --j) {
            // print top border for each row
            for (int i = 0; i < getLength(); ++i) {
                System.out.print("+---");
            }
            System.out.println("+");

            // print each space with its piece (if not empty)
            for (int i = 0; i < getLength(); ++i) {
                Space space = new Space(i, j);

                char piece_symbol = ' ';
                if (!isSpaceEmpty(space)) { piece_symbol = getPiece(space).getSymbol(); }
                System.out.printf("| %s ", piece_symbol);
            }
            System.out.print("|\n");
        }

        // print bottom border of the board
        for (int i = 0; i < getLength(); ++i) {
            System.out.print("+---");
        }
        System.out.println("+");
    }

    public Board after(Move move_in) {
        /* Create a temporary board with the move applied. Useful for handling checks and pinned pieces. */

        Board new_board = copy();
        move_in.apply(new_board);
        return new_board;
    }

    /* Fill the board with pieces */
    public abstract void initialise();
    /* Return a Space object based on inputted string (e.g. a1 = Space(0, 0)) */
    public abstract Space getSpaceByString(String input_string);
    /* Create a deep copy/clone of a given board (any changes won't affect the original) */
    public abstract Board copy();

    public void initialise(Piece[][] spaces_in) {
        /* Fill board with a given layout of pieces */

        // ensure given array matches axes length of board
        if (spaces_in.length != getLength()) {
            String message = "Given array is incorrect size! Expected: {0}, Actual: {1}" ;
            throw new ExceptionInInitializerError(
                MessageFormat.format(message, new Object[] {getLength(), spaces_in.length})
            );
        }
        for (Piece[] row : spaces_in) {
            if (row.length != getLength()) {
                String message = "Given array is incorrect size! Expected: {0}, Actual: {1}" ;
                throw new ExceptionInInitializerError(
                    MessageFormat.format(message, new Object[] {getLength(), row.length})
                );
            }
        }

        this.spaces = spaces_in;
    }
    public void initialise(String file_name_in) throws CsvValidationException, URISyntaxException, IOException {
        /* Fill board with pieces based on a CSV file */

        Piece[][] spaces_in = ChessCSVReader.readBoardCSV(file_name_in);
        
        initialise(spaces_in);
    }
    public void initialiseFEN(String fen_string_in)  {
        /* Fill board with pieces based FEN input
         * Standard starting position: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0"
         */
        
        String[] split_fen = fen_string_in.split(" ");

        // full fen format has 6 fields, but a simpler one can only have 1 (for piece layout)
        if (split_fen.length != 1 && split_fen.length != 6) {
            throw new RuntimeException("Invalid FEN Format: Incorrect number of fields!");
        }

        /* Place pieces in board */
        // validate first field
        String field_one_regex = "[1-9KkQqBbNnRrPp/]";
        if (!isFieldValid(field_one_regex, split_fen[0])) {
            throw new RuntimeException("Invalid FEN Format: First field format error!");
        }

        int x_index = 0;
        int y_index = 7; // start at 8th rank and move down
        for (char letter : split_fen[0].toCharArray()) {
            if (Character.isDigit(letter)) {
                x_index += letter - '0'; // convert char to int
                // x index cannot go over length of the board
                if (x_index > getLength()) {
                    throw new RuntimeException("Invalid FEN Format: Number of empty spaces greater than length of board!");
                }
            }
            else if (letter == '/') {
                if (x_index < getLength()) {
                    throw new RuntimeException("Invalid FEN Format: Not enough empty spaces specified!");
                }
                x_index = 0;
                --y_index;
            }
            else {
                updateSpace(new Space(x_index, y_index), PieceFactory.createPiece(letter));
                ++x_index;
            }
        }

        // end method if only board layout is given (first field only)
        if (split_fen.length == 1) { return; }

        /* Setup castling rights based on FEN input */
        // validate third field
        String field_three_regex = "[-KQkq]";
        if (!isFieldValid(field_three_regex, split_fen[2])) {
            throw new RuntimeException("Invalid FEN Format: Third field format error!");
        }

        Space white_king_space = getFriendlySpacesBySymbol('K').get(0);
        Space black_king_space = getFriendlySpacesBySymbol('k').get(0);
        boolean found_K, found_Q, found_k, found_q;
        found_K = found_Q = found_k = found_q = false;
        for (char letter : split_fen[2].toCharArray()) {
            // ensure king cannot castle
            if (letter == '-') {
                King new_king = (King) getPiece(white_king_space).copy();
                new_king.disableCastling();
                updateSpace(white_king_space, new_king);

                new_king = (King) getPiece(black_king_space).copy();
                new_king.disableCastling();
                updateSpace(black_king_space, new_king);
                break;
            }

            if (letter == 'K') {
                if (!(getPiece(new Space(0, 0)) instanceof Rook) || !white_king_space.equals(new Space(4, 0))) {
                    throw new RuntimeException("Invalid castling rights!");
                }
                found_K = true;
            }
            else if (letter == 'Q') {
                if (!(getPiece(new Space(7, 0)) instanceof Rook) || !white_king_space.equals(new Space(4, 0))) {
                    throw new RuntimeException("Invalid castling rights!");
                }
                found_Q = true;
            }
            else if (letter == 'k') {
                if (!(getPiece(new Space(0, 7)) instanceof Rook) || !black_king_space.equals(new Space(4, 7))) {
                    throw new RuntimeException("Invalid castling rights!");
                }
                found_k = true;
            }
            else if (letter == 'q') {
                if (!(getPiece(new Space(7, 7)) instanceof Rook) || !black_king_space.equals(new Space(4, 7))) {
                    throw new RuntimeException("Invalid castling rights!");
                }
                found_q = true;
            }
        }
        // only activate relevant rooks to disable relevant castling right
        if (!found_K) {
            Rook new_rook = new Rook(true);
            new_rook.activate();
            updateSpace(new Space(0, 0), new_rook);
        }
        if (!found_Q) {
            Rook new_rook = new Rook(true);
            new_rook.activate();
            updateSpace(new Space(7, 0), new_rook);
        }
        if (!found_k) {
            Rook new_rook = new Rook(false);
            new_rook.activate();
            updateSpace(new Space(0, 7), new_rook);
        }
        if (!found_q) {
            Rook new_rook = new Rook(false);
            new_rook.activate();
            updateSpace(new Space(7, 7), new_rook);
        }

        // skip final step if no pawns are en-passant-able
        if (split_fen[3].equals("-")) { return; }

        /* Setup en passant targets */
        // validate fourth field
        String field_four_regex = "[a-hA-H][1-8]";
        if (!isFieldValid(field_four_regex, split_fen[3]) && split_fen[3].length() != 2) {
            throw new RuntimeException("Invalid FEN Format: Fourth field format error!");
        }

        // determine which player's turn it is (+ second field validation)
        boolean is_white_turn = false;
        if (split_fen[1].equalsIgnoreCase("w")) {
            is_white_turn = true;
        }
        else if (!split_fen[1].equalsIgnoreCase("b")) {
            throw new RuntimeException("Invalid FEN Format: Second field format error!");
        }

        // convert en passant string to space
        Space en_passant_space = new Space(split_fen[3]);
        // get location of pawn
        int en_passant_offset;
        // if white just moved, it would be black's turn
        if (!is_white_turn) { en_passant_offset = 1; }
        else { en_passant_offset = -1; }
        Space pawn_space = new Space(en_passant_space.getX(), en_passant_space.getY() + en_passant_offset);

        // check if pawn exists in correct location
        if (isSpaceEmpty(pawn_space) || !(getPiece(pawn_space) instanceof Pawn)) {
            throw new RuntimeException("En passant info does not match with board layout!");
        }

        Pawn pawn = (Pawn) getPiece(pawn_space);
        pawn.setEnPassant(true);
        updateSpace(pawn_space, pawn);
    }
    private boolean isFieldValid(String regex, String field) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(field);
        if (!matcher.find()) { return false; }
        return true;
    }
}
