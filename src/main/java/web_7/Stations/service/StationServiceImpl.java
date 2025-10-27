package web_7.Stations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Stations.model.StationModel;
import web_7.Stations.repository.StationRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;

    @Autowired
    public StationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Page<StationModel> getAllStations(int page, int size) {
        return stationRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<StationModel> getAllStations() {
        return stationRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void addStation(StationModel station) {
        stationRepository.save(station);
    }

    @Override
    public void updateStation(StationModel station) {
        stationRepository.save(station);
    }

    @Override
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }

    @Override
    public Optional<StationModel> findById(Long id) {
        return stationRepository.findById(id);
    }

    @Override
    public List<StationModel> searchStations(String keyword, String searchBy) {
        List<StationModel> stations;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return stationRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "stationName":
                stations = stationRepository.findByStationName(keyword);
                break;
            case "city":
                stations = stationRepository.findByCity(keyword);
                break;
            case "country":
                stations = stationRepository.findByCountry(keyword);
                break;
            default:
                stations = stationRepository.findAllByOrderByIdAsc();
        }
        return stations.stream()
                .sorted(Comparator.comparing(StationModel::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByStationName(String stationName) {
        return stationRepository.existsByStationName(stationName);
    }
}