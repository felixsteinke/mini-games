package com.spring.server.game.cardOwner;

import com.spring.server.game.card.entity.BusinessCardEntity;
import com.spring.server.game.card.entity.ProjectCardEntity;
import com.spring.server.game.exception.CardNotAvailableException;
import com.spring.server.game.exception.TurnNotPossibleException;

import java.util.HashMap;
import java.util.Map;

import static com.spring.server.game.card.entity.BusinessCardEntity.BAECKEREI;
import static com.spring.server.game.card.entity.BusinessCardEntity.WEIZENFELD;
import static com.spring.server.game.card.entity.ProjectCardEntity.*;

public class Player {

    private final String name;
    private final Map<ProjectCardEntity, Boolean> projects;
    private final Map<BusinessCardEntity, Integer> businesses;
    private int money;

    /**
     * Start values:
     * <ul>
     *     <li>Money: 3</li>
     *     <li>No Projects owned.</li>
     *     <li>Businesses: 1x {@link BusinessCardEntity#WEIZENFELD} + 1x {@link BusinessCardEntity#BAECKEREI}</li>
     * </ul>
     *
     * @param name identification value for the entire game
     */
    public Player(String name) {
        this.name = name;
        this.money = 3;
        this.projects = new HashMap<>();
        projects.put(BAHNHOF, false);
        projects.put(EINKAUFSZENTRUM, false);
        projects.put(FREIZEITPARK, false);
        projects.put(FUNKTURM, false);
        this.businesses = new HashMap<>();
        businesses.put(WEIZENFELD, 1);
        businesses.put(BAECKEREI, 1);
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    /*
    Money operations
     */
    public void addMoney(int value) {
        money += value;
    }

    public void bookPurchase(int value) throws TurnNotPossibleException {
        if (money - value < 0) {
            throw new TurnNotPossibleException(name + " got not enough money for purchase.");
        }
        money -= value;
    }

    public int steelMoney(int value) {
        money -= value;
        if (money < 0) {
            value += money;
            money = 0;
        }
        return value;
    }

    /*
    Card operations
     */
    public boolean hasProject(ProjectCardEntity card) {
        return this.projects.get(card);
    }

    public void addProject(ProjectCardEntity card) {
        projects.replace(card, true);
    }

    public Map<ProjectCardEntity, Boolean> getAllProjects() {
        return this.projects;
    }

    public int hasBusiness(BusinessCardEntity card) {
        return this.businesses.getOrDefault(card, 0);
    }

    public void addBusiness(BusinessCardEntity card) {
        if (businesses.containsKey(card)) {
            businesses.replace(card, businesses.get(card) + 1);
        } else {
            businesses.put(card, 1);
        }
    }

    public void steelBusiness(BusinessCardEntity card) throws CardNotAvailableException {
        if (hasBusiness(card) > 0) {
            businesses.replace(card, businesses.get(card) - 1);
        } else {
            throw new CardNotAvailableException(card, name + " does not own card.");
        }
    }

    public Map<BusinessCardEntity, Integer> getBusinesses() {
        return businesses;
    }
}
