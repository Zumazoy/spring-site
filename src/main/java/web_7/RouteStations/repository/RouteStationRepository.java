package web_7.RouteStations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.RouteStations.model.RouteStationModel;

import java.util.List;

@Repository
public interface RouteStationRepository extends JpaRepository<RouteStationModel, Long> {
    Page<RouteStationModel> findAllByOrderByIdAsc(Pageable pageable);
    List<RouteStationModel> findAllByOrderByIdAsc();
}