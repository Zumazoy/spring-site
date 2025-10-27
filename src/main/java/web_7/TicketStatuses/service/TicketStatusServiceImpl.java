package web_7.TicketStatuses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.TicketStatuses.model.TicketStatusModel;
import web_7.TicketStatuses.repository.TicketStatusRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketStatusServiceImpl implements TicketStatusService {
    private final TicketStatusRepository ticketStatusRepository;

    @Autowired
    public TicketStatusServiceImpl(TicketStatusRepository ticketStatusRepository) {
        this.ticketStatusRepository = ticketStatusRepository;
    }

    @Override
    public Page<TicketStatusModel> getAllTicketStatuses(int page, int size) {
        return ticketStatusRepository.findAllByOrderByTicketStatusIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<TicketStatusModel> getAllTicketStatuses() {
        return ticketStatusRepository.findAllByOrderByTicketStatusIdAsc();
    }

    @Override
    public void addTicketStatus(TicketStatusModel ticketStatus) {
        ticketStatusRepository.save(ticketStatus);
    }

    @Override
    public void updateTicketStatus(TicketStatusModel ticketStatus) {
        ticketStatusRepository.save(ticketStatus);
    }

    @Override
    public void deleteTicketStatus(Long id) {
        ticketStatusRepository.deleteById(id);
    }

    @Override
    public Optional<TicketStatusModel> findById(Long id) {
        return ticketStatusRepository.findById(id);
    }

    @Override
    public List<TicketStatusModel> searchTicketStatuses(String keyword, String searchBy) {
        List<TicketStatusModel> statuses;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return ticketStatusRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "title":
                statuses = ticketStatusRepository.findByTicketStatusTitle(keyword);
                break;
            default:
                statuses = ticketStatusRepository.findAllByOrderByTicketStatusIdAsc();
        }
        return statuses.stream()
                .sorted(Comparator.comparing(TicketStatusModel::getTicketStatusId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByTicketStatusTitle(String ticketStatusTitle) {
        return ticketStatusRepository.existsByTicketStatusTitle(ticketStatusTitle);
    }
}