package web_7.Routes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Routes.model.RouteModel;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<RouteModel, Long> {
    List<RouteModel> findByStartStation(String startStation);
    List<RouteModel> findByEndStation(String endStation);
    Page<RouteModel> findAllByOrderByIdAsc(Pageable pageable);
    List<RouteModel> findAllByOrderByIdAsc();
}