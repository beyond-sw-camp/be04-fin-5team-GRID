package org.highfives.grid.review.query.controller;

import org.highfives.grid.review.command.service.CommandReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class QueryReviewController {

    private final CommandReviewService commandReviewService;

    @Autowired
    public QueryReviewController(CommandReviewService commandReviewService) {
        this.commandReviewService = commandReviewService;
    }




}
