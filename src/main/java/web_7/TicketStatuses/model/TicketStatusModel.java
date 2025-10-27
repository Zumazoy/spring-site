package web_7.TicketStatuses.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import web_7.Tickets.model.TicketModel;

import java.util.List;

@Entity
@Table(name = "ticket_statuses")
public class TicketStatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketStatusId;

    @Size(min = 3, max = 30, message = "Название статуса билета должно содержать от 3 до 30 символов")
    @NotNull(message = "Название статуса билета не может быть null")
    @NotBlank(message = "Название статуса билета не может быть пустым")
    @Column(unique = true)
    private String ticketStatusTitle;

    @OneToMany(mappedBy = "ticketStatus", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<TicketModel> tickets;

    public TicketStatusModel() {}

    public TicketStatusModel(Long ticketStatusId, String ticketStatusTitle) {
        this.ticketStatusId = ticketStatusId;
        this.ticketStatusTitle = ticketStatusTitle;
    }

    public Long getTicketStatusId() {
        return ticketStatusId;
    }
    public void setTicketStatusId(Long ticketStatusId) {
        this.ticketStatusId = ticketStatusId;
    }

    public String getTicketStatusTitle() {
        return ticketStatusTitle;
    }
    public void setTicketStatusTitle(String ticketStatusTitle) {
        this.ticketStatusTitle = ticketStatusTitle;
    }

    public List<TicketModel> getTickets() {
        return tickets;
    }
    public void setTickets(List<TicketModel> tickets) {
        this.tickets = tickets;
    }
}
