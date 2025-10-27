package web_7.RouteStations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.RouteStations.model.RouteStationModel;
import web_7.RouteStations.repository.RouteStationRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteStationServiceImpl implements RouteStationService {
    private final RouteStationRepository routeStationRepository;

    @Autowired
    public RouteStationServiceImpl(RouteStationRepository routeStationRepository) {
        this.routeStationRepository = routeStationRepository;
    }

    @Override
    public Page<RouteStationModel> getAllRouteStations(int page, int size) {
        return routeStationRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<RouteStationModel> findAllByOrderByIdAsc() {
        return routeStationRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void addRouteStation(RouteStationModel routeStation) {
        routeStationRepository.save(routeStation);
    }

    @Override
    public void updateRouteStation(RouteStationModel routeStation) {
        routeStationRepository.save(routeStation);
    }

    @Override
    public void deleteRouteStation(Long id) {
        routeStationRepository.deleteById(id);
    }

    @Override
    public Optional<RouteStationModel> findById(Long id) {
        return routeStationRepository.findById(id);
    }

    @Override
    public List<RouteStationModel> searchRouteStations(String keyword, String searchBy) {
        List<RouteStationModel> routeStations;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return routeStationRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            default:
                routeStations = routeStationRepository.findAllByOrderByIdAsc();
        }
        return routeStations.stream()
                .sorted(Comparator.comparing(RouteStationModel::getId))
                .collect(Collectors.toList());
    }
}