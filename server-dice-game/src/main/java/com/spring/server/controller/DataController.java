package com.spring.server.controller;

import com.spring.server.game.card.BusinessCard;
import com.spring.server.game.card.CardData;
import com.spring.server.game.card.ProjectCard;
import com.spring.server.game.card.entity.BusinessCardEntity;
import com.spring.server.game.card.entity.ProjectCardEntity;
import com.spring.server.game.exception.CardNotAvailableException;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DataController {

    private final CardData cardData = new CardData();

    @ApiOperation("Returns the information about a Project Card.")
    @RequestMapping(value = "/data/project-card",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectCard> getProjectCard(@RequestParam(value = "name", defaultValue = "BAHNHOF") ProjectCardEntity card) throws CardNotAvailableException {
        return ResponseEntity.ok(cardData.getProjectCard(card));
        //TODO exception handling
    }

    @ApiOperation("Returns the information about a Project Card.")
    @RequestMapping(value = "/data/business-card",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessCard> getBusinessCard(@RequestParam(value = "name", defaultValue = "WEIZENFELD") BusinessCardEntity card) throws CardNotAvailableException {
        return ResponseEntity.ok(cardData.getBusinessCard(card));
        //TODO exception handling
    }

    @ApiOperation("Returns the information about all Project Cards.")
    @RequestMapping(value = "/data/all/project-card",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<ProjectCardEntity, ProjectCard>> getAllProjectCards() {
        return ResponseEntity.ok(cardData.getAllProjectCards());
    }

    @ApiOperation("Returns the information about all Business Cards.")
    @RequestMapping(value = "/data/all/business-card",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<BusinessCardEntity, BusinessCard>> getAllBusinessCards() {
        return ResponseEntity.ok(cardData.getAllBusinessCards());
    }
}
