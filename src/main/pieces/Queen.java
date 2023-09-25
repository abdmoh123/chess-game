package main.pieces;

import main.Board;
import main.moves.Move;
import main.Space;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(boolean is_white_in) {
        super(is_white_in, 9);
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        // queen is basically a combination of rook and bishop
        Piece temp_rook = new Rook(isWhite());
        Piece temp_bishop = new Bishop(isWhite());

        temp_rook.computeVision(location, chess_board);
        temp_bishop.computeVision(location, chess_board);

        setVisibleSpaces(temp_rook.getVisibleSpaces());
        addVisibleSpaces(temp_bishop.getVisibleSpaces());
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        computeVision(location, chess_board);

        for (Space visible_space : getVisibleSpaces()) {
            possible_moves.add(new Move(location, visible_space));
        }

        return possible_moves;
    }

    @Override
    public String getName() {
        return "Queen";
    }
}
