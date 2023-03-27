package com.example.demo.entity;
// Object required for user to submit in order to make a move
public class Gameplay {
    private Integer coordX; 
    private Integer coordY;
    private String playerId;


    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Integer getCoordX() {
        return coordX;
    }

    public void setCoordX(Integer coordX) {
        this.coordX = coordX;
    }

    public Integer getCoordY() {
        return coordY;
    }

    public void setCoordY(Integer coordY) {
        this.coordY = coordY;
    }


}
