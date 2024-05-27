package com.abdmoh123.chessgame.javafx.controllers;

import java.util.ArrayList;
import java.util.List;

import com.abdmoh123.chessgame.Game;
import com.abdmoh123.chessgame.GameState;
import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.moves.PromotePawnMove;
import com.abdmoh123.chessgame.players.Human;
import com.abdmoh123.chessgame.players.Player;
import com.abdmoh123.chessgame.players.bots.BotPlayer;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.javafx.components.BoardPane;
import com.abdmoh123.chessgame.javafx.components.SpacePane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainController {
    @FXML
    private BorderPane main_window;

    @FXML
    private Text output_text;
    @FXML
    public BoardPane chess_board_pane;

    @FXML
    public VBox move_history_index;
    @FXML
    public VBox white_move_history;
    @FXML
    public VBox black_move_history;

    private Game chess_game;

    private Space selected_space;
    private List<Move> selected_possible_moves;

    @FXML
    private VBox promote_popup;

    public void initialize() {
        selected_possible_moves = new ArrayList<>();
        promote_popup = createPromotionPopup();
        Node center_content = main_window.getCenter();
        ((StackPane) center_content).getChildren().add(new Group(promote_popup));
        main_window.setCenter(center_content);
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        output_text.setText("Chess!");
    }

    @FXML
    protected void startGame(ActionEvent event) {
        // do nothing if game is already active
        if (this.chess_game != null && this.chess_game.getState() == GameState.ACTIVE)
            return;

        Board chess_board = new StandardBoard();
        chess_board.initialise();
        this.chess_game = new Game(new Player[] { new Human(true), new Human(false) }, chess_board);

        move_history_index.getChildren().clear();
        white_move_history.getChildren().clear();
        black_move_history.getChildren().clear();

        chess_board_pane.reset();
        chess_board_pane.setBoard(chess_board);
        System.out.println("Game started!");
    }

    @FXML
    protected void endGame(ActionEvent event) {
        // do nothing if it has already ended or if game hasn't started
        if (this.chess_game == null || this.chess_game.hasEnded())
            return;

        this.chess_game.endGame(this.chess_game.isP1Turn());
        System.out.println("Game ended!");
    }

    @FXML
    protected void handleSpaceSelection(MouseEvent event) {
        // don't do anything if game hasn't started or if it has ended
        if (this.chess_game == null)
            return;
        if (this.chess_game.getState() != GameState.ACTIVE)
            return;

        // reset highlighting every time the player clicks a space
        resetHighlighting();
        Player current_player = this.chess_game.getCurrentPlayer();
        // don't let player select pieces if a bot is playing
        if (current_player instanceof BotPlayer)
            return;

        // get square/grid cell that the user clicked
        Node clicked_node = event.getPickResult().getIntersectedNode();
        // do nothing if clicked node is not a chess board space
        if (!(clicked_node instanceof SpacePane))
            return;

        this.selected_space = getSpacePaneLocation((SpacePane) clicked_node);
        // apply move if player selected the correct spaces
        Move selected_move = getMoveFromSelection(this.selected_space);
        if (selected_move instanceof PromotePawnMove) {
            promote_popup.setVisible(true);
        }
        if (selected_move != null) {
            // move is recorded before applied to prevent null pointer exception
            this.chess_game.recordMove(selected_move);
            current_player.addPoints(selected_move.getKillPoints());

            selected_move.apply(this.chess_game.getBoard());

            resetHighlighting();
            chess_board_pane.setBoard(this.chess_game.getBoard());

            this.selected_space = null;
            this.selected_possible_moves.clear();

            /* Add move to move history list */
            List<Move> move_history = this.chess_game.getMoveHistory();
            String move_notation = this.chess_game.getEngine()
                    .convertMoveToNotation(move_history.get(move_history.size() - 1));

            // left column = white moves, right = black moves, with move index at far left
            Label move_entry = new Label(move_notation);
            move_entry.setAlignment(Pos.CENTER);

            if (this.chess_game.isP1Turn()) {
                Label index_label = new Label(Integer.toString((move_history.size() + 1) / 2));
                move_history_index.getChildren().add(index_label);
                white_move_history.getChildren().add(move_entry);
            } else
                black_move_history.getChildren().add(move_entry);

            this.chess_game.switchTurn();

            return;
        }

        if (this.chess_game.getBoard().isSpaceFriendly(this.selected_space, current_player.isWhite())) {
            char piece_symbol = this.chess_game.getBoard().getPiece(selected_space).getSymbol();
            System.out.printf("Found %s at cell: (%d, %d)\n", piece_symbol, selected_space.getX(),
                    selected_space.getY());

            // highlight space in orange
            ((SpacePane) clicked_node).highlight("#d65d0e");
        }

        updatePossibleMoves();
        highlightMovablePanes();
    }

    private VBox createPromotionPopup() {
        VBox popup_content = new VBox();

        Label popup_title = new Label("Select a piece to promote to");

        HBox piece_list = new HBox();
        Button rook_button = new Button("Rook");
        Button knight_button = new Button("Knight");
        Button bishop_button = new Button("Bishop");
        Button queen_button = new Button("Queen");
        Button[] choices = { rook_button, knight_button, bishop_button, queen_button };

        for (Button button : choices) {
            button.getStyleClass().add("popup-buttons");
            button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    popup_content.setVisible(false);
                }
            });
        }
        piece_list.getChildren().addAll(rook_button, knight_button, bishop_button, queen_button);

        popup_content.getChildren().addAll(popup_title, piece_list);

        popup_content.setVisible(false);
        popup_content.setId("promote_popup");

        return popup_content;
    }

    private Move getMoveFromSelection(Space space_in) {
        for (Move move : this.selected_possible_moves) {
            if (space_in.equals(move.getNewLocation()))
                return move;
        }
        return null;
    }

    private Space getSpacePaneLocation(SpacePane pane) {
        Integer column_index = BoardPane.getColumnIndex(pane);
        Integer row_index = BoardPane.getRowIndex(pane);

        // convert to correct coordinates (following Space class)
        column_index -= 1;
        row_index = chess_board_pane.getSize() - row_index - 1;

        return new Space(column_index, row_index);
    }

    private void updatePossibleMoves() {
        Board chess_board = this.chess_game.getBoard();
        // clear memory if piece cannot be moved
        if (!chess_board.isSpaceFriendly(selected_space, this.chess_game.isP1Turn())) {
            this.selected_possible_moves.clear();
        } else {
            this.selected_possible_moves = this.chess_game.getEngine().generateLegalMoves(
                    this.selected_space,
                    this.chess_game.isP1Turn());
        }
    }

    private void highlightMovablePanes() {
        Board chess_board = this.chess_game.getBoard();
        // do nothing if piece cannot be moved
        if (!chess_board.isSpaceFriendly(selected_space, this.chess_game.isP1Turn()))
            return;

        for (Move move : selected_possible_moves) {
            Space space = move.getNewLocation();
            SpacePane pane = chess_board_pane.getCell(space.getX(), space.getY());
            // highlight the spaces in blue
            pane.highlight("#458588", "#076678");
        }
    }

    private void resetHighlighting() {
        List<SpacePane> pane_list = chess_board_pane.getCells();
        for (SpacePane pane : pane_list) {
            pane.resetBackground();
        }
    }
}
