package web_7.Trains.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Trains.model.TrainModel;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<TrainModel, Long> {
    Page<TrainModel> findAllByOrderByIdAsc(Pageable pageable);
    List<TrainModel> findAllByOrderByIdAsc();
    List<TrainModel> findByTrainTitle(String keyword);
    List<TrainModel> findByTrainType(String keyword);
    List<TrainModel> findByCarrierCompany(String keyword);

    boolean existsByTrainTitleAndTrainType(String trainTitle, String trainType);
}