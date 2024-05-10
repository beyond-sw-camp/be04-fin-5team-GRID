package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.dto.ReviewDTO;

public interface CommandReviewService {
    ReviewDTO findReviewById(int id);
}
