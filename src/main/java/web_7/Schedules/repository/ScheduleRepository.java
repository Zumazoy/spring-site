package web_7.Schedules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Schedules.model.ScheduleModel;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleModel, Long> {
    List<ScheduleModel> findByDepartureDate(LocalDate departureDate);
    Page<ScheduleModel> findAllByOrderByIdAsc(Pageable pageable);
    List<ScheduleModel> findAllByOrderByIdAsc();
}