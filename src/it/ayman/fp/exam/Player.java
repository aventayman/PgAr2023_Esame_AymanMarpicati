package it.ayman.fp.exam;

public class Player {
    private int hp, attack, numLives;

    public Player() {
        this.hp = 20;
        this.attack = 5;
        this.numLives = 10;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getNumLives() {
        return numLives;
    }

    public void setNumLives(int numLives) {
        this.numLives = numLives;
    }
}
