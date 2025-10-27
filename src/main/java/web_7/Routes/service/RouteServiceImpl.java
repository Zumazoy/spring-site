package web_7.Routes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Routes.model.RouteModel;
import web_7.Routes.repository.RouteRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public Page<RouteModel> getAllRoutes(int page, int size) {
        return routeRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<RouteModel> getAllRoutes() {
        return routeRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void addRoute(RouteModel route) {
        routeRepository.save(route);
    }

    @Override
    public void updateRoute(RouteModel route) {
        routeRepository.save(route);
    }

    @Override
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    @Override
    public Optional<RouteModel> findById(Long id) {
        return routeRepository.findById(id);
    }

    @Override
    public List<RouteModel> searchRoutes(String keyword, String searchBy) {
        List<RouteModel> routes;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return routeRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "startStation":
                routes = routeRepository.findByStartStation(keyword);
                break;
            case "endStation":
                routes = routeRepository.findByEndStation(keyword);
                break;
            default:
                routes = routeRepository.findAllByOrderByIdAsc();
        }
        return routes.stream()
                .sorted(Comparator.comparing(RouteModel::getId))
                .collect(Collectors.toList());
    }
}