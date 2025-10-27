package web_7.Tickets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Tickets.model.TicketModel;
import web_7.Tickets.repository.TicketRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Page<TicketModel> getAllTickets(int page, int size) {
        return ticketRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<TicketModel> getAllTickets() {
        return ticketRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void addTicket(TicketModel ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public void updateTicket(TicketModel ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Optional<TicketModel> findById(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public List<TicketModel> searchTickets(String keyword, String searchBy) {
        List<TicketModel> tickets;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return ticketRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            default:
                tickets = ticketRepository.findAllByOrderByIdAsc();
        }
        return tickets.stream()
                .sorted(Comparator.comparing(TicketModel::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCarriageAndSeat(Integer carriageNumber, Integer seatNumber) {
        return ticketRepository.existsByCarriageNumberAndSeatNumber(carriageNumber, seatNumber);
    }

    @Override
    public boolean isTicketAssignedToAnotherPayment(Long ticketId, Long paymentId) {
        Optional<TicketModel> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isPresent()) {
            TicketModel ticket = ticketOpt.get();
            return ticket.getPayment() != null &&
                    !ticket.getPayment().getId().equals(paymentId);
        }
        return false;
    }
}