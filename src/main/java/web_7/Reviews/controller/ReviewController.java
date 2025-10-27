package web_7.Reviews.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_7.PaginationValidUtils;
import web_7.Reviews.model.ReviewModel;
import web_7.Reviews.service.ReviewService;
import web_7.Trains.service.TrainService;
import web_7.Users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainService trainService;

    @GetMapping("/reviews")
    public String reviews(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("review")) {
            model.addAttribute("review", new ReviewModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<ReviewModel> reviewPage = reviewService.getAllReviews(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, reviewPage, page, pageSize);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("trains", trainService.getAllTrains());

        return "reviews";
    }

    @PostMapping("/reviews/add")
    public String addReview(@Valid @ModelAttribute ReviewModel review,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        review.setReviewDate(LocalDateTime.now());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.review", bindingResult);
            redirectAttributes.addFlashAttribute("review", review);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/reviews";
        }

        reviewService.addReview(review);
        return "redirect:/admin/reviews";
    }

    @PostMapping("/reviews/update")
    public String updateReview(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                               RedirectAttributes redirectAttributes,
                               @Valid @ModelAttribute ReviewModel review,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, review.getId());
            return "redirect:/admin/reviews";
        }

        ReviewModel updatedReview = reviewService.findById(review.getId())
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));

        updatedReview.setUser(review.getUser());
        updatedReview.setTrain(review.getTrain());
        updatedReview.setRating(review.getRating());
        updatedReview.setComment(review.getComment());

        reviewService.updateReview(updatedReview);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/reviews";
    }

    @PostMapping("/reviews/delete")
    public String deleteReview(@RequestParam Long id) {
        try {
            reviewService.deleteReview(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/reviews";
    }

    @GetMapping("/reviews/search")
    public String searchReviews(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("review")) {
            model.addAttribute("review", new ReviewModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<ReviewModel> reviews = reviewService.searchReviews(keyword, searchBy);
        int totalItems = reviews.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<ReviewModel> pagedReviews = reviews.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedReviews, page, pageSize, totalItems);
        model.addAttribute("reviews", pagedReviews);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "reviews";
    }
}