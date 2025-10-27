package web_7.Reviews.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import web_7.Trains.model.TrainModel;
import web_7.Users.model.UserModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class ReviewModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Пользователь не выбран")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @NotNull(message = "Поезд не выбран")
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private TrainModel train;

    @NotNull(message = "Рейтинг не может быть null")
    @Min(value = 1, message = "Рейтинг должен быть не менее 1")
    @Max(value = 10, message = "Рейтинг должен быть не более 10")
    private Short rating;

    @Size(max = 1000, message = "Комментарий не должен превышать 1000 символов")
    private String comment;

    @PastOrPresent(message = "Дата отзыва не может быть в будущем")
    private LocalDateTime reviewDate;

    public ReviewModel() {}

    public ReviewModel(Long id, UserModel user, TrainModel train, Short rating, String comment, LocalDateTime reviewDate) {
        this.id = id;
        this.user = user;
        this.train = train;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user = user;
    }

    public TrainModel getTrain() {
        return train;
    }
    public void setTrain(TrainModel train) {
        this.train = train;
    }

    public Short getRating() {
        return rating;
    }
    public void setRating(Short rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}