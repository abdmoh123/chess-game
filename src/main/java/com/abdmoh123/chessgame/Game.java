package com.abdmoh123.chessgame;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.pieces.Bishop;
import com.abdmoh123.chessgame.pieces.King;
import com.abdmoh123.chessgame.pieces.Pawn;
import com.abdmoh123.chessgame.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameState game_state;
    private Player[] players;
    private boolean p1_turn;
    private Board chess_board;
    private List<Move> move_history;

    int quiet_move_count;
    int three_fold_repetition_count;

    public Game(Player[] players_in, Board chess_board_in) {
        this.players = players_in;
        this.p1_turn = getPlayer(1).isWhite();
        this.move_history = new ArrayList<>();

        this.quiet_move_count = 0;
        this.three_fold_repetition_count = 0;
        
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

        // quiet move count (for 50 move rule)
        if (generated_move.getKillPoints() == 0) {
            ++this.quiet_move_count;
        }
        else {
            this.quiet_move_count = 0;
        }
        // three-fold repetition count
        if (move_history.size() > 4 && generated_move.equals(move_history.get(move_history.size() - 4))) {
            ++this.three_fold_repetition_count;
        }
        else {
            this.three_fold_repetition_count = 0;
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
        /* Check for stalemate, 50 quiet move rule, 3-fold repetition and dead positions */

        if (isStalemate(player_in, chess_board)) {
            return true;
        }

        // 50 quiet move rule check
        if (this.quiet_move_count >= 50) {
            return true;
        }
        // 3-fold repetition check
        if (this.three_fold_repetition_count >= 6) {
            return true;
        }

        // only check for dead position if only few pieces remain
        if (chess_board.getAllSpacesWithPieces().size() < 5) { return isDeadPosition(chess_board); }
        return false;
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
                    if (!player_in.doesMoveCauseCheck(move, chess_board)) {
                        return false;
                    }
                }
            }
        }

        System.out.println("Game has ended in stalemate!");
        return true;
    }
    private boolean hasOnlyBishopOrKnight(List<Space> spaces) {
        if (spaces.size() == 2) {
            for (Space space : spaces) {
                // if only other piece is bishop or knight, then player cannot checkmate
                if (chess_board.getPiece(space).getValue() == 3) {
                    return true;
                }
            }
        }
        return false;
    }
    private List<Space> removeKingFromList(List<Space> spaces, Board chess_board) {
        List<Space> new_list = new ArrayList<>();
        for (Space space : spaces) {
            if (!(chess_board.getPiece(space) instanceof King)) {
                new_list.add(space);
            }
        }
        return new_list;
    }
    public boolean isDeadPosition(Board chess_board) {
        List<Space> white_spaces = chess_board.getFriendlySpaces(true);
        List<Space> black_spaces = chess_board.getFriendlySpaces(false);

        // only 2 kings remain
        if (white_spaces.size() + black_spaces.size() == 2) { return true; }

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
        
        List<Space> white_spaces_without_king = removeKingFromList(white_spaces, chess_board);
        List<Space> black_spaces_without_king = removeKingFromList(black_spaces, chess_board);

        // check if bishops are same colour
        if (white_spaces.size() == 2 && black_spaces.size() == 2) {
            Space white_other_space = white_spaces_without_king.get(0);
            Space black_other_space = black_spaces_without_king.get(0);
            Piece white_other_piece = chess_board.getPiece(white_other_space);
            Piece black_other_piece = chess_board.getPiece(black_other_space);
            
            if (white_other_piece instanceof Bishop && black_other_piece instanceof Bishop) {
                if (((Bishop) white_other_piece).isDark(white_other_space) == ((Bishop) black_other_piece).isDark(black_other_space)) {
                    return true;
                }
            }
        }

        return false;
    }
}
