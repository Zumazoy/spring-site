package web_7.RouteStations.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import web_7.Routes.model.RouteModel;
import web_7.Stations.model.StationModel;

import java.time.LocalTime;

@Entity
@Table(name = "route_stations")
public class RouteStationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Маршрут не выбран")
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private RouteModel route;

    @NotNull(message = "Станция не выбрана")
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private StationModel station;

    @NotNull(message = "Порядковый номер не может быть null")
    @Min(value = 1, message = "Порядковый номер должен быть не менее 1")
    @Max(value = 1000, message = "Порядковый номер должен быть не более 1000")
    private Integer orderNumber;

    @NotNull(message = "Время прибытия не может быть null")
    private LocalTime arrivalTime;

    @NotNull(message = "Время отправления не может быть null")
    private LocalTime departureTime;

    public RouteStationModel() {}

    public RouteStationModel(Long id, RouteModel route, StationModel station, Integer orderNumber, LocalTime arrivalTime, LocalTime departureTime) {
        this.id = id;
        this.route = route;
        this.station = station;
        this.orderNumber = orderNumber;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public RouteModel getRoute() {
        return route;
    }
    public void setRoute(RouteModel route) {
        this.route = route;
    }

    public StationModel getStation() {
        return station;
    }
    public void setStation(StationModel station) {
        this.station = station;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    @AssertTrue(message = "Время отправления должно быть позже времени прибытия")
    public boolean isDepartureAfterArrival() {
        return departureTime == null || arrivalTime == null || departureTime.isAfter(arrivalTime);
    }
}