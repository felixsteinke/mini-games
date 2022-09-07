package com.spring.server.game;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {

    private final int dice1;
    private final int dice2;

    public Dice(boolean rollTwoDices) {
        if (rollTwoDices) {
            this.dice1 = generateRandomInteger();
            this.dice2 = generateRandomInteger();
        } else {
            this.dice1 = generateRandomInteger();
            this.dice2 = 0;
        }
    }

    public int getDice1() {
        return this.dice1;
    }

    public int getDice2() {
        return this.dice2;
    }

    public int getValue() {
        return dice1 + dice2;
    }

    public boolean isDouble() {
        return dice1 == dice2;
    }

    private int generateRandomInteger() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }
}
