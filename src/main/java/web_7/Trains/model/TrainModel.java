package web_7.Trains.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import web_7.Reviews.model.ReviewModel;

import java.util.List;

@Entity
@Table(name = "trains", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"train_title", "train_type"})
})
public class TrainModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 40, message = "Название поезда должно содержать от 3 до 40 символов")
    @NotNull(message = "Название поезда не может быть null")
    @NotBlank(message = "Название поезда не может быть пустым")
    private String trainTitle;

    @Size(min = 3, max = 40, message = "Тип поезда должен содержать от 3 до 40 символов")
    @NotNull(message = "Тип поезда не может быть null")
    @NotBlank(message = "Тип поезда не может быть пустым")
    private String trainType;

    @NotNull(message = "Количество вагонов не может быть null")
    @Min(value = 1, message = "Количество вагонов должно быть не менее 1")
    @Max(value = 100, message = "Количество вагонов должно быть не более 100")
    private Integer carriageCount;

    @Size(max = 50, message = "Компания-перевозчик должна содержать не более 50 символов")
    @NotNull(message = "Компания-перевозчик не может быть null")
    @NotBlank(message = "Компания-перевозчик не может быть пустым")
    private String carrierCompany;

    @OneToMany(mappedBy = "train", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ReviewModel> review;

    public TrainModel() {}

    public TrainModel(Long id, String trainTitle, String trainType, Integer carriageCount, String carrierCompany) {
        this.id = id;
        this.trainTitle = trainTitle;
        this.trainType = trainType;
        this.carriageCount = carriageCount;
        this.carrierCompany = carrierCompany;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainTitle() {
        return trainTitle;
    }
    public void setTrainTitle(String trainTitle) {
        this.trainTitle = trainTitle;
    }

    public String getTrainType() {
        return trainType;
    }
    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public Integer getCarriageCount() {
        return carriageCount;
    }
    public void setCarriageCount(Integer carriageCount) {
        this.carriageCount = carriageCount;
    }

    public String getCarrierCompany() {
        return carrierCompany;
    }
    public void setCarrierCompany(String carrierCompany) {
        this.carrierCompany = carrierCompany;
    }

    public List<ReviewModel> getReview() {
        return review;
    }
    public void setReview(List<ReviewModel> review) {
        this.review = review;
    }
}