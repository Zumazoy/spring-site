package web_7.Stations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Stations.model.StationModel;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationModel, Long> {
    List<StationModel> findByStationName(String stationName);
    List<StationModel> findByCity(String city);
    List<StationModel> findByCountry(String country);
    Page<StationModel> findAllByOrderByIdAsc(Pageable pageable);
    List<StationModel> findAllByOrderByIdAsc();

    boolean existsByStationName(String stationName);
}