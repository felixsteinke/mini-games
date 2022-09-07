package com.spring.server.game.card;

import com.spring.server.game.card.entity.CardBranch;
import com.spring.server.game.card.entity.ProjectCardEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectCard {

    private final ProjectCardEntity entity;
    private final CardBranch branch;
    private final int price;
    private final String description;
}
