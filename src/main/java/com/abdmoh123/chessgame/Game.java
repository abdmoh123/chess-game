package com.abdmoh123.chessgame;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.control.engine.Engine;
import com.abdmoh123.chessgame.moves.Move;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player[] players;
    private boolean p1_turn;
    private List<Move> move_history;

    private Engine chess_engine;
    private GameState game_state;

    public Game(Player[] players_in, Board chess_board_in) {
        this.players = players_in;
        this.p1_turn = getPlayer(1).isWhite();
        this.move_history = new ArrayList<>();
        
        this.chess_engine = new Engine(chess_board_in);
        this.game_state = GameState.ACTIVE;
    }

    public void runGame() {
        while (!hasEnded()) {
            startTurn();
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

    private void startTurn() {
        getBoard().display();

        Player current_player = selectPlayer();

        checkGameEnded(current_player);
        if (hasEnded()) {
            return;
        }

        displayPlayerMessage(current_player);

        Move generated_move = current_player.startMove(getEngine());
        getBoard().refreshEnPassant();
        // apply after refreshing en passant so double pawn moves work correctly
        generated_move.apply(getBoard());

        current_player.addPoints(generated_move.getKillPoints());
        recordMove(generated_move);
    }

    private void displayPlayerMessage(Player player) {
        if (!isP1Turn()) {
            System.out.println("Player 1's turn:");
        }
        else {
            System.out.println("Player 2's turn:");
        }

        if (getEngine().isCheck(player.isWhite())) {
            System.out.println("Your king is in check!");
        }
    }

    public Player selectPlayer() {
        /* Automatically select the correct player for the turn */

        if (isP1Turn()) {
            this.p1_turn = !isP1Turn(); // switch turn for next time
            return getPlayer(1);
        }
        else {
            this.p1_turn = !isP1Turn(); // switch turn for next time
            return getPlayer(2);
        }
    }

    public void checkGameEnded(Player player_in) {
        /* Check if current position is a check mate or draw and set game state accordingly */

        if (getEngine().isCheckMate(player_in.isWhite())) {
            endGame(player_in.isWhite());
        }
        if (isDraw(player_in)) {
            setState(GameState.DRAW);
        }
    }

    public boolean isP1Turn() {
        return this.p1_turn;
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
        return getEngine().getBoard();
    }
    public Engine getEngine() {
        return this.chess_engine;
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
        else {
            setState(GameState.WHITE_WIN);
        }
    }

    public boolean isDraw(Player player_in) {
        // TODO: Add check for 3 fold repetition
        // TODO: Add check for 50 move rule (draw if 50 quiet moves happen consecutively)
        // TODO: Add check for dead position (impossible to checkmate)

        if (getEngine().isStalemate(player_in.isWhite())) {
            System.out.println("Game has ended in stalemate!");
            return true;
        }

        List<Space> all_spaces_with_pieces = getBoard().getAllSpacesWithPieces();
        return all_spaces_with_pieces.size() == 2;  // returns true if only 2 kings remain.java
    }
}
