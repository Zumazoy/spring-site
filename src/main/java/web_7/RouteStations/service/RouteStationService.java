package web_7.RouteStations.service;

import org.springframework.data.domain.Page;
import web_7.RouteStations.model.RouteStationModel;

import java.util.List;
import java.util.Optional;

public interface RouteStationService {
    Page<RouteStationModel> getAllRouteStations(int page, int size);
    void addRouteStation(RouteStationModel routeStation);
    void updateRouteStation(RouteStationModel routeStation);
    void deleteRouteStation(Long routeStationId);
    Optional<RouteStationModel> findById(Long routeStationId);
    List<RouteStationModel> searchRouteStations(String keyword, String searchBy);
    List<RouteStationModel> findAllByOrderByIdAsc();
}