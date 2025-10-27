package web_7.Tickets.service;

import org.springframework.data.domain.Page;
import web_7.Tickets.model.TicketModel;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Page<TicketModel> getAllTickets(int page, int size);
    void addTicket(TicketModel ticket);
    void updateTicket(TicketModel ticket);
    void deleteTicket(Long ticketId);
    Optional<TicketModel> findById(Long ticketId);
    List<TicketModel> searchTickets(String keyword, String searchBy);
    List<TicketModel> getAllTickets();

    boolean existsByCarriageAndSeat(Integer carriageNumber, Integer seatNumber);
    boolean isTicketAssignedToAnotherPayment(Long ticketId, Long paymentId);
}