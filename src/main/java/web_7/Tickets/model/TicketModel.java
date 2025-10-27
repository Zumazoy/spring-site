package web_7.Tickets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import web_7.Payments.model.PaymentModel;
import web_7.Schedules.model.ScheduleModel;
import web_7.TicketStatuses.model.TicketStatusModel;
import web_7.Users.model.UserModel;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"schedule_id", "carriage_number", "seat_number"})
})
public class TicketModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Расписание не выбрано")
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleModel schedule;

    @NotNull(message = "Пользователь не выбран")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @NotNull(message = "Статус билета не выбран")
    @ManyToOne
    @JoinColumn(name = "ticket_status_id", nullable = false)
    private TicketStatusModel ticketStatus;

    @NotNull(message = "Номер вагона не может быть null")
    @Min(value = 1, message = "Номер вагона должен быть не менее 1")
    @Max(value = 100, message = "Номер вагона должен быть не более 100")
    private Integer carriageNumber;

    @NotNull(message = "Номер места не может быть null")
    @Min(value = 1, message = "Номер места должен быть не менее 1")
    @Max(value = 10000, message = "Номер места должен быть не более 10000")
    private Integer seatNumber;

    @NotNull(message = "Цена билета не может быть null")
    @Positive(message = "Цена билета должна быть положительной")
    @Digits(integer = 8, fraction = 2, message = "Цена билета должна быть в формате 8 целых и 2 дробных знака")
    private BigDecimal price;

    @OneToOne(mappedBy = "ticket")
    private PaymentModel payment;

    public PaymentModel getPayment() {
        return payment;
    }
    public void setPayment(PaymentModel payment) {
        this.payment = payment;
    }


    public TicketModel() {}

    public TicketModel(Long id, ScheduleModel schedule, UserModel user, TicketStatusModel ticketStatus, Integer carriageNumber, Integer seatNumber, BigDecimal price) {
        this.id = id;
        this.schedule = schedule;
        this.user = user;
        this.ticketStatus = ticketStatus;
        this.carriageNumber = carriageNumber;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }
    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }

    public UserModel getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user = user;
    }

    public TicketStatusModel getTicketStatus() {
        return ticketStatus;
    }
    public void setTicketStatus(TicketStatusModel ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Integer getCarriageNumber() {
        return carriageNumber;
    }
    public void setCarriageNumber(Integer carriageNumber) {
        this.carriageNumber = carriageNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    public String getTicket() {
        return "Вагон: " + getCarriageNumber().toString() + " Место: " + getSeatNumber().toString();
    }
}