package com.lcwd.user.service.external.services;


import com.lcwd.user.service.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@FeignClient(name="RATING-SERVICE")
public interface RatingService {

    @PostMapping("/ratings")
    Rating createRating(Rating values);

    @GetMapping("/ratings/users/{userId}")
    List<Rating> getRatingsByUserId(@PathVariable String userId);

    @PutMapping("/ratings/{ratingId}")
    Rating updateRating(@PathVariable String ratingId,Rating rating);

    @DeleteMapping("/ratings/{ratingId}")
    void deleteRating(@PathVariable String ratingId);
}
