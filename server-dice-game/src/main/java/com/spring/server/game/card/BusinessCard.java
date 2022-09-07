package com.spring.server.game.card;

import com.spring.server.game.card.entity.BusinessCardEntity;
import com.spring.server.game.card.entity.CardBranch;
import lombok.Getter;

@Getter
public class BusinessCard {
    private final BusinessCardEntity entity;
    private final CardBranch branch;
    private final int price;
    private final int[] rollEntity;
    private final String description;

    public BusinessCard(BusinessCardEntity entity, CardBranch branch, int price, int rollEntity, String description) {
        this.entity = entity;
        this.branch = branch;
        this.price = price;
        this.rollEntity = new int[1];
        this.rollEntity[0] = rollEntity;
        this.description = description;
    }

    public BusinessCard(BusinessCardEntity entity, CardBranch branch, int price, int rollEntity1, int rollEntity2, String description) {
        this.entity = entity;
        this.branch = branch;
        this.price = price;
        this.rollEntity = new int[2];
        this.rollEntity[0] = rollEntity1;
        this.rollEntity[1] = rollEntity2;
        this.description = description;
    }
}
