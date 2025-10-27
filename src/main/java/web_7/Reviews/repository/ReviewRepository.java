package web_7.Reviews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Reviews.model.ReviewModel;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {
    List<ReviewModel> findByRating(Short rating);
    Page<ReviewModel> findAllByOrderByIdAsc(Pageable pageable);
    List<ReviewModel> findAllByOrderByIdAsc();
}