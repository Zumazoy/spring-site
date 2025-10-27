package web_7.Tickets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Tickets.model.TicketModel;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Long> {
    Page<TicketModel> findAllByOrderByIdAsc(Pageable pageable);
    List<TicketModel> findAllByOrderByIdAsc();

    boolean existsByCarriageNumberAndSeatNumber(Integer carriageNumber, Integer seatNumber);
}