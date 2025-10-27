package web_7.Trains.service;

import org.springframework.data.domain.Page;
import web_7.Trains.model.TrainModel;

import java.util.List;
import java.util.Optional;

public interface TrainService {
    Page<TrainModel> getAllTrains(int page, int size);
    void addTrain(TrainModel train);
    void updateTrain(TrainModel train);
    void deleteTrain(Long trainId);
    Optional<TrainModel> findById(Long trainId);
    List<TrainModel> searchTrains(String keyword, String searchBy);
    List<TrainModel> getAllTrains();

    boolean existsByTrainTitleAndTrainType(String trainTitle, String trainType);
}