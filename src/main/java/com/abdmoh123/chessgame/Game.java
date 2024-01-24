package com.abdmoh123.chessgame;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.pieces.Pawn;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameState game_state;
    private Player[] players;
    private boolean p1_turn;
    private Board chess_board;
    private List<Move> move_history;

    public Game(Player[] players_in, Board chess_board_in) {
        this.players = players_in;
        this.p1_turn = getPlayer(1).isWhite();
        this.move_history = new ArrayList<>();
        
        this.chess_board = chess_board_in;
        this.game_state = GameState.ACTIVE;
    }

    public void runGame() {
        while (!hasEnded()) {
            start_turn();
        }

        switch (game_state) {
            case WHITE_WIN:
                System.out.println("White has won the game! Black has been Checkmated!");
                break;
            case BLACK_WIN:
                System.out.println("Black has won the game! White has been Checkmated!");
                break;
            case DRAW:
                System.out.println("Game is a draw!");
                break;
            default:
                throw new RuntimeException("Invalid game state!");
        }
    }

    private void start_turn() {
        Player player;
        if (isP1Turn()) {
            System.out.println("Player 1's turn:");
            player = getPlayer(1);
        }
        else {
            System.out.println("Player 2's turn:");
            player = getPlayer(2);
        }

        getBoard().display();

        if (isCheckMate(player, getBoard())) {
            endGame(player.isWhite());
            return;
        }
        if (isDraw(player, getBoard())) {
            setState(GameState.DRAW);
            return;
        }
        if (player.isCheck(getBoard())) {
            System.out.println("Your king is in check!");
        }

        Move generated_move = player.startMove(getBoard());

        // disable en passant for all pawns after player made a move
        List<Space> pawn_spaces = getBoard().getAllSpacesByPieceName("Pawn");
        for (Space pawn_space : pawn_spaces) {
            Pawn pawn_piece = (Pawn) getBoard().getPiece(pawn_space);
            pawn_piece.setEnPassant(false);
            getBoard().updateSpace(pawn_space, pawn_piece);
        }

        generated_move.apply(getBoard());

        player.addPoints(generated_move.getKillPoints());
        recordMove(generated_move);

        switchTurn();
    }

    public boolean isP1Turn() {
        return this.p1_turn;
    }
    public void switchTurn() {
        this.p1_turn = !isP1Turn();
    }

    public List<Move> getMoveHistory() {
        return this.move_history;
    }
    public void recordMove(Move move_in) {
        this.move_history.add(move_in);
    }

    public Player getPlayer(int choice) {
        if (choice > this.players.length) {
            throw new RuntimeException("Invalid input");
        }
        return this.players[choice - 1]; // getPlayer(1) = player[0]
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

    public boolean hasEnded() {
        return getState() != GameState.ACTIVE;
    }
    public void endGame(boolean has_white_lost) {
        if (has_white_lost) {
            setState(GameState.BLACK_WIN);
        }
        setState(GameState.WHITE_WIN);
    }

    public boolean isCheckMate(Player player_in, Board chess_board) {
        if (!player_in.isCheck(chess_board)) {
            return false;
        }

        List<Space> friendly_spaces = chess_board.getFriendlySpaces(player_in.isWhite());
        for (Space friendly_space : friendly_spaces) {
            if (player_in.canPieceMove(friendly_space, chess_board)) {
                return false;
            }
        }
        return true;
    }
    public boolean isDraw(Player player_in, Board chess_board) {
        // TODO: Add check for 3 fold repetition
        // TODO: Add check for 50 move rule (draw if 50 quiet moves happen consecutively)
        // TODO: Add check for dead position (impossible to checkmate)

        if (isStalemate(player_in, chess_board)) {
            return true;
        }

        List<Space> all_spaces_with_pieces = chess_board.getAllSpacesWithPieces();
        return all_spaces_with_pieces.size() == 2;  // returns true if only 2 kings remain.java
    }
    public boolean isStalemate(Player player_in, Board chess_board) {
        /* Check if player has any moves that don't lead getting checked */

        // can't be stalemate if player is in check
        if (player_in.isCheck(chess_board)) {
            return false;
        }

        List<Space> friendly_spaces = chess_board.getFriendlySpaces(player_in.isWhite());
        for (Space friendly_space : friendly_spaces) {
            List<Move> possible_moves = chess_board.getPiece(friendly_space).getPossibleMoves(
                friendly_space, chess_board
            );

            if (possible_moves.size() > 0) {
                for (Move move : possible_moves) {
                    if (!player_in.isCheckAfterMove(move, chess_board)) {
                        return false;
                    }
                }
            }
        }

        System.out.println("Game has ended in stalemate!");
        return true;
    }
}
