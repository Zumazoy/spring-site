package web_7.Reviews.service;

import org.springframework.data.domain.Page;
import web_7.Reviews.model.ReviewModel;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Page<ReviewModel> getAllReviews(int page, int size);
    void addReview(ReviewModel review);
    void updateReview(ReviewModel review);
    void deleteReview(Long reviewId);
    Optional<ReviewModel> findById(Long reviewId);
    List<ReviewModel> searchReviews(String keyword, String searchBy);
}