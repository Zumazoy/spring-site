package web_7.Routes.service;

import org.springframework.data.domain.Page;
import web_7.Routes.model.RouteModel;

import java.util.List;
import java.util.Optional;

public interface RouteService {
    Page<RouteModel> getAllRoutes(int page, int size);
    void addRoute(RouteModel route);
    void updateRoute(RouteModel route);
    void deleteRoute(Long routeId);
    Optional<RouteModel> findById(Long routeId);
    List<RouteModel> searchRoutes(String keyword, String searchBy);
    List<RouteModel> getAllRoutes();
}