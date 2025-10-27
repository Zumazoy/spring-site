package web_7.TicketStatuses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.TicketStatuses.model.TicketStatusModel;

import java.util.List;

@Repository
public interface TicketStatusRepository extends JpaRepository<TicketStatusModel, Long> {
    Page<TicketStatusModel> findAllByOrderByTicketStatusIdAsc(Pageable pageable);
    List<TicketStatusModel> findAllByOrderByTicketStatusIdAsc();
    List<TicketStatusModel> findByTicketStatusTitle(String keyword);

    boolean existsByTicketStatusTitle(String ticketStatusTitle);
}