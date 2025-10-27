package web_7.TicketStatuses.service;

import org.springframework.data.domain.Page;
import web_7.TicketStatuses.model.TicketStatusModel;

import java.util.List;
import java.util.Optional;

public interface TicketStatusService {
    Page<TicketStatusModel> getAllTicketStatuses(int page, int size);
    void addTicketStatus(TicketStatusModel ticketStatus);
    void updateTicketStatus(TicketStatusModel ticketStatus);
    void deleteTicketStatus(Long ticketStatusId);
    Optional<TicketStatusModel> findById(Long ticketStatusId);
    List<TicketStatusModel> searchTicketStatuses(String keyword, String searchBy);
    List<TicketStatusModel> getAllTicketStatuses();

    boolean existsByTicketStatusTitle(String title);
}