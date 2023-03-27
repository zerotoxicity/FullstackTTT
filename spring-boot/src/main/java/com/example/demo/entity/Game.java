package com.example.demo.entity;

import com.example.demo.enums.Piece;
import com.example.demo.enums.Status;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.java.IntegerPrimitiveArrayJavaType;

import java.util.Arrays;

// Entity of the Tic-Tac-Toe to be stored in Database
@Entity
@Table(name="game")
public class Game {
    @Id
    @Column(name = "gameId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    @Type(IntArrayType.class)
    @Column(name = "board",columnDefinition = "int[][]")
    private int[][] board;

    @Column(name = "move_count")
    private int moveCount;

    @Column(name="player1")
    private String player1;

    @Column(name="player2")
    private String player2;

    @Column(name="player_turn")
    private String playerTurn;

    @Column(name="current_status")
    private Status status;

    // @Enumerated(EnumType.STRING)
    @Column(name="winner")
    private Piece winner;



    public Game( int[][] board, String player1){
        for(int i=0; i<board.length;i++) Arrays.fill(board[i],-1);
        this.board= board;
        this.player1=player1;
        this.status=Status.NEW;
        this.moveCount = 0;
        this.playerTurn=null;
    }

    public Game() {
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Piece getWinner() {
        return winner;
    }

    public void setWinner(Piece winner) {
        this.winner = winner;
        this.status = Status.END;
    }
}
