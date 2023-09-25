package main;

import main.boards.Board;
import main.boards.StandardChessBoard;
import main.control.Player;
import main.moves.Move;
import main.pieces.Piece;
import main.pieces.Pawn;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameState game_state;
    private Player player_1;
    private Player player_2;
    private boolean p1_turn;
    private Board chess_board;

    public Game(Player p1, Player p2) {
        this.player_1 = p1;
        this.player_2 = p2;
        this.p1_turn = p1.isWhite();
        this.chess_board = new StandardChessBoard();
        this.game_state = GameState.ACTIVE;
    }

    public void run_game() {
        while (!hasEnded()) {
            // game loop starts
            start_turn();
        }
    }

    private void start_turn() {
        // determine whose turn it is
        Player player;
        if (isP1Turn()) {
            System.out.println("Player 1's turn:");
            player = getPlayer(1);
        }
        else {
            System.out.println("Player 2's turn:");
            player = getPlayer(2);
        }

        // display the board onto the terminal/console
        chess_board.display();

        if (player.quickCheck()) {
            System.out.println("Your king is in check!");
        }
        // end game if player is checkmated
        if (isCheckMate(player, chess_board)) {
            System.out.println("You have been checkmated!");
            endGame(!player.isWhite());
            return;
        }
        if (isDraw(player, chess_board)) {
            System.out.println("Game has ended in a draw!");
            setState(GameState.DRAW);
            return;
        }

        // player generates move
        Move generated_move = player.startMove(chess_board);

        // disable en passant for all pawns after player made a move (except double pawn moves)
        List<Space> pawn_spaces = chess_board.getSpacesByPieceName("Pawn");
        for (Space pawn_space : pawn_spaces) {
            // disable en passant for selected pawn
            Pawn pawn_piece = (Pawn) pawn_space.getPiece();
            pawn_piece.setEnPassant(false);
            // update board with new pawn piece
            chess_board.updateSpace(pawn_space, pawn_piece);
        }

        // move is applied to board (piece is moved)
        generated_move.apply(chess_board);;

        // add points gained and saves move to history
        player.addPoints(generated_move.getKillPoints());
        player.recordMove(generated_move);

        // turn switches after the move was made
        switchTurn();
    }

    public boolean isP1Turn() {
        return this.p1_turn;
    }
    public void switchTurn() {
        this.p1_turn = !isP1Turn();
    }

    public Player getPlayer(int choice) {
        if (choice == 1) { return this.player_1; }
        else if (choice == 2) { return this.player_2; }
        else {
            throw new RuntimeException("Invalid input");
        }
    }
    public Board getBoard() {
        return this.chess_board;
    }

    public GameState getState() {
        return this.game_state;
    }
    public void setState(GameState new_state) {
        this.game_state = new_state;
    }

    // check if game has ended
    public boolean hasEnded() {
        return getState() != GameState.ACTIVE;
    }
    public void endGame(boolean white_won) {
        // set game state according to who lost the game
        if (white_won) {
            setState(GameState.WHITE_WIN);
        }
        setState(GameState.BLACK_WIN);
    }

    public boolean isCheckMate(Player player_in, Board chess_board) {
        if (!player_in.isCheck(chess_board)) {
            return false;
        }

        for (Space[] row : chess_board.getSpaces()) {
            for (Space space : row) {
                if (space.isFriendly(player_in.isWhite())) {
                    if (player_in.canSpacePreventCheck(space, chess_board)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public boolean isDraw(Player player_in, Board chess_board) {
        // stalemate = draw
        if (isStalemate(player_in, chess_board)) {
            return true;
        }

        List<Piece> all_pieces = new ArrayList<>();
        // get all pieces on the board and put them in a list
        for (Space[] row : chess_board.getSpaces()) {
            for (Space space : row) {
                if (!space.isEmpty()) {
                    all_pieces.add(space.getPiece());
                }
            }
        }
        // returns true if only 2 kings remain
        return all_pieces.size() == 2;
    }
    public boolean isStalemate(Player player_in, Board chess_board) {
        /* Check if player has any moves that don't lead to check */

        // can't be stalemate if player is in check
        if (player_in.isCheck(chess_board)) {
            return false;
        }

        for (Space[] row : chess_board.getSpaces()) {
            for (Space space : row) {
                if (space.isFriendly(player_in.isWhite())) {
                    // get all possible moves that the piece can make
                    List<Move> possible_moves = space.getPiece().getPossibleMoves(space, chess_board);

                    if (possible_moves.size() > 0) {
                        // check if move is possible (must not lead to check)
                        for (Move move : possible_moves) {
                            if (!player_in.doesMoveCauseCheck(move, chess_board)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Game has ended in stalemate!");
        return true;
    }
}
