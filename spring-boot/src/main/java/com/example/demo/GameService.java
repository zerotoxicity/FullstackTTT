package com.example.demo;

import com.example.demo.entity.Game;
import com.example.demo.entity.Gameplay;
import com.example.demo.enums.Piece;
import com.example.demo.enums.Status;
import com.example.demo.storage.GamesContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

@Service
public class GameService {

    private final GameRepo gameRepo;

    @Autowired
    public GameService(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }
    // Retrieve all games
    public List<Game> getGame(){
        return gameRepo.findAll();
    }
    // Find game with gameId
    public Game getGame(String gameId) throws Exception {
       return gameRepo.findById(gameId).orElseThrow();
    }
    // Create new game and save it to db
    public Game newGame(String player){
        Game game = new Game(new int[3][3],player);
        gameRepo.save(game);
        return game;
    }

    // Connect Player2 to the game with gameId
    public ResponseEntity<?> connectToGame(String player2, String gameId) throws Exception {
        Game game = gameRepo.findById(gameId).orElseThrow();
        //Throw error if the game is full
        if(game.getPlayer2()!=null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Game is full!");
        }
        // Throw error if Player 2 is using the same name as Player 1
        if(game.getPlayer1().equals(player2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name is taken");
        }
        game.setPlayer2(player2);
        game.setStatus(Status.STARTED);
        // Use random function to determine who goes first
        Random rn = new Random();
        int startingPlayer = rn.nextInt(1+1);
        String nextPlayer = startingPlayer == 0 ? game.getPlayer1() : player2;
        game.setPlayerTurn(nextPlayer);
        //Update the entity
        gameRepo.save(game);
        return ResponseEntity.ok(game);
    }
    // Make a move on current gameplay
    // Gameplay refers to the move made by the player
    // ID - Game ID
    public Game gameplay(Gameplay gameplay, String id) throws Exception {
        boolean completed = false;
        Game game = gameRepo.findById(id).orElseThrow();
        if(!game.getPlayerTurn().equals(gameplay.getPlayerId())) {
            throw new Exception("Invalid player turn");}
        if(game.getStatus().equals(Status.END)) throw new Exception("Game has ended");
        int[][] board = game.getBoard();
        int x = gameplay.getCoordX(),y = gameplay.getCoordY();
        if(board[x][y]!=-1) throw new Exception("Invalid spot");
        int piece = gameplay.getPlayerId().equals(game.getPlayer1())? 0 : 1;
        board[x][y] = piece;
        if(checkWinner(board,piece)){
            game.setWinner(Piece.values()[piece]);
            completed = true;
        }
        else if(game.getMoveCount()+1==board.length*board[0].length) {
            game.setWinner(Piece.D);
            completed = true;
        }
        else{
            String currPlayer = game.getPlayerTurn();
            String nextPlayer = currPlayer.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
            game.setPlayerTurn(nextPlayer);
        }
        if(completed) game.setPlayerTurn("");
        game.setBoard(board);
        game.setMoveCount(game.getMoveCount()+1);

        GamesContainer.getInstance().setGames(game);
        gameRepo.save(game);
        return game;
    }

//  Check game state for winner
    private boolean checkWinner(int[][] board, int pieceVal){
        //Check vertical/horizontal wins
        for(int i=0; i<3;i++){
            //Horizontal check
            if(pieceVal==board[i][0] && board[i][0]==board[i][1] && board[i][0]==board[i][2]) return true;
            //Vertical check
            if(pieceVal==board[0][i] && board[0][i]==board[1][i] && board[0][i]==board[2][i]) return true;
        }
        //Diagonal win
        if(pieceVal == board[1][1]){
            if( board[0][0] == board[1][1] && board[0][0]==board[2][2]) return true;
            return board[0][2] == board[1][1] && board[0][2] == board[2][0];
        }


        return false;
    }
}
