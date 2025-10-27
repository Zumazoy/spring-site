package web_7.Trains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Trains.model.TrainModel;
import web_7.Trains.repository.TrainRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainServiceImpl implements TrainService {
    private final TrainRepository trainRepository;

    @Autowired
    public TrainServiceImpl(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Page<TrainModel> getAllTrains(int page, int size) {
        return trainRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<TrainModel> getAllTrains() {
        return trainRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void addTrain(TrainModel train) {
        trainRepository.save(train);
    }

    @Override
    public void updateTrain(TrainModel train) {
        trainRepository.save(train);
    }

    @Override
    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    @Override
    public Optional<TrainModel> findById(Long id) {
        return trainRepository.findById(id);
    }

    @Override
    public List<TrainModel> searchTrains(String keyword, String searchBy) {
        List<TrainModel> trains;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return trainRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "trainTitle":
                trains = trainRepository.findByTrainTitle(keyword);
                break;
            case "trainType":
                trains = trainRepository.findByTrainType(keyword);
                break;
            case "carrierCompany":
                trains = trainRepository.findByCarrierCompany(keyword);
                break;
            default:
                trains = trainRepository.findAllByOrderByIdAsc();
        }
        return trains.stream()
                .sorted(Comparator.comparing(TrainModel::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByTrainTitleAndTrainType(String trainTitle, String trainType) {
        return trainRepository.existsByTrainTitleAndTrainType(trainTitle, trainType);
    }
}