package com.example.demo;

import com.example.demo.entity.Game;
import com.example.demo.entity.Gameplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// REST API controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    //Fetch all games
    @GetMapping("/games")
    public List<Game> getGames(){
        return gameService.getGame();
    }

    //Fetch the game with game id
    @GetMapping("/games/{id}")
    public Game getGame(@PathVariable String id) throws Exception {
        return gameService.getGame(id);
    }

    //Create new game
    @PostMapping("/games/{id}")
    public ResponseEntity<Game> postGame(@PathVariable String id){
        return ResponseEntity.ok(gameService.newGame(id));
    }

    //Make a move in the game with id
    @PutMapping("/games/{id}")
    public ResponseEntity<Game> putGame(@PathVariable String id, @RequestBody Gameplay gameplay) throws Exception {
        //Send message to websocket's /topic/lobby 
        template.convertAndSend("/topic/lobby",id+"@Update");
        return ResponseEntity.ok(gameService.gameplay(gameplay,id));
    }

    //Connect playerTwoId to the game with id
    @PutMapping("/games/{id}/{playerTwoId}")
    public ResponseEntity<?> putGamePlayerTwo(@PathVariable String id, @PathVariable String playerTwoId) throws Exception {
         ResponseEntity re= gameService.connectToGame(playerTwoId, id);
         //Send message to websocket's /topic/lobby 
        template.convertAndSend("/topic/lobby",id+"@New player");
        return re;
    }
}
