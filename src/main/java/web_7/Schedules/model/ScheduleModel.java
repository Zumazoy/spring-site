package web_7.Schedules.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import web_7.Routes.model.RouteModel;
import web_7.Tickets.model.TicketModel;
import web_7.Trains.model.TrainModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "schedules")
public class ScheduleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поезд не выбран")
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private TrainModel train;

    @NotNull(message = "Маршрут не выбран")
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private RouteModel route;

    @NotNull(message = "Дата отправления не может быть null")
    @FutureOrPresent(message = "Дата отправления должна содержать будущую дату или сегодняшнее число")
    private LocalDate departureDate;

    @NotNull(message = "Время отправления не может быть null")
    private LocalTime departureTime;

    @NotNull(message = "Время прибытия не может быть null")
    private LocalTime arrivalTime;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<TicketModel> tickets;

    public ScheduleModel() {}

    public ScheduleModel(Long id, TrainModel train, RouteModel route, LocalDate departureDate, LocalTime departureTime, LocalTime arrivalTime) {
        this.id = id;
        this.train = train;
        this.route = route;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public TrainModel getTrain() {
        return train;
    }
    public void setTrain(TrainModel train) {
        this.train = train;
    }

    public RouteModel getRoute() {
        return route;
    }
    public void setRoute(RouteModel route) {
        this.route = route;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<TicketModel> getTickets() {
        return tickets;
    }
    public void setTickets(List<TicketModel> tickets) {
        this.tickets = tickets;
    }

    public String getTime() {
        return departureTime.toString() + " - " + arrivalTime.toString();
    }
}