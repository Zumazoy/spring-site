package web_7.Reviews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Reviews.model.ReviewModel;
import web_7.Reviews.repository.ReviewRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Page<ReviewModel> getAllReviews(int page, int size) {
        return reviewRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public void addReview(ReviewModel review) {
        reviewRepository.save(review);
    }

    @Override
    public void updateReview(ReviewModel review) {
        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Optional<ReviewModel> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<ReviewModel> searchReviews(String keyword, String searchBy) {
        List<ReviewModel> reviews;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return reviewRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "rating":
                try {
                    Short rating = Short.parseShort(keyword);
                    reviews = reviewRepository.findByRating(rating);
                    break;
                } catch (NumberFormatException e) {
                    return List.of();
                }
            default:
                reviews = reviewRepository.findAllByOrderByIdAsc();
        }
        return reviews.stream()
                .sorted(Comparator.comparing(ReviewModel::getId))
                .collect(Collectors.toList());
    }
}