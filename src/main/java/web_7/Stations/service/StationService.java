package web_7.Stations.service;

import org.springframework.data.domain.Page;
import web_7.Stations.model.StationModel;

import java.util.List;
import java.util.Optional;

public interface StationService {
    Page<StationModel> getAllStations(int page, int size);
    void addStation(StationModel station);
    void updateStation(StationModel station);
    void deleteStation(Long stationId);
    Optional<StationModel> findById(Long stationId);
    List<StationModel> searchStations(String keyword, String searchBy);
    List<StationModel> getAllStations();

    boolean existsByStationName(String stationName);
}